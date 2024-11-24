<template>
  <div class="chat-container" :class="{ 'chat-open': isOpen }">
    <button @click="toggleChat" class="chat-toggle-btn" :class="{ 'chat-open': isOpen }">
      <div v-if="!isOpen" class="ai-icon">
        <div class="ai-circle">
          <span class="ai-text">AI</span>
        </div>
        <div class="pulse"></div>
      </div>
      <span v-else class="close-icon">&times;</span>
    </button>

    <div v-if="isOpen"
         class="chat-window"
         :style="{ width: width + 'px', height: height + 'px' }"
         ref="chatWindow">
      <div class="chat-header" @mousedown="startDragging">
        <h3>Sweet Home AI 상담</h3>
        <div class="header-buttons">
          <button class="reset-button" @click="resetChat" title="대화 초기화">
            🔄
          </button>
          <button class="close-button" @click="toggleChat">&times;</button>
        </div>
      </div>

      <div class="chat-messages" ref="messageContainer">
        <div v-for="(message, index) in messages"
             :key="index"
             :class="['message', message.type]">
          <div v-if="message.type === 'bot' && message.loading" class="loading-dots">
            <span></span><span></span><span></span>
          </div>
          <div v-else class="message-text" v-html="formatMessage(message.text)"></div>
        </div>
      </div>

      <div class="chat-input">
        <input v-model="userInput"
               type="text"
               placeholder="질문을 입력하세요..."
               @keyup.enter="sendMessage"
               :disabled="isLoading"/>
        <button @click="sendMessage"
                class="send-btn"
                :disabled="!userInput.trim() || isLoading">
          전송
        </button>
      </div>

      <div class="resize-handle"
           @mousedown="startResize"
           @touchstart="startResize">
        <span class="resize-icon">⋌</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, computed, onUnmounted } from 'vue'

const props = defineProps({
  initialLocation: {
    type: String,
    default: ''
  }
})

const isOpen = ref(false)
const messages = ref([])
const userInput = ref('')
const messageContainer = ref(null)
const isLoading = ref(false)

const GROQ_API_KEY = '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'

const sendChatMessage = async (message) => {
  try {
    console.log('Sending message to Groq:', message);

    const response = await fetch('https://api.groq.com/openai/v1/chat/completions', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer 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`,

        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        messages: [
          {
            role: "system",
            content: "당신은 부동산 전문가입니다.  부동산 동향과 투자 가치에 대해 전문적인 조언을 제공합니다. 하지만, 부동산이외의 질문에도 답을 잘 할 수 있습니다. 또한 한국어로만 대답을 합니다."
          },
          {
            role: "user",
            content: message
          }
        ],
        model: "mixtral-8x7b-32768",
        temperature: 0.5,
        max_tokens: 2048
      })
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    console.log('Groq API response:', data);

    if (!data.choices || !data.choices[0] || !data.choices[0].message) {
      throw new Error('Invalid response format from Groq API');
    }

    return {
      message: data.choices[0].message.content.trim()
    };
  } catch (error) {
    console.error('Detailed Chat API Error:', error);
    throw new Error(error.message || '알 수 없는 오류가 발생했습니다.');
  }
}

// 메시지 포맷팅 함수 추가
const formatMessage = (text) => {
  return text.replace(/\n/g, '<br>')
}

const toggleChat = () => {
  isOpen.value = !isOpen.value
  if (isOpen.value && messages.value.length === 0) {
    messages.value.push({
      type: 'bot',
      text: `안녕하세요! Sweet Home AI 상담사입니다.
부동산에 관해 다음과 같은 질문을 해보세요:

• [동네명]의 부동산 시장 동향이 어떤가요?
• [동네명]의 교통/학군/상권 정보는 어떤가요?
• [동네명]의 향후 개발 계획이 있나요?
• [아파트명]의 장단점을 알려주세요.`
    })
  }
}

const sendMessage = async () => {
  if (!userInput.value.trim() || isLoading.value) return

  const message = userInput.value
  userInput.value = ''

  messages.value.push({
    type: 'user',
    text: message
  })

  messages.value.push({
    type: 'bot',
    loading: true,
    text: ''
  })

  try {
    isLoading.value = true
    const response = await sendChatMessage(message)
    messages.value.pop()

    messages.value.push({
      type: 'bot',
      text: response.message
    })
  } catch (error) {
    console.error('Chat error:', error)
    messages.value.pop()

    messages.value.push({
      type: 'bot',
      text: `죄송합니다. ${error.message || '응답 중 오류가 발생했습니다.'} 다시 시도해주세요.`
    })
  } finally {
    isLoading.value = false
  }
}

// 새 메시지가 추가될 때마다 스크롤
watch(messages, () => {
  setTimeout(() => {
    if (messageContainer.value) {
      messageContainer.value.scrollTop = messageContainer.value.scrollHeight
    }
  }, 100)
})

// 초기 위치가 설정되면 자동으로 질문 생성
watch(() => props.initialLocation, (newLocation) => {
  if (newLocation && messages.value.length === 0) {
    const initialQuestion = `${newLocation}의 특징을 알려주세요.
    1. 주변 인프라 (교통, 학군, 상권)
    2. 주요 아파 단지
    3. 최근 시세 동향
    4. 향후 개발 계획
    에 대해 설명해주세요.`

    sendMessage(initialQuestion)
  }
}, { immediate: true })

// 크기 조절 관련 상태 추가
const currentSize = ref('medium')
const sizes = {
  small: { width: '300px', height: '400px' },
  medium: { width: '400px', height: '500px' },
  large: { width: '500px', height: '600px' }
}

const chatWindowStyle = computed(() => sizes[currentSize.value])

const adjustSize = (size) => {
  currentSize.value = size
}

const width = ref(350)
const height = ref(500)
const isDragging = ref(false)
const isResizing = ref(false)
const dragOffset = ref({ x: 0, y: 0 })
const chatWindow = ref(null)

// 드래그 시작
const startDragging = (e) => {
  if (e.target.classList.contains('close-button')) return

  isDragging.value = true
  const rect = chatWindow.value.getBoundingClientRect()
  dragOffset.value = {
    x: e.clientX - rect.left,
    y: e.clientY - rect.top
  }
}

// 리사이즈 시작
const startResize = (e) => {
  isResizing.value = true
  e.preventDefault()

  const startX = e.type === 'mousedown' ? e.clientX : e.touches[0].clientX
  const startY = e.type === 'mousedown' ? e.clientY : e.touches[0].clientY
  const startWidth = width.value
  const startHeight = height.value

  const resize = (e) => {
    const currentX = e.type === 'mousemove' ? e.clientX : e.touches[0].clientX
    const currentY = e.type === 'mousemove' ? e.clientY : e.touches[0].clientY

    const newWidth = Math.max(300, startWidth + (currentX - startX))
    const newHeight = Math.max(400, startHeight + (currentY - startY))

    width.value = newWidth
    height.value = newHeight
  }

  const stopResize = () => {
    isResizing.value = false
    document.removeEventListener('mousemove', resize)
    document.removeEventListener('mouseup', stopResize)
    document.removeEventListener('touchmove', resize)
    document.removeEventListener('touchend', stopResize)
  }

  document.addEventListener('mousemove', resize)
  document.addEventListener('mouseup', stopResize)
  document.addEventListener('touchmove', resize)
  document.addEventListener('touchend', stopResize)
}

// 마우스 이동 처리
const handleMouseMove = (e) => {
  if (isDragging.value) {
    const chatContainer = chatWindow.value
    const x = e.clientX - dragOffset.value.x
    const y = e.clientY - dragOffset.value.y

    // 화면 경계 체크
    const maxX = window.innerWidth - chatContainer.offsetWidth
    const maxY = window.innerHeight - chatContainer.offsetHeight

    chatContainer.style.left = `${Math.min(Math.max(0, x), maxX)}px`
    chatContainer.style.top = `${Math.min(Math.max(0, y), maxY)}px`
  }

  if (isResizing.value) {
    const minWidth = 300
    const minHeight = 400
    const maxWidth = window.innerWidth - chatWindow.value.offsetLeft
    const maxHeight = window.innerHeight - chatWindow.value.offsetTop

    width.value = Math.min(Math.max(minWidth, e.clientX - chatWindow.value.offsetLeft), maxWidth)
    height.value = Math.min(Math.max(minHeight, e.clientY - chatWindow.value.offsetTop), maxHeight)
  }
}

// 마우스 업 처리
const handleMouseUp = () => {
  isDragging.value = false
  isResizing.value = false
}

// 이벤트 리스너 등록/제거
onMounted(() => {
  document.addEventListener('mousemove', handleMouseMove)
  document.addEventListener('mouseup', handleMouseUp)
})

onUnmounted(() => {
  document.removeEventListener('mousemove', handleMouseMove)
  document.removeEventListener('mouseup', handleMouseUp)
})

const resetChat = () => {
  messages.value = []
  // 초기 메시지 다시 표시
  messages.value.push({
    type: 'bot',
    text: `안녕하세요! Sweet Home AI 상담사입니다.
부동산에 관해 다음과 같은 질문을 해보세요:

• [동네명]의 부동산 시장 동향이 어떤가요?
• [동네명]의 교통/학군/상권 정보는 어떤가요?
• [동네명]의 향후 개발 계획이 있나요?
• [아파트명]의 장단점을 알려주세요.`
  })
}
</script>

<style scoped>
.chat-container {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
}

.chat-toggle-btn {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: #2196F3;
  color: white;
  border: none;
  cursor: pointer;
  position: relative;
  box-shadow: 0 2px 10px rgba(0,0,0,0.2);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.ai-icon {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ai-circle {
  width: 40px;
  height: 40px;
  background: #2196F3;
  border: 2px solid white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 2;
}

.ai-text {
  font-size: 20px;
  font-weight: bold;
  color: white;
  text-shadow: 0 1px 2px rgba(0,0,0,0.2);
}

.pulse {
  position: absolute;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.4);
  border-radius: 50%;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.6;
  }
  70% {
    transform: scale(1.3);
    opacity: 0;
  }
  100% {
    transform: scale(1.3);
    opacity: 0;
  }
}

.close-icon {
  font-size: 32px;
  color: white;
}

.chat-toggle-btn:hover {
  transform: scale(1.1);
  background: #1976D2;
}

.chat-toggle-btn.chat-open {
  background: #f44336;
}

.chat-toggle-btn.chat-open:hover {
  background: #d32f2f;
}

.chat-window {
  position: fixed;
  bottom: 70px;
  right: 20px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.2);
  display: flex;
  flex-direction: column;
  min-width: 300px;
  min-height: 400px;
  overflow: hidden;
}

.chat-header {
  padding: 15px;
  background: #2196F3;
  color: white;
  border-radius: 10px 10px 0 0;
  cursor: move;
}

.chat-header h3 {
  margin: 0;
  font-size: 16px;
  flex: 1;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 15px;
  margin-bottom: 0;
}

.message {
  margin-bottom: 10px;
  padding: 8px 12px;
  border-radius: 15px;
  max-width: 85%;
  word-break: break-word;  /* 긴 텍스트 줄바꿈 */
}

.bot {
  background: #f0f0f0;
  margin-right: auto;
}

.user {
  background: #2196F3;
  color: white;
  margin-left: auto;
}

.chat-input {
  padding: 15px;
  border-top: 1px solid #eee;
  display: flex;
  gap: 10px;
  width: 100%;
  box-sizing: border-box;
  background: white;
}

input {
  flex: 1;
  min-width: 0;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
}

.send-btn {
  padding: 8px 15px;
  background: #2196F3;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  white-space: nowrap;
}

.send-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.loading-dots {
  display: flex;
  gap: 4px;
  padding: 8px;
}

.loading-dots span {
  width: 8px;
  height: 8px;
  background: #2196F3;
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out both;
}

.loading-dots span:nth-child(1) { animation-delay: -0.32s; }
.loading-dots span:nth-child(2) { animation-delay: -0.16s; }

@keyframes bounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

.close-button {
  background: none;
  border: none;
  color: white;
  font-size: 24px;
  cursor: pointer;
  padding: 0 5px;
}

.close-button:hover {
  opacity: 0.8;
}

.resize-handle {
  position: absolute;
  right: 0;
  bottom: 0;
  width: 20px;
  height: 20px;
  cursor: se-resize;
  display: flex;
  align-items: center;
  justify-content: center;
}

.resize-icon {
  color: #2196F3;
  font-size: 16px;
  transform: rotate(-45deg);
}

/* 드래그 중일 때 텍스트 선택 방지 */
.chat-window * {
  user-select: none;
}

.message-text {
  white-space: pre-line;  /* 줄바꿈 보존 */
  line-height: 1.5;       /* 줄간격 조정 */
}

/* 메시지 컨테이너 스크롤바 스타일링 */
.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #555;
}

.header-buttons {
  display: flex;
  gap: 10px;
  align-items: center;
}

.reset-button {
  background: none;
  border: none;
  color: white;
  font-size: 18px;
  cursor: pointer;
  padding: 0 5px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.2s;
}

.reset-button:hover {
  transform: rotate(180deg);
}
</style>

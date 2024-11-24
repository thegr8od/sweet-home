<template>
  <header>
    <NavBar></NavBar>
  </header>

  <main>
    <div class="content">
      <RouterView />
    </div>
  </main>

  <ChatBot />
</template>

<script setup>
import { RouterView } from 'vue-router'
import NavBar from './components/layout/NavBar.vue'
import ChatBot from './components/chat/ChatBot.vue'
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { apiInstance } from '@/api/index'

const api = apiInstance()
const userStore = useUserStore()

onMounted(async () => {
  const token = localStorage.getItem('token')
  if (token) {
    try {
      const response = await api.get('/user/info', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      userStore.setLoginSuccess(response.data.id, response.data)
    } catch (error) {
      console.error('자동 로그인 실패:', error)
      userStore.setLogout()
    }
  } else {
    userStore.setLogout()
  }
})
</script>

<style>
.chat-container {
  position: fixed !important;
  bottom: 20px !important;
  right: 20px !important;
  z-index: 99999 !important;
}

header {
  background-color: #fff;
  padding: 10px 20px;
  border-bottom: 1px solid #ddd;
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 1000;
  height: 60px;
}

.content {
  height: calc(100vh - 60px);
  margin-top: 60px;
}

button-group {
  margin: 10px 0px;
}

button {
  margin: 0 3px;
}
</style>


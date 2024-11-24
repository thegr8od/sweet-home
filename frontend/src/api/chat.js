import { apiInstance } from './index.js'

const api = apiInstance()

const GROQ_API_KEY = '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'

export const sendChatMessage = async (message) => {
  try {
    const response = await fetch('https://api.groq.com/v1/chat/completions', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${GROQ_API_KEY}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        messages: [
          {
            role: "system",
            content: "당신은 부동산 전문가입니다. 한국의 부동산 시장에 대해 잘 알고 있으며, 특히 서울 지역의 부동산 동향과 투자 가치에 대해 전문적인 조언을 제공합니다."
          },
          {
            role: "user",
            content: message
          }
        ],
        model: "llama3-8b-8192",
        temperature: 0.7,
        max_tokens: 1000
      })
    });

    const data = await response.json();
    return {
      message: data.choices[0].message.content
    };
  } catch (error) {
    console.error('Chat API Error:', error);
    throw error;
  }
}

<template>
  <div class="flex justify-center items-center h-screen">
    <div class="animate-spin rounded-full h-16 w-16 border-t-2 border-b-2 border-blue-500"></div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const getKakaoToken = async (code) => {
  try {
    console.log('인가코드:', code)
    const response = await axios.post('http://localhost:8080/auth/kakao',
      { code },
      {
        headers: {
          'Content-Type': 'application/json',
        },
        withCredentials: true
      }
    )

    const { token } = response.data

    if (token) {
      localStorage.setItem('token', token)

      // 사용자 정보 확인
      const userResponse = await axios.get('http://localhost:8080/user/info', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })

      // 나이가 0인 경우 마이페이지로 이동
      if (userResponse.data.age === 0) {
        alert('회원가입을 축하합니다! 원활한 서비스 이용을 위해 추가 정보를 입력해주세요.')
        window.location.href = '/mypage'
      } else {
        window.location.href = '/'
      }
    }
  } catch (error) {
    console.error('카카오 로그인 에러:', error)
    router.push('/login')
  }
}

onMounted(() => {
  const code = new URL(window.location.href).searchParams.get('code')
  if (code) {
    getKakaoToken(code)
  } else {
    router.push('/login')
  }
})
</script>

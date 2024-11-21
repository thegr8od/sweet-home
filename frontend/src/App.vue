<template>
  <header>
    <NavBar></NavBar>
  </header>

  <main>
    <div class="content">
      <RouterView />
    </div>
  </main>
</template>

<script setup>
import { RouterView } from 'vue-router'
import NavBar from './components/layout/NavBar.vue'
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
      userStore.setUserInfo(response.data)
    } catch (error) {
      console.error('자동 로그인 실패:', error)
      userStore.clearUserInfo()
    }
  }
})
</script>

<style scoped>
header {
  background-color: #fff;
  padding: 10px 20px;
  border-bottom: 1px solid #ddd;
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 1000; /* navBar가 맨 위로 오도록 설정 */
}

.content {
  /* padding: 20px; */
  margin-top: 60px; /* navBar의 높이만큼 아래로 밀어서 겹침 방지 */
  height: calc(100vh - 60px);
}

button-group {
  margin: 10px 0px;
}

button {
  margin: 0 3px;
}
</style>

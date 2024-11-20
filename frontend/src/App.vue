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

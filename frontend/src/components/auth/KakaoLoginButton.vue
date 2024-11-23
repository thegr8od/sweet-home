<template>
  <button
    @click="loginWithKakao"
    class="flex items-center justify-center gap-2 w-full bg-[#FEE500] text-[#000000] py-2 px-4 rounded-lg hover:bg-[#FDD835] transition-colors"
  >
    <img src="@/assets/images/kakao-logo.png" alt="Kakao" class="w-6 h-6" />
    카카오로 시작하기
  </button>
</template>

<script setup>
const loginWithKakao = () => {
  const REDIRECT_URI = import.meta.env.VITE_KAKAO_REDIRECT_URI;
  const CLIENT_ID = import.meta.env.VITE_KAKAO_CLIENT_ID;

  const kakaoURL = `https://kauth.kakao.com/oauth/authorize?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code`;
  window.location.href = kakaoURL;
};
</script>

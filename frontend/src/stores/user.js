import { defineStore } from 'pinia'

export const useUserStore = defineStore(
  'user',
  {
    state: () => ({
      userInfo: null,
      isLoggedIn: false,
    }),

    getters: {
      user: (state) => state.userInfo,
      getIsLoggedIn: (state) => state.isLoggedIn,
    },

    actions: {
      setUserInfo(user) {
        this.userInfo = user
        this.isLoggedIn = true
      },

      setToken(token) {
        const cleanToken = token.replace(/\s+/g, '')
        localStorage.setItem('token', cleanToken)
        this.isLoggedIn = true
      },

      logout() {
        this.userInfo = null
        this.isLoggedIn = false
        localStorage.removeItem('token')
      },

      // 페이지 새로고침시 로그인 상태 복구
      initializeStore() {
        const token = localStorage.getItem('token')
        if (token) {
          this.isLoggedIn = true
        }
      },
    },
  },
  {
    persist: true,
  },
)

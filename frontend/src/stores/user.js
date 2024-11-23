import { defineStore } from 'pinia'

export const useUserStore = defineStore(
  'user',
  {
    state: () => ({
      isLoggedIn: false,
      userId: null,
      userInfo: null,
    }),

    getters: {
      getIsLoggedIn: (state) => state.isLoggedIn,
      getUserId: (state) => state.userId,
      getUserInfo: (state) => state.userInfo,
    },

    actions: {
      setLoginSuccess(userId, userInfo) {
        this.isLoggedIn = true
        this.userId = userId
        this.userInfo = userInfo
      },

      setLogout() {
        this.isLoggedIn = false
        this.userId = null
        this.userInfo = null
        localStorage.removeItem('token')
      },

      updateUserInfo(userInfo) {
        this.userInfo = userInfo
      },

      initializeLoginState() {
        const token = localStorage.getItem('token')
        if (token) {
          this.isLoggedIn = true
        } else {
          this.setLogout()
        }
      }
    },
  },
  {
    persist: true,
  },
)

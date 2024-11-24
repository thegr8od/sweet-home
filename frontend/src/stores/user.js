import { defineStore } from 'pinia'
import { useInterestStore } from './interestStore'

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
        this.initializeLoginState()
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

      async initializeLoginState() {
        const token = localStorage.getItem('token')
        if (token) {
          this.isLoggedIn = true
          const interestStore = useInterestStore()
          try {
            await interestStore.fetchInterestList()
          } catch (error) {
            console.error('관심 목록 초기화 실패:', error)
          }
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

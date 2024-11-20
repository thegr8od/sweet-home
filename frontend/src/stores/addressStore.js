// stores/addressStore.js
import { defineStore } from 'pinia'
import { sidoList, gugunList, dongList } from '@/api/house.js'

export const useAddressStore = defineStore('addressStore', {
  state: () => ({
    sidos: [],
    guguns: [],
    dongs: [],
  }),

  actions: {
    async getSido() {
      try {
        const { data } = await sidoList()
        this.sidos = data
      } catch (error) {
        console.error('Error fetching sido list', error)
      }
    },

    async getGugun(sidoName) {
      const params = { sidoName }
      try {
        const { data } = await gugunList(params)
        this.guguns = data
      } catch (error) {
        console.error('Error fetching gugun list', error)
      }
    },

    async getDong(params) {
      try {
        const { data } = await dongList(params)
        this.dongs = data
      } catch (error) {
        console.error('Error fetching dong list', error)
      }
    },

    clearAddressData() {
      this.sidos = []
      this.guguns = []
      this.dongs = []
    },
  },
})

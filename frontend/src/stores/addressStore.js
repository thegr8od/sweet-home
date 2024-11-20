// stores/addressStore.js
import { defineStore } from 'pinia'
import { getSidoList, getGugunList, getDongList } from '@/api/location.js'
export const useAddressStore = defineStore('addressStore', {
  state: () => ({
    sidos: [],
    guguns: [],
    dongs: [],
  }),

  actions: {
    async getSido() {
      await getSidoList(
        (response) => {
          console.log(response)
          this.sidos = response.data || []
        },
        (error) => {
          console.error('Error fetching sido list', error)
          this.sidos = []
        },
      )
    },

    async getGugun(sidoName) {
      await getGugunList(
        { sido: sidoName },
        (response) => {
          console.log(response)
          this.guguns = response.data || []
          console.log('Guguns:', this.guguns)
        },
        (error) => {
          console.error('Error fetching gugun list', error)
          this.guguns = []
        },
      )
    },

    async getDong(sidoName, gugunName) {
      await getDongList(
        { sido: sidoName, gugun: gugunName },
        (response) => {
          console.log(response)
          this.dongs = response.data || []
          console.log('Dongs:', this.dongs)
        },
        (error) => {
          console.error('Error fetching dong list', error)
          this.dongs = []
        },
      )
    },

    clearAddressData() {
      this.sidos = []
      this.guguns = []
      this.dongs = []
    },
  },
})

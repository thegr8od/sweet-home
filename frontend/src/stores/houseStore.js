// stores/houseStore.js
import { defineStore } from 'pinia'
import {
  houseList,
  housDeal,
  houseAddress,
  houseName,
  getKapt,
  houseDetail1,
  houseDetail2,
  houseListByAddress,
} from '@/api/house.js'
import { useAddressStore } from './addressStore'

export const useHouseStore = defineStore('houseStore', {
  state: () => ({
    houses: [],
    house: null,
    deals: [
      {
        no: 115602104000060,
        dealAmount: '193,000',
        dealYear: 2021,
        dealMonth: 4,
        dealDay: 25,
        area: '93.06',
        floor: '2',
        cancelDealType: '',
        aptCode: '11560000000025',
      },
    ],
    none: true,
    markerPositions: [],
    detail1: [],
    detail2: [],
  }),

  actions: {
    async getHouseListByAddress(address) {
      try {
        const { data } = await houseListByAddress(address)
        this.houses = data
        this.markerPositions = data.map((house) => [house.lat, house.lng])
      } catch (error) {
        console.error('Error fetching house list by address', error)
      }
    },

    async getHouseList(dongName) {
      const params = { dongName }
      try {
        const { data } = await houseList(params)
        this.houses = data
        this.markerPositions = data.map((house) => [house.lat, house.lng])
      } catch (error) {
        console.error('Error fetching house list', error)
      }
    },

    async detailHouse(house) {
      this.house = house
      const params = { aptCode: house.aptCode }
      try {
        const { data } = await housDeal(params)
        this.deals = data
        this.none = false
      } catch (error) {
        console.error('Error fetching house deal details', error)
      }
    },

    setNoneFalse(data) {
      this.none = data
    },

    async addressHouse(data) {
      const params = {
        pa: data.pa,
        qa: data.qa,
        ha: data.ha,
        oa: data.oa,
      }
      try {
        const { data } = await houseAddress(params)
        this.houses = data
        this.markerPositions = data.map((house) => [house.lat, house.lng])
      } catch (error) {
        console.error('Error fetching house address', error)
      }
    },

    async getNameList(data) {
      const params = { apartmentName: data }
      try {
        const { data } = await houseName(params)
        this.houses = data
        this.markerPositions = data.map((house) => [house.lat, house.lng])
      } catch (error) {
        console.error('Error fetching house name list', error)
      }
    },

    async getDetail(houseinfo) {
      try {
        const { data } = await getKapt(houseinfo)
        const params = { kaptCode: data.data }

        const [detail1Data, detail2Data] = await Promise.all([
          houseDetail1(params),
          houseDetail2(params),
        ])

        this.detail1 = detail1Data.response.body.item
        this.detail2 = detail2Data.response.body.item
      } catch (error) {
        console.error('Error fetching house details', error)
      }
    },
  },
})

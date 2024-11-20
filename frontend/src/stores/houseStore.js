// stores/houseStore.js
import { defineStore } from 'pinia'
import { houseDealListByAddress } from '@/api/house.js'

export const useHouseStore = defineStore('houseStore', {
  state: () => ({
    houses: [],
    selectedHouse: null,
    markerPositions: [],
    selectedPosition: null,
    showDetail: false,
  }),

  actions: {
    async getHouseListByAddress(address) {
      try {
        this.houses = []
        this.markerPositions = []
        this.selectedPosition = null

        await houseDealListByAddress(
          address,
          (response) => {
            // success callback
            const data = response.data
            this.houses = data
            this.markerPositions = data
              .filter((house) => house.latitude && house.longitude)
              .map((house) => {
                const lat = parseFloat(house.latitude)
                const lng = parseFloat(house.longitude)
                if (isNaN(lat) || isNaN(lng)) return null
                return [lat, lng]
              })
              .filter((position) => position !== null)
            console.log('houses:', this.houses)
            console.log('markerPositions:', this.markerPositions)
          },
          (error) => {
            // error callback
            console.error('Error fetching house list by address:', error)
            this.houses = []
            this.markerPositions = []
          },
        )
      } catch (error) {
        console.error('Error in getHouseListByAddress:', error)
        this.houses = []
        this.markerPositions = []
      }
    },

    // async getHouseList(dongName) {
    //   const params = { dongName }
    //   try {
    //     const { data } = await houseList(params)
    //     this.houses = data
    //     this.markerPositions = data.map((house) => [house.lat, house.lng])
    //   } catch (error) {
    //     console.error('Error fetching house list', error)
    //   }
    // },

    // async detailHouse(house) {
    //   this.house = house
    //   const params = { aptCode: house.aptCode }
    //   try {
    //     const { data } = await housDeal(params)
    //     this.deals = data
    //     this.none = false
    //   } catch (error) {
    //     console.error('Error fetching house deal details', error)
    //   }
    // },

    // setNoneFalse(data) {
    //   this.none = data
    // },

    // async addressHouse(data) {
    //   const params = {
    //     pa: data.pa,
    //     qa: data.qa,
    //     ha: data.ha,
    //     oa: data.oa,
    //   }
    //   try {
    //     const { data } = await houseAddress(params)
    //     this.houses = data
    //     this.markerPositions = data.map((house) => [house.lat, house.lng])
    //   } catch (error) {
    //     console.error('Error fetching house address', error)
    //   }
    // },

    // async getNameList(data) {
    //   const params = { apartmentName: data }
    //   try {
    //     const { data } = await houseName(params)
    //     this.houses = data
    //     this.markerPositions = data.map((house) => [house.lat, house.lng])
    //   } catch (error) {
    //     console.error('Error fetching house name list', error)
    //   }
    // },

    //   async getDetail(houseinfo) {
    //     try {
    //       const { data } = await getKapt(houseinfo)
    //       const params = { kaptCode: data.data }

    //       const [detail1Data, detail2Data] = await Promise.all([
    //         houseDetail1(params),
    //         houseDetail2(params),
    //       ])

    //       this.detail1 = detail1Data.response.body.item
    //       this.detail2 = detail2Data.response.body.item
    //     } catch (error) {
    //       console.error('Error fetching house details', error)
    //     }
    //   },

    setSelectedPosition(house) {
      if (house && house.latitude && house.longitude) {
        this.selectedPosition = {
          lat: parseFloat(house.latitude),
          lng: parseFloat(house.longitude),
        }
      }
    },

    setSelectedHouse(house) {
      this.selectedHouse = house
      this.showDetail = true
    },

    closeDetail() {
      this.showDetail = false
      this.selectedHouse = null
    },
  },
})

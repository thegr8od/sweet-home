// stores/houseStore.js
import { defineStore } from 'pinia'
import { houseDealListByAddress, getHouseInfoByAptSeq, houseDealListByAptSeq } from '@/api/house.js'

export const useHouseStore = defineStore('houseStore', {
  state: () => ({
    houses: [],
    selectedHouse: null,
    markerPositions: [],
    selectedPosition: null,
    showDetail: false,
    aptInfo: null,
    aptDeals: [],
    none: true,
    showCommentList: false,
    showReCommentList: false,
    isCommentWritingPanelVisible: false,
    showCommentWriting: false,
    selectedBoardId: null,
  }),

  actions: {
    async getHouseDealsByAddress(address) {
      try {
        const response = await new Promise((resolve, reject) => {
          houseDealListByAddress(address, resolve, reject)
        })
        return response.data
      } catch (error) {
        console.error('Error in getHouseDealsByAddress:', error)
        return []
      }
    },

    async getAptInfo(aptSeq) {
      try {
        console.log('aptSeq:', aptSeq)
        await getHouseInfoByAptSeq(
          aptSeq,
          (response) => {
            this.aptInfo = response.data
            console.log('Apartment Info:', response.data)
          },
          (error) => {
            console.error('Error fetching house details:', error)
            this.aptInfo = null
          },
        )
      } catch (error) {
        console.error('Error in getAptInfo:', error)
        this.aptInfo = null
      }
    },

    async getAptDeals(aptSeq) {
      try {
        console.log('aptSeq:', aptSeq)
        await houseDealListByAptSeq(
          aptSeq,
          (response) => {
            this.aptDeals = response.data
            console.log('Apartment Deals:', response.data)
          },
          (error) => {
            console.error('Error fetching house deals:', error)
            this.aptDeals = []
          },
        )
      } catch (error) {
        console.error('Error in getAptDeals:', error)
        this.aptDeals = []
      }
    },

    async getDetail(aptSeq) {
      try {
        console.log('상세정보 조회 aptSeq:', aptSeq)
        // 아파트 정보와 거래 내역을 순차적으로 조회
        await this.getAptInfo(aptSeq)
        await this.getAptDeals(aptSeq)
      } catch (error) {
        console.error('아파트 상세 정보 조회 실패:', error)
      }
    },

    async getHouseListByAddress(address) {
      this.clearHouses()

      try {
        const data = await this.getHouseDealsByAddress(address)
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
      } catch (error) {
        console.error('Error fetching house list by address:', error)
        this.clearHouses()
      }
    },

    setSelectedPosition(position) {
      this.selectedPosition = position
      const selectedHouse = this.houses.find(
        (house) =>
          parseFloat(house.latitude) === position.lat &&
          parseFloat(house.longitude) === position.lng,
      )
      if (selectedHouse) {
        console.log('Selected House:', {
          aptName: selectedHouse.aptName,
          legalDong: selectedHouse.legalDong,
          latitude: selectedHouse.latitude,
          longitude: selectedHouse.longitude,
        })
      }
    },

    setSelectedHouse(house) {
      this.selectedHouse = house
      this.showDetail = true
      this.showCommentList = false
      this.showReCommentList = false
      this.showCommentWriting = false
    },

    closeDetail() {
      this.showDetail = false
      this.showCommentList = false
      this.selectedHouse = null
      this.selectedPosition = null
      this.aptInfo = null
      this.aptDeals = []
    },

    clearHouses() {
      this.houses = []
      this.markerPositions = []
      this.selectedPosition = null
      this.selectedHouse = null
      this.showDetail = false
      this.showCommentList = false
      this.showReCommentList = false
      this.showCommentWriting = false
      this.aptInfo = null
      this.aptDeals = []
    },

    showComments() {
      this.showCommentList = true
      this.showCommentWriting = false
      this.showDetail = true
    },

    hideComments() {
      this.showCommentList = false
      this.showCommentWriting = false
      this.showDetail = true
    },

    showReComments(boardId) {
      this.showReCommentList = true
      this.showCommentList = false
      this.showDetail = true
      this.selectedBoardId = boardId
    },

    hideReComments() {
      this.showReCommentList = false
      this.showCommentList = true
      this.showDetail = true
      this.selectedBoardId = null
    },

    showCommentWritingPanel() {
      this.showCommentWriting = true
      this.showCommentList = false
      this.showDetail = true
    },

    hideCommentWritingPanel() {
      this.showCommentWriting = false
      this.showCommentList = true
      this.showDetail = true
    },

    showDetailPanel() {
      this.showCommentList = false
      this.showDetail = true
    },
  },
})

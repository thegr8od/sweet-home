// stores/houseStore.js
import { defineStore } from 'pinia'
import {
  houseDealListByAddress,
  getHouseInfoByAptSeq,
  houseDealListByAptSeq,
  getHouseDealsByMapBounds,
  getHouseInfoByAptName,
  } from '@/api/house.js'
import { useInterestStore } from '@/stores/interestStore'
=======


export const useHouseStore = defineStore('houseStore', {
  state: () => ({
    houses: [],
    selectedHouse: null,
    markerPositions: [],
    interestMarkerPositions: [],
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
    houseDetails: [],
    updateMapFlag: true,
  }),

  actions: {
    // 지도 영역이 변경될 때 호출되는 함수
    async onBoundsChanged(bounds) {
      console.log(bounds)
      const interestStore = useInterestStore()


      try {
        await getHouseDealsByMapBounds(
          bounds,
          ({ data }) => {
            if (data && Array.isArray(data)) {
              this.houses = data.map((house) => ({
                aptName: house.aptName,
                aptSeq: house.aptSeq,
                legalDong: house.legalDong,
                latitude: house.latitude,
                longitude: house.longitude,
                maxPrice: house.maxPrice,
                maxPriceArea: house.maxPriceArea,
              }))

              // markerPositions 업데이트 - 유효한 위도/경도만 필터링
              this.markerPositions = data
                .filter((house) => house.latitude && house.longitude)
                .map((house) => {
                  const lat = parseFloat(house.latitude)
                  const lng = parseFloat(house.longitude)
                  return !isNaN(lat) && !isNaN(lng) ? [lat, lng] : null
                })
                .filter((position) => position !== null)


              // 각 아파트의 최고가 정보 저장
              this.houseDetails = data.map((house) => ({
                maxPrice: house.maxPrice || '?',
                maxPriceArea: house.maxPriceArea || '?',
              }))

              // 관심 매물 정보 업데이트
              interestStore.updateInterestDetails(this.houses, this.houseDetails)


              console.log('houses:', this.houses)
              console.log('markerPositions:', this.markerPositions)
              console.log('houseDetails:', this.houseDetails)
            }
          },
          (error) => {
            console.error('아파트 정보 조회 실패:', error)
          },
        )
      } catch (error) {
        console.error('Error in onBoundsChanged:', error)
      }
    },

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
        // Promise.all을 사용하여 두 요청을 동시에 처리
        await Promise.all([this.getAptInfo(aptSeq), this.getAptDeals(aptSeq)])
      } catch (error) {
        console.error('아파트 상세 정보 조회 실패:', error)
        // 에러 발생 시 상태 초기화
        this.aptInfo = null
        this.aptDeals = []
      }
    },

    async getHouseListByAddress(address) {
      this.clearHouses()

      try {
        const data = await this.getHouseDealsByAddress(address)
        console.log('data:', data)
        if (data && Array.isArray(data)) {
          // houses 배열 업데이트
          this.houses = data.map((house) => ({
            aptName: house.aptName,
            aptSeq: house.aptSeq,
            latitude: house.latitude,
            longitude: house.longitude,
            legalDong: house.legalDong,
          }))

          // markerPositions 업데이트 - 유효한 위도/경도만 필터링
          this.markerPositions = data
            .filter((house) => house.latitude && house.longitude)
            .map((house) => {
              const lat = parseFloat(house.latitude)
              const lng = parseFloat(house.longitude)
              return !isNaN(lat) && !isNaN(lng) ? [lat, lng] : null
            })
            .filter((position) => position !== null)

          console.log('houses:', this.houses)
          console.log('markerPositions:', this.markerPositions)
        } else {
          this.houses = []
          this.markerPositions = []
          console.log('No data found')
        }
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
      this.houseDetails = []
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

    addInterestMarker(interest) {
      if (interest.latitude && interest.longitude) {
        const lat = parseFloat(interest.latitude)
        const lng = parseFloat(interest.longitude)
        if (!isNaN(lat) && !isNaN(lng)) {
          const position = [lat, lng]
          if (
            !this.interestMarkerPositions.some(
              (pos) => pos[0] === position[0] && pos[1] === position[1],
            )
          ) {
            this.interestMarkerPositions.push(position)
          }
        }
      }
    },

    removeInterestMarker(interest) {
      if (interest.latitude && interest.longitude) {
        const lat = parseFloat(interest.latitude)
        const lng = parseFloat(interest.longitude)
        this.interestMarkerPositions = this.interestMarkerPositions.filter(
          (pos) => pos[0] !== lat || pos[1] !== lng,
        )
      }
    },

    clearInterestMarkers() {
      this.interestMarkerPositions = []
    },

    clearMarkerPositions() {
      this.markerPositions = []
    },


    setUpdateMapFlag(flag) {
      this.updateMapFlag = flag

    async searchByAptName(aptName) {
      this.clearHouses()

      try {
        const response = await new Promise((resolve, reject) => {
          getHouseInfoByAptName(
            aptName,
            (response) => {
              if (response.data && Array.isArray(response.data)) {
                // houses 배열 업데이트
                this.houses = response.data.map((house) => ({
                  aptName: house.aptNm,
                  aptSeq: house.aptSeq,
                  latitude: house.latitude,
                  longitude: house.longitude,
                  legalDong: house.umdNm,
                }))

                // markerPositions 업데이트
                this.markerPositions = response.data
                  .filter((house) => house.latitude && house.longitude)
                  .map((house) => {
                    const lat = parseFloat(house.latitude)
                    const lng = parseFloat(house.longitude)
                    return !isNaN(lat) && !isNaN(lng) ? [lat, lng] : null
                  })
                  .filter((position) => position !== null)

                console.log('검색된 아파트:', this.houses)
                console.log('마커 위치:', this.markerPositions)
              }
              resolve(response)
            },
            reject,
          )
        })
        return response.data
      } catch (error) {
        console.error('아파트 검색 실패:', error)
        this.clearHouses()
        return []
      }
    },

    async getHouseListByAptName(aptName) {
      console.log('아파트 이름으로 검색 시작:', aptName)
      this.clearHouses()

      try {
        const response = await new Promise((resolve, reject) => {
          getHouseInfoByAptName(
            aptName,
            (response) => {
              console.log('검색 응답:', response)
              if (response.data && Array.isArray(response.data)) {
                // houses 배열 업데이트 - 동(legalDong) 값이 있는 데이터만 필터링
                this.houses = response.data
                  .filter((house) => house.umdNm) // null이나 빈 문자열이 아닌 데이터만 선택
                  .map((house) => ({
                    aptName: house.aptNm,
                    aptSeq: house.aptSeq,
                    latitude: house.latitude,
                    longitude: house.longitude,
                    legalDong: house.umdNm,
                  }))

                // markerPositions 업데이트 - 동(legalDong)과 좌표가 모두 있는 데이터만 선택
                this.markerPositions = response.data
                  .filter(
                    (house) =>
                      house.umdNm && // 동 값이 있고
                      house.latitude &&
                      house.longitude,
                  )
                  .map((house) => {
                    const lat = parseFloat(house.latitude)
                    const lng = parseFloat(house.longitude)
                    return !isNaN(lat) && !isNaN(lng) ? [lat, lng] : null
                  })
                  .filter((position) => position !== null)

                console.log('검색된 아파트:', this.houses)
                console.log('마커 위치:', this.markerPositions)
              }
              resolve(response)
            },
            (error) => {
              console.error('API 호출 실패:', error)
              reject(error)
            },
          )
        })
        return response.data
      } catch (error) {
        console.error('아파트 검색 실패:', error)
        return []
      }
    },
  },
})

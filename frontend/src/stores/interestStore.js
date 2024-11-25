import { defineStore } from 'pinia'
import { getInterestList, addInterest, removeInterest } from '@/api/interest'
import { useUserStore } from '@/stores/user'
import { useHouseStore } from '@/stores/houseStore'

export const useInterestStore = defineStore('interest', {
  state: () => ({
    interests: [],
    isLoading: false,
    error: null,
    interestDetails: {},
  }),

  getters: {
    getInterests: (state) => state.interests,
    isInterestLoading: (state) => state.isLoading,
    hasError: (state) => state.error !== null,
  },

  actions: {
    // 관심 아파트 목록 조회
    async fetchInterestList() {
      const userStore = useUserStore()
      const houseStore = useHouseStore()

      if (!userStore.isLoggedIn) {
        this.error = '로그인이 필요한 서비스입니다.'
        return
      }

      this.isLoading = true
      this.error = null

      try {
        await getInterestList(
          (response) => {
            this.interests = response.data

            this.updateInterestDetails(houseStore.houses, houseStore.houseDetails)

            houseStore.clearInterestMarkers()
            this.interests.forEach((interest) => {
              if (interest.latitude && interest.longitude) {
                const lat = parseFloat(interest.latitude)
                const lng = parseFloat(interest.longitude)
                if (!isNaN(lat) && !isNaN(lng)) {
                  houseStore.addInterestMarker(interest)
                }
              }
            })
          },
          (error) => {
            console.error('관심 목록 조회 실패:', error)
            this.error = '관심 목록을 불러오는데 실패했습니다.'
            this.interests = []
            houseStore.clearInterestMarkers()
          },
        )
      } catch (error) {
        console.error('관심 목록 조회 중 에러:', error)
        this.error = '관심 목록을 불러오는데 실패했습니다.'
        this.interests = []
        houseStore.clearInterestMarkers()
      } finally {
        this.isLoading = false
      }
    },

    // 관심 아파트 등록
    async addToInterest(aptSeq) {
      const userStore = useUserStore()
      const houseStore = useHouseStore()

      if (!userStore.isLoggedIn) {
        throw new Error('로그인이 필요한 서비스입니다.')
      }

      this.isLoading = true
      this.error = null
      console.log('요청할때 aptSeq : ', aptSeq)
      console.log(typeof aptSeq)
      try {
        await addInterest(
          aptSeq,
          (response) => {
            this.fetchInterestList()
          },
          (error) => {
            console.error('관심 아파트 등록 실패:', error)
            throw error
          },
        )
      } catch (error) {
        this.error = '관심 아파트 등록에 실패했습니다.'
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // 관심 아파트 삭제
    async removeFromInterest(aptSeq) {
      const userStore = useUserStore()
      const houseStore = useHouseStore()

      if (!userStore.isLoggedIn) {
        throw new Error('로그인이 필요한 서비스입니다.')
      }

      this.isLoading = true
      this.error = null

      try {
        await removeInterest(
          aptSeq,
          () => {
            const removedInterest = this.interests.find((interest) => interest.aptSeq === aptSeq)

            // 관심 목록에서 제거
            this.interests = this.interests.filter((interest) => interest.aptSeq !== aptSeq)

            if (removedInterest) {
              // 관심 마커 제거
              houseStore.removeInterestMarker(removedInterest)

              // 해당 위치의 일반 마커가 있는지 확인
              const matchingHouse = houseStore.houses.find(
                (house) =>
                  parseFloat(house.latitude) === parseFloat(removedInterest.latitude) &&
                  parseFloat(house.longitude) === parseFloat(removedInterest.longitude),
              )

              // 일반 마커가 있다면 해당 위치에 일반 마커 추가
              if (matchingHouse) {
                const position = [
                  parseFloat(matchingHouse.latitude),
                  parseFloat(matchingHouse.longitude),
                ]
                if (!houseStore.markerPositions.includes(position)) {
                  houseStore.markerPositions.push(position)
                }
              }
            }

            // interestDetails에서도 제거
            delete this.interestDetails[aptSeq]

            console.log('관심 아파트 목록 : ', this.interests)
          },
          (error) => {
            console.error('관심 아파트 삭제 실패:', error)
            throw error
          },
        )
      } catch (error) {
        this.error = '관심 아파트 삭제에 실패했습니다.'
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // 특정 아파트가 관심 목록에 있는지 확인
    isInterested(aptSeq) {
      if (this.interests.length === 0) {
        return false
      }
      console.log(
        '관심 아파트 목록에 포함 여부 : ',
        this.interests.some((interest) => interest.aptSeq === aptSeq),
      )
      return this.interests.some((interest) => interest.aptSeq === aptSeq)
    },

    // 초기화
    clearInterests() {
      this.interests = []
      this.error = null
      this.isLoading = false
    },

    // 새로운 메서드 추가
    updateInterestDetails(houses, houseDetails) {
      this.interestDetails = {}

      this.interests.forEach((interest) => {
        // 위도/경도로 매칭
        const matchedHouse = houses.find(
          (house) =>
            parseFloat(house.latitude) === parseFloat(interest.latitude) &&
            parseFloat(house.longitude) === parseFloat(interest.longitude),
        )

        if (matchedHouse) {
          const index = houses.indexOf(matchedHouse)
          this.interestDetails[interest.aptSeq] = {
            maxPrice: houseDetails[index]?.maxPrice || '?',
            maxPriceArea: houseDetails[index]?.maxPriceArea || '?',
          }
          console.log('interestDetails : ', this.interestDetails)
        }
      })
    },
  },
})

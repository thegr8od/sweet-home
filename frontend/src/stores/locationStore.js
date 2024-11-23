import { defineStore } from 'pinia'
import axios from 'axios'

export const useLocationStore = defineStore('location', {
  state: () => ({
    locations: [],
    searchResults: [],
    loading: false,
    error: null,
  }),

  actions: {
    // 법정동 코드 데이터 초기 로딩
    async fetchLocationCodes() {
      this.loading = true
      try {
        const response = await axios.get('/api/location/administ', {
          params: {
            serviceKey: import.meta.env.VITE_APP_DATA_API_KEY,
            type: 'json',
            pageNo: '1',
            numOfRows: '1000', // 적절한 수로 조정
          },
        })

        // 응답 데이터 파싱 및 저장
        this.locations = response.data.administ.items.map((item) => ({
          code: item.admCode,
          name: item.admName,
          type: item.admType,
          fullName: `${item.admName} ${item.admType === '동' ? '동' : ''}`,
        }))
      } catch (error) {
        console.error('법정동 코드 조회 실패:', error)
        this.error = '법정동 데이터를 불러오는데 실패했습니다.'
      } finally {
        this.loading = false
      }
    },

    // 검색어로 법정동 검색
    searchLocation(keyword) {
      if (!keyword) {
        this.searchResults = []
        return
      }

      this.searchResults = this.locations
        .filter(
          (location) => location.fullName.includes(keyword) && location.type === '동', // 동 단위만 필터링
        )
        .slice(0, 10) // 최대 10개 결과만 표시
    },
  },
})

// populationStore.js
import { defineStore } from 'pinia'
import axios from 'axios'
import { XMLParser } from 'fast-xml-parser'

export const usePopulationStore = defineStore('population', {
  state: () => ({
    populationData: [],
    loading: false,
    error: null,
    chartData: {
      labels: [],
      datasets: [],
    },
    growthChartData: {
      labels: [],
      datasets: [],
    },
  }),

  actions: {
    async fetchPopulationData(dongName, fromMonth, toMonth) {
      this.loading = true
      this.error = null

      try {
        console.log('검색할 동 이름:', dongName)

        // 행정표준코드 API 호출
        const response = await axios.get(
          'https://apis.data.go.kr/1741000/StanReginCd/getStanReginCdList',
          {
            params: {
              serviceKey: import.meta.env.VITE_ADMIN_REGION_API_KEY,
              pageNo: '1',
              numOfRows: '100',
              type: 'json',
              locatadd_nm: dongName,
            },
          },
        )

        console.log('행정표준코드 API 응답:', response.data)

        const regionInfo = response.data.StanReginCd?.[1]?.row?.[0]
        if (!regionInfo) {
          throw new Error('해당 동 정보를 찾을 수 없습니다.')
        }

        // region_cd 사용
        const dongCode = regionInfo.region_cd
        console.log('법정동 코드:', dongCode)

        // 날짜 형식 변환 (YYYY-MM)
        const srchFrYm = fromMonth.replace('-', '')
        const srchToYm = toMonth.replace('-', '')

        // 전체 데이터 수를 먼저 확인
        const countResponse = await axios.get(
          `https://apis.data.go.kr/1741000/stdgPpltnHhStus/selectStdgPpltnHhStus`,
          {
            params: {
              serviceKey: import.meta.env.VITE_APP_DATA_API_KEY,
              stdgCd: dongCode,
              srchFrYm: srchFrYm,
              srchToYm: srchToYm,
              numOfRows: 1,
              pageNo: 1,
            },
            responseType: 'text',
          }
        )

        const parser = new XMLParser()
        const countData = parser.parse(countResponse.data)
        const totalCount = parseInt(countData.Response?.head?.totalCount || 0)

        // 전체 데이터 가져오기
        const populationResponse = await axios.get(
          `https://apis.data.go.kr/1741000/stdgPpltnHhStus/selectStdgPpltnHhStus`,
          {
            params: {
              serviceKey: import.meta.env.VITE_APP_DATA_API_KEY,
              stdgCd: dongCode,
              srchFrYm: srchFrYm,
              srchToYm: srchToYm,
              numOfRows: totalCount,
              pageNo: 1,
            },
            responseType: 'text',
          }
        )

        const parsedData = parser.parse(populationResponse.data)
        const items = parsedData.Response?.items?.item
        if (!items) {
          throw new Error('인구 데이터를 찾을 수 없습니다.')
        }

        const itemArray = Array.isArray(items) ? items : [items]

        // 월별 총계 데이터 구성
        const monthlyTotals = {}
        itemArray.forEach(item => {
          const month = item.statsYm
          if (!monthlyTotals[month]) {
            monthlyTotals[month] = {
              totalPopulation: 0,
              households: 0,
              male: 0,
              female: 0
            }
          }
          monthlyTotals[month].totalPopulation += Number(item.totNmprCnt || 0)
          monthlyTotals[month].households += Number(item.hhCnt || 0)
          monthlyTotals[month].male += Number(item.maleNmprCnt || 0)
          monthlyTotals[month].female += Number(item.femlNmprCnt || 0)
        })

        // 월별 증감률 계산
        const months = Object.keys(monthlyTotals).sort()
        const growthRates = []

        for (let i = 1; i < months.length; i++) {
          const prevMonth = months[i - 1]
          const currMonth = months[i]
          const prevPop = monthlyTotals[prevMonth].totalPopulation
          const currPop = monthlyTotals[currMonth].totalPopulation
          const growthRate = ((currPop - prevPop) / prevPop * 100).toFixed(2)

          growthRates.push({
            month: currMonth,
            population: currPop,
            growthRate: Number(growthRate)
          })
        }

        // 차트 데이터 업데이트
        this.chartData = {
          labels: months.map(m => `${m.slice(0, 4)}-${m.slice(4)}`),
          datasets: [{
            label: '총 인구수',
            data: months.map(m => monthlyTotals[m].totalPopulation),
            backgroundColor: 'rgb(59, 130, 246)',
            borderColor: 'rgb(59, 130, 246)',
            borderWidth: 1,
            borderRadius: 4,
            barPercentage: 0.4,
            categoryPercentage: 0.9
          }]
        }

        // 증감률 차트 데이터 업데이트
        this.growthChartData = {
          labels: growthRates.map(r => `${r.month.slice(0, 4)}-${r.month.slice(4)}`),
          datasets: [
            // 막대 그래프
            {
              label: '인구 증감률',
              data: growthRates.map(r => r.growthRate),
              backgroundColor: growthRates.map(r =>
                r.growthRate > 0
                  ? 'rgb(239, 68, 68)'
                  : 'rgb(59, 130, 246)'
              ),
              borderColor: growthRates.map(r =>
                r.growthRate > 0
                  ? 'rgb(239, 68, 68)'
                  : 'rgb(59, 130, 246)'
              ),
              borderWidth: 1,
              borderRadius: 4,
              barPercentage: 0.08,    // 막대 더 얇게
              categoryPercentage: 0.9,
              originalData: growthRates.map(r => r.population),
              order: 2
            },
            // 선 그래프
            {
              label: '증감률 추이',
              data: growthRates.map(r => r.growthRate),
              type: 'line',
              borderColor: '#374151',
              borderWidth: 2,
              pointBackgroundColor: 'white',
              pointBorderColor: '#374151',
              pointBorderWidth: 2,
              pointRadius: 4,
              pointHoverRadius: 6,
              fill: false,
              tension: 0.1,
              order: 1
            }
          ]
        }

        // 전체 데이터 저장
        this.populationData = itemArray.map(item => ({
          statsYm: item.statsYm,
          tong: item.tong,
          ban: item.ban,
          totalPopulation: Number(item.totNmprCnt || 0),
          households: Number(item.hhCnt || 0),
          male: Number(item.maleNmprCnt || 0),
          female: Number(item.femlNmprCnt || 0)
        }))

      } catch (error) {
        console.error('데이터 조회 실패:', error)
        this.error = error.message
      } finally {
        this.loading = false
      }
    },
  },
})

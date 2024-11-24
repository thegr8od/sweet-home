<template>
  <div class="purchase-recommendation">
    <h3>매수 추천 분석</h3>
    <div class="score-container">
      <div class="score-item">
        <div class="score-label">실거래가 추이</div>
        <div class="score-value" :class="getPriceScoreClass()">
          {{ priceScore }}점
        </div>
      </div>
      <div class="score-item">
        <div class="score-label">금리 상황</div>
        <div class="score-value" :class="getInterestScoreClass()">
          {{ interestScore }}점
        </div>
      </div>
      <div class="score-item">
        <div class="score-label">GDP 성장률</div>
        <div class="score-value" :class="getGdpScoreClass()">
          {{ gdpScore }}점
        </div>
      </div>
      <div class="score-item">
        <div class="score-label">주변 시세 대비</div>
        <div class="score-value" :class="getAreaPriceScoreClass()">
          {{ areaPriceScore }}점
        </div>
      </div>
    </div>
    <div class="total-score">
      <h4>종합 평가: {{ totalScore }}점</h4>
      <div class="recommendation" :class="getRecommendationClass()">
        {{ getRecommendationText() }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PurchaseRecommendation',
  props: {
    dealHistory: {
      type: Array,
      required: true
    },
    sggCd: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      priceScore: 0,
      interestScore: 0,
      gdpScore: 0,
      areaPriceScore: 0,
      currentInterestRate: 3.50, // 하드코딩된 현재 금리
      currentGdpGrowth: 2.30,    // 하드코딩된 GDP 성장률
      priceChangeRate: 0,
      averagePrice: 0
    }
  },
  computed: {
    totalScore() {
      return Math.round((this.priceScore + this.interestScore + this.gdpScore + this.areaPriceScore) / 4)
    }
  },
  methods: {
    analyzePriceTrend() {
      if (!this.dealHistory || this.dealHistory.length < 3) {
        this.priceScore = 50
        this.priceChangeRate = 0
        return
      }

      // 날짜순으로 정렬 (최신순)
      const sortedDeals = [...this.dealHistory].sort((a, b) => {
        return new Date(b.dealYear, b.dealMonth - 1, b.dealDay) -
               new Date(a.dealYear, a.dealMonth - 1, a.dealDay)
      })

      // 면적별로 그룹화
      const areaGroups = {}
      sortedDeals.forEach(deal => {
        if (!areaGroups[deal.excluUseAr]) {
          areaGroups[deal.excluUseAr] = []
        }
        areaGroups[deal.excluUseAr].push(deal)
      })

      // 각 면적별 변동률 계산
      let totalChangeRate = 0
      let validAreaCount = 0

      for (const area in areaGroups) {
        const deals = areaGroups[area]
        if (deals.length >= 2) {
          const prices = deals.slice(0, 2).map(deal =>
            parseInt(deal.tradeAmount.replace(/,/g, ''))
          )
          const changeRate = ((prices[0] - prices[1]) / prices[1] * 100)
          totalChangeRate += changeRate
          validAreaCount++
        }
      }

      // 평균 변동률 계산
      if (validAreaCount === 0) {
        this.priceScore = 50
        this.priceChangeRate = 0
        return
      }

      this.priceChangeRate = (totalChangeRate / validAreaCount).toFixed(1)
      const rate = parseFloat(this.priceChangeRate)

      // 점수 계산 로직
      let score
      if (rate < -5) {
        // 큰 폭의 하락: 매수 관망 필요
        score = Math.max(30, 50 + rate)
      } else if (rate < -2) {
        // 소폭 하락: 매수 고려 가능
        score = 70 + Math.abs(rate)
      } else if (rate <= 2) {
        // 보합세: 양호
        score = 75
      } else if (rate <= 5) {
        // 적정 상승: 가장 좋은 시점
        score = 85 + (5 - rate) * 3
      } else {
        // 급격한 상승: 과열 우려
        score = Math.max(40, 85 - (rate - 5) * 4)
      }

      this.priceScore = Math.round(score)
    },

    analyzeInterestRate() {
      // 금리가 낮을수록 높은 점수
      this.interestScore = Math.round(100 - (this.currentInterestRate * 10))
      this.interestScore = Math.max(0, Math.min(100, this.interestScore))
    },

    analyzeGdpGrowth() {
      // GDP 성장률이 높을수록 높은 점수
      this.gdpScore = Math.round(this.currentGdpGrowth * 20)
      this.gdpScore = Math.max(0, Math.min(100, this.gdpScore))
    },

    analyzeAreaPrice() {
      if (!this.dealHistory || this.dealHistory.length === 0) {
        this.areaPriceScore = 50
        return
      }

      const recentDeal = this.dealHistory[0]
      const recentPrice = parseInt(recentDeal.tradeAmount.replace(/,/g, ''))

      // 실제로는 API를 통해 주변 시세 정보를 가져와야 함
      // 임시로 랜덤 점수 부여
      this.areaPriceScore = Math.round(Math.random() * 40 + 30)
    },

    getPriceScoreClass() {
      return this.getScoreClass(this.priceScore)
    },

    getInterestScoreClass() {
      return this.getScoreClass(this.interestScore)
    },

    getGdpScoreClass() {
      return this.getScoreClass(this.gdpScore)
    },

    getAreaPriceScoreClass() {
      return this.getScoreClass(this.areaPriceScore)
    },

    getScoreClass(score) {
      if (score >= 80) return 'excellent'
      if (score >= 60) return 'good'
      if (score >= 40) return 'moderate'
      return 'poor'
    },

    getRecommendationClass() {
      return this.getScoreClass(this.totalScore)
    },

    getRecommendationText() {
      const score = this.totalScore
      if (score >= 80) return '매수 적극 추천'
      if (score >= 60) return '매수 추천'
      if (score >= 40) return '매수 중립'
      return '매수 비추천'
    },

    getPriceTrendText() {
      const rate = parseFloat(this.priceChangeRate)
      if (rate < -5) return '하락세 (관망 필요)'
      if (rate < -2) return '소폭 하락 (매수 고려)'
      if (rate <= 2) return '보합세 (안정적)'
      if (rate <= 5) return '상승세 (적정 시점)'
      return '급상승 (과열 우려)'
    },

    getPriceTrendIcon() {
      const rate = parseFloat(this.priceChangeRate)
      const prices = this.dealHistory.slice(0, 3).map(deal =>
        parseInt(deal.tradeAmount.replace(/,/g, ''))
      )
      const firstChange = ((prices[1] - prices[2]) / prices[2] * 100)
      const secondChange = ((prices[0] - prices[1]) / prices[1] * 100)

      if (firstChange < -2 && secondChange > 0) return 'fas fa-arrow-trend-up text-blue-600'
      if (rate > 0 && rate <= 5) return 'fas fa-arrow-up text-green-600'
      if (Math.abs(rate) <= 2) return 'fas fa-minus text-gray-600'
      if (rate > 5) return 'fas fa-arrow-up text-red-600'
      return 'fas fa-arrow-down text-red-400'
    }
  },
  watch: {
    dealHistory: {
      immediate: true,
      handler() {
        this.analyzePriceTrend()
        this.analyzeInterestRate()
        this.analyzeGdpGrowth()
        this.analyzeAreaPrice()
      }
    }
  }
}
</script>

<style scoped>
.purchase-recommendation {
  padding: 20px;
}

.score-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-top: 16px;
}

.score-item {
  background-color: #f8fafc;
  padding: 16px;
  border-radius: 8px;
  text-align: center;
}

.score-label {
  font-size: 14px;
  color: #64748b;
  margin-bottom: 8px;
}

.score-value {
  font-size: 20px;
  font-weight: 600;
}

.excellent {
  color: #2563eb;
}

.good {
  color: #059669;
}

.moderate {
  color: #d97706;
}

.poor {
  color: #dc2626;
}

.total-score {
  margin-top: 24px;
  text-align: center;
}

.total-score h4 {
  margin-bottom: 12px;
}

.recommendation {
  margin-top: 12px;
  font-size: 18px;
  font-weight: 600;
}
</style>

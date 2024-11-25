<template>
  <div class="purchase-recommendation">
    <div class="recommendation-header">
      <h3>매수 추천 분석</h3>
      <p class="disclaimer">* 본 분석은 참고용 지표입니다.</p>
    </div>
    <div class="score-container">
      <div class="score-item">
        <div class="score-label">실거래가 추이</div>
        <div class="score-value" :class="getPriceScoreClass()">{{ priceScore }}점</div>
      </div>
      <div class="score-item">
        <div class="score-label">금리 상황</div>
        <div class="score-value" :class="getInterestScoreClass()">{{ interestScore }}점</div>
      </div>
      <div class="score-item">
        <div class="score-label">GDP 성장률</div>
        <div class="score-value" :class="getGdpScoreClass()">{{ gdpScore }}점</div>
      </div>
      <div class="score-item">
        <div class="score-label">주변 시세 대비</div>
        <div class="score-value" :class="getAreaPriceScoreClass()">{{ areaPriceScore }}점</div>
      </div>
    </div>
    <div class="total-score">
      <h4>종합 평가: {{ totalScore }}점</h4>
      <div class="recommendation" :class="getRecommendationClass()">
        {{ getRecommendationText() }}
      </div>
    </div>
    <div class="ai-analysis-section">
      <button v-if="!isAnalyzing && !aiAnalysis" @click="startAIAnalysis" class="analyze-button">
        <span class="button-text">AI 상세 분석하기</span>
      </button>

      <div v-if="isAnalyzing || aiAnalysis" class="ai-analysis">
        <h4>AI 상세 분석</h4>

        <div v-if="isAnalyzing" class="loading">
          <div class="loading-spinner"></div>
          <span>분석 중...</span>
        </div>

        <div v-else-if="aiAnalysis" class="analysis-content" v-html="formattedAiAnalysis"></div>
        <div v-else-if="aiError" class="error-message">{{ aiError }}</div>

        <button v-if="!isAnalyzing" @click="resetAnalysis" class="reset-button">
          다시 분석하기
        </button>
      </div>
    </div>
  </div>
</template>

<script>
const OPENAI_API_KEY = import.meta.env.VITE_OPENAI_API_KEY

export default {
  name: 'PurchaseRecommendation',
  props: {
    dealHistory: {
      type: Array,
      required: true,
    },
    sggCd: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      priceScore: 0,
      interestScore: 0,
      gdpScore: 0,
      areaPriceScore: 0,
      currentInterestRate: 3.5, // 하드코딩된 현재 금리
      currentGdpGrowth: 2.3, // 하드코딩된 GDP 성장률
      priceChangeRate: 0,
      averagePrice: 0,
      aiAnalysis: null,
      isAnalyzing: false,
      aiError: null,
    }
  },
  computed: {
    totalScore() {
      return Math.round(
        (this.priceScore + this.interestScore + this.gdpScore + this.areaPriceScore) / 4,
      )
    },
    formattedAiAnalysis() {
      return this.aiAnalysis?.replace(/\n/g, '<br>')
    },
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
        return (
          new Date(b.dealYear, b.dealMonth - 1, b.dealDay) -
          new Date(a.dealYear, a.dealMonth - 1, a.dealDay)
        )
      })

      // 면적별로 그룹화
      const areaGroups = {}
      sortedDeals.forEach((deal) => {
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
          const prices = deals
            .slice(0, 2)
            .map((deal) => parseInt(deal.tradeAmount.replace(/,/g, '')))
          const changeRate = ((prices[0] - prices[1]) / prices[1]) * 100
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
      this.interestScore = Math.round(100 - this.currentInterestRate * 10)
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
      if (score >= 80) return '매수 극추천'
      if (score >= 60) return '매수 추천'
      if (score >= 40) return '매수 보류'
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
      const prices = this.dealHistory
        .slice(0, 3)
        .map((deal) => parseInt(deal.tradeAmount.replace(/,/g, '')))
      const firstChange = ((prices[1] - prices[2]) / prices[2]) * 100
      const secondChange = ((prices[0] - prices[1]) / prices[1]) * 100

      if (firstChange < -2 && secondChange > 0) return 'fas fa-arrow-trend-up text-blue-600'
      if (rate > 0 && rate <= 5) return 'fas fa-arrow-up text-green-600'
      if (Math.abs(rate) <= 2) return 'fas fa-minus text-gray-600'
      if (rate > 5) return 'fas fa-arrow-up text-red-600'
      return 'fas fa-arrow-down text-red-400'
    },

    async getAIAnalysis() {
      if (!this.dealHistory?.length) return

      this.isAnalyzing = true
      try {
        const prompt = this.generateAnalysisPrompt()
        const response = await fetch('https://api.openai.com/v1/chat/completions', {
          method: 'POST',
          headers: {
            Authorization: `Bearer ${OPENAI_API_KEY}`,
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            model: 'gpt-4',
            messages: [
              {
                role: 'system',
                content:
                  '당신은 부동산 전문 분석가입니다. 주어진 데이터를 바탕으로 매수 시점에 대한 전문적인 분석을 제공합니다.',
              },
              {
                role: 'user',
                content: prompt,
              },
            ],
            temperature: 0.7,
            max_tokens: 1000,
          }),
        })

        const data = await response.json()
        this.aiAnalysis = data.choices[0].message.content
      } catch (error) {
        console.error('AI 분석 중 오류 발생:', error)
        this.aiAnalysis = '분석 중 오류가 발생했습니다.'
      } finally {
        this.isAnalyzing = false
      }
    },

    generateAnalysisPrompt() {
      const recentDeals = this.dealHistory.slice(0, 5)
      const priceData = recentDeals.map((deal) => ({
        date: `${deal.dealYear}-${deal.dealMonth}-${deal.dealDay}`,
        price: parseInt(deal.tradeAmount.replace(/,/g, '')),
        area: deal.excluUseAr,
      }))

      return `
다음 데이터를 바탕으로 매수 ���점에 대한 전문적인 분석을 제공해주세요:

1. 실거래가 변동률: ${this.priceChangeRate}%
2. 현재 기준금리: ${this.currentInterestRate}%
3. GDP 성장률: ${this.currentGdpGrowth}%
4. 최근 거래 기록:
${JSON.stringify(priceData, null, 2)}

다음 항목들을 포함해서 분석해주세요:
1. 가격 동향 분석
2. 투자 위험도
3. 향후 가격 전망
4. 매수 시점 추천
`
    },

    async startAIAnalysis() {
      this.getAIAnalysis()
    },

    resetAnalysis() {
      this.aiAnalysis = null
      this.aiError = null
      this.startAIAnalysis()
    },
  },
  watch: {
    dealHistory: {
      immediate: true,
      handler() {
        this.aiAnalysis = null
        this.isAnalyzing = false
        this.aiError = null

        this.analyzePriceTrend()
        this.analyzeInterestRate()
        this.analyzeGdpGrowth()
        this.analyzeAreaPrice()
      },
    },
  },
}
</script>

<style scoped>
.purchase-recommendation {
  padding: 20px;
}

.recommendation-header {
  margin-bottom: 20px;
}

.recommendation-header h3 {
  margin-bottom: 8px;
  color: #1f2937;
  font-size: 1.25rem;
  font-weight: 600;
}

.disclaimer {
  font-size: 0.875rem;
  color: #6b7280;
  line-height: 1.25rem;
  margin-top: 4px;
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

.ai-analysis-section {
  margin-top: 24px;
  border-top: 1px solid #e5e7eb;
  padding-top: 20px;
}

.analyze-button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  padding: 12px;
  background-color: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  color: #1f2937;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.analyze-button:hover {
  background-color: #f1f5f9;
  border-color: #cbd5e1;
}

.button-icon {
  font-size: 1.25rem;
}

.button-text {
  color: #1f2937;
}

.ai-analysis {
  margin-top: 16px;
  padding: 16px;
  background-color: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
}

.ai-analysis h4 {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #1f2937;
  font-size: 1rem;
  font-weight: 600;
  margin-bottom: 16px;
}

.ai-analysis h4::before {
  content: '🤖';
  font-size: 1.25rem;
}

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 24px 0;
}

.loading-spinner {
  width: 28px;
  height: 28px;
  border: 3px solid #e2e8f0;
  border-top: 3px solid #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.analysis-content {
  font-size: 0.875rem;
  line-height: 1.5;
  color: #374151;
  white-space: pre-line;
}

.reset-button {
  margin-top: 16px;
  padding: 8px 16px;
  background-color: #f1f5f9;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  color: #4b5563;
  font-size: 0.75rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.reset-button:hover {
  background-color: #e2e8f0;
  color: #1f2937;
}

.error-message {
  padding: 12px;
  background-color: #fef2f2;
  border: 1px solid #fee2e2;
  border-radius: 6px;
  color: #dc2626;
  font-size: 0.875rem;
  margin-top: 8px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>

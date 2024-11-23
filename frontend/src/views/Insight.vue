<template>
  <div class="max-w-7xl mx-auto px-4 mt-20">
    <h1 class="text-2xl font-bold mb-6">부동산 뉴스</h1>

    <div v-if="loading" class="text-center py-10">
      <p>뉴스를 불러오는 중입니다...</p>
    </div>

    <div v-else-if="error" class="text-center py-10 text-red-500">
      <p>{{ error }}</p>
    </div>

    <div
      v-else-if="newsItems.length > 0"
      class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
    >
      <div v-for="news in newsItems" :key="news.link" class="bg-white rounded-lg shadow-md p-4">
        <a :href="news.link" target="_blank" rel="noopener noreferrer">
          <img
            :src="news.image"
            :alt="news.title"
            class="w-full h-48 object-cover rounded-md mb-4"
          />
          <h2 class="text-lg font-semibold mb-2" v-html="news.title"></h2>
          <p class="text-gray-600 text-sm mb-2" v-html="news.description"></p>
          <p class="text-gray-400 text-xs">{{ news.date }}</p>
        </a>
      </div>
    </div>

    <div v-else class="text-center py-10">
      <p>표시할 뉴스가 없습니다.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
// 이미지 import
import apartmentImg from '@/assets/img/insight/apartment.jpg'
import houseImg from '@/assets/img/insight/house.jpg'
import salesImg from '@/assets/img/insight/sales.jpg'
import subscriptionImg from '@/assets/img/insight/subscription.jpg'
import rentImg from '@/assets/img/insight/rent.jpg'
import loanImg from '@/assets/img/insight/loan.jpg'
const getCategoryImage = (description) => {
  const categoryImages = {
    // 금융/대출/정책 관련 이미지 (비즈니스/문서/회의 이미지)
    금융관련: [
      'https://images.unsplash.com/photo-1554224155-6726b3ff858f',
      'https://images.unsplash.com/photo-1590283603385-17ffb3a7f29f',
      'https://images.unsplash.com/photo-1579532537598-459ecdaf39cc',
      'https://images.unsplash.com/photo-1604594849809-dfedbc827105',
      'https://images.unsplash.com/photo-1434626881859-194d67b2b86f',
      'https://images.unsplash.com/photo-1450101499163-c8848c66ca85',
      'https://images.unsplash.com/photo-1507679799987-c73779587ccf',
      'https://images.unsplash.com/photo-1591696205602-2f950c417cb9',
      'https://images.unsplash.com/photo-1554224154-26032ffc0d07',
      'https://images.unsplash.com/photo-1626266061368-46a8f578ddd6',
    ],
    // 부동산/매매/아파트 관련 이미지 (건물/주택 이미지)
    부동산관련: [
      'https://images.unsplash.com/photo-1545324418-cc1a3fa10c00',
      'https://images.unsplash.com/photo-1560448204-603b3fc33ddc',
      'https://images.unsplash.com/photo-1518780664697-55e3ad937233',
      'https://images.unsplash.com/photo-1576941089067-2de3c901e126',
      'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267',
      'https://images.unsplash.com/photo-1560518883-ce09059eeffa',
      'https://images.unsplash.com/photo-1582407947304-fd86f028f716',
      'https://images.unsplash.com/photo-1585543805890-6051f7829f98',
      'https://images.unsplash.com/photo-1460472178825-e5240623afd5',
    ],
  }
  // 키워드 그룹 정의
  const keywordGroups = {
    금융관련: ['대출', '금리', '이자', '주담대', '규제', '정책', '시장', '금융', '은행', '담보'],
    부동산관련: [
      '아파트',
      '주택',
      '빌라',
      '오피스텔',
      '원룸',
      '매매',
      '분양',
      '청약',
      '전세',
      '월세',
      '재건축',
      '재개발',
      '입주',
      '부동산',
      '집값',
      '임대',
    ],
  }
  // 키워드 매칭 및 랜덤 이미지 선택
  for (const [group, keywords] of Object.entries(keywordGroups)) {
    if (keywords.some((keyword) => description.includes(keyword))) {
      const images = categoryImages[group]
      const randomIndex = Math.floor(Math.random() * images.length)
      return images[randomIndex]
    }
  }
  // 매칭되는 키워드가 없을 경우 모든 이미지 중에서 랜덤 선택
  const allImages = [...categoryImages.금융관련, ...categoryImages.부동산관련]
  const randomIndex = Math.floor(Math.random() * allImages.length)
  return allImages[randomIndex]
}
// 사용된 이미지 추적을 위한 Set (중복 최소화)
const usedImages = new Set()
const newsItems = ref([])
const loading = ref(true)
const error = ref(null)
const fetchNews = async () => {
  try {
    loading.value = true
    error.value = null
    usedImages.clear() // 새로운 fetch 시작 시 초기화
    const response = await axios.get('/api/news')
    newsItems.value = response.data.items.map((item) => {
      let image
      let attempts = 0
      const maxAttempts = 5 // 최대 시도 횟수
      // 이미 사용된 이미지를 피해서 새로운 이미지 선택
      do {
        image = getCategoryImage(item.description)
        attempts++
      } while (usedImages.has(image) && attempts < maxAttempts)
      usedImages.add(image)
      return {
        title: item.title.replace(/(<([^>]+)>)/gi, ''),
        link: item.link,
        description: item.description.replace(/(<([^>]+)>)/gi, ''),
        date: new Date(item.pubDate).toLocaleDateString('ko-KR'),
        image: image,
      }
    })
  } catch (error) {
    console.error('뉴스를 불러오는데 실패했습니다:', error)
    if (error.response?.status === 403) {
      error.value = '네이버 검색 API 권한이 필요합니다. 개발자 센터에서 검색 API를 활성화해주세요.'
    } else {
      error.value = '뉴스를 불러오는데 실패했습니다.'
    }
  } finally {
    loading.value = false
  }
}
onMounted(() => {
  fetchNews()
})
</script>

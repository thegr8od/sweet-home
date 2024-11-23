import axios from 'axios'
import { API_BASE_URL, APT_DEAL_URL, APT_DETAIL_URL1, APT_DETAIL_URL2 } from '@/config/index.js'

// axios 객체 생성
function apiInstance() {
  const instance = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-type': 'application/json',
    },
  })

  // 요청 인터셉터 수정
  instance.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem('token')
      if (token) {
        // 토큰에서 불필요한 공백 제거 및 Bearer 접두사 확인
        const cleanToken = token.replace(/^Bearer\s+/, '').trim()
        config.headers.Authorization = `Bearer ${cleanToken}`
      }
      return config
    },
    (error) => {
      return Promise.reject(error)
    },
  )

  // 응답 인터셉터 추가
  instance.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 401) {
        // 토큰이 만료되었거나 유효하지 않은 경우
        localStorage.removeItem('token') // 토큰 제거
        // 로그인 페이지로 리다이렉트 또는 다른 처리
        window.location.href = '/login'
      }
      return Promise.reject(error)
    }
  )

  return instance
}

function houseInstance() {
  const instance = axios.create({
    baseURL: APT_DEAL_URL,
    headers: {
      'Content-type': 'application/json',
    },
  })
  return instance
}

const KAKAO_SERVICE_KEY = '56b9eb363581e27c66bd552d48937f74'
function kakaoInstance() {
  const instance = axios.create({
    baseURL: 'https://dapi.kakao.com/v2/local',
    headers: {
      'Content-type': 'application/json',
      Authorization: `KakaoAK ${KAKAO_SERVICE_KEY}`,
    },
  })
  return instance
}

const SERVICE_KEY = ''
function detailInstance1() {
  const instance = axios.create({
    baseURL: APT_DETAIL_URL1 + '?serviceKey=' + SERVICE_KEY,
    headers: {
      'Content-type': 'application/json',
    },
  })
  return instance
}

function detailInstance2() {
  const instance = axios.create({
    baseURL: APT_DETAIL_URL2 + '?serviceKey=' + SERVICE_KEY,
    headers: {
      'Content-type': 'application/json',
    },
  })
  return instance
}

// 카카오 로그인용 인스턴스 추가
function authInstance() {
  const instance = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-type': 'application/json',
    },
  })
  return instance
}

export { apiInstance, houseInstance, kakaoInstance, detailInstance1, detailInstance2, authInstance }

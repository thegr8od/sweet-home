import { apiInstance } from './index.js'
const api = apiInstance()

// 지역 목록 조회 - 시도, 구군, 동
export function locationList(params, success, fail) {
  if (!params.type) {
    console.error('type 파라미터가 필요합니다 (sido, gugun, dong)')
    return
  }
  api.get(`house/location`, { params }).then(success).catch(fail)
}

export function getSidoList(success, fail) {
  locationList({ type: 'sido' }, success, fail)
}

export function getGugunList(params, success, fail) {
  console.log('params:', params)
  locationList({ type: 'gugun', ...params }, success, fail)
}

export function getDongList(params, success, fail) {
  locationList({ type: 'dong', ...params }, success, fail)
}

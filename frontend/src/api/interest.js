import { apiInstance } from './index.js'
const api = apiInstance()

/**
 * 관심 아파트 목록 조회
 * JWT 토큰은 apiInstance의 인터셉터에서 자동으로 처리됨
 */
async function getInterestList(success, fail) {
  await api.get(`/interest/list`).then(success).catch(fail)
}

/**
 * 관심 아파트 등록
 * @param {string} aptSeq - 관심 아파트 정보 (aptSeq 필수)
 */
async function addInterest(aptSeq, success, fail) {
    console.log('전송할 아파트 번호: ', aptSeq);
  await api.post(`/interest/${aptSeq}`).then(success).catch(fail)
}

/**
 * 관심 아파트 삭제
 * @param {string} aptSeq - 아파트 고유 번호
 */
async function removeInterest(aptSeq, success, fail) {
  await api.delete(`/interest/${aptSeq}`).then(success).catch(fail)
}

export { getInterestList, addInterest, removeInterest } 
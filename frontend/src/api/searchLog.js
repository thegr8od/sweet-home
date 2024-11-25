import { apiInstance } from './index.js'
const api = apiInstance()

function saveSearchLog(searchKeyword, success, fail) {
  api.post(`/searchlog`, null, { params: { searchKeyword } }).then(success).catch(fail)
}

function getPopularSearchKeywords(limit, success, fail) {
  api.get(`/searchlog/popular`, { params: { limit } }).then(success).catch(fail)
}

export { saveSearchLog, getPopularSearchKeywords }

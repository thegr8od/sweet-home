import { apiInstance } from './index.js'
const api = apiInstance()

function houseDealListByAddress(params, success, fail) {
  api.get(`/house/deal/address`, { params: params }).then(success).catch(fail)
}

function houseDealListByAptSeq(aptSeq, success, fail) {
  api.get(`/house/deal/apt`, { params: aptSeq }).then(success).catch(fail)
}

function getHouseInfoByAptSeq(aptSeq, success, fail) {
  api.get(`/house/${aptSeq.aptSeq}`).then(success).catch(fail)
}

export { houseDealListByAddress, getHouseInfoByAptSeq, houseDealListByAptSeq }

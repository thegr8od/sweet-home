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

function getHouseDealsByMapBounds(bounds, success, fail) {
  api
    .get(`/house/map/area`, {
      params: {
        minLat: bounds.minLat,
        maxLat: bounds.maxLat,
        minLng: bounds.minLng,
        maxLng: bounds.maxLng,
        limit: bounds.limit,
      },
    })
    .then(success)
    .catch(fail)
}

function getHouseInfoByAptName(aptName, success, fail) {
  api
    .get(`/house/info`, { params: { aptName: aptName } })
    .then(success)
    .catch(fail)
}

export {
  houseDealListByAddress,
  getHouseInfoByAptSeq,
  houseDealListByAptSeq,
  getHouseDealsByMapBounds,
  getHouseInfoByAptName,
}

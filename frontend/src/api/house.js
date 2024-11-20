import { apiInstance, detailInstance1, detailInstance2 } from './index.js'

const api = apiInstance()
const detail1 = detailInstance1()
const detail2 = detailInstance2()

function sidoList(success, fail) {
  api.get(`/address/sido`).then(success).catch(fail)
}

function gugunList(params, success, fail) {
  api.get(`/address/gugun`, { params: params }).then(success).catch(fail)
}

function dongList(params, success, fail) {
  console.log('Request params:', params) // 여기서 sidoName과 gugunName 값 확인
  api.get(`/address/dong`, { params: params }).then(success).catch(fail)
}

function houseList(params, success, fail) {
  api.get(`/deal/dong`, { params: params }).then(success).catch(fail)
}

function houseListByAddress(params, success, fail) {
  api.get(`/deal/list`, { params: params }).then(success).catch(fail)
}

function housDeal(params, success, fail) {
  api.get(`/deal/deal`, { params: params }).then(success).catch(fail)
}

function houseAddress(params, success, fail) {
  api.get(`/deal/address`, { params: params }).then(success).catch(fail)
}

function houseName(params, success, fail) {
  api.get(`/deal/name`, { params: params }).then(success).catch(fail)
}

function getKapt(params, success, fail) {
  //console.log("houseinfo", params);
  api.get(`/deal/kapt`, { params: params }).then(success).catch(fail)
}

function houseDetail1(params, success, fail) {
  // detail1.get(``, { params: params }).then(success).catch(fail);
  api.get(`/api/detail1`, { params: params }).then(success).catch(fail)
}
function houseDetail2(params, success, fail) {
  // detail2.get(``, { params: params }).then(success).catch(fail);
  api.get(`/api/detail2`, { params: params }).then(success).catch(fail)
}

export {
  sidoList,
  gugunList,
  dongList,
  houseList,
  housDeal,
  houseAddress,
  houseName,
  getKapt,
  houseDetail1,
  houseDetail2,
  houseListByAddress,
}

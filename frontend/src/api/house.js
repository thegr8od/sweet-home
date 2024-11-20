import { apiInstance } from './index.js'
const api = apiInstance()
// const detail1 = detailInstance1()
// const detail2 = detailInstance2()

function houseDealListByAddress(params, success, fail) {
  api.get(`/house/houseDealListByAddress`, { params: params }).then(success).catch(fail)
}

// function houseList(params, success, fail) {
//   api.get(`/deal/dong`, { params: params }).then(success).catch(fail)
// }

// function housDeal(params, success, fail) {
//   api.get(`/deal/deal`, { params: params }).then(success).catch(fail)
// }

// function houseAddress(params, success, fail) {
//   api.get(`/deal/address`, { params: params }).then(success).catch(fail)
// }

// function houseName(params, success, fail) {
//   api.get(`/deal/name`, { params: params }).then(success).catch(fail)
// }

// function getKapt(params, success, fail) {
//   //console.log("houseinfo", params);
//   api.get(`/deal/kapt`, { params: params }).then(success).catch(fail)
// }

// function houseDetail1(params, success, fail) {
//   // detail1.get(``, { params: params }).then(success).catch(fail);
//   api.get(`/api/detail1`, { params: params }).then(success).catch(fail)
// }
// function houseDetail2(params, success, fail) {
//   // detail2.get(``, { params: params }).then(success).catch(fail);
//   api.get(`/api/detail2`, { params: params }).then(success).catch(fail)
// }

export { houseDealListByAddress }

import { apiInstance } from './index.js'

const api = apiInstance()

async function login(user, success, fail) {
  await api.post(`/user/login`, JSON.stringify(user)).then(success).catch(fail)
}

async function findById(userid, success, fail) {
  //console.log("인증할 아이디:", userid);

  api.defaults.headers['access-token'] = sessionStorage.getItem('access-token')
  await api.get(`/user/info/${userid}`).then(success).catch(fail)
}

async function signup(user, success, fail) {
  await api.post(`/user/regist`, JSON.stringify(user)).then(success).catch(fail)
}

async function update(user, success, fail) {
  await api.put(`/user/update`, JSON.stringify(user)).then(success).catch(fail)
}

// function logout(success, fail)

export { login, findById, signup, update }

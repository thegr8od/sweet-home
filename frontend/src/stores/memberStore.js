// import { defineStore } from 'pinia'
// import { jwtDecode } from 'jwt-decode'
// import { login, update } from '@/api/member.js'
// import { findById } from '@/api/member'

// export const useMemberStore = defineStore('member', {
//   state: () => ({
//     isLogin: false,
//     isLoginError: false,
//     userInfo: null,
//     isAdmin: false,
//     /// 테스트용
//     // isLogin: true,
//     // isLoginError: false,
//     // userInfo: {
//     //   id: "admin",
//     //   password: "admin",
//     //   email: "admin",
//     //   age: "1",
//     //   name: "admin",
//     // },
//     // isAdmin: true,
//     // end 테스트 값
//   }),
//   getters: {
//     checkUserInfo: (state) => state.userInfo,
//   },
//   actions: {
//     async userConfirm(user) {
//       await login(
//         user,
//         (response) => {
//           if (response.data.message === 'success') {
//             const token = response.data['access-token']
//             this.isLogin = true
//             this.isLoginError = false
//             sessionStorage.setItem('access-token', token)
//             if (user.id === 'admin') this.isAdmin = true
//           } else {
//             this.isLogin = false
//             this.isLoginError = true
//           }
//         },
//         () => {},
//       )
//     },
//     logOut() {
//       if (sessionStorage.getItem('access-token')) {
//         sessionStorage.removeItem('access-token')
//       }
//       this.testLogout()
//     },
//     async getUserInfo(token) {
//       const decode_token = jwt_decode(token)
//       findById(
//         decode_token.userid,
//         (response) => {
//           if (response.data.message === 'success') {
//             this.userInfo = response.data.userInfo
//           } else {
//             console.error('유저 정보 없음!!')
//           }
//         },
//         (error) => {
//           console.error(error)
//         },
//       )
//     },
//     async updateUserInfo(user) {
//       await update(
//         user,
//         (res) => {
//           this.userInfo = res.data
//           alert('회원 정보 수정 완료 !')
//         },
//         () => {},
//       )
//     },
//     // UI 테스트용 함수
//     testAdmin() {
//       this.isLogin = true
//       this.isAdmin = true
//       // this.userInfo = {
//       //   id: "admin",
//       //   password: "admin",
//       //   email: "admin",
//       //   age: "1",
//       //   name: "admin",
//       // };
//     },
//     testUser() {
//       this.isLogin = true
//       this.isAdmin = false
//       // this.userInfo = {
//       //   id: "ssafy",
//       //   password: "ssfay",
//       //   email: "ssafy123",
//       //   age: "1",
//       //   name: "ssafy",
//       // };
//     },
//     testLogout() {
//       sessionStorage.removeItem('access-token')
//       this.userInfo = null
//       this.isLogin = false
//       this.isAdmin = false
//     },
//   },
// })

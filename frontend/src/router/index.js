import { createRouter, createWebHistory } from 'vue-router'
import Register from '@/components/user/Register.vue'
import Insight from '@/views/Insight.vue'
import Population from '@/views/Population.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'map',
      component: () => import('../views/MapView.vue'),
    },
    {
      path: '/house',
      name: 'house',
      component: () => import('../views/HouseView.vue'),
    },
    {
      path: '/qna',
      name: 'qna',
      component: () => import('../views/QnaView.vue'),
      children: [
        {
          path: '',
          name: 'qna-list',
          component: () => import('../components/board/qna/QnaList.vue'),
        },
        {
          path: 'write',
          name: 'qna-write',
          component: () => import('../components/board/qna/WritePost.vue'),
        },
        {
          path: 'post/:id',
          name: 'qna-view',
          component: () => import('../components/board/qna/ViewPost.vue'),
        },
      ],
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
    },
    {
      path: '/mypage',
      name: 'MyPage',
      component: () => import('@/components/user/MyPage.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/register',
      name: 'Register',
      component: Register,
    },
    {
      path: '/change-password',
      name: 'ChangePassword',
      component: () => import('@/views/ChangePasswordView.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/insight',
      name: 'Insight',
      component: Insight,
    },
    {
      path: '/population',
      name: 'Population',
      component: Population,
    },
    {
      path: '/oauth/callback/kakao',
      name: 'KakaoCallback',
      component: () => import('@/views/KakaoCallback.vue'),
    },
  ],
})

export default router

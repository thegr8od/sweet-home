import { createRouter, createWebHistory } from 'vue-router'

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
  ],
})

export default router

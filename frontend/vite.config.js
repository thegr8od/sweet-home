import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), vueDevTools()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    proxy: {
      '/api/news': {
        target: 'https://openapi.naver.com',
        changeOrigin: true,
        rewrite: (path) => {
          const query = encodeURIComponent('부동산')
          return `/v1/search/news.json?query=${query}&display=20`
        },
        configure: (proxy, options) => {
          proxy.on('proxyReq', (proxyReq, req, res) => {
            proxyReq.setHeader('X-Naver-Client-Id', 'kXiw1sVLnQcrPOnyfBUC')
            proxyReq.setHeader('X-Naver-Client-Secret', 'x7ALAQA0OR')
          })
        },
      },
      '/api/population': {
        target: 'https://apis.data.go.kr',
        changeOrigin: true,
        rewrite: (path) => {
          if (path.includes('stdgPpltnHhStus')) {
            return path.replace('/api/population', '/1741000/stdgPpltnHhStus/selectStdgPpltnHhStus')
          }
          return path.replace('/api/population', '/1741000/StanReginCd/getStanReginCdList')
        }
      },
      '/api/location': {
        target: 'http://apis.data.go.kr/1741000/AdminCodeInfoService',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/location/, ''),
      },
    },
  },
})

/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{vue,js,ts,jsx,tsx}', // 필요한 파일 경로에 따라 수정
  ],
  theme: {
    extend: {
      fontFamily: {
        spoqa: ['Spoqa Han Sans Neo', 'sans-serif'],
      },
      fontWeight: {
        light: 300,
        normal: 400,
        medium: 500,
        bold: 700,
      },
    },
  },
  plugins: [],
}

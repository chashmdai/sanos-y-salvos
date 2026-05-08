/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'sanos-blue': '#1e3a8a',
        'sanos-orange': '#f97316',
      }
    },
  },
  plugins: [],
}
/** @type {import('tailwindcss').Config} */
module.exports = {
	content: [
		'./index.html',
		'./src/**/*.{ts,tsx}',
	],
	theme: {
		extend: {
			colors: {
				primary: {
					DEFAULT: '#2563EB',
					50: '#E8F0FE',
					100: '#DDE9FE',
					200: '#C3D8FD',
					300: '#9AC0FB',
					400: '#6EA1F7',
					500: '#4C8AF3',
					600: '#2563EB',
					700: '#1D4ED8',
					800: '#1E40AF',
					900: '#1E3A8A',
				},
				accent: '#22C55E'
			},
			boxShadow: {
				soft: '0 10px 25px -10px rgba(0,0,0,0.2)'
			}
		}
	},
	plugins: []
} 
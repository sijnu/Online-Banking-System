import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
	  base: '/onlinebanking/', // This ensures all asset paths are prefixed correctly

	plugins: [react()],
	server: {
		port: 5173,
		proxy: {
			'/auth': 'http://localhost:8080',
			'/admin': 'http://localhost:8080',
			'/user': 'http://localhost:8080'
		}
	}
}) 
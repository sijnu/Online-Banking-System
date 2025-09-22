import axios, { AxiosRequestConfig } from 'axios'

const client = axios.create()

client.interceptors.request.use((config: AxiosRequestConfig) => {
	const token = localStorage.getItem('token')
	if (token) {
		config.headers = config.headers ?? {}
		;(config.headers as any)['Authorization'] = `Bearer ${token}`
	}
	return config
})

export default client 
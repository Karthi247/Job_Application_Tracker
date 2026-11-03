import axios from 'axios'

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  headers: { 'Content-Type': 'application/json' },
  timeout: 15000,
})

apiClient.interceptors.request.use((config) => {
  const token = sessionStorage.getItem('accessToken')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

apiClient.interceptors.response.use((response) => response, (error) => {
  const url = error.config?.url || ''
  const isPublicAuthRequest = url.includes('/auth/login') || url.includes('/auth/signup')
  if (error.response?.status === 401 && !isPublicAuthRequest) {
    sessionStorage.removeItem('accessToken')
    window.dispatchEvent(new Event('auth:unauthorized'))
  }
  return Promise.reject(error)
})

export function apiErrorMessage(error, fallback = 'Something went wrong. Please try again.') {
  if (!error.response) return 'Could not reach the server. Check your connection and try again.'
  return error.response.data?.message || fallback
}

export default apiClient

import apiClient from './apiClient'

const authService = {
  async login(credentials) {
    const response = await apiClient.post('/auth/login', credentials)
    return response.data
  },
  async signup(profile) {
    const response = await apiClient.post('/auth/signup', profile)
    return response.data
  },
  async me() {
    const response = await apiClient.get('/auth/me')
    return response.data
  },
}

export default authService

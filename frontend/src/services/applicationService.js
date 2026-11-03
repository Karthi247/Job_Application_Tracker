import apiClient from './apiClient'

const applicationService = {
  async list(filters = {}) {
    const response = await apiClient.get('/applications', { params: filters })
    return response.data
  },
  async get(id) {
    const response = await apiClient.get(`/applications/${id}`)
    return response.data
  },
  async create(application) {
    const response = await apiClient.post('/applications', application)
    return response.data
  },
  async update(id, application) {
    const response = await apiClient.put(`/applications/${id}`, application)
    return response.data
  },
  async updateStatus(id, status) {
    const response = await apiClient.patch(`/applications/${id}/status`, { status })
    return response.data
  },
  async remove(id) {
    await apiClient.delete(`/applications/${id}`)
  },
}

export default applicationService

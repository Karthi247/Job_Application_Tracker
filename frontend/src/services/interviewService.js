import apiClient from './apiClient'

const interviewService = {
  async list(params = {}) { return (await apiClient.get('/interviews', { params })).data },
  async get(id) { return (await apiClient.get(`/interviews/${id}`)).data },
  async create(interview) { return (await apiClient.post('/interviews', interview)).data },
  async update(id, interview) { return (await apiClient.put(`/interviews/${id}`, interview)).data },
  async remove(id) { await apiClient.delete(`/interviews/${id}`) },
}

export default interviewService

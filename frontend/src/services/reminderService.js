import apiClient from './apiClient'

const reminderService = {
  async list(params = {}) { return (await apiClient.get('/reminders', { params })).data },
  async create(reminder) { return (await apiClient.post('/reminders', reminder)).data },
  async update(id, reminder) { return (await apiClient.put(`/reminders/${id}`, reminder)).data },
  async complete(id) { return (await apiClient.patch(`/reminders/${id}/complete`)).data },
  async remove(id) { await apiClient.delete(`/reminders/${id}`) },
}

export default reminderService

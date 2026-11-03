import apiClient from './apiClient'

const calendarService = {
  async events(from, to) { return (await apiClient.get('/calendar/events', { params: { from, to } })).data },
}

export default calendarService

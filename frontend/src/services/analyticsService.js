import apiClient from './apiClient'

const analyticsService = {
  async statistics() { return (await apiClient.get('/analytics/application-statistics')).data },
  async statuses() { return (await apiClient.get('/analytics/status-distribution')).data },
  async monthlyTrends() { return (await apiClient.get('/analytics/monthly-trends')).data },
  async interviewConversion() { return (await apiClient.get('/analytics/interview-conversion')).data },
  async companies() { return (await apiClient.get('/analytics/companies')).data },
  async outcomes() { return (await apiClient.get('/analytics/outcomes')).data },
}

export default analyticsService

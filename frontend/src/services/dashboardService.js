import apiClient from './apiClient'

const dashboardService = {
  async summary() { return (await apiClient.get('/dashboard/summary')).data },
  async recentApplications() { return (await apiClient.get('/dashboard/recent-applications')).data },
  async upcomingInterviews() { return (await apiClient.get('/dashboard/upcoming-interviews')).data },
  async upcomingDeadlines() { return (await apiClient.get('/dashboard/upcoming-deadlines')).data },
  async statistics() { return (await apiClient.get('/dashboard/application-statistics')).data },
}

export default dashboardService

import apiClient from './apiClient'

const userService = {
  async profile() { return (await apiClient.get('/users/profile')).data },
  async updateProfile(profile) { return (await apiClient.put('/users/profile', profile)).data },
  async changePassword(passwords) { await apiClient.put('/users/change-password', passwords) },
}

export default userService

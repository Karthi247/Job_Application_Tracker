import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react'
import authService from '../services/authService'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => sessionStorage.getItem('accessToken'))
  const [user, setUser] = useState(null)
  const [isLoading, setIsLoading] = useState(true)

  const clearSession = useCallback(() => {
    sessionStorage.removeItem('accessToken')
    setToken(null)
    setUser(null)
  }, [])

  useEffect(() => {
    const onUnauthorized = () => clearSession()
    window.addEventListener('auth:unauthorized', onUnauthorized)
    const storedToken = sessionStorage.getItem('accessToken')
    if (!storedToken) {
      setIsLoading(false)
    } else {
      authService.me()
        .then(setUser)
        .catch(clearSession)
        .finally(() => setIsLoading(false))
    }
    return () => window.removeEventListener('auth:unauthorized', onUnauthorized)
  }, [clearSession])

  const setSession = useCallback((authResponse) => {
    sessionStorage.setItem('accessToken', authResponse.accessToken)
    setToken(authResponse.accessToken)
    setUser(authResponse.user)
  }, [])

  const updateUser = useCallback((updatedUser) => setUser(updatedUser), [])

  const signIn = useCallback(async (credentials) => {
    const response = await authService.login(credentials)
    setSession(response)
    return response.user
  }, [setSession])

  const signUp = useCallback(async (profile) => {
    const response = await authService.signup(profile)
    setSession(response)
    return response.user
  }, [setSession])

  const value = useMemo(() => ({
    token,
    user,
    isLoading,
    isAuthenticated: Boolean(token),
    signIn,
    signUp,
    updateUser,
    clearSession,
  }), [token, user, isLoading, signIn, signUp, updateUser, clearSession])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) throw new Error('useAuth must be used inside AuthProvider')
  return context
}

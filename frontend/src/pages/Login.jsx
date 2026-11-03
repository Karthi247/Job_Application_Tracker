import { useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { apiErrorMessage } from '../services/apiClient'
import { useAuth } from '../context/AuthContext'
import './AuthPages.css'

export default function Login() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const { signIn } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  async function handleSubmit(event) {
    event.preventDefault()
    setError('')
    setIsSubmitting(true)
    try {
      await signIn({ email: email.trim(), password })
      navigate(location.state?.from?.pathname || '/dashboard', { replace: true })
    } catch (requestError) {
      setError(apiErrorMessage(requestError, 'Email or password is incorrect.'))
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <main className="auth-page">
      <section className="auth-card">
        <Link to="/dashboard" className="auth-brand"><span className="brand-mark"><i className="bi bi-compass" /></span>daymark<span className="brand-period">.</span></Link>
        <div className="auth-heading"><span className="auth-eyebrow">Welcome back</span><h1>Sign in to your workspace</h1><p>Keep your job search moving, one step at a time.</p></div>
        {location.state?.message && <p className="auth-notice" role="status">{location.state.message}</p>}
        <form className="auth-form" onSubmit={handleSubmit}>
          <label>Email address<input type="email" name="email" autoComplete="email" placeholder="you@example.com" value={email} onChange={(event) => setEmail(event.target.value)} required maxLength={254} /></label>
          <label>Password<input type="password" name="password" autoComplete="current-password" placeholder="Enter your password" value={password} onChange={(event) => setPassword(event.target.value)} required maxLength={72} /></label>
          {error && <p className="auth-error" role="alert">{error}</p>}
          <button className="primary-button" type="submit" disabled={isSubmitting}>{isSubmitting ? 'Signing in…' : 'Sign in'}</button>
        </form>
        <p className="auth-switch">New to daymark? <Link to="/signup">Create an account</Link></p>
      </section>
    </main>
  )
}

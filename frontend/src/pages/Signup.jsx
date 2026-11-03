import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { apiErrorMessage } from '../services/apiClient'
import { useAuth } from '../context/AuthContext'
import './AuthPages.css'

export default function Signup() {
  const [form, setForm] = useState({ fullName: '', email: '', mobile: '', password: '' })
  const [error, setError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const { signUp } = useAuth()
  const navigate = useNavigate()

  function updateField(event) {
    setForm((current) => ({ ...current, [event.target.name]: event.target.value }))
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setError('')
    setIsSubmitting(true)
    try {
      await signUp({ ...form, fullName: form.fullName.trim(), email: form.email.trim(), mobile: form.mobile.trim() })
      navigate('/dashboard', { replace: true })
    } catch (requestError) {
      setError(apiErrorMessage(requestError, 'Could not create your account. Please review your details.'))
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <main className="auth-page">
      <section className="auth-card auth-card--signup">
        <Link to="/dashboard" className="auth-brand"><span className="brand-mark"><i className="bi bi-compass" /></span>daymark<span className="brand-period">.</span></Link>
        <div className="auth-heading"><span className="auth-eyebrow">Get started</span><h1>Create your account</h1><p>Make space for a more organized job search.</p></div>
        <form className="auth-form" onSubmit={handleSubmit}>
          <label>Full name<input type="text" name="fullName" autoComplete="name" placeholder="Your name" value={form.fullName} onChange={updateField} required maxLength={160} /></label>
          <label>Email address<input type="email" name="email" autoComplete="email" placeholder="you@example.com" value={form.email} onChange={updateField} required maxLength={254} /></label>
          <label>Mobile number<input type="tel" name="mobile" autoComplete="tel" placeholder="Your mobile number" value={form.mobile} onChange={updateField} required maxLength={32} /></label>
          <label>Password<input type="password" name="password" autoComplete="new-password" placeholder="At least 8 characters" value={form.password} onChange={updateField} required minLength={8} maxLength={72} /></label>
          {error && <p className="auth-error" role="alert">{error}</p>}
          <button className="primary-button" type="submit" disabled={isSubmitting}>{isSubmitting ? 'Creating account…' : 'Create account'}</button>
        </form>
        <p className="auth-switch">Already have an account? <Link to="/login">Sign in</Link></p>
      </section>
    </main>
  )
}

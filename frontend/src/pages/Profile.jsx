import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import userService from '../services/userService'
import { apiErrorMessage } from '../services/apiClient'
import { useAuth } from '../context/AuthContext'
import PageIntro from '../components/PageIntro'
import LoadingSpinner from '../components/LoadingSpinner'
import Toast from '../components/Toast'
import './UserPages.css'

export default function Profile() {
  const { user, updateUser, clearSession } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ fullName: '', email: '', mobile: '' })
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  const [toast, setToast] = useState('')
  useEffect(() => {
    let active = true
    userService.profile().then((profile) => { if (active) setForm({ fullName: profile.fullName, email: profile.email, mobile: profile.mobile }) })
      .catch((requestError) => { if (active) setError(apiErrorMessage(requestError, 'Could not load your profile.')) })
      .finally(() => { if (active) setLoading(false) })
    return () => { active = false }
  }, [])
  function update(event) { setForm((current) => ({ ...current, [event.target.name]: event.target.value })) }
  async function submit(event) {
    event.preventDefault(); setError(''); setSaving(true)
    try {
      const changedEmail = form.email.trim().toLowerCase() !== user?.email?.toLowerCase()
      const profile = await userService.updateProfile({ ...form, fullName: form.fullName.trim(), email: form.email.trim(), mobile: form.mobile.trim() })
      updateUser(profile)
      if (changedEmail) { clearSession(); navigate('/login', { replace: true, state: { message: 'Your email changed. Please sign in again.' } }) }
      else setToast('Profile updated.')
    } catch (requestError) { setError(apiErrorMessage(requestError, 'Could not update your profile.')) }
    finally { setSaving(false) }
  }
  return <>
    <PageIntro eyebrow="Account" title="Profile" description="Manage the personal details associated with your account." />
    {error && <div className="inline-error" role="alert">{error}</div>}
    {loading ? <LoadingSpinner label="Loading profile…" /> : <section className="user-settings-card"><div className="user-settings-heading"><span className="account-avatar"><i className="bi bi-person" /></span><div><h2>Personal information</h2><p>These details are private to your account.</p></div></div>
      <form className="user-profile-form" onSubmit={submit}><label>Full name<input name="fullName" value={form.fullName} onChange={update} required maxLength={160} autoComplete="name" /></label><label>Email address<input name="email" type="email" value={form.email} onChange={update} required maxLength={254} autoComplete="email" /></label><label>Mobile number<input name="mobile" type="tel" value={form.mobile} onChange={update} required maxLength={32} autoComplete="tel" /></label><div className="user-form-actions"><button className="primary-action" disabled={saving}>{saving ? 'Saving…' : 'Save changes'}</button></div></form>
    </section>}
    <Toast message={toast} onClose={() => setToast('')} />
  </>
}

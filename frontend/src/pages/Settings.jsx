import { useState } from 'react'
import userService from '../services/userService'
import { apiErrorMessage } from '../services/apiClient'
import PageIntro from '../components/PageIntro'
import Toast from '../components/Toast'
import './UserPages.css'

export default function Settings() {
  const [form, setForm] = useState({ currentPassword: '', newPassword: '', confirmPassword: '' })
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  const [toast, setToast] = useState('')
  function update(event) { setForm((current) => ({ ...current, [event.target.name]: event.target.value })) }
  async function submit(event) {
    event.preventDefault(); setError('')
    if (form.newPassword !== form.confirmPassword) { setError('The new passwords do not match.'); return }
    setSaving(true)
    try { await userService.changePassword({ currentPassword: form.currentPassword, newPassword: form.newPassword }); setForm({ currentPassword: '', newPassword: '', confirmPassword: '' }); setToast('Password changed.') }
    catch (requestError) { setError(apiErrorMessage(requestError, 'Could not change your password.')) }
    finally { setSaving(false) }
  }
  return <>
    <PageIntro eyebrow="Account" title="Settings" description="Keep your account secure and manage your sign-in details." />
    <section className="user-settings-card"><div className="user-settings-heading"><span className="account-avatar"><i className="bi bi-shield-lock" /></span><div><h2>Change password</h2><p>Choose a password with at least 8 characters.</p></div></div>
      <form className="user-profile-form" onSubmit={submit}><label>Current password<input name="currentPassword" type="password" autoComplete="current-password" value={form.currentPassword} onChange={update} required maxLength={72} /></label><label>New password<input name="newPassword" type="password" autoComplete="new-password" value={form.newPassword} onChange={update} required minLength={8} maxLength={72} /></label><label>Confirm new password<input name="confirmPassword" type="password" autoComplete="new-password" value={form.confirmPassword} onChange={update} required minLength={8} maxLength={72} /></label>{error && <div className="inline-error" role="alert">{error}</div>}<div className="user-form-actions"><button className="primary-action" disabled={saving}>{saving ? 'Updating…' : 'Update password'}</button></div></form>
    </section>
    <Toast message={toast} onClose={() => setToast('')} />
  </>
}

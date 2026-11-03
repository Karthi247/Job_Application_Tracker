import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import ApplicationForm from '../components/ApplicationForm'
import PageIntro from '../components/PageIntro'
import applicationService from '../services/applicationService'
import { apiErrorMessage } from '../services/apiClient'
import './ApplicationEditor.css'

export default function AddApplication() {
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)
  const navigate = useNavigate()
  async function submit(payload) {
    setError(''); setBusy(true)
    try { const created = await applicationService.create(payload); navigate(`/applications/${created.id}`, { replace: true }) }
    catch (requestError) { setError(apiErrorMessage(requestError, 'Could not save this application.')) }
    finally { setBusy(false) }
  }
  return <><PageIntro eyebrow="Applications" title="Add an application" description="Capture the role details and any dates you want to track." />{error && <div className="editor-error" role="alert">{error}</div>}<ApplicationForm onSubmit={submit} onCancel={() => navigate('/applications')} submitLabel="Save application" busy={busy} /></>
}

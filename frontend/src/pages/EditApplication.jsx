import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import ApplicationForm from '../components/ApplicationForm'
import PageIntro from '../components/PageIntro'
import LoadingSpinner from '../components/LoadingSpinner'
import applicationService from '../services/applicationService'
import { apiErrorMessage } from '../services/apiClient'
import './ApplicationEditor.css'

export default function EditApplication() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [application, setApplication] = useState(null)
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)
  const [loading, setLoading] = useState(true)
  useEffect(() => {
    let active = true
    applicationService.get(id).then((data) => { if (active) setApplication(data) })
      .catch((requestError) => { if (active) setError(apiErrorMessage(requestError, 'Could not load this application.')) })
      .finally(() => { if (active) setLoading(false) })
    return () => { active = false }
  }, [id])
  async function submit(payload) {
    setError(''); setBusy(true)
    try { await applicationService.update(id, payload); navigate(`/applications/${id}`, { replace: true }) }
    catch (requestError) { setError(apiErrorMessage(requestError, 'Could not save these changes.')) }
    finally { setBusy(false) }
  }
  return <><PageIntro eyebrow="Applications" title="Edit application" description="Update the opportunity details and keep your search current." />{error && <div className="editor-error" role="alert">{error}<button onClick={() => navigate('/applications')}>Back to applications</button></div>}{loading ? <LoadingSpinner label="Loading application…" /> : application && <ApplicationForm key={application.id} initialValue={application} onSubmit={submit} onCancel={() => navigate(`/applications/${id}`)} submitLabel="Save changes" busy={busy} />}</>
}

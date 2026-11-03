import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import applicationService from '../services/applicationService'
import { apiErrorMessage } from '../services/apiClient'
import PageIntro from '../components/PageIntro'
import StatusBadge from '../components/StatusBadge'
import LoadingSpinner from '../components/LoadingSpinner'
import ConfirmDialog from '../components/ConfirmDialog'
import Toast from '../components/Toast'
import './ApplicationDetails.css'

const statuses = ['SAVED','APPLIED','UNDER_REVIEW','SHORTLISTED','ASSESSMENT','INTERVIEW','OFFER','REJECTED','WITHDRAWN']
function display(value) { return value || 'Not provided' }
function safeExternalUrl(value) {
  if (!value) return null
  try {
    const url = new URL(value)
    return url.protocol === 'http:' || url.protocol === 'https:' ? url.toString() : null
  } catch { return null }
}

export default function ApplicationDetails() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [application, setApplication] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)
  const [deleteOpen, setDeleteOpen] = useState(false)
  const [busy, setBusy] = useState(false)
  const [toast, setToast] = useState('')
  const [refresh, setRefresh] = useState(0)
  useEffect(() => {
    let active = true
    setLoading(true); setError('')
    applicationService.get(id).then((data) => { if (active) setApplication(data) })
      .catch((requestError) => { if (active) setError(apiErrorMessage(requestError, 'Could not load this application.')) })
      .finally(() => { if (active) setLoading(false) })
    return () => { active = false }
  }, [id, refresh])

  async function updateStatus(status) {
    try { const updated = await applicationService.updateStatus(id, status); setApplication(updated); setToast('Application status updated.') }
    catch (requestError) { setError(apiErrorMessage(requestError, 'Could not update the status.')) }
  }
  async function remove() {
    setBusy(true)
    try { await applicationService.remove(id); navigate('/applications', { replace: true }) }
    catch (requestError) { setError(apiErrorMessage(requestError, 'Could not delete this application.')); setDeleteOpen(false) }
    finally { setBusy(false) }
  }
  const title = application ? `${application.jobTitle} at ${application.companyName}` : 'Application details'

  return <>
    <PageIntro eyebrow="Applications" title={title} description="Review role details, dates, and progress."
      action={<div className="detail-actions"><Link className="secondary-link" to="/applications"><i className="bi bi-arrow-left" />Back</Link>{application && <Link className="primary-action" to={`/applications/${id}/edit`}><i className="bi bi-pencil" />Edit</Link>}</div>} />
    {loading && <LoadingSpinner label="Loading application…" />}
    {error && <div className="inline-error" role="alert">{error}<button onClick={() => setRefresh((value) => value + 1)}>Retry</button></div>}
    {application && !loading && <div className="application-detail-grid">
      <main className="application-detail-main">
        <section className="detail-card detail-hero"><div className="detail-company-mark">{application.companyName.charAt(0).toUpperCase()}</div><div className="detail-company-copy"><h2>{application.companyName}</h2><p>{application.location || 'Location not specified'}{application.workMode ? ` · ${application.workMode}` : ''}</p></div><StatusBadge status={application.status} /></section>
        <section className="detail-card"><div className="detail-section-heading"><div><h2>Opportunity</h2><p>Role information and application progress.</p></div><label className="detail-status-control"><span>Status</span><select value={application.status} onChange={(event) => updateStatus(event.target.value)}>{statuses.map((status) => <option key={status} value={status}>{status.replaceAll('_', ' ')}</option>)}</select></label></div>
          <div className="detail-info-grid"><Info label="Job title" value={application.jobTitle} /><Info label="Employment type" value={application.employmentType} /><Info label="Applied date" value={application.appliedDate ? new Date(`${application.appliedDate}T00:00:00`).toLocaleDateString() : null} /><Info label="Deadline" value={application.deadline ? new Date(`${application.deadline}T00:00:00`).toLocaleDateString() : null} /><Info label="Priority" value={application.priority} /><Info label="Salary" value={application.salary} /></div>
          {safeExternalUrl(application.jobUrl) && <a className="external-job-link" href={safeExternalUrl(application.jobUrl)} target="_blank" rel="noreferrer">Open job posting <i className="bi bi-box-arrow-up-right" /></a>}
          <DetailText label="Job description" value={application.jobDescription} />
        </section>
        <section className="detail-card"><h2>Notes</h2><p className="detail-notes">{application.notes || 'No notes yet.'}</p></section>
      </main>
      <aside className="application-detail-aside">
        <section className="detail-card"><h2>Recruiter</h2><Info label="Name" value={application.recruiterName} /><Info label="Email" value={application.recruiterEmail} /><Info label="Phone" value={application.recruiterPhone} /></section>
        <section className="detail-card"><h2>Documents</h2><Info label="Resume link" value={application.resumeUrl} /><DetailText label="Cover letter" value={application.coverLetter} /></section>
        <button className="delete-application-button" onClick={() => setDeleteOpen(true)}><i className="bi bi-trash3" />Delete application</button>
      </aside>
    </div>}
    <ConfirmDialog open={deleteOpen} title="Delete this application?" message={application ? `This will permanently remove ${application.jobTitle} at ${application.companyName}.` : ''} onConfirm={remove} onCancel={() => setDeleteOpen(false)} busy={busy} />
    <Toast message={toast} onClose={() => setToast('')} />
  </>
}

function Info({ label, value }) { return <div className="detail-info"><span>{label}</span><strong>{display(value)}</strong></div> }
function DetailText({ label, value }) { return <div className="detail-text"><h3>{label}</h3><p>{value || 'Not provided'}</p></div> }

import { useCallback, useEffect, useState } from 'react'
import interviewService from '../services/interviewService'
import applicationService from '../services/applicationService'
import { apiErrorMessage } from '../services/apiClient'
import PageIntro from '../components/PageIntro'
import LoadingSpinner from '../components/LoadingSpinner'
import ConfirmDialog from '../components/ConfirmDialog'
import Toast from '../components/Toast'
import './Interviews.css'

const blank = { applicationId: '', interviewRound: '', interviewType: 'VIDEO', interviewDate: '', interviewTime: '', meetingLink: '', interviewerName: '', interviewerEmail: '', location: '', notes: '', status: 'SCHEDULED' }
function safeMeetingUrl(value) {
  try { const url = new URL(value); return ['http:', 'https:'].includes(url.protocol) ? url.toString() : null }
  catch { return null }
}
export default function Interviews() {
  const [interviews, setInterviews] = useState([])
  const [applications, setApplications] = useState([])
  const [dateRange, setDateRange] = useState({ from: '', to: '' })
  const [modal, setModal] = useState(false)
  const [editing, setEditing] = useState(null)
  const [form, setForm] = useState(blank)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  const [formError, setFormError] = useState('')
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)
  const [toast, setToast] = useState('')
  const [refresh, setRefresh] = useState(0)

  const load = useCallback(async () => {
    setLoading(true); setError('')
    try {
      const result = await interviewService.list({ page: 0, size: 100, from: dateRange.from || undefined, to: dateRange.to || undefined })
      setInterviews(result.content)
    } catch (requestError) { setError(apiErrorMessage(requestError, 'Could not load interviews.')) }
    finally { setLoading(false) }
  }, [dateRange])
  useEffect(() => { load() }, [load, refresh])

  async function openForm(interview = null) {
    setFormError(''); setEditing(interview); setModal(true)
    setForm(interview ? { ...blank, ...interview, applicationId: String(interview.applicationId), interviewDate: interview.interviewDate, interviewTime: interview.interviewTime || '' } : blank)
    try {
      const result = await applicationService.list({ page: 0, size: 100, sortBy: 'companyName', direction: 'asc' })
      setApplications(result.content)
    } catch (requestError) { setFormError(apiErrorMessage(requestError, 'Could not load applications for this interview.')) }
  }
  function update(event) { setForm((current) => ({ ...current, [event.target.name]: event.target.value })) }
  async function submit(event) {
    event.preventDefault(); setFormError(''); setSaving(true)
    const payload = { ...form, applicationId: Number(form.applicationId), interviewTime: form.interviewTime || null,
      meetingLink: form.meetingLink || null, interviewerName: form.interviewerName || null,
      interviewerEmail: form.interviewerEmail || null, location: form.location || null, notes: form.notes || null }
    try {
      if (editing) await interviewService.update(editing.id, payload)
      else await interviewService.create(payload)
      setModal(false); setToast(editing ? 'Interview updated.' : 'Interview scheduled.'); setRefresh((value) => value + 1)
    } catch (requestError) { setFormError(apiErrorMessage(requestError, 'Could not save this interview.')) }
    finally { setSaving(false) }
  }
  async function remove() {
    setDeleting(true)
    try { await interviewService.remove(deleteTarget.id); setDeleteTarget(null); setToast('Interview deleted.'); setRefresh((value) => value + 1) }
    catch (requestError) { setError(apiErrorMessage(requestError, 'Could not delete this interview.')); setDeleteTarget(null) }
    finally { setDeleting(false) }
  }

  return <>
    <PageIntro eyebrow="Planning" title="Interviews" description="Prepare for upcoming conversations and keep notes from every round." action={<button className="primary-action" onClick={() => openForm()}><i className="bi bi-plus-lg" />Schedule interview</button>} />
    <section className="interviews-panel">
      <div className="interviews-toolbar"><span><i className="bi bi-calendar3" /> Interview schedule</span><div><label>From<input type="date" value={dateRange.from} onChange={(event) => setDateRange((value) => ({ ...value, from: event.target.value }))} /></label><label>To<input type="date" value={dateRange.to} onChange={(event) => setDateRange((value) => ({ ...value, to: event.target.value }))} /></label></div></div>
      {error && <div className="inline-error" role="alert">{error}<button onClick={() => setRefresh((value) => value + 1)}>Retry</button></div>}
      {loading ? <LoadingSpinner label="Loading interviews…" /> : interviews.length ? <div className="table-scroll"><table className="interview-table"><thead><tr><th>Date & time</th><th>Company / role</th><th>Round</th><th>Type</th><th>Status</th><th>Details</th><th /></tr></thead><tbody>
        {interviews.map((item) => <tr key={item.id}><td><strong>{new Date(`${item.interviewDate}T00:00:00`).toLocaleDateString()}</strong><small>{item.interviewTime || 'Time not set'}</small></td><td><strong>{item.companyName}</strong><small>{item.jobTitle}</small></td><td>{item.interviewRound}</td><td>{item.interviewType.toLowerCase()}</td><td><span className={`interview-status interview-status--${item.status.toLowerCase()}`}>{item.status.toLowerCase()}</span></td><td>{safeMeetingUrl(item.meetingLink) ? <a href={safeMeetingUrl(item.meetingLink)} target="_blank" rel="noreferrer">Meeting link <i className="bi bi-box-arrow-up-right" /></a> : item.location || '—'}</td><td><div className="row-actions"><button onClick={() => openForm(item)} aria-label="Edit interview"><i className="bi bi-pencil" /></button><button onClick={() => setDeleteTarget(item)} aria-label="Delete interview"><i className="bi bi-trash3" /></button></div></td></tr>)}
      </tbody></table></div> : <div className="interview-empty"><i className="bi bi-camera-video" /><h2>No interviews in this date range</h2><p>Schedule an interview from one of your applications to keep the details together.</p><button className="primary-action" onClick={() => openForm()}>Schedule interview</button></div>}
    </section>
    {modal && <div className="dialog-backdrop" role="presentation" onMouseDown={(event) => event.target === event.currentTarget && setModal(false)}><section className="interview-dialog" role="dialog" aria-modal="true" aria-labelledby="interview-form-title"><div className="dialog-heading"><div><h2 id="interview-form-title">{editing ? 'Edit interview' : 'Schedule an interview'}</h2><p>Keep the conversation details with its application.</p></div><button onClick={() => setModal(false)} aria-label="Close"><i className="bi bi-x-lg" /></button></div>
      {formError && <div className="editor-error" role="alert">{formError}</div>}
      <form className="interview-form" onSubmit={submit}>
        <label>Application<select name="applicationId" value={form.applicationId} onChange={update} required><option value="">Select an application</option>{applications.map((application) => <option key={application.id} value={application.id}>{application.companyName} · {application.jobTitle}</option>)}</select></label>
        <label>Interview round<input name="interviewRound" value={form.interviewRound} onChange={update} placeholder="e.g. Hiring manager" maxLength={120} required /></label>
        <div className="interview-form-grid"><label>Type<select name="interviewType" value={form.interviewType} onChange={update}>{['PHONE','VIDEO','TECHNICAL','HR','ONSITE'].map((type) => <option key={type}>{type}</option>)}</select></label><label>Status<select name="status" value={form.status} onChange={update}>{['SCHEDULED','COMPLETED','CANCELLED','RESCHEDULED'].map((status) => <option key={status}>{status}</option>)}</select></label></div>
        <div className="interview-form-grid"><label>Date<input name="interviewDate" type="date" value={form.interviewDate} onChange={update} required /></label><label>Time<input name="interviewTime" type="time" value={form.interviewTime} onChange={update} /></label></div>
        <label>Meeting link<input name="meetingLink" type="url" value={form.meetingLink || ''} onChange={update} placeholder="https://" maxLength={2048} /></label>
        <div className="interview-form-grid"><label>Interviewer<input name="interviewerName" value={form.interviewerName || ''} onChange={update} maxLength={160} /></label><label>Interviewer email<input name="interviewerEmail" type="email" value={form.interviewerEmail || ''} onChange={update} maxLength={254} /></label></div>
        <label>Location<input name="location" value={form.location || ''} onChange={update} maxLength={255} /></label>
        <label>Notes<textarea name="notes" rows="3" value={form.notes || ''} onChange={update} /></label>
        <div className="dialog-actions"><button type="button" className="secondary-button" onClick={() => setModal(false)}>Cancel</button><button className="primary-button" disabled={saving}>{saving ? 'Saving…' : editing ? 'Save changes' : 'Schedule interview'}</button></div>
      </form>
    </section></div>}
    <ConfirmDialog open={Boolean(deleteTarget)} title="Delete this interview?" message={deleteTarget ? `${deleteTarget.interviewRound} at ${deleteTarget.companyName} will be removed.` : ''} onConfirm={remove} onCancel={() => setDeleteTarget(null)} busy={deleting} />
    <Toast message={toast} onClose={() => setToast('')} />
  </>
}

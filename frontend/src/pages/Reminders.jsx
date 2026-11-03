import { useCallback, useEffect, useState } from 'react'
import reminderService from '../services/reminderService'
import applicationService from '../services/applicationService'
import interviewService from '../services/interviewService'
import { apiErrorMessage } from '../services/apiClient'
import PageIntro from '../components/PageIntro'
import LoadingSpinner from '../components/LoadingSpinner'
import ConfirmDialog from '../components/ConfirmDialog'
import Toast from '../components/Toast'
import './Reminders.css'

const blank = { reminderType: 'FOLLOW_UP', title: '', remindAt: '', applicationId: '', interviewId: '' }
function localDateTime(value = new Date()) { const date = new Date(value.getTime() - value.getTimezoneOffset() * 60000); return date.toISOString().slice(0, 16) }
export default function Reminders() {
  const [reminders, setReminders] = useState([])
  const [applications, setApplications] = useState([])
  const [interviews, setInterviews] = useState([])
  const [filter, setFilter] = useState('upcoming')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [formError, setFormError] = useState('')
  const [modal, setModal] = useState(false)
  const [editing, setEditing] = useState(null)
  const [form, setForm] = useState(blank)
  const [saving, setSaving] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)
  const [toast, setToast] = useState('')
  const [refresh, setRefresh] = useState(0)
  const load = useCallback(async () => {
    setLoading(true); setError('')
    try { const result = await reminderService.list({ page: 0, size: 100 }); setReminders(result.content) }
    catch (requestError) { setError(apiErrorMessage(requestError, 'Could not load reminders.')) }
    finally { setLoading(false) }
  }, [])
  useEffect(() => { load() }, [load, refresh])
  const shown = reminders.filter((reminder) => filter === 'all' || (filter === 'completed' ? reminder.completedAt : !reminder.completedAt))

  async function openForm(reminder = null) {
    setEditing(reminder); setFormError(''); setModal(true)
    setForm(reminder ? { reminderType: reminder.reminderType, title: reminder.title, remindAt: localDateTime(new Date(reminder.remindAt)), applicationId: reminder.applicationId ? String(reminder.applicationId) : '', interviewId: reminder.interviewId ? String(reminder.interviewId) : '' } : { ...blank, remindAt: localDateTime() })
    try {
      const [apps, rounds] = await Promise.all([applicationService.list({ page: 0, size: 100 }), interviewService.list({ page: 0, size: 100 })])
      setApplications(apps.content); setInterviews(rounds.content)
    } catch (requestError) { setFormError(apiErrorMessage(requestError, 'Could not load linked applications and interviews.')) }
  }
  function update(event) {
    const { name, value } = event.target
    setForm((current) => ({ ...current, [name]: value, ...(name === 'applicationId' ? { interviewId: '' } : {}) }))
  }
  async function submit(event) {
    event.preventDefault(); setSaving(true); setFormError('')
    const payload = { reminderType: form.reminderType, title: form.title.trim(), remindAt: form.remindAt,
      applicationId: form.applicationId ? Number(form.applicationId) : null,
      interviewId: form.interviewId ? Number(form.interviewId) : null }
    try {
      if (editing) await reminderService.update(editing.id, payload)
      else await reminderService.create(payload)
      setModal(false); setToast(editing ? 'Reminder updated.' : 'Reminder created.'); setRefresh((value) => value + 1)
    } catch (requestError) { setFormError(apiErrorMessage(requestError, 'Could not save this reminder.')) }
    finally { setSaving(false) }
  }
  async function complete(reminder) {
    try { await reminderService.complete(reminder.id); setToast('Reminder completed.'); setRefresh((value) => value + 1) }
    catch (requestError) { setError(apiErrorMessage(requestError, 'Could not complete this reminder.')) }
  }
  async function remove() {
    setDeleting(true)
    try { await reminderService.remove(deleteTarget.id); setDeleteTarget(null); setToast('Reminder deleted.'); setRefresh((value) => value + 1) }
    catch (requestError) { setError(apiErrorMessage(requestError, 'Could not delete this reminder.')); setDeleteTarget(null) }
    finally { setDeleting(false) }
  }

  return <>
    <PageIntro eyebrow="Planning" title="Reminders" description="Keep important follow-ups and dates from slipping by." action={<button className="primary-action" onClick={() => openForm()}><i className="bi bi-plus-lg" />New reminder</button>} />
    <section className="reminders-panel"><div className="reminder-tabs" role="tablist" aria-label="Reminder views">{[['upcoming','Active'],['completed','Completed'],['all','All reminders']].map(([value,label]) => <button role="tab" aria-selected={filter === value} className={filter === value ? 'is-current' : ''} key={value} onClick={() => setFilter(value)}>{label}</button>)}</div>
      {error && <div className="inline-error" role="alert">{error}<button onClick={() => setRefresh((value) => value + 1)}>Retry</button></div>}
      {loading ? <LoadingSpinner label="Loading reminders…" /> : shown.length ? <div className="reminder-list">{shown.map((reminder) => <article className={`reminder-row${reminder.completedAt ? ' is-completed' : ''}`} key={reminder.id}>
        <span className={`reminder-type-icon reminder-type-icon--${reminder.reminderType.toLowerCase()}`}><i className={`bi ${reminder.reminderType === 'INTERVIEW' ? 'bi-camera-video' : reminder.reminderType === 'DEADLINE' ? 'bi-flag' : reminder.reminderType === 'FOLLOW_UP' ? 'bi-arrow-repeat' : 'bi-bell'}`} /></span>
        <div className="reminder-copy"><strong>{reminder.title}</strong><span>{reminder.reminderType.replaceAll('_', ' ').toLowerCase()}{reminder.applicationId ? ` · Application #${reminder.applicationId}` : ''}{reminder.interviewId ? ` · Interview #${reminder.interviewId}` : ''}</span></div>
        <time dateTime={reminder.remindAt}>{new Date(reminder.remindAt).toLocaleString(undefined, { month: 'short', day: 'numeric', hour: 'numeric', minute: '2-digit' })}</time>
        <div className="reminder-actions">{!reminder.completedAt && <button onClick={() => complete(reminder)} aria-label="Mark complete"><i className="bi bi-check2" /></button>}<button onClick={() => openForm(reminder)} aria-label="Edit reminder"><i className="bi bi-pencil" /></button><button onClick={() => setDeleteTarget(reminder)} aria-label="Delete reminder"><i className="bi bi-trash3" /></button></div>
      </article>)}</div> : <div className="reminders-empty"><i className="bi bi-bell" /><h2>{filter === 'completed' ? 'No completed reminders yet' : 'No reminders here'}</h2><p>Create a follow-up or a custom reminder to keep your next step visible.</p>{filter !== 'completed' && <button className="primary-action" onClick={() => openForm()}>Create reminder</button>}</div>}
    </section>
    {modal && <div className="dialog-backdrop" role="presentation" onMouseDown={(event) => event.target === event.currentTarget && setModal(false)}><section className="reminder-dialog" role="dialog" aria-modal="true" aria-labelledby="reminder-form-title"><div className="dialog-heading"><div><h2 id="reminder-form-title">{editing ? 'Edit reminder' : 'Create reminder'}</h2><p>Choose when you want this to come back to you.</p></div><button onClick={() => setModal(false)} aria-label="Close"><i className="bi bi-x-lg" /></button></div>
      {formError && <div className="editor-error" role="alert">{formError}</div>}
      <form className="reminder-form" onSubmit={submit}><label>Reminder type<select name="reminderType" value={form.reminderType} onChange={update}>{['FOLLOW_UP','DEADLINE','INTERVIEW','CUSTOM'].map((type) => <option key={type} value={type}>{type.replaceAll('_',' ')}</option>)}</select></label>
        <label>Title<input name="title" value={form.title} onChange={update} maxLength={180} required placeholder="e.g. Follow up with recruiter" /></label>
        <label>Date and time<input type="datetime-local" name="remindAt" value={form.remindAt} onChange={update} required /></label>
        <label>Application (optional)<select name="applicationId" value={form.applicationId} onChange={update}><option value="">No linked application</option>{applications.map((application) => <option key={application.id} value={application.id}>{application.companyName} · {application.jobTitle}</option>)}</select></label>
        <label>Interview (optional)<select name="interviewId" value={form.interviewId} onChange={update}><option value="">No linked interview</option>{interviews.filter((interview) => !form.applicationId || String(interview.applicationId) === form.applicationId).map((interview) => <option key={interview.id} value={interview.id}>{interview.companyName} · {interview.interviewRound} · {interview.interviewDate}</option>)}</select></label>
        <div className="dialog-actions"><button type="button" className="secondary-button" onClick={() => setModal(false)}>Cancel</button><button className="primary-button" disabled={saving}>{saving ? 'Saving…' : editing ? 'Save changes' : 'Create reminder'}</button></div>
      </form>
    </section></div>}
    <ConfirmDialog open={Boolean(deleteTarget)} title="Delete this reminder?" message={deleteTarget?.title || ''} onConfirm={remove} onCancel={() => setDeleteTarget(null)} busy={deleting} />
    <Toast message={toast} onClose={() => setToast('')} />
  </>
}

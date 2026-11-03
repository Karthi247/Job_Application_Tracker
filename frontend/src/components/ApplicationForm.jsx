import { useState } from 'react'
import './ApplicationForm.css'

const emptyForm = { companyName: '', jobTitle: '', jobDescription: '', location: '', employmentType: '', workMode: '', salary: '', jobUrl: '', recruiterName: '', recruiterEmail: '', recruiterPhone: '', resumeUrl: '', coverLetter: '', status: 'SAVED', priority: 'MEDIUM', appliedDate: '', deadline: '', notes: '' }
const fields = [
  ['companyName', 'Company', 'text', true], ['jobTitle', 'Job title', 'text', true],
  ['location', 'Location', 'text'], ['employmentType', 'Employment type', 'text'],
  ['workMode', 'Work mode', 'text'], ['salary', 'Salary range', 'text'],
  ['jobUrl', 'Job posting URL', 'url'], ['resumeUrl', 'Resume URL', 'url'],
  ['recruiterName', 'Recruiter name', 'text'], ['recruiterEmail', 'Recruiter email', 'email'],
  ['recruiterPhone', 'Recruiter phone', 'tel'], ['appliedDate', 'Applied date', 'date'],
  ['deadline', 'Application deadline', 'date'],
]

export default function ApplicationForm({ initialValue, onSubmit, onCancel, submitLabel, busy = false }) {
  const [form, setForm] = useState(() => ({ ...emptyForm, ...initialValue }))
  function update(event) { setForm((current) => ({ ...current, [event.target.name]: event.target.value })) }
  function submit(event) {
    event.preventDefault()
    const normalized = Object.fromEntries(Object.keys(emptyForm).map((key) => [key, form[key] === '' ? null : form[key]]))
    onSubmit(normalized)
  }
  return (
    <form className="application-form" onSubmit={submit}>
      <div className="form-section-heading"><h2>Role details</h2><p>Start with the company and position.</p></div>
      <div className="application-form-grid">
        {fields.map(([name, label, type, required]) => <label className="form-field" key={name}>{label}<input name={name} type={type} value={form[name] || ''} onChange={update} required={Boolean(required)} maxLength={type === 'url' ? 2048 : undefined} /></label>)}
        <label className="form-field">Status<select name="status" value={form.status || 'SAVED'} onChange={update}>{['SAVED','APPLIED','UNDER_REVIEW','SHORTLISTED','ASSESSMENT','INTERVIEW','OFFER','REJECTED','WITHDRAWN'].map((value) => <option key={value}>{value}</option>)}</select></label>
        <label className="form-field">Priority<select name="priority" value={form.priority || 'MEDIUM'} onChange={update}>{['LOW','MEDIUM','HIGH'].map((value) => <option key={value}>{value}</option>)}</select></label>
      </div>
      <label className="form-field form-field--wide">Job description<textarea name="jobDescription" rows="4" value={form.jobDescription || ''} onChange={update} /></label>
      <label className="form-field form-field--wide">Cover letter<textarea name="coverLetter" rows="4" value={form.coverLetter || ''} onChange={update} /></label>
      <label className="form-field form-field--wide">Notes<textarea name="notes" rows="4" value={form.notes || ''} onChange={update} placeholder="Add a follow-up, contact detail, or interview prep note…" /></label>
      <div className="application-form-actions"><button className="secondary-button" type="button" onClick={onCancel}>Cancel</button><button className="primary-button" type="submit" disabled={busy}>{busy ? 'Saving…' : submitLabel}</button></div>
    </form>
  )
}

import { Link } from 'react-router-dom'
import StatusBadge from './StatusBadge'
import './ApplicationCard.css'

export default function ApplicationCard({ application, onStatusChange }) {
  return (
    <article className="application-card" draggable onDragStart={(event) => event.dataTransfer.setData('text/application-id', String(application.id))}>
      <div className="application-card-top"><span className="company-avatar">{application.companyName?.trim().charAt(0).toUpperCase() || '?'}</span><button className="card-more" aria-label={`Open ${application.companyName} details`}><i className="bi bi-three-dots" /></button></div>
      <Link className="application-card-title" to={`/applications/${application.id}`}>{application.jobTitle}</Link>
      <div className="application-card-company">{application.companyName}</div>
      <div className="application-card-meta">{application.location && <span><i className="bi bi-geo-alt" />{application.location}</span>}<span><i className="bi bi-calendar3" />{application.appliedDate ? new Date(`${application.appliedDate}T00:00:00`).toLocaleDateString() : 'Not applied yet'}</span></div>
      <div className="application-card-bottom"><StatusBadge status={application.status} /><label className="card-status-select"><span className="visually-hidden">Change status</span><select value="" onChange={(event) => event.target.value && onStatusChange(application, event.target.value)} aria-label={`Change ${application.companyName} application status`}><option value="">Move to…</option>{['SAVED','APPLIED','UNDER_REVIEW','SHORTLISTED','ASSESSMENT','INTERVIEW','OFFER','REJECTED','WITHDRAWN'].filter((status) => status !== application.status).map((status) => <option key={status} value={status}>{status.replaceAll('_', ' ')}</option>)}</select></label></div>
    </article>
  )
}

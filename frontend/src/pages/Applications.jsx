import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import applicationService from '../services/applicationService'
import { apiErrorMessage } from '../services/apiClient'
import PageIntro from '../components/PageIntro'
import SearchBar from '../components/SearchBar'
import FilterPanel from '../components/FilterPanel'
import StatusBadge from '../components/StatusBadge'
import Pagination from '../components/Pagination'
import LoadingSpinner from '../components/LoadingSpinner'
import ConfirmDialog from '../components/ConfirmDialog'
import Toast from '../components/Toast'
import './Applications.css'

const pageSize = 10
export default function Applications() {
  const [result, setResult] = useState(null)
  const [search, setSearch] = useState('')
  const [filters, setFilters] = useState({ status: '', priority: '', appliedFrom: '', appliedTo: '' })
  const [page, setPage] = useState(0)
  const [busy, setBusy] = useState(true)
  const [error, setError] = useState('')
  const [refresh, setRefresh] = useState(0)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)
  const [toast, setToast] = useState('')

  useEffect(() => {
    let active = true
    setBusy(true)
    setError('')
    applicationService.list({ page, size: pageSize, sortBy: 'createdAt', direction: 'desc', search: search || undefined,
      status: filters.status || undefined, priority: filters.priority || undefined,
      appliedFrom: filters.appliedFrom || undefined, appliedTo: filters.appliedTo || undefined })
      .then((data) => { if (active) setResult(data) })
      .catch((requestError) => { if (active) setError(apiErrorMessage(requestError, 'Could not load your applications.')) })
      .finally(() => { if (active) setBusy(false) })
    return () => { active = false }
  }, [page, search, filters, refresh])

  function changeFilter(name, value) { setPage(0); setFilters((current) => ({ ...current, [name]: value })) }
  async function confirmDelete() {
    setDeleting(true)
    try {
      await applicationService.remove(deleteTarget.id)
      setDeleteTarget(null)
      setToast('Application deleted.')
      setRefresh((value) => value + 1)
    } catch (requestError) { setError(apiErrorMessage(requestError, 'Could not delete this application.')); setDeleteTarget(null) }
    finally { setDeleting(false) }
  }

  return (
    <>
      <PageIntro eyebrow="Your workspace" title="Applications" description="Keep every opportunity and its next step in one place."
        action={<Link className="primary-action" to="/applications/new"><i className="bi bi-plus-lg" />Add application</Link>} />
      <section className="applications-panel">
        <div className="applications-toolbar"><SearchBar value={search} onChange={(value) => { setSearch(value); setPage(0) }} /><FilterPanel {...filters} onChange={changeFilter} /></div>
        {error && <div className="inline-error" role="alert"><i className="bi bi-exclamation-circle" />{error}<button onClick={() => setRefresh((value) => value + 1)}>Retry</button></div>}
        {busy ? <LoadingSpinner label="Loading applications…" /> : result?.content?.length ? (
          <div className="table-scroll"><table className="application-table"><thead><tr><th>Company</th><th>Position</th><th>Location</th><th>Applied</th><th>Status</th><th>Priority</th><th><span className="visually-hidden">Actions</span></th></tr></thead>
            <tbody>{result.content.map((application) => <tr key={application.id}>
              <td><Link className="company-cell" to={`/applications/${application.id}`}><span className="company-avatar">{application.companyName?.charAt(0).toUpperCase()}</span><strong>{application.companyName}</strong></Link></td>
              <td><Link className="position-cell" to={`/applications/${application.id}`}>{application.jobTitle}</Link></td>
              <td>{application.location || '—'}</td><td>{application.appliedDate ? new Date(`${application.appliedDate}T00:00:00`).toLocaleDateString() : '—'}</td>
              <td><StatusBadge status={application.status} /></td><td><span className={`priority priority--${application.priority?.toLowerCase()}`}>{application.priority?.toLowerCase()}</span></td>
              <td><div className="row-actions"><Link to={`/applications/${application.id}/edit`} aria-label={`Edit ${application.companyName}`}><i className="bi bi-pencil" /></Link><button onClick={() => setDeleteTarget(application)} aria-label={`Delete ${application.companyName}`}><i className="bi bi-trash3" /></button></div></td>
            </tr>)}</tbody></table></div>
        ) : !error ? <div className="applications-empty"><span><i className="bi bi-briefcase" /></span><h2>{search || Object.values(filters).some(Boolean) ? 'No matching applications' : 'Start with your first application'}</h2><p>{search || Object.values(filters).some(Boolean) ? 'Try changing your search or filters.' : 'Add a role to keep its details, dates, and progress together.'}</p>{!search && <Link className="primary-action" to="/applications/new"><i className="bi bi-plus-lg" />Add application</Link>}</div> : null}
        {!busy && result && result.content.length > 0 && <Pagination page={result.page} size={result.size} totalElements={result.totalElements} totalPages={result.totalPages} onPageChange={setPage} />}
      </section>
      <ConfirmDialog open={Boolean(deleteTarget)} title="Delete this application?" message={deleteTarget ? `This will permanently remove ${deleteTarget.jobTitle} at ${deleteTarget.companyName}.` : ''} onConfirm={confirmDelete} onCancel={() => setDeleteTarget(null)} busy={deleting} />
      <Toast message={toast} onClose={() => setToast('')} />
    </>
  )
}

import { useCallback, useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import applicationService from '../services/applicationService'
import { apiErrorMessage } from '../services/apiClient'
import PageIntro from '../components/PageIntro'
import ApplicationCard from '../components/ApplicationCard'
import LoadingSpinner from '../components/LoadingSpinner'
import Toast from '../components/Toast'
import './KanbanBoard.css'

const stages = ['SAVED','APPLIED','UNDER_REVIEW','SHORTLISTED','ASSESSMENT','INTERVIEW','OFFER','REJECTED','WITHDRAWN']
export default function KanbanBoard() {
  const [applications, setApplications] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [toast, setToast] = useState('')
  const [refresh, setRefresh] = useState(0)
  useEffect(() => {
    let active = true
    async function loadAll() {
      setLoading(true); setError('')
      try {
        const first = await applicationService.list({ page: 0, size: 100, sortBy: 'createdAt', direction: 'desc' })
        const remaining = await Promise.all(Array.from({ length: Math.max(0, first.totalPages - 1) }, (_, index) =>
          applicationService.list({ page: index + 1, size: 100, sortBy: 'createdAt', direction: 'desc' })))
        if (active) setApplications([...first.content, ...remaining.flatMap((page) => page.content)])
      } catch (requestError) { if (active) setError(apiErrorMessage(requestError, 'Could not load your pipeline.')) }
      finally { if (active) setLoading(false) }
    }
    loadAll()
    return () => { active = false }
  }, [refresh])

  const moveApplication = useCallback(async (application, status) => {
    const previous = applications
    setApplications((current) => current.map((item) => item.id === application.id ? { ...item, status } : item))
    try { await applicationService.updateStatus(application.id, status); setToast('Application moved.') }
    catch (requestError) { setApplications(previous); setError(apiErrorMessage(requestError, 'Could not update this application.')) }
  }, [applications])

  function drop(event, status) {
    event.preventDefault()
    const id = Number(event.dataTransfer.getData('text/application-id'))
    const application = applications.find((item) => item.id === id)
    if (application && application.status !== status) moveApplication(application, status)
  }

  return <>
    <PageIntro eyebrow="Applications" title="Pipeline" description="Move roles through each stage as your search progresses."
      action={<Link className="primary-action" to="/applications/new"><i className="bi bi-plus-lg" />Add application</Link>} />
    {error && <div className="inline-error" role="alert">{error}<button onClick={() => setRefresh((value) => value + 1)}>Retry</button></div>}
    {loading ? <LoadingSpinner label="Loading your pipeline…" /> : <div className="kanban-scroller"><div className="kanban-board">
      {stages.map((status) => {
        const items = applications.filter((application) => application.status === status)
        return <section className="kanban-column" key={status} onDragOver={(event) => event.preventDefault()} onDrop={(event) => drop(event, status)}>
          <header><span className={`stage-dot stage-dot--${status.toLowerCase().replaceAll('_','-')}`} /><h2>{status.replaceAll('_', ' ')}</h2><span className="stage-count">{items.length}</span></header>
          <div className="kanban-cards">{items.map((application) => <ApplicationCard key={application.id} application={application} onStatusChange={moveApplication} />)}
            {!items.length && <div className="kanban-empty">Drop an application here</div>}
          </div>
        </section>
      })}
    </div></div>}
    <Toast message={toast} onClose={() => setToast('')} />
  </>
}

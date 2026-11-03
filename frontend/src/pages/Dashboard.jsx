import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import dashboardService from '../services/dashboardService'
import { apiErrorMessage } from '../services/apiClient'
import PageIntro from '../components/PageIntro'
import StatsCard from '../components/StatsCard'
import StatusBadge from '../components/StatusBadge'
import LoadingSpinner from '../components/LoadingSpinner'
import './Dashboard.css'

const statusColors = { SAVED: '#9ca8af', APPLIED: '#7797a8', UNDER_REVIEW: '#c1a264', SHORTLISTED: '#83a28b', ASSESSMENT: '#b99a59', INTERVIEW: '#689878', OFFER: '#4b9166', REJECTED: '#be7d76', WITHDRAWN: '#9e8c91' }
function dateLabel(value) { return value ? new Date(`${value}T00:00:00`).toLocaleDateString(undefined, { month: 'short', day: 'numeric' }) : '—' }

export default function Dashboard() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [refresh, setRefresh] = useState(0)
  const today = new Intl.DateTimeFormat(undefined, { weekday: 'long', month: 'long', day: 'numeric', year: 'numeric' }).format(new Date())
  useEffect(() => {
    let active = true
    setLoading(true); setError('')
    Promise.all([dashboardService.summary(), dashboardService.recentApplications(), dashboardService.upcomingInterviews(), dashboardService.upcomingDeadlines(), dashboardService.statistics()])
      .then(([summary, recent, interviews, deadlines, statistics]) => { if (active) setData({ summary, recent, interviews, deadlines, statistics }) })
      .catch((requestError) => { if (active) setError(apiErrorMessage(requestError, 'Could not load your dashboard.')) })
      .finally(() => { if (active) setLoading(false) })
    return () => { active = false }
  }, [refresh])

  const distribution = data?.statistics?.statusDistribution || []
  const totalByStatus = distribution.reduce((total, item) => total + item.count, 0)
  let cumulative = 0
  const ring = totalByStatus ? `conic-gradient(${distribution.map((item) => {
    const start = cumulative
    cumulative += item.count / totalByStatus * 100
    return `${statusColors[item.status]} ${start}% ${cumulative}%`
  }).join(', ')})` : '#edf0f2'
  const monthly = data?.statistics?.monthlyApplications || []
  const maximumMonth = Math.max(1, ...monthly.map((month) => month.count))

  return <>
    <PageIntro eyebrow={today} title="Your search, in focus" description="A clear view of your applications, upcoming conversations, and next steps."
      action={<Link className="primary-action" to="/applications/new"><i className="bi bi-plus-lg" />Add application</Link>} />
    {error && <div className="inline-error" role="alert"><i className="bi bi-exclamation-circle" />{error}<button onClick={() => setRefresh((value) => value + 1)}>Retry</button></div>}
    {loading ? <LoadingSpinner label="Loading your dashboard…" /> : data && <>
      <section className="dashboard-welcome"><div className="welcome-copy"><span className="welcome-kicker">A little progress, every day</span><h2>Your job search workspace</h2><p>Track your opportunities, keep an eye on upcoming dates, and see how your search is moving.</p></div><div className="welcome-art" aria-hidden="true"><span className="welcome-orbit orbit-one" /><span className="welcome-orbit orbit-two" /><i className="bi bi-compass" /></div></section>
      <section className="dashboard-section-title"><div><h2>Today’s overview</h2><p>All activity in your workspace</p></div><span className="dashboard-period"><i className="bi bi-calendar3" /> All time</span></section>
      <div className="stats-grid">
        <StatsCard label="Total applications" value={data.summary.totalApplications} icon="bi-briefcase" />
        <StatsCard label="Applied" value={data.summary.appliedApplications} icon="bi-send" accent="blue" />
        <StatsCard label="Interviews" value={data.summary.applicationsWithInterviews} icon="bi-camera-video" accent="gold" hint="Applications with an interview" />
        <StatsCard label="Offers" value={data.summary.offers} icon="bi-trophy" accent="green" hint={`${data.summary.successRatePercent}% offer rate`} />
      </div>
      <div className="dashboard-chart-grid">
        <section className="dashboard-card"><div className="dashboard-card-heading"><div><h2>Application status</h2><p>How opportunities are distributed</p></div><Link to="/analytics">Details</Link></div>
          <div className="status-chart-layout"><div className="status-donut" style={{ background: ring }}><div><strong>{data.summary.totalApplications}</strong><span>total</span></div></div><div className="status-legend">{distribution.filter((item) => item.count > 0).map((item) => <div key={item.status}><span style={{ backgroundColor: statusColors[item.status] }} /><span>{item.status.replaceAll('_', ' ').toLowerCase()}</span><strong>{item.count}</strong></div>)}{!totalByStatus && <p>No applications yet.</p>}</div></div>
        </section>
        <section className="dashboard-card"><div className="dashboard-card-heading"><div><h2>Monthly applications</h2><p>Applications by applied date, last 12 months</p></div><Link to="/analytics">Details</Link></div>
          <div className="monthly-chart">{monthly.map((month) => <div className="month-bar" key={`${month.year}-${month.month}`} title={`${month.count} applications`}><div className="month-bar-track"><span style={{ height: `${Math.max(month.count ? 8 : 0, month.count / maximumMonth * 100)}%` }} /></div><small>{new Date(month.year, month.month - 1).toLocaleDateString(undefined, { month: 'short' })}</small></div>)}</div>
        </section>
      </div>
      <div className="dashboard-lists-grid">
        <section className="dashboard-card dashboard-list-card"><div className="dashboard-card-heading"><div><h2>Recent applications</h2><p>Your latest opportunities</p></div><Link to="/applications">View all</Link></div>
          {data.recent.content.length ? <div className="recent-list">{data.recent.content.map((application) => <Link className="recent-row" to={`/applications/${application.id}`} key={application.id}><span className="company-avatar">{application.companyName.charAt(0).toUpperCase()}</span><span className="recent-company"><strong>{application.companyName}</strong><small>{application.jobTitle}</small></span><StatusBadge status={application.status} /></Link>)}</div> : <div className="dashboard-list-empty">No applications yet. <Link to="/applications/new">Add your first one</Link></div>}
        </section>
        <section className="dashboard-card dashboard-list-card"><div className="dashboard-card-heading"><div><h2>Coming up</h2><p>Interviews and deadlines</p></div><Link to="/calendar">Calendar</Link></div>
          <div className="upcoming-list">{data.interviews.content.map((interview) => <div className="upcoming-row" key={`i-${interview.id}`}><span className="upcoming-date"><strong>{new Date(`${interview.interviewDate}T00:00:00`).getDate()}</strong><small>{new Date(`${interview.interviewDate}T00:00:00`).toLocaleDateString(undefined, { month: 'short' })}</small></span><span className="upcoming-copy"><strong>{interview.companyName}</strong><small>{interview.interviewRound} · {interview.jobTitle}</small></span><i className="bi bi-camera-video upcoming-type" /></div>)}
            {data.deadlines.content.map((application) => <div className="upcoming-row" key={`d-${application.id}`}><span className="upcoming-date upcoming-date--deadline"><strong>{new Date(`${application.deadline}T00:00:00`).getDate()}</strong><small>{new Date(`${application.deadline}T00:00:00`).toLocaleDateString(undefined, { month: 'short' })}</small></span><span className="upcoming-copy"><strong>{application.companyName}</strong><small>Application deadline · {application.jobTitle}</small></span><i className="bi bi-flag upcoming-type" /></div>)}
            {!data.interviews.content.length && !data.deadlines.content.length && <div className="dashboard-list-empty">No upcoming interviews or deadlines.</div>}
          </div>
        </section>
      </div>
    </>}
  </>
}

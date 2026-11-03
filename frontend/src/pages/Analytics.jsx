import { useEffect, useState } from 'react'
import analyticsService from '../services/analyticsService'
import { apiErrorMessage } from '../services/apiClient'
import PageIntro from '../components/PageIntro'
import StatsCard from '../components/StatsCard'
import LoadingSpinner from '../components/LoadingSpinner'
import './Analytics.css'

const colors = ['#9ca8af','#7797a8','#c1a264','#83a28b','#b99a59','#689878','#4b9166','#be7d76','#9e8c91']
export default function Analytics() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [refresh, setRefresh] = useState(0)
  useEffect(() => {
    let active = true
    setLoading(true); setError('')
    Promise.all([analyticsService.statistics(), analyticsService.companies()])
      .then(([statistics, companies]) => { if (active) setData({ statistics, companies }) })
      .catch((requestError) => { if (active) setError(apiErrorMessage(requestError, 'Could not load analytics.')) })
      .finally(() => { if (active) setLoading(false) })
    return () => { active = false }
  }, [refresh])
  const statuses = data?.statistics?.statusDistribution || []
  const total = statuses.reduce((sum, item) => sum + item.count, 0)
  let position = 0
  const ring = total ? `conic-gradient(${statuses.map((item, index) => { const start = position; position += item.count / total * 100; return `${colors[index % colors.length]} ${start}% ${position}%` }).join(', ')})` : '#edf0f2'
  const monthly = data?.statistics?.monthlyApplications || []
  const maxMonth = Math.max(1, ...monthly.map((item) => item.count))
  const maxCompany = Math.max(1, ...(data?.companies || []).map((item) => item.count))

  return <>
    <PageIntro eyebrow="Insights" title="Analytics" description="Understand your search patterns and where opportunities are moving." />
    {error && <div className="inline-error" role="alert">{error}<button onClick={() => setRefresh((value) => value + 1)}>Retry</button></div>}
    {loading ? <LoadingSpinner label="Loading analytics…" /> : data && <>
      <div className="analytics-stats"><StatsCard label="Applications" value={data.statistics.totalApplications} icon="bi-briefcase" /><StatsCard label="Interview conversion" value={`${data.statistics.interviewConversionRatePercent}%`} icon="bi-camera-video" accent="gold" hint={`${data.statistics.applicationsWithInterviews} applications reached interview`} /><StatsCard label="Offers" value={data.statistics.offers} icon="bi-trophy" accent="green" hint={`${data.statistics.offerRatePercent}% of applications`} /><StatsCard label="Rejections" value={data.statistics.rejections} icon="bi-x-circle" accent="rose" hint={`${data.statistics.rejectionRatePercent}% of applications`} /></div>
      <div className="analytics-grid">
        <section className="dashboard-card analytics-status-card"><div className="dashboard-card-heading"><div><h2>Status distribution</h2><p>All current application stages</p></div></div><div className="analytics-status-content"><div className="status-donut" style={{ background: ring }}><div><strong>{data.statistics.totalApplications}</strong><span>applications</span></div></div><div className="analytics-status-list">{statuses.filter((item) => item.count > 0).map((item) => <div key={item.status}><span style={{ background: colors[statuses.findIndex((status) => status.status === item.status) % colors.length] }} /><span>{item.status.replaceAll('_', ' ').toLowerCase()}</span><strong>{item.count}</strong></div>)}{!total && <p>No application data yet.</p>}</div></div></section>
        <section className="dashboard-card analytics-month-card"><div className="dashboard-card-heading"><div><h2>Monthly application trend</h2><p>Based on applied date · last 12 months</p></div></div><div className="analytics-month-chart">{monthly.map((item) => <div className="analytics-month-bar" key={`${item.year}-${item.month}`} title={`${item.count} applications`}><strong>{item.count || ''}</strong><span className="analytics-bar-track"><i style={{ height: `${Math.max(item.count ? 7 : 0, item.count / maxMonth * 100)}%` }} /></span><small>{new Date(item.year, item.month - 1).toLocaleDateString(undefined, { month: 'short' })}</small></div>)}</div></section>
        <section className="dashboard-card analytics-company-card"><div className="dashboard-card-heading"><div><h2>Applications by company</h2><p>Top companies in your search</p></div></div>{data.companies.length ? <div className="company-rankings">{data.companies.map((company, index) => <div className="company-ranking" key={company.companyName}><span className="company-rank">{index + 1}</span><strong>{company.companyName}</strong><span className="company-rank-track"><i style={{ width: `${company.count / maxCompany * 100}%` }} /></span><small>{company.count}</small></div>)}</div> : <div className="analytics-empty">Company counts will appear as applications are added.</div>}</section>
        <section className="dashboard-card analytics-conversion-card"><div className="dashboard-card-heading"><div><h2>Search outcomes</h2><p>Offers and rejections by application</p></div></div><div className="outcome-line"><span>Offer rate</span><strong>{data.statistics.offerRatePercent}%</strong><div><i className="outcome-bar outcome-bar--offer" style={{ width: `${data.statistics.offerRatePercent}%` }} /></div><small>{data.statistics.offers} offers</small></div><div className="outcome-line"><span>Rejection rate</span><strong>{data.statistics.rejectionRatePercent}%</strong><div><i className="outcome-bar outcome-bar--rejection" style={{ width: `${data.statistics.rejectionRatePercent}%` }} /></div><small>{data.statistics.rejections} rejections</small></div><p className="analytics-note">Rates use total applications as the denominator.</p></section>
      </div>
    </>}
  </>
}

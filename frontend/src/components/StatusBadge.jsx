import './StatusBadge.css'

const labels = { UNDER_REVIEW: 'Under review' }
export default function StatusBadge({ status }) {
  const label = labels[status] || status?.toLowerCase().replaceAll('_', ' ')
  return <span className={`status-badge status-badge--${status?.toLowerCase().replaceAll('_', '-') || 'saved'}`}>{label || 'Saved'}</span>
}

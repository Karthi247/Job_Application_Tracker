import './FilterPanel.css'

const statusOptions = ['SAVED', 'APPLIED', 'UNDER_REVIEW', 'SHORTLISTED', 'ASSESSMENT', 'INTERVIEW', 'OFFER', 'REJECTED', 'WITHDRAWN']
export default function FilterPanel({ status, priority, appliedFrom, appliedTo, onChange }) {
  return (
    <div className="filter-panel">
      <select aria-label="Filter by status" value={status} onChange={(event) => onChange('status', event.target.value)}>
        <option value="">All statuses</option>{statusOptions.map((item) => <option key={item} value={item}>{item.replaceAll('_', ' ').toLowerCase()}</option>)}
      </select>
      <select aria-label="Filter by priority" value={priority} onChange={(event) => onChange('priority', event.target.value)}>
        <option value="">All priorities</option><option value="HIGH">High priority</option><option value="MEDIUM">Medium priority</option><option value="LOW">Low priority</option>
      </select>
      <input aria-label="Applied from" type="date" value={appliedFrom} onChange={(event) => onChange('appliedFrom', event.target.value)} />
      <input aria-label="Applied to" type="date" value={appliedTo} onChange={(event) => onChange('appliedTo', event.target.value)} />
    </div>
  )
}

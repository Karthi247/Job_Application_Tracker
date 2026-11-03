import './StatsCard.css'

export default function StatsCard({ label, value, icon, hint, accent = 'blue' }) {
  return (
    <article className="stats-card">
      <div className="stats-card-top"><span>{label}</span><i className={`bi ${icon} stats-icon stats-icon--${accent}`} /></div>
      <strong className="stats-value">{value ?? '—'}</strong>
      {hint && <span className="stats-hint">{hint}</span>}
    </article>
  )
}

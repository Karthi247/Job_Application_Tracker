import './Pagination.css'

export default function Pagination({ page, totalPages, totalElements, size, onPageChange }) {
  if (totalPages <= 1) return <div className="pagination-summary">{totalElements} {totalElements === 1 ? 'application' : 'applications'}</div>
  return (
    <div className="pagination-bar">
      <span>{page * size + 1}–{Math.min((page + 1) * size, totalElements)} of {totalElements}</span>
      <div><button onClick={() => onPageChange(page - 1)} disabled={page === 0} aria-label="Previous page"><i className="bi bi-chevron-left" /></button><span>Page {page + 1} of {totalPages}</span><button onClick={() => onPageChange(page + 1)} disabled={page + 1 >= totalPages} aria-label="Next page"><i className="bi bi-chevron-right" /></button></div>
    </div>
  )
}

import './ConfirmDialog.css'

export default function ConfirmDialog({ open, title, message, confirmLabel = 'Delete', onConfirm, onCancel, busy = false }) {
  if (!open) return null
  return (
    <div className="dialog-backdrop" role="presentation" onMouseDown={(event) => event.target === event.currentTarget && onCancel()}>
      <section className="confirm-dialog" role="alertdialog" aria-modal="true" aria-labelledby="dialog-title" aria-describedby="dialog-message">
        <span className="dialog-icon"><i className="bi bi-exclamation-triangle" /></span>
        <h2 id="dialog-title">{title}</h2><p id="dialog-message">{message}</p>
        <div className="dialog-actions"><button className="secondary-button" onClick={onCancel} disabled={busy}>Cancel</button><button className="danger-button" onClick={onConfirm} disabled={busy}>{busy ? 'Deleting…' : confirmLabel}</button></div>
      </section>
    </div>
  )
}

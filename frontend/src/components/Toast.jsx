import { useEffect } from 'react'
import './Toast.css'

export default function Toast({ message, type = 'success', onClose }) {
  useEffect(() => {
    if (!message) return undefined
    const timeout = window.setTimeout(onClose, 3200)
    return () => window.clearTimeout(timeout)
  }, [message, onClose])
  if (!message) return null
  return <div className={`toast toast--${type}`} role={type === 'error' ? 'alert' : 'status'}><i className={`bi ${type === 'error' ? 'bi-exclamation-circle' : 'bi-check-circle'}`} />{message}<button onClick={onClose} aria-label="Dismiss message"><i className="bi bi-x" /></button></div>
}

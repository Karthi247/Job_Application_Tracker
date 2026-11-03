import './Avatar.css'

export default function Avatar({ name, size = 'small' }) {
  const initials = name?.trim().split(/\s+/).slice(0, 2).map((part) => part[0]).join('').toUpperCase()
  return <span className={`avatar avatar--${size}`} aria-hidden="true">{initials || <i className="bi bi-person" />}</span>
}

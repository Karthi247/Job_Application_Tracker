import { NavLink } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import Avatar from './Avatar'
import './Sidebar.css'

const sections = [
  { label: 'Workspace', links: [
    { to: '/dashboard', icon: 'bi-grid-1x2', label: 'Overview' },
    { to: '/applications', icon: 'bi-briefcase', label: 'Applications' },
    { to: '/board', icon: 'bi-kanban', label: 'Pipeline' },
  ] },
  { label: 'Planning', links: [
    { to: '/interviews', icon: 'bi-camera-video', label: 'Interviews' },
    { to: '/calendar', icon: 'bi-calendar3', label: 'Calendar' },
    { to: '/reminders', icon: 'bi-bell', label: 'Reminders' },
  ] },
  { label: 'Insights', links: [
    { to: '/analytics', icon: 'bi-bar-chart', label: 'Analytics' },
  ] },
]

export default function Sidebar({ onNavigate }) {
  const { user } = useAuth()
  return (
    <aside className="sidebar" aria-label="Main navigation">
      <NavLink to="/dashboard" className="brand" onClick={onNavigate}>
        <span className="brand-mark"><i className="bi bi-compass" /></span>
        <span>daymark<span className="brand-period">.</span></span>
      </NavLink>
      <div className="sidebar-profile">
        <Avatar name={user?.fullName} />
        <div className="sidebar-profile-copy"><strong>{user?.fullName || 'My workspace'}</strong><span>{user?.email || 'Job search'}</span></div>
        <i className="bi bi-chevron-expand sidebar-expand" />
      </div>
      <nav className="nav-groups">
        {sections.map((section) => (
          <section className="nav-section" key={section.label}>
            <h2>{section.label}</h2>
            {section.links.map((link) => (
              <NavLink key={link.to} to={link.to} onClick={onNavigate}
                className={({ isActive }) => `nav-link${isActive ? ' is-active' : ''}`}>
                <i className={`bi ${link.icon}`} /><span>{link.label}</span>
              </NavLink>
            ))}
          </section>
        ))}
      </nav>
      <div className="sidebar-bottom">
        <NavLink to="/profile" className="nav-link" onClick={onNavigate}><i className="bi bi-person" /><span>Profile</span></NavLink>
        <NavLink to="/settings" className="nav-link" onClick={onNavigate}><i className="bi bi-gear" /><span>Settings</span></NavLink>
        <div className="sidebar-footer"><span className="footer-indicator" />Your search, in focus</div>
      </div>
    </aside>
  )
}

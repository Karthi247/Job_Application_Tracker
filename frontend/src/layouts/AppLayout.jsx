import { useState } from 'react'
import { Outlet, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import Sidebar from '../components/Sidebar'
import Avatar from '../components/Avatar'
import './AppLayout.css'

export default function AppLayout() {
  const [menuOpen, setMenuOpen] = useState(false)
  const { clearSession, user } = useAuth()
  const location = useLocation()
  const navigate = useNavigate()
  return (
    <div className="app-shell">
      {menuOpen && <button className="mobile-scrim" aria-label="Close navigation" onClick={() => setMenuOpen(false)} />}
      <div className={`app-sidebar${menuOpen ? ' app-sidebar--open' : ''}`}><Sidebar onNavigate={() => setMenuOpen(false)} /></div>
      <div className="app-main">
        <header className="app-topbar">
          <button className="icon-button mobile-menu-button" aria-label="Open navigation" onClick={() => setMenuOpen(true)}>
            <i className="bi bi-list" />
          </button>
          <div className="topbar-crumb"><span>Workspace</span><i className="bi bi-chevron-right" /><strong>{location.pathname.split('/').filter(Boolean).at(-1)?.replaceAll('-', ' ') || 'Overview'}</strong></div>
          <div className="topbar-actions">
            <button className="icon-button" aria-label="Notifications"><i className="bi bi-bell" /></button>
            <span className="topbar-divider" />
            <button className="topbar-account" onClick={() => navigate('/profile')} aria-label="Open profile">
              <Avatar name={user?.fullName} />
              <i className="bi bi-chevron-down account-chevron" />
            </button>
            <button className="sign-out-button" onClick={() => { clearSession(); navigate('/login') }}>Sign out</button>
          </div>
        </header>
        <main className="page-container"><Outlet /></main>
      </div>
    </div>
  )
}

import { Link } from 'react-router-dom'
import './NotFound.css'

export default function NotFound() {
  return <main className="not-found"><span>404</span><h1>This page isn’t here</h1><p>The link may be out of date, or the page may have moved.</p><Link className="primary-button" to="/dashboard">Return to workspace</Link></main>
}

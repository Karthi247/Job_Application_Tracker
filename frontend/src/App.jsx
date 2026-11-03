import { Navigate, Route, Routes } from 'react-router-dom'
import AppLayout from './layouts/AppLayout'
import ProtectedRoute from './components/ProtectedRoute'
import Login from './pages/Login'
import Signup from './pages/Signup'
import Dashboard from './pages/Dashboard'
import Applications from './pages/Applications'
import ApplicationDetails from './pages/ApplicationDetails'
import AddApplication from './pages/AddApplication'
import EditApplication from './pages/EditApplication'
import KanbanBoard from './pages/KanbanBoard'
import Interviews from './pages/Interviews'
import Calendar from './pages/Calendar'
import Analytics from './pages/Analytics'
import Reminders from './pages/Reminders'
import Profile from './pages/Profile'
import Settings from './pages/Settings'
import NotFound from './pages/NotFound'

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<Signup />} />
      <Route element={<ProtectedRoute />}>
        <Route element={<AppLayout />}>
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/applications" element={<Applications />} />
          <Route path="/applications/new" element={<AddApplication />} />
          <Route path="/applications/:id/edit" element={<EditApplication />} />
          <Route path="/applications/:id" element={<ApplicationDetails />} />
          <Route path="/board" element={<KanbanBoard />} />
          <Route path="/interviews" element={<Interviews />} />
          <Route path="/calendar" element={<Calendar />} />
          <Route path="/analytics" element={<Analytics />} />
          <Route path="/reminders" element={<Reminders />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/settings" element={<Settings />} />
        </Route>
      </Route>
      <Route path="*" element={<NotFound />} />
    </Routes>
  )
}

export default App

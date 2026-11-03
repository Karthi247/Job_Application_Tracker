import { useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import calendarService from '../services/calendarService'
import { apiErrorMessage } from '../services/apiClient'
import PageIntro from '../components/PageIntro'
import LoadingSpinner from '../components/LoadingSpinner'
import './Calendar.css'

const dayKey = (date) => `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
const weekdays = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
export default function Calendar() {
  const [month, setMonth] = useState(() => new Date(new Date().getFullYear(), new Date().getMonth(), 1))
  const [events, setEvents] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [selectedDay, setSelectedDay] = useState(dayKey(new Date()))
  const from = dayKey(month)
  const to = dayKey(new Date(month.getFullYear(), month.getMonth() + 1, 0))

  useEffect(() => {
    let active = true
    setLoading(true); setError('')
    calendarService.events(from, to).then((result) => { if (active) setEvents(result) })
      .catch((requestError) => { if (active) setError(apiErrorMessage(requestError, 'Could not load calendar events.')) })
      .finally(() => { if (active) setLoading(false) })
    return () => { active = false }
  }, [from, to])

  const days = useMemo(() => {
    const offset = (month.getDay() + 6) % 7
    const first = new Date(month.getFullYear(), month.getMonth(), 1 - offset)
    return Array.from({ length: 42 }, (_, index) => new Date(first.getFullYear(), first.getMonth(), first.getDate() + index))
  }, [month])
  const eventsByDay = useMemo(() => events.reduce((map, event) => {
    const key = event.start.slice(0, 10)
    map.set(key, [...(map.get(key) || []), event])
    return map
  }, new Map()), [events])
  const selectedEvents = eventsByDay.get(selectedDay) || []
  function shiftMonth(amount) {
    const next = new Date(month.getFullYear(), month.getMonth() + amount, 1)
    setMonth(next); setSelectedDay(dayKey(next))
  }

  return <>
    <PageIntro eyebrow="Planning" title="Calendar" description="Bring interviews, deadlines, and follow-ups into view." action={<Link className="primary-action" to="/interviews"><i className="bi bi-plus-lg" />Schedule interview</Link>} />
    <section className="calendar-panel">
      <header className="calendar-heading"><div><h2>{month.toLocaleDateString(undefined, { month: 'long', year: 'numeric' })}</h2><span>{events.length} {events.length === 1 ? 'event' : 'events'} this month</span></div><div className="calendar-controls"><button onClick={() => setMonth(new Date(new Date().getFullYear(), new Date().getMonth(), 1))}>Today</button><button onClick={() => shiftMonth(-1)} aria-label="Previous month"><i className="bi bi-chevron-left" /></button><button onClick={() => shiftMonth(1)} aria-label="Next month"><i className="bi bi-chevron-right" /></button></div></header>
      <div className="calendar-legend"><span><i className="event-dot event-dot--interview" />Interview</span><span><i className="event-dot event-dot--deadline" />Deadline</span><span><i className="event-dot event-dot--reminder" />Reminder</span></div>
      {error && <div className="inline-error" role="alert">{error}</div>}
      {loading ? <LoadingSpinner label="Loading calendar…" /> : <div className="calendar-content">
        <div className="month-grid">
          {weekdays.map((day) => <div className="weekday-label" key={day}>{day}</div>)}
          {days.map((day) => {
            const key = dayKey(day)
            const dayEvents = eventsByDay.get(key) || []
            return <button type="button" key={key} className={`calendar-day${day.getMonth() !== month.getMonth() ? ' is-outside' : ''}${key === dayKey(new Date()) ? ' is-today' : ''}${key === selectedDay ? ' is-selected' : ''}`} onClick={() => setSelectedDay(key)}>
              <span className="day-number">{day.getDate()}</span>
              <span className="day-event-pills">{dayEvents.slice(0, 2).map((event) => <span key={event.id} className={`day-event day-event--${event.eventType.toLowerCase()}`}>{event.title}</span>)}{dayEvents.length > 2 && <small>+{dayEvents.length - 2} more</small>}</span>
              {dayEvents.length > 0 && <span className="day-mobile-dots">{dayEvents.slice(0, 3).map((event) => <i key={event.id} className={`event-dot event-dot--${event.eventType.toLowerCase()}`} />)}</span>}
            </button>
          })}
        </div>
        <aside className="selected-day-panel"><div className="selected-day-heading"><div><span>Selected date</span><h3>{new Date(`${selectedDay}T00:00:00`).toLocaleDateString(undefined, { weekday: 'long', month: 'long', day: 'numeric' })}</h3></div><span className="selected-event-count">{selectedEvents.length}</span></div>
          {selectedEvents.length ? <div className="selected-event-list">{selectedEvents.map((event) => <article className="selected-event" key={event.id}><i className={`event-dot event-dot--${event.eventType.toLowerCase()}`} /><div><strong>{event.title}</strong><span>{event.companyName ? `${event.companyName}${event.jobTitle ? ` · ${event.jobTitle}` : ''}` : event.eventType.toLowerCase()}</span><small>{event.start.slice(11,16) !== '00:00' ? event.start.slice(11,16) : 'All day'}</small></div></article>)}</div> : <p className="selected-day-empty">Nothing scheduled for this day.</p>}
        </aside>
      </div>}
    </section>
  </>
}

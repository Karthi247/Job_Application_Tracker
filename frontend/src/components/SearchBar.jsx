import './SearchBar.css'

export default function SearchBar({ value, onChange, placeholder = 'Search applications' }) {
  return <label className="search-field"><i className="bi bi-search" /><input value={value} onChange={(event) => onChange(event.target.value)} placeholder={placeholder} aria-label={placeholder} /></label>
}

import PageIntro from '../components/PageIntro'
import './SectionPage.css'

export default function SectionPage({ title, eyebrow = 'Your workspace', description }) {
  return (
    <>
      <PageIntro title={title} eyebrow={eyebrow} description={description} />
      <section className="section-connection-card" aria-live="polite">
        <span className="connection-icon"><i className="bi bi-layers" /></span>
        <div><h2>{title} is ready for your data</h2><p>This view will be connected to your account in the next build phase.</p></div>
      </section>
    </>
  )
}

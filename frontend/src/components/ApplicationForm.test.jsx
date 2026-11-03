import { describe, expect, it, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import ApplicationForm from './ApplicationForm'

describe('ApplicationForm', () => {
  it('requires a company and job title before submitting', async () => {
    const user = userEvent.setup()
    const onSubmit = vi.fn()
    render(<ApplicationForm onSubmit={onSubmit} onCancel={vi.fn()} submitLabel="Save application" />)

    await user.click(screen.getByRole('button', { name: 'Save application' }))

    expect(onSubmit).not.toHaveBeenCalled()
  })

  it('submits only API fields and converts empty optional fields to null', async () => {
    const user = userEvent.setup()
    const onSubmit = vi.fn()
    render(<ApplicationForm initialValue={{ id: 83, createdAt: '2026-01-01T00:00:00' }} onSubmit={onSubmit} onCancel={vi.fn()} submitLabel="Save changes" />)
    await user.type(screen.getByLabelText('Company'), 'Northwind')
    await user.type(screen.getByLabelText('Job title'), 'Product Designer')
    await user.click(screen.getByRole('button', { name: 'Save changes' }))

    expect(onSubmit).toHaveBeenCalledOnce()
    const payload = onSubmit.mock.calls[0][0]
    expect(payload).toMatchObject({ companyName: 'Northwind', jobTitle: 'Product Designer', status: 'SAVED', priority: 'MEDIUM', notes: null })
    expect(payload).not.toHaveProperty('id')
    expect(payload).not.toHaveProperty('createdAt')
  })
})

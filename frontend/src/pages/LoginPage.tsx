import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import client from '../api/client'

export default function LoginPage() {
	const [email, setEmail] = useState('admin@bank.com')
	const [password, setPassword] = useState('Admin@123')
	const [error, setError] = useState('')
	const navigate = useNavigate()

	async function login(e: React.FormEvent) {
		e.preventDefault()
		setError('')
		try {
			const { data } = await client.post('/auth/login', { email, password })
			localStorage.setItem('token', data.token)
			localStorage.setItem('role', data.role)
			if (data.role === 'ADMIN') navigate('/admin')
			else navigate('/user')
		} catch (err: any) {
			setError(err?.response?.data?.message || 'Login failed')
		}
	}

	return (
		<div style={{ maxWidth: 400, margin: '80px auto' }}>
			<h3>Login</h3>
			<form onSubmit={login}>
				<div>
					<label>Email</label>
					<input value={email} onChange={e => setEmail(e.target.value)} style={{ width: '100%' }} />
				</div>
				<div>
					<label>Password</label>
					<input type="password" value={password} onChange={e => setPassword(e.target.value)} style={{ width: '100%' }} />
				</div>
				{error && <div style={{ color: 'red' }}>{error}</div>}
				<button type="submit">Login</button>
			</form>
		</div>
	)
} 
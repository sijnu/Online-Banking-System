import { useEffect, useState } from 'react'
import client from '../api/client'

export default function AdminDashboard() {
	const [users, setUsers] = useState<any[]>([])
	const [loans, setLoans] = useState<any[]>([])
	const [email, setEmail] = useState('user1@bank.com')
	const [password, setPassword] = useState('User@123')
	const [username, setUsername] = useState('user1')
	const [fullName, setFullName] = useState('User One')
	const [aadhaar, setAadhaar] = useState('1111-2222-3333')
	const [initialBalance, setInitialBalance] = useState(1000)

	async function load() {
		const u = await client.get('/admin/users')
		setUsers(u.data)
		const l = await client.get('/admin/loans')
		setLoans(l.data)
	}
	useEffect(() => { load() }, [])

	async function addUser(e: React.FormEvent) {
		e.preventDefault()
		await client.post('/admin/add-user', { email, password, username, fullName, aadhaarNumber: aadhaar, initialBalance })
		await load()
	}

	async function approve(loanId: number) {
		await client.put(`/admin/loans/${loanId}/approve`)
		await load()
	}
	async function reject(loanId: number) {
		await client.put(`/admin/loans/${loanId}/reject`)
		await load()
	}

	return (
		<div>
			<h3>Admin Dashboard</h3>
			<section>
				<h4>Add User</h4>
				<form onSubmit={addUser} style={{ display: 'grid', gap: 8, gridTemplateColumns: 'repeat(2, 1fr)' }}>
					<input placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} />
					<input placeholder="Password" value={password} onChange={e=>setPassword(e.target.value)} />
					<input placeholder="Username" value={username} onChange={e=>setUsername(e.target.value)} />
					<input placeholder="Full Name" value={fullName} onChange={e=>setFullName(e.target.value)} />
					<input placeholder="Aadhaar" value={aadhaar} onChange={e=>setAadhaar(e.target.value)} />
					<input placeholder="Initial Balance" type="number" value={initialBalance} onChange={e=>setInitialBalance(parseFloat(e.target.value))} />
					<button type="submit">Create</button>
				</form>
			</section>
			<section>
				<h4>Users</h4>
				<ul>
					{users.map(u => <li key={u.userId}>{u.fullName} ({u.username}) - {u.email}</li>)}
				</ul>
			</section>
			<section>
				<h4>Loan Requests</h4>
				<table>
					<thead><tr><th>ID</th><th>User</th><th>Amount</th><th>Status</th><th>Actions</th></tr></thead>
					<tbody>
					{loans.map(l => (
						<tr key={l.loanId}>
							<td>{l.loanId}</td>
							<td>{l.user?.fullName || l.userId}</td>
							<td>{l.amount}</td>
							<td>{l.status}</td>
							<td>
								<button onClick={() => approve(l.loanId)}>Approve</button>
								<button onClick={() => reject(l.loanId)}>Reject</button>
							</td>
						</tr>
					))}
					</tbody>
				</table>
			</section>
		</div>
	)
} 
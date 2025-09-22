import React from 'react'
import { useNavigate, NavLink } from 'react-router-dom'

export default function App({ children }: { children: React.ReactNode }) {
	const navigate = useNavigate()
	function logout() {
		localStorage.removeItem('token')
		localStorage.removeItem('role')
		navigate('/')
	}
	return (
		<div className="min-h-screen flex">
			<aside className="w-64 bg-white shadow-soft hidden md:flex flex-col">
				<div className="p-6 font-bold text-xl">Banking</div>
				<nav className="flex-1 px-4 space-y-2">
					<NavLink to="/user" className={({isActive}) => `block px-3 py-2 rounded ${isActive ? 'bg-primary-50 text-primary-700' : 'hover:bg-gray-100'}`}>Dashboard</NavLink>
					<NavLink to="/user#transactions" className="block px-3 py-2 rounded hover:bg-gray-100">Transactions</NavLink>
					<NavLink to="/user#loans" className="block px-3 py-2 rounded hover:bg-gray-100">Loans</NavLink>
					<NavLink to="/user#profile" className="block px-3 py-2 rounded hover:bg-gray-100">Profile</NavLink>
				</nav>
				<button onClick={logout} className="m-4 px-4 py-2 bg-primary-600 text-white rounded hover:bg-primary-700">Logout</button>
			</aside>
			<main className="flex-1 p-4 md:p-8">
				{children}
			</main>
		</div>
	)
} 
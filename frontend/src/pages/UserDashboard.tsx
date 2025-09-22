import { useEffect, useMemo, useState } from 'react'
import { motion } from 'framer-motion'
import {
	Area, AreaChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis,
} from 'recharts'
import client from '../api/client'

type Txn = { txnId: number; type: string; amount: number; timestamp: string; toAccount?: { accountId: number } }

type Loan = { loanId: number; amount: number; status: string }

const fmt = new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' })

export default function UserDashboard() {
	const [balance, setBalance] = useState<number>(0)
	const [txns, setTxns] = useState<Txn[]>([])
	const [amount, setAmount] = useState<string>('')
	const [toAccountId, setToAccountId] = useState<string>('')
	const [loanAmount, setLoanAmount] = useState<string>('')
	const [reason, setReason] = useState<string>('Personal')
	const [duration, setDuration] = useState<string>('12')
	const [loans, setLoans] = useState<Loan[]>([])
	const [loading, setLoading] = useState<boolean>(false)
	const [error, setError] = useState<string>('')

	async function safe<T>(fn: () => Promise<T>, fallback: T): Promise<T> {
		try { return await fn() } catch { return fallback }
	}

	async function load() {
		setLoading(true)
		setError('')
		const b = await safe(async () => (await client.get('/user/balance')).data as number, 0)
		setBalance(b)
		const t = await safe(async () => (await client.get('/user/transactions')).data as Txn[], [])
		setTxns(t)
		const ls = await safe(async () => (await client.get('/user/loan/status')).data as Loan[], [])
		setLoans(ls)
		setLoading(false)
	}
	useEffect(() => { load() }, [])

	const chartData = useMemo(() => {
		const now = Date.now()
		const points = txns.slice(-10).map(t => ({
			name: new Date(t.timestamp || now).toLocaleDateString(),
			amount: Number(t.amount || 0),
		}))
		if (!points.length) {
			return [
				{ name: 'Mon', amount: 200 },
				{ name: 'Tue', amount: 120 },
				{ name: 'Wed', amount: 350 },
				{ name: 'Thu', amount: 90 },
				{ name: 'Fri', amount: 480 },
			]
		}
		return points
	}, [txns])

	async function onDeposit() {
		if (!amount) return
		await client.post('/user/deposit', null, { params: { amount: parseFloat(amount) } })
		setAmount('')
		load()
	}
	async function onWithdraw() {
		if (!amount) return
		await client.post('/user/withdraw', null, { params: { amount: parseFloat(amount) } })
		setAmount('')
		load()
	}
	async function onTransfer() {
		if (!amount || !toAccountId) return
		await client.post('/user/transfer', null, { params: { toAccountId: parseInt(toAccountId), amount: parseFloat(amount) } })
		setAmount(''); setToAccountId('')
		load()
	}
	async function onApplyLoan() {
		if (!loanAmount) return
		await client.post('/user/loan/apply', null, { params: { amount: parseFloat(loanAmount), reason, duration: parseInt(duration) } })
		setLoanAmount('')
		load()
	}

	return (
		<div className="space-y-6">
			<header className="flex items-center justify-between">
				<h1 className="text-2xl md:text-3xl font-bold">Welcome back</h1>
				<div className="text-sm text-gray-500">Secure Banking Dashboard</div>
			</header>

			<section className="grid grid-cols-1 md:grid-cols-3 gap-4">
				<motion.div whileHover={{ scale: 1.01 }} className="rounded-xl p-5 bg-white shadow-soft">
					<div className="text-gray-500">Available Balance</div>
					<div className="text-3xl font-semibold mt-2">{fmt.format(balance)}</div>
				</motion.div>
				<motion.div whileHover={{ scale: 1.01 }} className="rounded-xl p-5 gradient-card text-white shadow-soft">
					<div className="text-white/90">Quick Deposit / Withdraw</div>
					<div className="flex items-end gap-2 mt-2">
						<input className="flex-1 px-3 py-2 rounded text-black" placeholder="Amount" value={amount} onChange={e=>setAmount(e.target.value)} />
						<button onClick={onDeposit} className="px-4 py-2 bg-white/20 hover:bg-white/30 rounded">Deposit</button>
						<button onClick={onWithdraw} className="px-4 py-2 bg-white/20 hover:bg-white/30 rounded">Withdraw</button>
					</div>
				</motion.div>
				<motion.div whileHover={{ scale: 1.01 }} className="rounded-xl p-5 bg-white shadow-soft">
					<div className="text-gray-500">Transfer</div>
					<div className="mt-2 flex gap-2">
						<input className="flex-1 px-3 py-2 rounded border" placeholder="Recipient Account ID" value={toAccountId} onChange={e=>setToAccountId(e.target.value)} />
						<input className="w-32 px-3 py-2 rounded border" placeholder="Amount" value={amount} onChange={e=>setAmount(e.target.value)} />
						<button onClick={onTransfer} className="px-4 py-2 bg-primary-600 text-white rounded hover:bg-primary-700">Send</button>
					</div>
				</motion.div>
			</section>

			<section className="grid grid-cols-1 lg:grid-cols-3 gap-4">
				<div className="col-span-2 rounded-xl p-5 bg-white shadow-soft" id="transactions">
					<div className="flex items-center justify-between">
						<h2 className="font-semibold">Transactions</h2>
					</div>
					<div className="mt-4 overflow-x-auto">
						<table className="min-w-full text-sm">
							<thead>
								<tr className="text-left text-gray-500">
									<th className="py-2">Date</th>
									<th className="py-2">Type</th>
									<th className="py-2">Amount</th>
									<th className="py-2">To</th>
								</tr>
							</thead>
							<tbody>
								{(txns.length ? txns : []).map(t => (
									<tr key={t.txnId} className="border-t">
										<td className="py-2">{new Date(t.timestamp || Date.now()).toLocaleString()}</td>
										<td className="py-2">{t.type}</td>
										<td className="py-2">{fmt.format(Number(t.amount))}</td>
										<td className="py-2">{t.toAccount ? `#${t.toAccount.accountId}` : '-'}</td>
									</tr>
								))}
								{!txns.length && (
									<tr><td colSpan={4} className="py-6 text-center text-gray-500">No transactions yet</td></tr>
								)}
							</tbody>
						</table>
					</div>
				</div>
				<div className="rounded-xl p-5 bg-white shadow-soft">
					<h2 className="font-semibold">Spending Trend</h2>
					<div className="h-56 mt-4">
						<ResponsiveContainer width="100%" height="100%">
							<AreaChart data={chartData}>
								<defs>
									<linearGradient id="colorAmt" x1="0" y1="0" x2="0" y2="1">
										<stop offset="5%" stopColor="#2563EB" stopOpacity={0.4}/>
										<stop offset="95%" stopColor="#2563EB" stopOpacity={0}/>
									</linearGradient>
								</defs>
								<CartesianGrid strokeDasharray="3 3" />
								<XAxis dataKey="name" />
								<YAxis />
								<Tooltip />
								<Area type="monotone" dataKey="amount" stroke="#2563EB" fillOpacity={1} fill="url(#colorAmt)" />
							</AreaChart>
						</ResponsiveContainer>
					</div>
				</div>
			</section>

			<section className="grid grid-cols-1 lg:grid-cols-3 gap-4" id="loans">
				<div className="rounded-xl p-5 bg-white shadow-soft">
					<h2 className="font-semibold">Apply for Loan</h2>
					<div className="mt-3 space-y-2">
						<input className="w-full px-3 py-2 rounded border" placeholder="Amount" value={loanAmount} onChange={e=>setLoanAmount(e.target.value)} />
						<input className="w-full px-3 py-2 rounded border" placeholder="Reason" value={reason} onChange={e=>setReason(e.target.value)} />
						<input className="w-full px-3 py-2 rounded border" placeholder="Duration (months)" value={duration} onChange={e=>setDuration(e.target.value)} />
						<button onClick={onApplyLoan} className="w-full px-4 py-2 bg-primary-600 text-white rounded hover:bg-primary-700">Submit</button>
					</div>
				</div>
				<div className="rounded-xl p-5 bg-white shadow-soft lg:col-span-2">
					<h2 className="font-semibold">Loan Requests</h2>
					<ul className="mt-3 space-y-2">
						{(loans.length ? loans : []).map(l => (
							<li key={l.loanId} className="flex justify-between items-center p-3 rounded border">
								<div>#{l.loanId} • {fmt.format(Number(l.amount))}</div>
								<span className={`text-xs px-2 py-1 rounded ${l.status === 'APPROVED' ? 'bg-green-100 text-green-700' : l.status === 'REJECTED' ? 'bg-red-100 text-red-700' : 'bg-yellow-100 text-yellow-700'}`}>{l.status}</span>
							</li>
						))}
						{!loans.length && <li className="text-gray-500 text-center p-4">No loan requests yet</li>}
					</ul>
				</div>
			</section>
		</div>
	)
} 
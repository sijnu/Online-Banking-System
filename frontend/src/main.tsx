import React from 'react'
import ReactDOM from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import App from './App'
import LoginPage from './pages/LoginPage'
import AdminDashboard from './pages/AdminDashboard'
import UserDashboard from './pages/UserDashboard'

const router = createBrowserRouter([
	{ path: '/', element: <LoginPage /> },
	{ path: '/admin', element: <App><AdminDashboard /></App> },
	{ path: '/user', element: <App><UserDashboard /></App> },
])

ReactDOM.createRoot(document.getElementById('root')!).render(
	<React.StrictMode>
		<RouterProvider router={router} />
	</React.StrictMode>
) 
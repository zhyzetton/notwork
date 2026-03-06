import { Outlet } from 'react-router-dom'
import AppHeader from './AppHeader'
import './layout.css'

const Layout = () => {
  return (
    <div className="app-layout">
      <AppHeader />
      <div className="app-content">
        <Outlet />
      </div>
    </div>
  )
}

export default Layout

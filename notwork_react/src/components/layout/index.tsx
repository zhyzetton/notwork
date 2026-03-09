import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import Sidebar from './Sidebar'
import Header from './AppHeader'
import './layout.css'

const AppLayout = () => {
  const navigate = useNavigate()
  const location = useLocation()

  return (
    <div className="app-layout">
      <Sidebar currentPath={location.pathname} onNavigate={navigate} />
      <div className="app-main">
        <Header />
        <div className="app-content">
          <Outlet />
        </div>
      </div>
    </div>
  )
}

export default AppLayout

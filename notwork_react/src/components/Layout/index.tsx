import { AliveScope } from 'react-activation'
import Header from './Header'
import { Outlet } from 'react-router-dom'

const Layout = () => {
  return (
    <div className="min-h-screen bg-gray-100">
      <Header />
      <div className="p-2">
        <main className="min-h-[calc(100vh-80px)] container mx-auto">
          <AliveScope>
            <Outlet />
          </AliveScope>
        </main>
      </div>
    </div>
  )
}

export default Layout

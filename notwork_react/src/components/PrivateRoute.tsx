import { Navigate } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { useEffect } from 'react'

const PrivateRoute = ({ children }: { children: React.ReactNode }) => {
  const { isLoggedIn } = useAuth()

  useEffect(() => {
    if (!isLoggedIn) {
      window.dispatchEvent(new Event('auth:showLogin'))
    }
  }, [isLoggedIn])

  if (!isLoggedIn) {
    return <Navigate to="/home" replace />
  }

  return <>{children}</>
}

export default PrivateRoute

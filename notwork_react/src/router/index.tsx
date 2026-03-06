import { createBrowserRouter } from 'react-router-dom'
import App from '@/App'
import Home from '@/pages/Home'

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        path: 'home',
        element: <Home />,
      },
      {
        path: 'chat',
        element: <div></div>
      },
      {
        path: 'my',
        element: <div></div>
      }
    ],
  },
])

export default router

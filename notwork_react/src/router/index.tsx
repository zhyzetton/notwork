import { createBrowserRouter, Navigate } from 'react-router-dom'
import App from '@/App'
import Home from '@/pages/Home'
import BlogDetail from '@/pages/BlogDetail'
import Chat from '@/pages/Chat'
import My from '@/pages/My'
import NewBlog from '@/pages/NewBlog'
import PrivateRoute from '@/components/PrivateRoute'

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        index: true,
        element: <Navigate to="/home" replace />,
      },
      {
        path: 'home',
        element: <Home />,
      },
      {
        path: 'blog/:id',
        element: <BlogDetail />,
      },
      {
        path: 'chat',
        element: <PrivateRoute><Chat /></PrivateRoute>,
      },
      {
        path: 'my',
        element: <PrivateRoute><My /></PrivateRoute>,
      },
      {
        path: 'new',
        element: <PrivateRoute><NewBlog /></PrivateRoute>,
      },
    ],
  },
])

export default router

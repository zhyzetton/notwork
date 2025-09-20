import { createBrowserRouter } from 'react-router-dom'
import Layout from '@/components/Layout/index'
import BlogList from '@/pages/BlogList'

const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      {
        path: '/blogs/:tag',
        element: <BlogList />,
      },
    ],
  },
])

export default router

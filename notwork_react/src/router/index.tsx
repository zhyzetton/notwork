import { createBrowserRouter } from 'react-router-dom'
import Layout from '@/components/Layout/index'
import BlogList from '@/pages/BlogList'
import Editor from '@/components/Editor'
import "vditor/dist/index.css";

const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      {
        path: '/blogs/:tag',
        element: <BlogList />,
      },
      {
        path: '/write',
        element: <Editor />
      }
    ],
  },
])

export default router

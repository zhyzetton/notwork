import { createBrowserRouter } from 'react-router-dom'
import Layout from '@/components/Layout/index'
import BlogList from '@/pages/BlogList'
import Editor from '@/components/Editor'
import "vditor/dist/index.css";
import BlogDetail from '@/pages/BlogDetail';
import ChatPage from '@/pages/ChatPage';
import KeepAlive from 'react-activation'

const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      {
        path: '/blogs/:tag',
        element: <KeepAlive name='blog-list' children={<BlogList />} />,
      },
      {
        path: '/blogs/detail/:id',
        element: <KeepAlive name='blog-detail' children={<BlogDetail />} />
      },
      {
        path: '/ragChat',
        element: <KeepAlive name='chat-page' children={<ChatPage />} />
      },
      {
        path: '/write',
        element: <KeepAlive name='blog-write' children={<Editor />} />
      }
    ],
  },
])

export default router

import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Empty, Spin } from 'antd'
import { getBlogs } from '@/api'
import { useAuth } from '@/contexts/AuthContext'
import './index.css'

interface BlogItem {
  id: number
  title: string
  createTime: string
  viewCount: number
  likeCount: number
  collectCount: number
  tagName?: string
}

const My = () => {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [blogs, setBlogs] = useState<BlogItem[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!user) return
    getBlogs({ pageNum: 1, pageSize: 50, userId: user.userId })
      .then((res: any) => setBlogs(res.data?.records || []))
      .finally(() => setLoading(false))
  }, [user])

  return (
    <div className="my-page">
      <div className="my-profile">
        <div className="my-avatar">
          {user?.avatarUrl ? (
            <img src={user.avatarUrl} alt="" />
          ) : (
            <span>{user?.username.charAt(0).toUpperCase()}</span>
          )}
        </div>
        <div>
          <h1 className="my-username">{user?.username}</h1>
          <p className="my-role">{user?.role === 1 ? '管理员' : '用户'}</p>
        </div>
      </div>

      <h2 className="my-section-title">我的文章</h2>

      {loading ? (
        <div className="my-loading"><Spin /></div>
      ) : blogs.length === 0 ? (
        <Empty description="还没有写过文章" />
      ) : (
        <div className="my-blog-list">
          {blogs.map((blog) => (
            <div
              key={blog.id}
              className="my-blog-item"
              onClick={() => navigate(`/blog/${blog.id}`)}
            >
              <div className="my-blog-info">
                <h3>{blog.title}</h3>
                <div className="my-blog-meta">
                  {blog.tagName && <span className="my-blog-tag">{blog.tagName}</span>}
                  <span>{blog.createTime?.substring(0, 10)}</span>
                </div>
              </div>
              <div className="my-blog-stats">
                <span><i className="ri-eye-line" /> {blog.viewCount || 0}</span>
                <span><i className="ri-thumb-up-line" /> {blog.likeCount || 0}</span>
                <span><i className="ri-bookmark-line" /> {blog.collectCount || 0}</span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

export default My

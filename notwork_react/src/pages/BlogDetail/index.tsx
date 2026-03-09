import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { Spin, Button, message, Input, Empty, Popconfirm } from 'antd'
import {
  getBlogById,
  toggleLike,
  toggleCollect,
  getLikeStatus,
  getCollectStatus,
  getComments,
  addComment,
  deleteComment,
} from '@/api'
import { useAuth } from '@/contexts/AuthContext'
import './index.css'

interface Blog {
  id: number
  userId: number
  title: string
  contentMarkdown: string
  contentHtml?: string
  coverUrl?: string
  viewCount: number
  likeCount: number
  collectCount: number
  createTime: string
}

interface Comment {
  id: number
  userId: number
  content: string
  createTime: string
}

const BlogDetail = () => {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { user, isLoggedIn } = useAuth()

  const [blog, setBlog] = useState<Blog | null>(null)
  const [loading, setLoading] = useState(true)
  const [liked, setLiked] = useState(false)
  const [collected, setCollected] = useState(false)
  const [comments, setComments] = useState<Comment[]>([])
  const [commentText, setCommentText] = useState('')

  const blogId = Number(id)

  useEffect(() => {
    setLoading(true)
    getBlogById(blogId)
      .then((res: any) => setBlog(res.data))
      .finally(() => setLoading(false))

    if (isLoggedIn) {
      getLikeStatus(blogId).then((res: any) => setLiked(res.data))
      getCollectStatus(blogId).then((res: any) => setCollected(res.data))
    }
    loadComments()
  }, [blogId, isLoggedIn])

  const loadComments = () => {
    getComments(blogId).then((res: any) => setComments(res.data?.records || []))
  }

  const handleLike = async () => {
    if (!isLoggedIn) return message.info('请先登录')
    const res: any = await toggleLike(blogId)
    setLiked(res.data)
    setBlog((prev) => prev ? {
      ...prev,
      likeCount: prev.likeCount + (res.data ? 1 : -1),
    } : prev)
  }

  const handleCollect = async () => {
    if (!isLoggedIn) return message.info('请先登录')
    const res: any = await toggleCollect(blogId)
    setCollected(res.data)
    setBlog((prev) => prev ? {
      ...prev,
      collectCount: prev.collectCount + (res.data ? 1 : -1),
    } : prev)
  }

  const handleAddComment = async () => {
    if (!commentText.trim()) return
    if (!isLoggedIn) return message.info('请先登录')
    await addComment(blogId, commentText)
    setCommentText('')
    loadComments()
    message.success('评论成功')
  }

  const handleDeleteComment = async (commentId: number) => {
    await deleteComment(commentId)
    loadComments()
    message.success('已删除')
  }

  if (loading) {
    return <div className="blog-detail-loading"><Spin /></div>
  }

  if (!blog) {
    return <Empty description="文章不存在" />
  }

  return (
    <div className="blog-detail">
      <button className="blog-back" onClick={() => navigate(-1)}>
        <i className="ri-arrow-left-line" /> 返回
      </button>

      <article className="blog-article">
        <h1 className="blog-title">{blog.title}</h1>
        <div className="blog-info">
          <span>{blog.createTime?.substring(0, 10)}</span>
          <span><i className="ri-eye-line" /> {blog.viewCount}</span>
        </div>

        {blog.coverUrl && (
          <div className="blog-cover">
            <img src={blog.coverUrl} alt="" />
          </div>
        )}

        <div
          className="blog-content"
          dangerouslySetInnerHTML={{ __html: blog.contentHtml || blog.contentMarkdown }}
        />
      </article>

      <div className="blog-actions">
        <button className={`action-btn ${liked ? 'active' : ''}`} onClick={handleLike}>
          <i className={liked ? 'ri-thumb-up-fill' : 'ri-thumb-up-line'} />
          <span>{blog.likeCount}</span>
        </button>
        <button className={`action-btn ${collected ? 'active' : ''}`} onClick={handleCollect}>
          <i className={collected ? 'ri-bookmark-fill' : 'ri-bookmark-line'} />
          <span>{blog.collectCount}</span>
        </button>
      </div>

      <section className="blog-comments">
        <h3>评论</h3>
        <div className="comment-input">
          <Input.TextArea
            value={commentText}
            onChange={(e) => setCommentText(e.target.value)}
            placeholder="写下你的想法..."
            autoSize={{ minRows: 2, maxRows: 4 }}
          />
          <Button type="primary" onClick={handleAddComment} disabled={!commentText.trim()}>
            发表
          </Button>
        </div>

        <div className="comment-list">
          {comments.length === 0 ? (
            <p className="comment-empty">还没有评论，来写第一条吧</p>
          ) : (
            comments.map((c) => (
              <div key={c.id} className="comment-item">
                <div className="comment-body">
                  <p className="comment-content">{c.content}</p>
                  <span className="comment-time">{c.createTime?.substring(0, 16)}</span>
                </div>
                {user && user.userId === c.userId && (
                  <Popconfirm title="确定删除?" onConfirm={() => handleDeleteComment(c.id)}>
                    <i className="ri-delete-bin-line comment-delete" />
                  </Popconfirm>
                )}
              </div>
            ))
          )}
        </div>
      </section>
    </div>
  )
}

export default BlogDetail

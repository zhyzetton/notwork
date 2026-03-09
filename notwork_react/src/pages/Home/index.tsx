import { useEffect, useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { Spin, Empty, Tag } from 'antd'
import { getBlogs, esSearchBlogs, getBlogTags } from '@/api'
import './index.css'

interface BlogItem {
  id: number
  title: string
  coverUrl?: string
  viewCount: number
  likeCount: number
  collectCount: number
  createTime: string
  username: string
  userAvatar?: string
  tagName?: string
}

interface TagItem {
  id: number
  tagCode: string
  tagName: string
}

const Home = () => {
  const [blogs, setBlogs] = useState<BlogItem[]>([])
  const [tags, setTags] = useState<TagItem[]>([])
  const [loading, setLoading] = useState(true)
  const [activeTag, setActiveTag] = useState<string>('')
  const [searchParams] = useSearchParams()
  const navigate = useNavigate()
  const keyword = searchParams.get('keyword')

  useEffect(() => {
    getBlogTags().then((res: any) => setTags(res.data || []))
  }, [])

  useEffect(() => {
    setLoading(true)
    if (keyword) {
      esSearchBlogs(keyword)
        .then((res: any) => setBlogs(res.data || []))
        .finally(() => setLoading(false))
    } else {
      getBlogs({ pageNum: 1, pageSize: 20, tagCode: activeTag || undefined })
        .then((res: any) => setBlogs(res.data?.records || []))
        .finally(() => setLoading(false))
    }
  }, [keyword, activeTag])

  return (
    <div className="home">
      <div className="home-heading">
        <h1>{keyword ? `搜索"${keyword}"的结果` : '文章'}</h1>
        {!keyword && (
          <p className="home-subtitle">探索知识、记录想法</p>
        )}
      </div>

      {!keyword && tags.length > 0 && (
        <div className="home-tags">
          <Tag
            className={`notion-tag ${activeTag === '' ? 'active' : ''}`}
            onClick={() => setActiveTag('')}
          >
            全部
          </Tag>
          {tags.map((tag) => (
            <Tag
              key={tag.id}
              className={`notion-tag ${activeTag === tag.tagCode ? 'active' : ''}`}
              onClick={() => setActiveTag(tag.tagCode)}
            >
              {tag.tagName}
            </Tag>
          ))}
        </div>
      )}

      {loading ? (
        <div className="home-loading">
          <Spin />
        </div>
      ) : blogs.length === 0 ? (
        <Empty description="暂无文章" />
      ) : (
        <div className="blog-list">
          {blogs.map((blog) => (
            <article
              key={blog.id}
              className="blog-card"
              onClick={() => navigate(`/blog/${blog.id}`)}
            >
              {blog.coverUrl && (
                <div className="blog-card-cover">
                  <img src={blog.coverUrl} alt="" />
                </div>
              )}
              <div className="blog-card-body">
                <h2 className="blog-card-title">
                  {(blog as any).title_highlight ? (
                    <span dangerouslySetInnerHTML={{ __html: (blog as any).title_highlight }} />
                  ) : (
                    blog.title
                  )}
                </h2>
                <div className="blog-card-meta">
                  <span className="blog-card-author">
                    <i className="ri-user-3-line" />
                    {blog.username || '匿名'}
                  </span>
                  {blog.tagName && (
                    <span className="blog-card-tag">{blog.tagName}</span>
                  )}
                  <span className="blog-card-time">
                    {blog.createTime?.substring(0, 10)}
                  </span>
                </div>
                <div className="blog-card-stats">
                  <span><i className="ri-eye-line" /> {blog.viewCount || 0}</span>
                  <span><i className="ri-thumb-up-line" /> {blog.likeCount || 0}</span>
                  <span><i className="ri-bookmark-line" /> {blog.collectCount || 0}</span>
                </div>
              </div>
            </article>
          ))}
        </div>
      )}
    </div>
  )
}

export default Home

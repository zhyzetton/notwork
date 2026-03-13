import request from './request'

// Auth
export const login = (username: string, password: string) =>
  request.post('/users/login', { username, password })

export const register = (username: string, password: string) =>
  request.post('/users/register', { username, password })

// Blogs
export const getBlogs = (params: {
  pageNum?: number
  pageSize?: number
  tagCode?: string
  userId?: number
}) => request.get('/blogs', { params })

export const getBlogById = (id: number) => request.get(`/blogs/${id}`)

export const createBlog = (data: {
  title: string
  contentMarkdown: string
  contentHtml?: string
  coverUrl?: string
  status?: number
  tagId: number
}) => request.post('/blogs', data)

export const updateBlog = (id: number, data: {
  title: string
  contentMarkdown: string
  contentHtml?: string
  coverUrl?: string
  status?: number
  tagId: number
}) => request.put(`/blogs/${id}`, data)

export const esSearchBlogs = (keyword: string, page = 1, pageSize = 10) =>
  request.get('/blogs/es', { params: { keyword, page, pageSize } })

// Tags
export const getBlogTags = () => request.get('/blogTag')

// Like
export const toggleLike = (blogId: number) =>
  request.post(`/blogs/${blogId}/likes`)

export const getLikeStatus = (blogId: number) =>
  request.get(`/blogs/${blogId}/likes/status`)

// Collect
export const toggleCollect = (blogId: number) =>
  request.post(`/blogs/${blogId}/collects`)

export const getCollectStatus = (blogId: number) =>
  request.get(`/blogs/${blogId}/collects/status`)

// Comments
export const getComments = (blogId: number, pageNum = 1, pageSize = 20) =>
  request.get(`/blogs/${blogId}/comments`, { params: { pageNum, pageSize } })

export const addComment = (
  blogId: number,
  content: string,
  parentId?: number,
) => request.post(`/blogs/${blogId}/comments`, null, { params: { content, parentId } })

export const deleteComment = (commentId: number) =>
  request.delete(`/comments/${commentId}`)

// Upload
export const uploadImage = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/upload/image', formData)
}

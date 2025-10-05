export interface Tag {
  id: string
  tagCode: string
  tagName: string
}

export interface SubmitBlogParams {
  userId: string
  title: string
  contentMarkdown: string
  coverUrl?: string
  status?: number
  tagId: number
}

export interface Page<T> {
  records: T[]
  total: number
  size: number
  currnet: number
  pages: number
}

export interface BlogSearchParams {
  pageNum: number
  pageSize: number
  title?: string
  tagId?: number
  tagCode?: string
  userId?: number
  status?: number
}

export interface BlogDetail {
  id: number
  title: string
  contentHtml: string
  contentMarkdown: string
  contentMarkdown_highlight?: string
  coverUrl: string
  status: number
  viewCount: number
  likeCount: number
  collectCount: number
  createTime: string
  updateTime: string
  userId: number
  username: string
  userAvatar: string
  tagName: string
}
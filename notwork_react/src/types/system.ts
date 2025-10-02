export interface MenuLinkItem {
  icon: string
  title: string
  link: string
  color?: string
}

export interface BaseRes<T> {
  code: number
  msg: string
  data: T
}

export interface LoginForm {
  username: string
  password: string
}

export interface UserInfo {
  id: string
  username: string
  email: string
  avatarUrl: string
  bio: string
  role: number
  status: number
  createTime: string
  updateTime: string
}
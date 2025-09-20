export interface BlogItemType {
  title: string
  imgUrl: string
  author: {
    name: string
    avatarUrl: string
  }
  tag: string
  date: string
  abstract: string
  views: number
  comments: number
  likes: number
}

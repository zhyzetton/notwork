export interface BlogItemType {
  id: string
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
  likes: number,
}

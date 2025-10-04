import type { BlogDetail } from '@/types/blog'
import { Avatar, AvatarFallback, AvatarImage } from '../ui/avatar'
interface BlogItemProps {
  item: BlogDetail,
  onClickItem: (id: number) => void
}

const BlogItem = ({item, onClickItem}: BlogItemProps) => {
  return (
    <div onClick={() =>onClickItem(item.id)} className="flex rounded-lg shadow hover:border-blue-300 hover:shadow-none hover:border hover:cursor-pointer">
      <div className="w-96 h-64 p-6">
        {item.coverUrl !== '' ? (
          <img src={item.coverUrl} alt="" />
        ) : (
          <div className="bg-gray-300 w-full h-full rounded-lg flex items-center justify-center">
            <span className="text-white text-xl">no image</span>
          </div>
        )}
      </div>

      <div className="flex flex-1 flex-col gap-2 p-4">
        <header className="flex justify-between">
          <span className="text-xl font-bold">{item.title}</span>
          <div className="flex gap-2 text-gray-500">
            <i className="ri-heart-line"></i>
            <span>{item.likeCount}</span>
          </div>
        </header>
        <p className="flex-1 text-gray-500">{item.username}</p>
        <div className="text-gray-500 flex gap-2 justify-between">
          <div className="flex gap-4">
            <div>
              <i className="ri-chat-4-line mr-1"></i>
              <span className="mr-1">{item.collectCount}</span>
              <span>收藏</span>
            </div>
            <div>
              <i className="ri-eye-line mr-1"></i>
              <span className="mr-1">{item.viewCount}</span>
              <span>阅读</span>
            </div>
          </div>
          <span>{item.updateTime}</span>
        </div>
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Avatar>
              <AvatarImage src={item.userAvatar} />
              <AvatarFallback>User</AvatarFallback>
            </Avatar>
            <span>{item.username}</span>
          </div>
          <span className="border rounded-lg px-2 py-1">{item.tagName}</span>
        </div>
      </div>
    </div>
  )
}

export default BlogItem

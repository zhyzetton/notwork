import type { BlogItemType } from '@/types/blog'
import { Avatar, AvatarFallback, AvatarImage } from '../ui/avatar'

const BlogItem = ({
  title,
  author,
  imgUrl,
  date,
  abstract,
  tag,
  likes,
  comments,
  views,
}: BlogItemType) => {
  return (
    <div className="flex rounded-lg shadow hover:border-blue-300 hover:shadow-none hover:border hover:cursor-pointer">
      <div className="w-96 h-64 p-6">
        {imgUrl !== '' ? (
          <img src={imgUrl} className="w-96 h-64 p-6" alt="" />
        ) : (
          <div className="bg-gray-300 w-full h-full rounded-lg flex items-center justify-center">
            <span className="text-white text-xl">no image</span>
          </div>
        )}
      </div>

      <div className="flex flex-col gap-2 p-4">
        <header className="flex justify-between">
          <span className="text-xl font-bold">{title}</span>
          <div className="flex gap-2 text-gray-500">
            <i className="ri-heart-line"></i>
            <span>{likes}</span>
          </div>
        </header>
        <p className="flex-1 text-gray-500">{abstract}</p>
        <div className="text-gray-500 flex gap-2 justify-between">
          <div className="flex gap-4">
            <div>
              <i className="ri-chat-4-line mr-1"></i>
              <span className="mr-1">{comments}</span>
              <span>评论</span>
            </div>
            <div>
              <i className="ri-eye-line mr-1"></i>
              <span className="mr-1">{views}</span>
              <span>阅读</span>
            </div>
          </div>
          <span>{date}</span>
        </div>
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Avatar>
              <AvatarImage src={author.avatarUrl} />
              <AvatarFallback>User</AvatarFallback>
            </Avatar>
            <span>{author.name}</span>
          </div>
          <span className="border rounded-lg px-2 py-1">{tag}</span>
        </div>
      </div>
    </div>
  )
}

export default BlogItem

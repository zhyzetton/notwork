import BlogItem from '@/components/BlogItem'
import Sider from '@/components/Layout/Sider'
import { blogList } from '@/tempData/blogs'

const BlogList = () => {
  
  return (
    <div className="flex gap-2">
      <Sider className="self-start" />
      <main className="bg-white min-h-[calc(100vh-88px)] flex-1 rounded-lg p-4">
        {blogList.map(blog => (
          <BlogItem key={blog.title} {...blog} />
        ))}
      </main>
    </div>
  )
}

export default BlogList

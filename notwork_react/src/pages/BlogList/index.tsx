import { getBlogListApi } from '@/api/blog'
import BlogItem from '@/components/BlogItem'
import Sider from '@/components/Layout/Sider'
import router from '@/router'
import type { BlogDetail } from '@/types/blog'
import { useEffect, useState } from 'react'

const BlogList = () => {
  const [blogList, setBlogList] = useState<BlogDetail[]>([])
  useEffect(() => {
    getBlogListApi({pageNum: 1, pageSize: 10}).then((res) => {
      setBlogList(res.data.records)
    })
  }, [])

  const onClick = (id: number) => {
    console.log('item id: ', id)
    router.navigate(`/blogs/detail/${id}`)
  }
  
  return (
    <div className="flex gap-2">
      <Sider className="self-start" />
      <main className="bg-white min-h-[calc(100vh-88px)] flex-1 rounded-lg p-4">
        {blogList.map(blog => (
          <BlogItem onClickItem={onClick} key={blog.title} item={blog} />
        ))}
      </main>
    </div>
  )
}

export default BlogList

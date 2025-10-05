import { getBlogListApi } from '@/api/blog'
import BlogItem from '@/components/BlogItem'
import GlobalSearch from '@/components/GlobalSearch'
import Sider from '@/components/Layout/Sider'
import router from '@/router'
import type { BlogDetail, BlogSearchParams } from '@/types/blog'
import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'

const BlogList = () => {
  const [blogList, setBlogList] = useState<BlogDetail[]>([])
  const {tag} = useParams()
  useEffect(() => {
    const param: BlogSearchParams = {
      pageNum: 1,
      pageSize: 10
    }
    if (tag !== 'all') {
      param.tagCode = tag!
    }
    getBlogListApi(param).then((res) => {
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
        <div className='mb-6'>
          <GlobalSearch />
        </div>
        <div className='flex gap-6 flex-col'>
          {blogList.map(blog => (
          <BlogItem onClickItem={onClick} key={blog.title} item={blog} />
        ))}
        </div>
      </main>
    </div>
  )
}

export default BlogList

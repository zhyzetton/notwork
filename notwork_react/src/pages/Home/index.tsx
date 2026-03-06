import { Avatar, Card, List, Menu, Skeleton } from 'antd'
import './index.css'
import { sidebarMenuData } from '@/config/appMenuConfig.tsx'
import { useEffect, useState } from 'react'
import request from '@/utils/request.ts'

const Siderbar = () => {
  return (
    <div className="sidebar">
      <Menu items={sidebarMenuData} />
    </div>
  )
}

const MainBlogContainer = () => {
  const [blogList, setBlogList] = useState<any[]>([])
  useEffect(() => {
    const url = 'http://localhost:8080/api/blog?pageNum=1&pageSize=10'
    request.get(url).then((response) => {
      setBlogList(response.data.records)
    })
  }, [])

  const renderItem = (item: any) => {
    return (
      <List.Item actions={[<span>read more</span>]}>
        <Skeleton avatar title={false} loading={false} active>
          <List.Item.Meta
            avatar={<Avatar src={item.avatarUrl} />}
            title={item.title}
            description={item.title}
          />
        </Skeleton>
      </List.Item>
    )
  }

  return (
    <div className="main-blog-container">
      <List
        itemLayout="horizontal"
        dataSource={blogList}
        renderItem={renderItem}
      />
    </div>
  )
}

const RightPanel = () => {
  return (
    <div className="right-panel">
      <Card>Right Panel Content</Card>
    </div>
  )
}

const Home = () => {
  return (
    <main className="main-container">
      <Siderbar />
      <MainBlogContainer />
      <RightPanel />
    </main>
  )
}

export default Home

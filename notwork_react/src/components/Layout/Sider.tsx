import { cn } from '@/lib/utils'
import type { MenuLinkItem } from '@/types/system'
interface SiderProps {
  className?: string
}
const Sider = ({ className }: SiderProps) => {
  const tempTag: MenuLinkItem[] = [
    {
      icon: 'ri-home-2-line',
      title: '全部',
      link: '/blogs/all',
      color: 'text-blue-300',
    },
    {
      icon: 'ri-code-line',
      title: '代码技术',
      link: '/blogs/code',
      color: 'text-purple-300',
    },
    {
      icon: 'ri-school-line',
      title: '校园生活',
      link: '/blogs/school',
      color: 'text-orange-300',
    },
    {
      icon: 'ri-handbag-line',
      title: '就业分享',
      link: '/blogs/job',
      color: 'text-green-300',
    },
    {
      icon: 'ri-chat-4-line',
      title: '闲聊',
      link: '/blogs/other',
      color: 'text-yellow-300',
    },
  ]

  return (
    <div className={cn('p-4 flex flex-col gap-2 w-64 bg-white rounded-lg', className)}>
      <div className="font-bold text-xl text-black">博客标签</div>
      <hr />
      {tempTag.map((tag) => (
        <div
          key={tag.link}
          className="flex gap-2 items-center hover:text-blue-300 h-12 transition-transform duration-200 hover:translate-x-1"
        >
          <i className={cn(tag.icon, tag.color && tag.color, 'text-xl')}></i>
          <a href={tag.link}>{tag.title}</a>
        </div>
      ))}
    </div>
  )
}

export default Sider

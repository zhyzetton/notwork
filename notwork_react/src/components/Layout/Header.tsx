import { cn } from '@/lib/utils'
import { Button } from '../ui/button'
import { Avatar, AvatarFallback, AvatarImage } from '../ui/avatar'
import type { MenuLinkItem } from '@/types/system'

const MenuLinkItem = ({ icon, title, link }: MenuLinkItem) => {
  return (
    <a href={link} className={cn('flex px-2 gap-1')}>
      <i className={icon}></i>
      <span>{title}</span>
    </a>
  )
}

const Logo = () => {
  return (
    <div>
      <i className="ri-graduation-cap-line text-3xl text-black"></i>
      <span className="text-xl text-black font-bold ml-2">Notwork</span>
    </div>
  )
}

const MenuLink = () => {
  const menuItemList: MenuLinkItem[] = [
    {
      icon: 'ri-home-2-line',
      title: '博客列表',
      link: '/blogs/all',
    },
    {
      icon: 'ri-pencil-line',
      title: '写博客',
      link: '/write',
    },
    {
      icon: 'ri-user-line',
      title: '个人中心',
      link: '/user',
    },
  ]
  return (
    <ul className="flex gap-4">
      {menuItemList.map((menuItem) => (
        <li key={menuItem.link}>
          <MenuLinkItem {...menuItem} />
        </li>
      ))}
    </ul>
  )
}

const Login = () => {
  return (
    <div className="flex gap-4">
      <Avatar>
        <AvatarImage src="https://github.com/shadcn.png" />
        <AvatarFallback>User</AvatarFallback>
      </Avatar>
      <Button>登录</Button>
    </div>
  )
}

const Header = () => {
  return (
    <div className="flex items-center justify-between w-screen bg-white h-16 px-6">
      <Logo />
      <MenuLink />
      <Login />
    </div>
  )
}

export default Header

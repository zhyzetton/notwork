import { cn } from '@/lib/utils'
import { Button } from '../ui/button'
import { Avatar, AvatarFallback, AvatarImage } from '../ui/avatar'
import type { MenuLinkItem, UserInfo } from '@/types/system'
import LoginDialog from '../LoginDialog'
import { useEffect, useState } from 'react'
import { getLocalUserInfo, setLocalUserInfo } from '@/lib/localTool'

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
  const [userInfo, setUserInfo] = useState<UserInfo|null>(null)
  const [dialogOpen, setDialogOpen] = useState<boolean>(false)

  useEffect(() => {
    const userInfo = getLocalUserInfo()
    if(userInfo) setUserInfo(userInfo)
  },[])

  const onClose = (open: boolean) => {
    setDialogOpen(open)
  }
  const loginSuccess = (userInfo: UserInfo) => {
    setUserInfo(userInfo)
    setLocalUserInfo(userInfo)
  }
  return (
    <div className="flex gap-4">
      <Avatar>
        <AvatarImage src={userInfo?.avatarUrl} />
        <AvatarFallback>User</AvatarFallback>
      </Avatar>
      {userInfo !=null && userInfo.username}
      {userInfo == null && <Button onClick={() => setDialogOpen(true)}>登录</Button>}
      <LoginDialog open={dialogOpen} onClose={onClose} onLoginSuccess={loginSuccess} />
    </div>
  )
}

const Header = () => {
  return (
    <div className="sticky z-20 flex items-center justify-between w-full bg-white h-16 px-6 top-0">
      <Logo />
      <MenuLink />
      <Login />
    </div>
  )
}

export default Header

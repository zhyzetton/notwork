import { useAuth } from '@/contexts/AuthContext'

interface SidebarProps {
  currentPath: string
  onNavigate: (path: string) => void
}

const navItems = [
  { path: '/home', label: '首页', icon: 'ri-home-4-line' },
  { path: '/chat', label: 'AI 问答', icon: 'ri-chat-1-line' },
  { path: '/my', label: '我的', icon: 'ri-user-3-line' },
]

const Sidebar = ({ currentPath, onNavigate }: SidebarProps) => {
  const { user, isLoggedIn, logout } = useAuth()

  return (
    <aside className="sidebar">
      <div className="sidebar-top">
        <div className="sidebar-logo" onClick={() => onNavigate('/home')}>
          <i className="ri-quill-pen-line sidebar-logo-icon" />
          <span>Notwork</span>
        </div>
      </div>

      <nav className="sidebar-nav">
        {navItems.map((item) => (
          <div
            key={item.path}
            className={`sidebar-item ${currentPath === item.path ? 'active' : ''}`}
            onClick={() => onNavigate(item.path)}
          >
            <i className={item.icon} />
            <span>{item.label}</span>
          </div>
        ))}
      </nav>

      <div className="sidebar-bottom">
        {isLoggedIn && user ? (
          <div className="sidebar-user">
            <div className="sidebar-user-avatar">
              {user.avatarUrl ? (
                <img src={user.avatarUrl} alt="" />
              ) : (
                <span>{user.username?.charAt(0).toUpperCase()}</span>
              )}
            </div>
            <span className="sidebar-user-name">{user.username}</span>
            <i className="ri-logout-box-r-line sidebar-logout" onClick={logout} title="退出登录" />
          </div>
        ) : null}
      </div>
    </aside>
  )
}

export default Sidebar

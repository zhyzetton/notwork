import './layout.css'
import { appMenuData } from '@/config/appMenuConfig'

const AppHeaderMenu = () => {
  return (
    <div className="header-menu">
      {appMenuData.map((menuItem) => (
        <span className="header-menu-item" key={menuItem.name}>
          {menuItem.label}
        </span>
      ))}
    </div>
  )
}

const AppHeader = () => {
  return (
    <header className="app-header">
      <div className="app-logo">logo</div>
      <AppHeaderMenu />
    </header>
  )
}

export default AppHeader

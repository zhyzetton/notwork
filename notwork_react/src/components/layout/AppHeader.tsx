import './layout.css'
import { appMenuData } from '@/config/appMenuConfig.tsx'
import { Input, Button, Modal, Form } from 'antd'
import { useState } from 'react'

type FieldType = {
  username?: string
  password?: string
}

/**
 * logo
 */
const AppLogo = () => {
  return <div className="logo">logo</div>
}

/**
 * header 菜单
 */
const AppHeaderMenu = () => {
  return (
    <div className="header-menu">
      {appMenuData.map((menuItem) => (
        <span className="header-menu-item" key={menuItem.name}>
          {menuItem.label}
        </span>
      ))}
      <Input
        className="global-search"
        placeholder="请输出关键词"
        size="large"
        suffix={<i className="ri-search-line" />}
      />
    </div>
  )
}

/**
 * 用户信息
 */
const UserInfo = () => {
  const [showLoginModal, setShowLoginModal] = useState(false)
  const onClickLoginModal = () => {
    setShowLoginModal(true)
  }
  const handleCancel = () => {
    setShowLoginModal(false)
  }
  const onFinish = (values: FieldType) => {
    console.log('Success:', values)
  }
  return (
    <div className="user-info">
      <Button onClick={onClickLoginModal} type="primary">
        登录|注册
      </Button>
      <Modal
        open={showLoginModal}
        onCancel={handleCancel}
        footer={null}
        title="用户登录"
      >
        <Form
          name="basic"
          labelCol={{ span: 6 }}
          wrapperCol={{ span: 16 }}
          style={{ maxWidth: 600 }}
          initialValues={{ remember: true }}
          onFinish={onFinish}
          autoComplete="off"
        >
          <Form.Item<FieldType>
            label="用户名"
            name="username"
            rules={[{ required: true, message: '请输入用户名！' }]}
          >
            <Input />
          </Form.Item>

          <Form.Item<FieldType>
            label="密码"
            name="password"
            rules={[{ required: true, message: '请输入密码！' }]}
          >
            <Input.Password />
          </Form.Item>

          <Form.Item label={null}>
            <div className='login-buttons'>
              <Button type="primary" htmlType="submit">
                登录
              </Button>
              <Button onClick={handleCancel}>取消</Button>
            </div>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

const AppHeader = () => {
  return (
    <header className="header">
      <AppLogo />
      <AppHeaderMenu />
      <UserInfo />
    </header>
  )
}

export default AppHeader

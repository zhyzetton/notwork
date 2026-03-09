import { Input, Modal, Form, Button, message, Tabs } from 'antd'
import { useState, useEffect, useCallback } from 'react'
import { useAuth } from '@/contexts/AuthContext'
import { useNavigate } from 'react-router-dom'

const Header = () => {
  const { isLoggedIn } = useAuth()
  const [showAuthModal, setShowAuthModal] = useState(false)
  const [keyword, setKeyword] = useState('')
  const navigate = useNavigate()

  const openAuthModal = useCallback(() => setShowAuthModal(true), [])

  useEffect(() => {
    window.addEventListener('auth:showLogin', openAuthModal)
    return () => window.removeEventListener('auth:showLogin', openAuthModal)
  }, [openAuthModal])

  const handleSearch = () => {
    if (keyword.trim()) {
      navigate(`/home?keyword=${encodeURIComponent(keyword.trim())}`)
    }
  }

  return (
    <header className="app-header">
      <div className="header-search">
        <Input
          placeholder="搜索文章..."
          prefix={<i className="ri-search-line" style={{ color: 'var(--color-text-tertiary)' }} />}
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          onPressEnter={handleSearch}
          variant="filled"
          style={{ width: 280 }}
        />
      </div>
      <div className="header-actions">
        {isLoggedIn ? (
          <Button
            type="text"
            icon={<i className="ri-add-line" />}
            onClick={() => navigate('/new')}
          >
            写文章
          </Button>
        ) : (
          <Button type="text" onClick={() => setShowAuthModal(true)}>
            登录
          </Button>
        )}
      </div>
      <AuthModal open={showAuthModal} onClose={() => setShowAuthModal(false)} />
    </header>
  )
}

const AuthModal = ({
  open,
  onClose,
}: {
  open: boolean
  onClose: () => void
}) => {
  const { login, register } = useAuth()
  const [loading, setLoading] = useState(false)

  const handleAuth = async (
    values: { username: string; password: string },
    mode: 'login' | 'register',
  ) => {
    setLoading(true)
    try {
      if (mode === 'login') {
        await login(values.username, values.password)
        message.success('登录成功')
      } else {
        await register(values.username, values.password)
        message.success('注册成功')
      }
      onClose()
    } catch (e: any) {
      message.error(e.message || '操作失败')
    } finally {
      setLoading(false)
    }
  }

  const renderForm = (mode: 'login' | 'register') => (
    <Form
      layout="vertical"
      onFinish={(v) => handleAuth(v, mode)}
      autoComplete="off"
    >
      <Form.Item
        name="username"
        label="用户名"
        rules={[{ required: true, message: '请输入用户名' }]}
      >
        <Input placeholder="输入用户名" />
      </Form.Item>
      <Form.Item
        name="password"
        label="密码"
        rules={[{ required: true, message: '请输入密码' }]}
      >
        <Input.Password placeholder="输入密码" />
      </Form.Item>
      <Form.Item>
        <Button type="primary" htmlType="submit" loading={loading} block>
          {mode === 'login' ? '登录' : '注册'}
        </Button>
      </Form.Item>
    </Form>
  )

  return (
    <Modal
      open={open}
      onCancel={onClose}
      footer={null}
      title={null}
      width={380}
      centered
    >
      <div style={{ padding: '8px 0' }}>
        <h2 style={{ textAlign: 'center', marginBottom: 24, fontWeight: 600 }}>
          欢迎来到 Notwork
        </h2>
        <Tabs
          centered
          items={[
            { key: 'login', label: '登录', children: renderForm('login') },
            { key: 'register', label: '注册', children: renderForm('register') },
          ]}
        />
      </div>
    </Modal>
  )
}

export default Header

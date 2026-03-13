import axios from 'axios'
import { message } from 'antd'

const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
})

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code !== 200) {
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
    return res
  },
  (error) => {
    if (error.response?.status === 401) {
      window.dispatchEvent(new Event('auth:logout'))
      window.dispatchEvent(new Event('auth:showLogin'))
      message.warning('登录已过期，请重新登录')
    }
    return Promise.reject(error)
  },
)

export default request

import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"
import axios from 'axios'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export const request = axios.create({
  baseURL: `${import.meta.env.VITE_API_URL}`,
  timeout: 60000,
})
// 请求拦截器
request.interceptors.request.use((config) => {
  // 注入token数据
  // const token = getToken()
  // if (token) {
  //   config.headers.Authorization = `Bearer ${token}`
  // }
  return config
}, (error) => {
  return Promise.reject(error)
})

// 响应拦截器
request.interceptors.response.use((response) => {
  // 业务出错
  if (response.data.code != 200) return Promise.reject(response.data.msg)
  // 业务正常，剥掉一层data
  return response.data
}, async (error) => {
  return Promise.reject(error.message)
})

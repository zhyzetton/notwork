import { useState, useRef, useEffect } from 'react'
import { Input, Button, message, Radio } from 'antd'
import './index.css'

interface ChatMessage {
  role: 'user' | 'assistant'
  content: string
}

const Chat = () => {
  const [messages, setMessages] = useState<ChatMessage[]>([])
  const [input, setInput] = useState('')
  const [loading, setLoading] = useState(false)
  const [aiType, setAiType] = useState<string>('chat')
  const bottomRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  const handleSend = async () => {
    if (!input.trim() || loading) return

    const query = input.trim()
    setInput('')
    setMessages((prev) => [...prev, { role: 'user', content: query }])
    setLoading(true)

    try {
      const token = localStorage.getItem('token')
      const res = await fetch(
        `/api/chat?query=${encodeURIComponent(query)}&aiType=${aiType}`,
        { headers: token ? { Authorization: `Bearer ${token}` } : {} },
      )

      if (res.status === 401) {
        window.dispatchEvent(new Event('auth:logout'))
        window.dispatchEvent(new Event('auth:showLogin'))
        throw new Error('登录已过期，请重新登录')
      }
      if (!res.ok) throw new Error('请求失败')

      const reader = res.body?.getReader()
      const decoder = new TextDecoder()

      setMessages((prev) => [...prev, { role: 'assistant', content: '' }])

      if (reader) {
        while (true) {
          const { done, value } = await reader.read()
          if (done) break
          const text = decoder.decode(value, { stream: true })
          // Parse SSE format: extract content from "data:" lines
          const lines = text.split('\n')
          let chunk = ''
          for (const line of lines) {
            if (line.startsWith('data:')) {
              chunk += line.substring(5)
            }
          }
          if (chunk) {
            setMessages((prev) => {
              const updated = [...prev]
              updated[updated.length - 1] = {
                ...updated[updated.length - 1],
                content: updated[updated.length - 1].content + chunk,
              }
              return updated
            })
          }
        }
      }
    } catch {
      message.error('对话失败，请重试')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="chat-page">
      <div className="chat-heading">
        <h1>AI 问答</h1>
        <p className="chat-subtitle">与 AI 助手对话，探索你的知识库</p>
      </div>

      <div className="chat-window">
        <div className="chat-messages">
          {messages.length === 0 && (
            <div className="chat-empty">
              <i className="ri-chat-1-line" />
              <p>开始一段对话吧</p>
            </div>
          )}
          {messages.map((msg, i) => (
            <div key={i} className={`chat-bubble ${msg.role}`}>
              <div className="bubble-avatar">
                {msg.role === 'user' ? (
                  <i className="ri-user-3-line" />
                ) : (
                  <i className="ri-robot-2-line" />
                )}
              </div>
              <div className="bubble-content">{msg.content}</div>
            </div>
          ))}
          {loading && (
            <div className="chat-typing">
              <span /><span /><span />
            </div>
          )}
          <div ref={bottomRef} />
        </div>

        <div className="chat-input-area">
          <div className="chat-mode">
            <Radio.Group value={aiType} onChange={(e) => setAiType(e.target.value)} size="small">
              <Radio.Button value="chat">普通对话</Radio.Button>
              <Radio.Button value="rag">知识库问答</Radio.Button>
            </Radio.Group>
          </div>
          <div className="chat-input-row">
            <Input.TextArea
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onPressEnter={(e) => {
                if (!e.shiftKey) {
                  e.preventDefault()
                  handleSend()
                }
              }}
              placeholder="输入消息，Enter 发送..."
              autoSize={{ minRows: 1, maxRows: 4 }}
              disabled={loading}
            />
            <Button
              type="primary"
              icon={<i className="ri-send-plane-2-line" />}
              onClick={handleSend}
              loading={loading}
            />
          </div>
        </div>
      </div>
    </div>
  )
}

export default Chat

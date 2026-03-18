import { useState, useRef, useEffect } from 'react'
import { Input, Button, message, Radio } from 'antd'
import { Link } from 'react-router-dom'
import ReactMarkdown from 'react-markdown'
import './index.css'

interface BlogReference {
  blogId: number
  title: string
  tagName: string
}

interface ChatMessage {
  role: 'user' | 'assistant'
  content: string
  references?: BlogReference[]
}

const Chat = () => {
  const [messages, setMessages] = useState<ChatMessage[]>([])
  const [input, setInput] = useState('')
  const [loading, setLoading] = useState(false)
  const [aiType, setAiType] = useState<string>('chat')
  const [initialLoaded, setInitialLoaded] = useState(false)
  const bottomRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    const loadHistory = async () => {
      if (initialLoaded) return
      setInitialLoaded(true)
      
      try {
        const token = localStorage.getItem('token')
        const res = await fetch('http://localhost:8080/api/chat/history', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        if (res.ok) {
          const data = await res.json()
          if (data.code === 200 && data.data) {
            const history: ChatMessage[] = data.data.map((msg: { role: string; content: string }) => ({
              role: msg.role as 'user' | 'assistant',
              content: msg.content,
            }))
            setMessages(history)
          }
        }
      } catch {
        // ignore
      }
    }
    loadHistory()
  }, [initialLoaded])

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
        `http://localhost:8080/api/chat?query=${encodeURIComponent(query)}&aiType=${aiType}`,
        {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${token}`,
            Accept: 'text/event-stream',
          },
        },
      )

      if (res.status === 401) {
        message.error('登录已过期，请重新登录')
        setLoading(false)
        return
      }
      if (res.status === 403) {
        message.error('没有权限')
        setLoading(false)
        return
      }
      if (!res.ok) {
        const errorData = await res.json().catch(() => ({}))
        message.error(errorData.message || '请求失败')
        return
      }

      const reader = res.body?.getReader()
      const decoder = new TextDecoder()
      let buffer = ''
      let currentEvent: string | null = null

      setMessages((prev) => [...prev, { role: 'assistant', content: '' }])

      if (reader) {
        while (true) {
          const { done, value } = await reader.read()
          if (done) break

          buffer += decoder.decode(value, { stream: true })

          const lines = buffer.split('\n')
          buffer = lines.pop() || ''

          for (const line of lines) {
            if (line.startsWith('event:')) {
              currentEvent = line.slice(6).trim()
            } else if (line.startsWith('data:')) {
              const data = line.slice(5).trim()

              if (currentEvent === 'references') {
                try {
                  const refs = JSON.parse(data) as BlogReference[]
                  setMessages((prev) => {
                    const updated = [...prev]
                    updated[updated.length - 1] = {
                      ...updated[updated.length - 1],
                      references: refs,
                    }
                    return updated
                  })
                } catch {
                  // ignore parse error
                }
                currentEvent = null
              } else if (data === '[DONE]') {
                return
              } else if (data) {
                setMessages((prev) => {
                  const updated = [...prev]
                  updated[updated.length - 1] = {
                    ...updated[updated.length - 1],
                    content: updated[updated.length - 1].content + data,
                  }
                  return updated
                })
              }
            }
          }
        }
      }
    } catch (err) {
      message.error('网络错误，请检查连接')
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
              <div className="bubble-body">
                <div className="bubble-content">
                  {msg.role === 'assistant' ? (
                    <ReactMarkdown>{msg.content}</ReactMarkdown>
                  ) : (
                    msg.content
                  )}
                </div>
                {msg.references && msg.references.length > 0 && (
                  <div className="chat-references">
                    <div className="references-title">
                      <i className="ri-book-open-line" />
                      参考来源
                    </div>
                    <div className="references-list">
                      {msg.references.map((ref) => (
                        <Link
                          key={ref.blogId}
                          to={`/blog/${ref.blogId}`}
                          className="reference-item"
                        >
                          <span className="reference-blog-title">{ref.title}</span>
                          {ref.tagName && (
                            <span className="reference-tag">{ref.tagName}</span>
                          )}
                        </Link>
                      ))}
                    </div>
                  </div>
                )}
              </div>
            </div>
          ))}
          {loading && (
            <div className="chat-typing">
              <span />
              <span />
              <span />
            </div>
          )}
          <div ref={bottomRef} />
        </div>

        <div className="chat-input-area">
          <div className="chat-mode">
            <Radio.Group
              value={aiType}
              onChange={(e) => setAiType(e.target.value)}
              size="small"
            >
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

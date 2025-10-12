import { useState } from 'react'
import { chatWithRagApi } from '@/api/blog'
import { getLocalUserInfo } from '@/lib/localTool'
import ReactMarkdown from 'react-markdown'
import remarkGfm from 'remark-gfm'

function ChatPage() {
  const [userQuery, setUserQuery] = useState('') // 用户输入的问题
  const [aiResponse, setAiResponse] = useState('') // AI 的回复
  const [isLoading, setIsLoading] = useState(false) // 加载状态
  const [error, setError] = useState('') // 错误信息

  const handleSendQuery = async () => {
    if (userQuery.trim() === '') {
      setError('请输入您的问题。')
      return
    }

    setIsLoading(true)
    setError('') // 清除之前的错误和回复
    setAiResponse('')

    try {
      const userId = getLocalUserInfo()!.id
      // const response = await chatWithRagApi(Number.parseInt(userId), userQuery)
      // setAiResponse(response.data)
      const eventSource = new EventSource(`http://localhost:8080/api/chat?query=${userQuery}&userId=${userId}`)
      eventSource.onmessage = (event) => {
        const data = event.data
        
        setAiResponse(prev => prev + data)
      }
      eventSource.onerror = (error) => {
        console.log(error)
        eventSource.close()
      }
    } catch (err) {
      console.error('Error fetching AI response:', err)
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="flex flex-col items-center justify-center min-h-[calc(100vh-150px)] bg-gray-100 p-4">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-2xl p-6 flex flex-col space-y-4">
        <h1 className="text-2xl font-bold text-gray-800 mb-4 text-center">个人知识库智能问答</h1>

        {/* AI 回复区域 */}
        <div className="bg-gray-50 p-4 rounded-lg border border-gray-200 min-h-[150px] overflow-y-auto">
          {isLoading ? (
            <div className="flex items-center justify-center text-gray-500">
              <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-gray-900 mr-2"></div>
              <span>AI 正在思考...</span>
            </div>
          ) : error ? (
            <p className="text-red-500 text-center">{error}</p>
          ) : aiResponse ? (
            <div className="prose prose-sm max-w-none">
              <ReactMarkdown remarkPlugins={[remarkGfm]}>
                {aiResponse}
              </ReactMarkdown>
            </div>
          ) : (
            <p className="text-gray-400 text-center">在这里输入你的问题，AI 将为你解答。</p>
          )}
        </div>

        {/* 用户输入区 */}
        <div className="flex space-x-2">
          <input
            type="text"
            value={userQuery}
            onChange={(e) => setUserQuery(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && !isLoading && handleSendQuery()}
            placeholder="输入你的问题..."
            className="flex-1 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-800"
            disabled={isLoading}
          />
          <button
            onClick={handleSendQuery}
            className="px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
            disabled={isLoading || userQuery.trim() === ''}
          >
            提问
          </button>
        </div>
      </div>
    </div>
  )
}

export default ChatPage

import { useState, useRef, useEffect } from 'react'
import { Button } from '../ui/button'
import { esSearchBlogApi } from '@/api/blog'
import { Input } from '../ui/input'

interface SearchResult {
  id: number
  title: string
  contentMarkdown_highlight?: string // 高亮片段
  title_highlight?: string
}

const GlobalSearch = () => {
  const [keyword, setKeyword] = useState('')
  const [results, setResults] = useState<SearchResult[]>([])
  const [showDropdown, setShowDropdown] = useState(false)
  const wrapperRef = useRef<HTMLDivElement>(null)

  // 点击外部关闭下拉
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (wrapperRef.current && !wrapperRef.current.contains(event.target as Node)) {
        setShowDropdown(false)
      }
    }
    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])

  const handleSearch = async () => {
    if (!keyword) return
    try {
      const res = await esSearchBlogApi(keyword, 1, 10)
      setResults(res.data)
      setShowDropdown(true)
    } catch (error) {
      console.error('搜索失败:', error)
    }
  }

  return (
    <div ref={wrapperRef} className="relative w-full">
      <div className="flex">
        <Input
          type="text"
          placeholder="请输入关键词全局搜索"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          onFocus={() => keyword && setShowDropdown(true)}
        />
        <Button onClick={handleSearch} className="ml-2">
          搜索
        </Button>
      </div>

      {showDropdown && results.length > 0 && (
        <ul className="absolute z-10 w-full mt-1 bg-white border border-gray-200 rounded shadow max-h-60 overflow-y-auto">
          {results.map((item) => (
            <li
              key={item.id}
              className="px-3 py-2 hover:bg-gray-100 cursor-pointer"
              onClick={() => {
                window.location.href = `/blogs/detail/${item.id}` // 点击跳转到文章详情
              }}
            >
              {item.title_highlight ? (
                <p className="font-semibold" dangerouslySetInnerHTML={{__html: item.title_highlight}} />
              ) : (
                <p className="font-semibold">{item.title}</p>
              )}
              {item.contentMarkdown_highlight && (
                <p
                  className="text-gray-600 text-sm mt-1"
                  dangerouslySetInnerHTML={{ __html: item.contentMarkdown_highlight }}
                />
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}

export default GlobalSearch

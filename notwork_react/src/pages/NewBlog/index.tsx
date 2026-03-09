import { useState, useEffect, useRef } from 'react'
import { Input, Button, Select, message } from 'antd'
import { useNavigate } from 'react-router-dom'
import Vditor from 'vditor'
import 'vditor/dist/index.css'
import { createBlog, getBlogTags } from '@/api'
import request from '@/api/request'
import './index.css'

interface TagItem {
  id: number
  tagCode: string
  tagName: string
}

const NewBlog = () => {
  const navigate = useNavigate()
  const [title, setTitle] = useState('')
  const [tagId, setTagId] = useState<number | undefined>()
  const [tags, setTags] = useState<TagItem[]>([])
  const [submitting, setSubmitting] = useState(false)

  const editorRef = useRef<HTMLDivElement>(null)
  const vditorRef = useRef<Vditor | null>(null)

  useEffect(() => {
    getBlogTags().then((res: any) => setTags(res.data || []))
  }, [])

  useEffect(() => {
    if (!editorRef.current) return

    const vditor = new Vditor(editorRef.current, {
      height: 'auto',
      minHeight: 500,
      mode: 'ir',
      placeholder: '开始写作...',
      toolbar: [
        'headings', 'bold', 'italic', 'strike', '|',
        'list', 'ordered-list', 'check', 'outdent', 'indent', '|',
        'quote', 'code', 'inline-code', 'link', 'upload', 'table', '|',
        'undo', 'redo', '|',
        'fullscreen', 'preview',
      ],
      cache: { enable: false },
      upload: {
        accept: 'image/*',
        multiple: true,
        handler: (files: File[]) => {
          for (const file of files) {
            const formData = new FormData()
            formData.append('file', file)
            request.post('/upload/image', formData).then((res: any) => {
              const url = res.data
              vditorRef.current?.insertValue(`![${file.name}](${url})\n`)
            }).catch(() => {
              message.error(`图片 ${file.name} 上传失败`)
            })
          }
          return null
        },
      },
      after: () => {
        vditorRef.current = vditor
      },
    })

    return () => {
      vditor.destroy()
      vditorRef.current = null
    }
  }, [])

  const handleSubmit = async () => {
    const contentMarkdown = vditorRef.current?.getValue()?.trim() || ''
    const contentHtml = vditorRef.current?.getHTML() || ''

    if (!title.trim()) return message.warning('请输入标题')
    if (!contentMarkdown) return message.warning('请输入内容')
    if (!tagId) return message.warning('请选择标签')

    setSubmitting(true)
    try {
      await createBlog({
        title: title.trim(),
        contentMarkdown,
        contentHtml,
        tagId,
        status: 1,
      })
      message.success('发布成功')
      navigate('/home')
    } catch {
      message.error('发布失败')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="new-blog">
      <div className="new-blog-header">
        <Button type="text" onClick={() => navigate(-1)}>
          <i className="ri-arrow-left-line" /> 返回
        </Button>
        <div className="new-blog-actions">
          <Select
            placeholder="选择标签"
            value={tagId}
            onChange={setTagId}
            style={{ width: 140 }}
            options={tags.map((t) => ({ label: t.tagName, value: t.id }))}
          />
          <Button type="primary" onClick={handleSubmit} loading={submitting}>
            发布
          </Button>
        </div>
      </div>

      <Input
        className="new-blog-title"
        placeholder="无标题"
        variant="borderless"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />

      <div ref={editorRef} className="new-blog-editor" />
    </div>
  )
}

export default NewBlog

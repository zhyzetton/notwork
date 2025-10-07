import { useEffect, useState } from 'react'
import Vditor from 'vditor'
import 'vditor/dist/index.css'
import { Button } from '../ui/button'
import { Input } from '@/components/ui/input'
import { getLocalUserInfo } from '@/lib/localTool'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../ui/select'
import type { Tag } from '@/types/blog'
import { getTagListApi, submitBlogApi } from '@/api/blog'

const Editor = () => {
  const [vd, setVd] = useState<Vditor>()
  const [title, setTitle] = useState<string>()
  const [tagId, setTagId] = useState<number>()
  const [tagList, setTagList] = useState<Tag[]>([])
  const onClickSubmit = () => {
    const userInfo = getLocalUserInfo()
    if (!userInfo || !userInfo.id) {
      alert('您未登录!')
      return
    }
    if (!title) {
      alert("请输入标题！")
      return
    }
    if (!vd?.getValue()) {
      alert("请输入内容")
      return
    }
    if (!tagId) {
      alert("请选择标签！")
      return
    }
    const submitParams = {
      userId: userInfo.id,
      title,
      contentMarkdown: vd?.getValue(),
      contentHtml: vd?.getHTML(),
      tagId,
    }
    submitBlogApi(submitParams).then(() => {
      alert("提交成功！")
    })
  }
  useEffect(() => {
    const vditor = new Vditor('vditor', {
      mode: 'ir',
      cache: {
        enable: true,
      },
      upload: {
        url: 'http://127.0.0.1:8080/api/upload/image',
        fieldName: 'file',
        multiple: true,
        max: 10 * 1024 * 1024,
        // headers: {Authorization: ...}
      },
      after: () => {
        vditor.setValue('')
        setVd(vditor)
      },
    })

    // 获取blog tag
    getTagListApi().then(res => {
      setTagList(res.data)
    })

    // Clear the effect
    return () => {
      vd?.destroy()
      setVd(undefined)
    }
  }, [])
  return (
    <div>
      <div className="flex gap-2">
        <Input placeholder="请输入标题" onChange={(e) => setTitle(e.target.value)} />
        <Select onValueChange={(value) => setTagId(Number.parseInt(value))}>
          <SelectTrigger className="w-[180px]">
            <SelectValue placeholder="选择标签" />
          </SelectTrigger>
          <SelectContent>
            <SelectGroup>
              {tagList.map(tag => (<SelectItem key={tag.id} value={tag.id}>{tag.tagName}</SelectItem>))}
            </SelectGroup>
          </SelectContent>
        </Select>
        <Button variant={'outline'}>草稿箱</Button>
        <Button onClick={onClickSubmit}>发布</Button>
      </div>
      <div id="vditor" className="vditor mt-4 min-h-[calc(100vh-150px)]" />
    </div>
  )
}

export default Editor

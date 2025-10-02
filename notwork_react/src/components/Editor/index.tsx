import { useEffect, useState } from 'react'
import Vditor from 'vditor'
import 'vditor/dist/index.css'
import { Button } from '../ui/button'
import { Input } from "@/components/ui/input"


const Editor = () => {
  const [vd, setVd] = useState<Vditor>()
  const [title, setTitle] = useState<string>('')
  useEffect(() => {
    const vditor = new Vditor('vditor', {
      mode: 'ir',
      cache: {
        enable: true
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
    // Clear the effect
    return () => {
      vd?.destroy()
      setVd(undefined)
    }
  }, [])
  return (<div>
    <div className='flex gap-2'>
        <Input placeholder='请输入标题' onChange={(e) => setTitle(e.target.value)} />
        <Button variant={'outline'}>草稿箱</Button>
        <Button onClick={() => console.log(title)}>发布</Button>
    </div>
    <div id="vditor" className="vditor mt-4 min-h-[calc(100vh-150px)] overflow-auto" />
  </div>)
}

export default Editor

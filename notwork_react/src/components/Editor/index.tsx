import { useEffect, useState } from 'react'
import Vditor from 'vditor'
import { Button } from '../ui/button'

const Editor = () => {
  const [vd, setVd] = useState<Vditor>()
  useEffect(() => {
    const vditor = new Vditor('vditor', {
      after: () => {
        vditor.setValue('`Vditor` 最小代码示例')
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
        <Button>草稿箱</Button>
        <Button>发布</Button>
    </div>
    <div id="vditor" className="vditor mt-4" />
  </div>)
}

export default Editor

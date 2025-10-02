import { useState } from 'react'
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from '../ui/dialog'
import { Input } from '../ui/input'
import { Button } from '../ui/button'
import { loginApi } from '@/api/user'
import type { UserInfo } from '@/types/system'
interface LoginDialogProps {
  open: boolean
  onClose: (open: boolean) => void
  onLoginSuccess: (userinfo: UserInfo) => void
}
const LoginDialog = ({ open, onClose, onLoginSuccess }: LoginDialogProps) => {
  const [username, setUserName] = useState('')
  const [password, setPassword] = useState('')

  const onClickLogin = async () => {
    try {
        const res = await loginApi({username, password})
        onLoginSuccess(res.data)
        onClose(false)
    }catch(error) {
        console.log(error)
    }
    
  }

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Login Notwork</DialogTitle>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <div className="grid grid-cols-4 items-center gap-4">
            <label htmlFor="username" className="text-right">
              Username
            </label>
            <Input
              id="username"
              onChange={(e) => setUserName(e.target.value)}
              className="col-span-3"
            />
          </div>
          <div className="grid grid-cols-4 items-center gap-4">
            <label htmlFor="password" className="text-right">
              Password
            </label>
            <Input
              id="password"
              type="password"
              onChange={(e) => setPassword(e.target.value)}
              className="col-span-3"
            />
          </div>
        </div>
        <DialogFooter>
          <Button onClick={onClickLogin}>Login</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}

export default LoginDialog

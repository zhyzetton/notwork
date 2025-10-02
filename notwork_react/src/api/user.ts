import { request } from "@/lib/utils"
import type { BaseRes, LoginForm, UserInfo } from "@/types/system"

export const loginApi = (loginFrom: LoginForm): Promise<BaseRes<UserInfo>> => {
    return request({
        url: '/user/login',
        method: 'POST',
        data: loginFrom
    })
}
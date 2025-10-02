import type { UserInfo } from "@/types/system"

const LOCAL_USER_INFO_KEY = 'LOCAL_USER'

export const getLocalUserInfo = (): UserInfo | null => {
    const userInfo = localStorage.getItem(LOCAL_USER_INFO_KEY)
    if(userInfo) {
        return JSON.parse(userInfo)
    }
    return null
}

export const setLocalUserInfo = (userInfo: UserInfo) => {
    localStorage.setItem(LOCAL_USER_INFO_KEY, JSON.stringify(userInfo))
}
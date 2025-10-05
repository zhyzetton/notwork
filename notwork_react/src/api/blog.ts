import { request } from "@/lib/utils"
import type { BlogDetail, BlogSearchParams, Page, SubmitBlogParams, Tag } from "@/types/blog"
import type { BaseRes } from "@/types/system"

export const getTagListApi = (): Promise<BaseRes<Tag[]>> => {
    return request({
        url: '/blogTag',
        method: 'GET',
    })
}

export const submitBlogApi = (submitBlog: SubmitBlogParams): Promise<BaseRes<null>> => {
    return request({
        url: '/blog',
        method: 'POST',
        data: submitBlog
    })
}

export const getBlogListApi = (searchParams: BlogSearchParams): Promise<BaseRes<Page<BlogDetail>>> => {
    return request({
        url: '/blog',
        method: 'GET',
        params: searchParams
    })
}

export const getBlogByIdApi = (id: number): Promise<BaseRes<BlogDetail>> => {
    return request({
        url: `/blog/${id}`,
        method: "GET"
    })
}

export const esSearchBlogApi = (keyword: string, page: number, pageSize: number): Promise<BaseRes<BlogDetail[]>> => {
    return request({
        url: '/blog/es',
        method: "GET",
        params: {
            keyword,
            page,
            pageSize
        }
    })
}
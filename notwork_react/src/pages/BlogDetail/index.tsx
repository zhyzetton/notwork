import { getBlogByIdApi } from '@/api/blog';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import BlogPreview from './BlogPreview';

const BlogDetail = () => {
    const {id} = useParams()
    const [content, setContent] = useState<string>()

    useEffect(() => {
        getBlogByIdApi(Number.parseInt(id!)).then((res) => {
            setContent(res.data.contentMarkdown)
        })
    }, [id])

    return (
        <div>
            <BlogPreview content={content!} />
        </div>
    )
}

export default BlogDetail
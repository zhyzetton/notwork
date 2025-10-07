interface BlogDetailProps {
  content: string
}
import ReactMarkdown from 'react-markdown'
import remarkGfm from 'remark-gfm'

const BlogPreview = ({ content }: BlogDetailProps) => {
  return (
    <div className="prose max-w-none px-16 bg-white mx-auto">
      <ReactMarkdown remarkPlugins={[remarkGfm]}>{content}</ReactMarkdown>
    </div>
  )
}

export default BlogPreview

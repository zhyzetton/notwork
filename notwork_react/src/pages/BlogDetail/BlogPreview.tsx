interface BlogDetailProps {
  content: string
}

const BlogPreview = ({ content }: BlogDetailProps) => {
  return <div dangerouslySetInnerHTML={{ __html: content }} className="prose mx-auto bg-white px-20 max-w-none" />
}

export default BlogPreview

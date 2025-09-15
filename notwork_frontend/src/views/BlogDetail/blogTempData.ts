interface BlogDetail {
  id: string;
  title: string;
  author: {
    name: string;
    avatar: string;
  };
  content: string;
}

export const blogListDetail: BlogDetail[] = [
  {
    id: "1",
    title: "如何高效管理个人时间",
    author: {
      name: "张明",
      avatar: "https://picsum.photos/id/1012/200/200?alt=张明的头像",
    },
    content: `# 前端性能优化实践指南

性能优化是前端开发中不可或缺的环节，直接影响用户体验和留存率。
## 1. 资源加载优化
合理优化资源加载是提升页面加载速度的基础。
### 1.1 图片优化策略
- 使用现代图片格式（WebP/AVIF）
- 实现响应式图片
- 图片懒加载实现
#### 1.1.1 WebP格式的优势
WebP格式相比JPEG通常能节省30%以上的带宽。
### 1.2 JavaScript优化
- 代码分割与懒加载
- 减少不必要的第三方库
- 使用tree-shaking减小体积
## 2. 运行时性能优化
优化页面运行时性能，提升交互流畅度。
### 2.1 减少重排重绘
- 使用CSS containment
- 批量DOM操作
- 使用will-change提示浏览器
### 2.2 优化事件处理
- 事件委托减少事件监听器
- 防抖节流处理高频事件
`,
  },
  {
    id: "2",
    title: "新手入门烘焙的5个注意事项",
    author: {
      name: "李华",
      avatar: "https://picsum.photos/id/1027/200/200?alt=李华的头像",
    },
    content:
      "# 新手入门烘焙的5个注意事项\n\n烘焙看似简单，实则需要注意细节：\n\n1. 严格按照配方称量材料，尤其是面粉和液体\n2. 烤箱提前预热，避免温度不稳定影响成品\n3. 区分常温、冷藏食材的使用时机\n4. 搅拌手法要正确，避免过度搅拌起筋\n5. 耐心等待烘烤时间，勿频繁开箱查看\n\n掌握这些，能减少烘焙失败率。",
  },
  {
    id: "3",
    title: "数字极简主义：减少信息过载",
    author: {
      name: "王芳",
      avatar: "https://picsum.photos/id/1025/200/200?alt=王芳的头像",
    },
    content:
      "# 数字极简主义：减少信息过载\n\n在信息爆炸的时代，数字极简很有必要：\n\n- 定期清理手机APP，只保留必要工具\n- 关闭非必要的推送通知，减少干扰\n- 设定每日屏幕使用时间，避免沉迷\n- 尝试「数字断舍离」，每周留一天远离电子设备\n\n数字极简能帮助我们回归专注，提升生活质量。",
  },
];

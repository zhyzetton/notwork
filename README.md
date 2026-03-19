# NotWork

基于 **Spring AI** 的个人知识库问答系统，支持 **RAG**（检索增强生成）模式，让 AI 能够基于你的博客内容进行智能问答。

## 核心特性

- **Spring AI + RAG**：集成 Spring AI 1.1.2，支持基于向量检索的智能问答
- **个人知识库**：将博客内容向量化存储到 Milvus，支持语义搜索
- **多源检索**：结合 Milvus 向量检索 + Elasticsearch 全文检索
- **流式响应**：支持 SSE 流式输出，实时展示 AI 生成内容
- **对话记忆**：基于 Spring AI ChatMemory 实现多轮对话上下文
- **博客管理**：完整的博客发布、标签、评论、点赞、收藏功能
- **MinIO 对象存储**：支持文件上传和图片存储

## 技术栈

### 后端
| 技术 | 说明 |
|------|------|
| Spring Boot 3.5.6 | 核心框架 |
| Spring AI 1.1.2 | AI 能力集成 |
| MySQL | 关系型数据库 |
| Redis | 缓存 & 会话存储 |
| Milvus | 向量数据库 (RAG 核心) |
| Elasticsearch | 全文搜索引擎 |
| RabbitMQ | 异步消息队列 |
| MinIO | 对象存储 |
| MyBatis-Plus | ORM 框架 |

### 前端
| 技术 | 说明 |
|------|------|
| React 18 | UI 框架 |
| TypeScript | 类型安全 |
| Vite | 构建工具 |

## 项目结构

```
notwork/
├── notwork_backend/          # Spring Boot 后端
│   ├── Dockerfile
│   ├── src/main/java/
│   │   └── com/notwork/
│   │       ├── controller/  # REST API 控制器
│   │       ├── service/     # 业务逻辑层
│   │       ├── entity/      # 数据实体
│   │       ├── config/      # 配置类
│   │       └── common/      # 通用工具
│   ├── src/main/resources/  # 配置文件
│   └── sql/                 # 数据库脚本
├── notwork_react/           # React 前端
│   ├── Dockerfile
│   ├── nginx.conf
│   └── src/
├── docker-compose.yml       # Docker Compose 配置
└── README.md
```

## 环境要求

- JDK 17+
- Node.js 18+
- Docker & Docker Compose
- Maven 3.8+

## Docker 部署

### 一键部署（推荐）

```bash
# 设置 OpenAI API Key
export SPRING_AI_OPENAI_API_KEY=your-api-key

# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看后端日志
docker-compose logs -f notwork-backend

# 查看前端日志
docker-compose logs -f notwork-frontend
```

启动后访问：
- 前端：http://localhost:3000
- Swagger API 文档：http://localhost:8080/swagger-ui.html
- RabbitMQ 管理界面：http://localhost:15672 (guest/guest)
- MinIO 控制台：http://localhost:9001 (minioadmin/minioadmin)
- Kibana：http://localhost:5601

### 默认账号

- 用户名：`admin`
- 密码：`admin123`

### 分步部署

#### 1. 仅启动基础设施服务

```bash
docker-compose up -d mysql redis rabbitmq minio etcd minio-storage milvus elasticsearch kibana
```

#### 2. 等待服务就绪

```bash
# 等待 MySQL
until docker exec notwork-mysql mysqladmin ping -h localhost -uroot -pnotwork2024 --silent; do
  echo "Waiting for MySQL..."
  sleep 3
done

# 等待 Milvus
until docker exec notwork-milvus curl -f http://localhost:9091/healthz 2>/dev/null; do
  echo "Waiting for Milvus..."
  sleep 10
done

# 等待 Elasticsearch
until curl -s http://localhost:9200 | grep -q "cluster_name"; do
  echo "Waiting for Elasticsearch..."
  sleep 5
done
```

#### 3. 构建并运行后端

```bash
cd notwork_backend

# 打包
./mvnw clean package -DskipTests

# 构建镜像
docker build -t notwork-backend:latest .

# 运行（设置你的 API Key）
SPRING_AI_OPENAI_API_KEY=your-api-key docker-compose up -d notwork-backend
```

#### 4. 构建并运行前端

```bash
cd notwork_react

# 构建
pnpm build

# 构建镜像
docker build -t notwork-frontend:latest .

# 运行
docker-compose up -d notwork-frontend
```

### 环境变量

| 变量 | 说明 | 默认值 |
|------|------|--------|
| `SPRING_AI_OPENAI_API_KEY` | OpenAI API Key | **必填** |
| `SPRING_AI_OPENAI_BASE_URL` | API 地址 | `https://api.openai.com` |

## 本地开发

### 1. 启动依赖服务

```bash
docker-compose up -d mysql redis rabbitmq minio etcd minio-storage milvus elasticsearch
```

### 2. 配置 API Key

创建 `notwork_backend/src/main/resources/application-dev.yml`：

```yaml
spring:
  ai:
    openai:
      api-key: your-api-key
      base-url: https://api.openai.com
  datasource:
    url: jdbc:mysql://localhost:3306/notwork
    username: root
    password: notwork2024
```

### 3. 启动后端

```bash
cd notwork_backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 4. 启动前端

```bash
cd notwork_react
pnpm install
pnpm dev
```

## API 文档

启动服务后访问 Swagger UI：

```
http://localhost:8080/swagger-ui.html
```

### 核心接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/chat` | POST | 普通对话 |
| `/api/chat/rag` | POST | RAG 智能问答 |
| `/api/chat/history` | GET | 获取对话历史 |
| `/api/blog` | CRUD | 博客管理 |
| `/api/user/login` | POST | 用户登录 |

## RAG 工作流程

```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│   博客发布   │ ──► │  RabbitMQ   │ ──► │ 向量化处理  │
└─────────────┘     └──────────────┘     └──────┬──────┘
                                                 │
                    ┌──────────────┐             ▼
                    │   Milvus     │ ◄── 文本分块 + Embedding
                    │ (向量存储)    │
                    └──────┬───────┘
                           │
                           ▼
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│   用户提问   │ ──► │ 向量检索 TopK │ ──► │  构建 Prompt │
└─────────────┘     └──────────────┘     └──────┬──────┘
                                                 │
                                                 ▼
                                          ┌─────────────┐
                                          │  AI 生成答案 │
                                          └─────────────┘
```

## 服务端口汇总

| 服务 | 端口 | 说明 |
|------|------|------|
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存 |
| RabbitMQ | 5672 | 消息队列 |
| RabbitMQ UI | 15672 | 管理界面 |
| MinIO | 9000 | 对象存储 API |
| MinIO Console | 9001 | 管理界面 |
| Milvus | 19530 | 向量数据库 |
| Elasticsearch | 9200 | 搜索引擎 |
| Kibana | 5601 | ES 可视化 |
| 后端 | 8080 | Spring Boot |
| 前端 | 3000 | React |

## License

MIT

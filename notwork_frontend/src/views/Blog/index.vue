<template>
  <div class="flex gap-4">
    <aside class="sticky top-20 z-10 self-start">
      <Sider />
    </aside>

    <!-- 右侧内容区 -->
    <div class="p-4 bg-white rounded-lg flex-1 min-h-[calc(100vh-100px)]">
      current type: {{ route.params.type }}
      <div class="flex gap-2 flex-col">
        <ElCard v-for="blog in blogList" :key="blog.id" shadow="never">
          <template #header>
            <div class="flex justify-between">
              <ElText>{{ blog.title }}</ElText>
              <span class="text-gray-500">{{ blog.date }}</span>
            </div>
          </template>
          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <ElAvatar
              src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
            />
            <span class="ml-5">{{ blog.author }}</span>
            </div>
            <span @click="onClickDetail(blog.url)" class="text-blue-400 hover:cursor-pointer hover:underline">查看详情</span>
          </div>
        </ElCard>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import Sider from "@/components/Layout/Sider.vue";
import { ElAvatar, ElCard, ElText } from "element-plus";
import { reactive } from "vue";
import { useRoute } from "vue-router";
import router from "@/router";

const route = useRoute();
const blogList = reactive([
  {
    id: 1,
    title: "测试文章1",
    url: "/blog/detail/1",
    author: "张三",
    date: "2025-9-14",
  },
  {
    id: 2,
    title: "测试文章2",
    url: "/blog/detail/2",
    author: "李四",
    date: "2025-9-14",
  },
  {
    id: 3,
    title: "测试文章3",
    url: "/blog/detail/3",
    author: "王五",
    date: "2025-9-14",
  },
]);
const onClickDetail = (url: string) => {
  router.push(url)
}
</script>

<template>
  <div class="flex flex-col md:flex-row gap-8 p-4 max-w-7xl mx-auto min-h-[calc(100vh-100px)]">
    <div class="md:w-60 flex-shrink-0 sticky top-4 max-h-[calc(100vh-2rem)] overflow-y-auto hidden md:block p-4">
      <h3 class="text-lg font-semibold mb-4">大纲</h3>
      <!-- 大纲为空时显示提示 -->
      <ul v-if="outlineList.length > 0" class="space-y-1 text-sm">
        <li 
          v-for="(item, i) in outlineList" 
          :key="item.id"
          :class="{ 
            'text-blue-600 font-medium': activeIndex === i,
            'hover:text-blue-500': true 
          }"
          :style="{ paddingLeft: `${(item.level - 1) * 16}px` }"
          @click="scrollTo(item.currentToken.attrs[1][1])"
          class="cursor-pointer py-1 transition-colors"
        >
          {{ item.text }}
        </li>
      </ul>
      <p v-else class="text-gray-500 text-sm">未检测到标题内容</p>
    </div>

    <!-- 右侧内容 -->
    <div class="flex-1">
      <MdPreview
        :md-heading-id="mdHeadingId"
        v-model="mdContent"
        class="prose mx-auto px-4 max-w-none"
        @on-get-catalog="initOutline"
      />
    </div>
  </div>
</template>

<script setup>
import { MdPreview } from "md-editor-v3";
import { blogListDetail } from "./blogTempData";
import { onMounted, ref, onUnmounted, nextTick } from "vue";
import { useRoute } from "vue-router";

const mdContent = ref("");
const outlineList = ref([]); // 大纲数据
const activeIndex = ref(-1);

const route = useRoute();
onMounted(() => {
  const blog = blogListDetail.find(b => b.id == route.params.id);
  mdContent.value = blog ? blog.content : "# 文章未找到";
});

const mdHeadingId = ({ index }) => `heading-${index}`;

const initOutline = (headingList) => {
  outlineList.value = headingList
};

// 点击大纲跳转
const scrollTo = (id) => {
  const el = document.getElementById(id);
  if (el) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
    activeIndex.value = outlineList.value.findIndex(item => item.id === id);
  }
};

</script>
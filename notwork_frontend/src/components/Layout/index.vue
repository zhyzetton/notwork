<template>
  <div class="">
    <!-- Header -->
    <div
      class="sticky top-0 left-0 right-0 z-20 transition-transform duration-300"
      :class="{ '!-translate-y-full': hidden }"
    >
      <Header />
    </div>

    <!-- 主内容区 -->
    <div class="container mx-auto p-4">
      <router-view :key="$route.fullPath"></router-view>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, onUnmounted, ref } from "vue";
import Header from "./Header.vue";

const hidden = ref(false);
const onScroll = () => {
  const currentScroll = window.scrollY;
  hidden.value = currentScroll > 50;
};

onMounted(() => {
  window.addEventListener("scroll", onScroll);
});

onUnmounted(() => {
  window.removeEventListener("scroll", onScroll);
});
</script>

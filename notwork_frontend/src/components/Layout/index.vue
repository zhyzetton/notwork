<template>
    <div>
        <!-- Header -->
        <div class="sticky top-0 left-0 right-0 z-20 transition-transform duration-300"
            :class="{ '!-translate-y-full': hidden }">
            <Header />
        </div>

        <!-- 主内容区 -->
        <div class="container mx-auto flex gap-6 p-4">
            <!-- 左侧Sider -->
            <aside class="sticky top-20 z-10 self-start">
                <Sider />
            </aside>

            <!-- 右侧内容区 -->
            <div class="p-4 bg-white rounded-lg flex-1">
                <div class="min-h-[2000px]">内容</div>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { onMounted, onUnmounted, ref } from 'vue';
import Header from './Header.vue';
import Sider from './Sider.vue';

const hidden = ref(false);
const onScroll = () => {
    const currentScroll = window.scrollY;
    hidden.value = currentScroll > 50;
};

onMounted(() => {
    window.addEventListener('scroll', onScroll);
});

onUnmounted(() => {
    window.removeEventListener('scroll', onScroll);
});
</script>

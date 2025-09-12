<template>
    <div class="w-48 rounded-lg p-2 flex flex-col gap-2 bg-white">
        <div @click="handleClick(data.url)" v-for="data in siderMenuData"
            class="text-gray-500 flex gap-4 py-2 px-4 hover:bg-gray-200 hover:text-blue-500 cursor-pointer rounded-lg">
            <i :class="data.icon"></i>
            {{ data.name }}
        </div>

    </div>
</template>

<script lang="ts" setup>
import { siderMenuData } from './layoutData';
import router from '@/router';

const handleClick = (url: string) => {
    router.push(url)
}
</script>
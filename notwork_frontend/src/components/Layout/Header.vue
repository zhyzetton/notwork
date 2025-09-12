<template>
    <header class="bg-white h-15 flex items-center">
        <div class="container mx-auto px-14 flex justify-between items-center">
            <div class="flex items-center">
                <img src="../../assets/logo.png" class="me-3 h-6 sm:h-9" alt="Flowbite Logo" />
                <span class="self-center text-xl font-semibold whitespace-nowrap dark:text-white">Notwork</span>
                <div class="flex gap-2 ml-6">
                    <span @click="onClickNav(navData.url)" class="text-gray-500 cursor-pointer hover:text-blue-400" v-for="navData in navBarData">
                        {{ navData.name }}
                    </span>
                </div>
            </div>
            <div>
                <ElButton @click="openDialog" type="primary">登录</ElButton>
                <Login :visible="loginVisible" @close="onClose" />
            </div>
        </div>
    </header>

</template>

<script lang="ts" setup>
import { ElButton } from 'element-plus';
import Login from '../Login/index.vue';
import { ref } from 'vue';
import { navBarData } from './layoutData';
import router from '@/router';

const loginVisible = ref(false)
const openDialog = () => {
    loginVisible.value = true
}
const onClose = (value: boolean) => {
    loginVisible.value = value
}
const onClickNav = (url: string) => {
    router.push(url)
}
</script>
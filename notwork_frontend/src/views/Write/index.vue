<template>
  <div class="min-h-[calc(100vh-100px)]">
    <div class="py-2 flex gap-2">
      <ElButton type="primary" @click="onClickSubmit">上传</ElButton>
      <ElButton type="primary" @click="onClickSubmit">上传</ElButton>
    </div>
    <MdEditor catalog-layout="fixed" class="prose max-w-none min-h-[calc(100vh-150px)]" v-model="text" />
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import { MdEditor } from "md-editor-v3";
import { ElButton } from "element-plus";

const text = ref("");
const onClickSubmit = () => {
  console.log(text.value);
};
</script>

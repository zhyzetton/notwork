<template>
  <div class="min-h-[calc(100vh-100px)]">
    <div class="py-2 flex gap-2 items-center">
      <ElInput v-model="title" placeholder="请输入文章标题" class="h-10" />
      <ElButton @click="onClickSave">保存至草稿</ElButton>
      <ElButton type="primary" @click="onClickPublish">发布</ElButton>
    </div>
    <MdEditor catalog-layout="fixed" class="prose max-w-none min-h-[calc(100vh-150px)]" v-model="mdText" />
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import { MdEditor } from "md-editor-v3";
import { ElButton, ElInput } from "element-plus";

const mdText = ref("");
const title = ref("")

const onClickSave = () => {

}
const onClickPublish = () => {
  console.log(mdText.value);
};
</script>

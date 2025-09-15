<template>
  <ElDialog v-model="dialogVisible" title="Notwork Login" width="500px">
    <ElForm>
      <ElFormItem>
        <ElInput v-model="loginForm.username" placeholder="username">
          <template #prefix>
            <i class="ri-user-line"></i>
          </template>
        </ElInput>
      </ElFormItem>
      <ElFormItem>
        <ElInput
          v-model="loginForm.password"
          placeholder="password"
          type="password"
        >
          <template #prefix>
            <i class="ri-lock-line"></i>
          </template>
        </ElInput>
      </ElFormItem>
    </ElForm>
    <template #footer>
      <ElButton type="primary" @click="onClickLogin">登录</ElButton>
      <ElButton @click="emit('close', false)">取消</ElButton>
    </template>
  </ElDialog>
</template>

<script lang="ts" setup>
import { ElButton, ElDialog, ElForm, ElFormItem, ElInput } from "element-plus";
import { computed, reactive } from "vue";
const props = defineProps({
  visible: Boolean,
});
const emit = defineEmits(["close"]);

const loginForm = reactive({
  username: "",
  password: "",
});
const dialogVisible = computed({
  get: () => props.visible,
  set: (val) => emit("close", val),
});
const onClickLogin = () => {
  console.log(loginForm);
};
</script>

<template>
  <ai-editor :init-value="props.initValue"  :height="props.height"  ref="editorRef"></ai-editor>
</template>


<script setup>
import {ref, watch} from "vue";
import AiEditor from "@/core/components/editor/ai-editor.vue";

const props = defineProps(['editorType', 'initValue', 'height'])
const editorRef = ref();

watch(() => props.initValue, (val) => {
  if (editorRef.value) {
    editorRef.value.setContent(val);
  }
})

function resetContent() {
  editorRef.value.resetContent();
}

function setContent(content) {
  editorRef.value.setContent(content);
}

function getValue() {
  return editorRef.value.getValue();
}

defineExpose({
  resetContent,
  setContent,
  getValue
})
</script>

<style scoped>
:deep(> div) {
  height: 100%;
}
</style>

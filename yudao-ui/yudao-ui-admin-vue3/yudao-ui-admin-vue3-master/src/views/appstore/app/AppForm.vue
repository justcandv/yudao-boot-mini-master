<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="700">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-form-item label="所属分类" prop="categoryId">
        <el-select v-model="formData.categoryId" placeholder="请选择分类">
          <el-option
            v-for="item in categoryList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="应用名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入应用名称" />
      </el-form-item>
      <el-form-item label="安卓包名" prop="packageName">
        <el-input v-model="formData.packageName" placeholder="请输入安卓包名，如 com.example.app" />
      </el-form-item>
      <el-form-item label="应用图标" prop="iconUrl">
        <el-input v-model="formData.iconUrl" placeholder="请输入图标 URL">
          <template #append>
            <el-image
              v-if="formData.iconUrl"
              :src="formData.iconUrl"
              class="w-24px h-24px"
              fit="contain"
            />
          </template>
        </el-input>
      </el-form-item>
      <el-form-item label="开发者" prop="developer">
        <el-input v-model="formData.developer" placeholder="请输入开发者名称" />
      </el-form-item>
      <el-form-item label="应用描述" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入应用描述"
        />
      </el-form-item>
      <el-form-item label="应用截图" prop="screenshots">
        <el-input
          v-model="formData.screenshots"
          type="textarea"
          :rows="3"
          placeholder="请输入截图 URL，多个以换行分隔"
        />
        <div v-if="screenshotList.length" class="mt-8px flex gap-8px flex-wrap">
          <el-image
            v-for="(url, idx) in screenshotList"
            :key="idx"
            :src="url"
            :preview-src-list="screenshotList"
            :initial-index="idx"
            class="w-80px h-60px"
            fit="cover"
            preview-teleported
          />
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as AppApi from '@/api/appstore/app'
import type { CategorySimpleVO } from '@/api/appstore/category'

defineOptions({ name: 'AppstoreAppForm' })

const props = defineProps<{
  categoryList: CategorySimpleVO[]
}>()

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined,
  categoryId: undefined,
  name: '',
  packageName: '',
  iconUrl: '',
  description: '',
  screenshots: '',
  developer: ''
})
const formRules = reactive({
  categoryId: [{ required: true, message: '所属分类不能为空', trigger: 'change' }],
  name: [{ required: true, message: '应用名称不能为空', trigger: 'blur' }],
  packageName: [{ required: true, message: '安卓包名不能为空', trigger: 'blur' }]
})
const formRef = ref()

/** 截图预览列表 */
const screenshotList = computed(() => {
  const raw = formData.value.screenshots
  if (!raw) return []
  try {
    const parsed = JSON.parse(raw)
    if (Array.isArray(parsed)) return parsed.filter((u: string) => u)
  } catch {
    // noop
  }
  return raw
    .split('\n')
    .map((s: string) => s.trim())
    .filter((s: string) => s)
})

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      const data = await AppApi.getApp(id)
      formData.value = {
        ...data,
        screenshots: formatScreenshotsForEdit(data.screenshots)
      }
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open })

/** 将 JSON 数组格式的截图转为换行分隔文本 */
const formatScreenshotsForEdit = (raw?: string) => {
  if (!raw) return ''
  try {
    const parsed = JSON.parse(raw)
    if (Array.isArray(parsed)) return parsed.join('\n')
  } catch {
    // noop
  }
  return raw
}

/** 将换行分隔的截图文本转为 JSON 数组字符串 */
const formatScreenshotsForSave = (raw: string) => {
  if (!raw) return ''
  const urls = raw
    .split('\n')
    .map((s) => s.trim())
    .filter((s) => s)
  return JSON.stringify(urls)
}

/** 提交表单 */
const emit = defineEmits(['success'])
const submitForm = async () => {
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  formLoading.value = true
  try {
    const data = {
      ...formData.value,
      screenshots: formatScreenshotsForSave(formData.value.screenshots)
    } as unknown as AppApi.AppVO
    if (formType.value === 'create') {
      await AppApi.createApp(data)
      message.success(t('common.createSuccess'))
    } else {
      await AppApi.updateApp(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    categoryId: undefined,
    name: '',
    packageName: '',
    iconUrl: '',
    description: '',
    screenshots: '',
    developer: ''
  } as any
  formRef.value?.resetFields()
}
</script>

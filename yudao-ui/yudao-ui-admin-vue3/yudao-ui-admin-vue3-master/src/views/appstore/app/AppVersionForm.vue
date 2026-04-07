<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="600">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-form-item label="版本号" prop="versionName">
        <el-input v-model="formData.versionName" placeholder="请输入版本号，如 1.0.0" />
      </el-form-item>
      <el-form-item label="versionCode" prop="versionCode">
        <el-input-number
          v-model="formData.versionCode"
          :min="1"
          controls-position="right"
          placeholder="请输入 versionCode"
        />
      </el-form-item>
      <el-form-item label="APK 文件" prop="apkUrl">
        <el-input v-model="formData.apkUrl" placeholder="上传 APK 或输入下载地址" class="mb-8px" />
        <el-upload
          :auto-upload="true"
          :show-file-list="false"
          :http-request="handleApkUpload"
          accept=".apk"
        >
          <el-button type="primary" :loading="uploading">
            <Icon icon="ep:upload" class="mr-5px" /> 上传 APK
          </el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="文件大小" prop="apkSize">
        <el-input-number
          v-model="formData.apkSize"
          :min="0"
          controls-position="right"
          placeholder="上传后自动填入（字节）"
        />
      </el-form-item>
      <el-form-item label="更新说明" prop="updateDescription">
        <el-input
          v-model="formData.updateDescription"
          type="textarea"
          :rows="3"
          placeholder="请输入更新说明"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :value="0">草稿</el-radio>
          <el-radio :value="1">已发布</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as AppVersionApi from '@/api/appstore/appversion'

defineOptions({ name: 'AppstoreAppVersionForm' })

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const uploading = ref(false)
const currentAppId = ref<number>()
const formData = ref({
  id: undefined,
  appId: undefined as number | undefined,
  versionName: '',
  versionCode: undefined as number | undefined,
  apkUrl: '',
  apkSize: undefined as number | undefined,
  updateDescription: '',
  status: 0
})
const formRules = reactive({
  versionName: [{ required: true, message: '版本号不能为空', trigger: 'blur' }],
  versionCode: [{ required: true, message: 'versionCode 不能为空', trigger: 'blur' }],
  apkUrl: [{ required: true, message: 'APK 下载地址不能为空', trigger: 'blur' }]
})
const formRef = ref()

/** 打开弹窗 */
const open = async (type: string, appId: number, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  currentAppId.value = appId
  resetForm()
  formData.value.appId = appId
  if (id) {
    formLoading.value = true
    try {
      formData.value = await AppVersionApi.getAppVersion(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open })

/** 上传 APK */
const handleApkUpload = async (options: { file: File }) => {
  uploading.value = true
  try {
    const url = await AppVersionApi.uploadApk(options.file)
    formData.value.apkUrl = url
    formData.value.apkSize = options.file.size
    message.success('上传成功')
  } catch {
    message.error('上传失败')
  } finally {
    uploading.value = false
  }
}

/** 提交表单 */
const emit = defineEmits(['success'])
const submitForm = async () => {
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  formLoading.value = true
  try {
    const data = { ...formData.value } as unknown as AppVersionApi.AppVersionVO
    data.appId = currentAppId.value!
    if (formType.value === 'create') {
      await AppVersionApi.createAppVersion(data)
      message.success(t('common.createSuccess'))
    } else {
      await AppVersionApi.updateAppVersion(data)
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
    appId: currentAppId.value,
    versionName: '',
    versionCode: undefined,
    apkUrl: '',
    apkSize: undefined,
    updateDescription: '',
    status: 0
  } as any
  formRef.value?.resetFields()
}
</script>

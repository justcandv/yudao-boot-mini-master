<template>
  <el-drawer v-model="drawerVisible" :title="`${appName} - 版本管理`" size="70%">
    <div class="mb-16px flex justify-between items-center">
      <el-form :model="queryParams" :inline="true" class="-mb-15px">
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="queryParams.status"
            placeholder="全部"
            clearable
            class="!w-160px"
            @change="handleQuery"
          >
            <el-option
              v-for="item in versionStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <el-button
        type="primary"
        @click="openForm('create')"
        v-hasPermi="['appstore:app-version:create']"
      >
        <Icon icon="ep:plus" class="mr-5px" /> 新增版本
      </el-button>
    </div>

    <el-table v-loading="loading" :data="list">
      <el-table-column label="版本号" align="center" prop="versionName" width="120" />
      <el-table-column label="versionCode" align="center" prop="versionCode" width="120" />
      <el-table-column label="APK 地址" align="center" prop="apkUrl" min-width="200" show-overflow-tooltip />
      <el-table-column label="文件大小" align="center" prop="apkSize" width="120">
        <template #default="scope">
          {{ scope.row.apkSize ? formatFileSize(scope.row.apkSize) : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="更新说明" align="center" prop="updateDescription" min-width="160" show-overflow-tooltip />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center" width="140" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['appstore:app-version:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['appstore:app-version:delete']"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 版本表单弹窗 -->
    <AppVersionForm ref="formRef" @success="getList" />
  </el-drawer>
</template>
<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import * as AppVersionApi from '@/api/appstore/appversion'
import AppVersionForm from './AppVersionForm.vue'

defineOptions({ name: 'AppstoreAppVersionDrawer' })

const message = useMessage()
const { t } = useI18n()

const drawerVisible = ref(false)
const appId = ref<number>()
const appName = ref('')
const loading = ref(false)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  appId: undefined as number | undefined,
  status: undefined as number | undefined
})

const versionStatusOptions = [
  { label: '草稿', value: 0 },
  { label: '已发布', value: 1 }
]

const formatFileSize = (bytes: number) => {
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  return (bytes / (1024 * 1024)).toFixed(1) + ' MB'
}

/** 打开抽屉 */
const open = (id: number, name: string) => {
  drawerVisible.value = true
  appId.value = id
  appName.value = name
  queryParams.appId = id
  queryParams.pageNo = 1
  queryParams.status = undefined
  getList()
}
defineExpose({ open })

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await AppVersionApi.getAppVersionPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, appId.value!, id)
}

/** 删除 */
const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await AppVersionApi.deleteAppVersion(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}
</script>

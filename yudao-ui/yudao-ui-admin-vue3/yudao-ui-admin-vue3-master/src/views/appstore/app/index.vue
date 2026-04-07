<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="应用名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入应用名称"
          clearable
          class="!w-240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="包名" prop="packageName">
        <el-input
          v-model="queryParams.packageName"
          placeholder="请输入安卓包名"
          clearable
          class="!w-240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类" prop="categoryId">
        <el-select
          v-model="queryParams.categoryId"
          placeholder="请选择分类"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="item in categoryList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable class="!w-240px">
          <el-option
            v-for="item in appStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['appstore:app:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="编号" align="center" prop="id" width="80" />
      <el-table-column label="应用图标" align="center" width="80">
        <template #default="scope">
          <el-image
            v-if="scope.row.iconUrl"
            :src="scope.row.iconUrl"
            :preview-src-list="[scope.row.iconUrl]"
            class="w-40px h-40px"
            preview-teleported
          />
        </template>
      </el-table-column>
      <el-table-column label="应用名称" align="center" prop="name" min-width="120" />
      <el-table-column label="包名" align="center" prop="packageName" min-width="160" />
      <el-table-column label="分类" align="center" prop="categoryId" width="120">
        <template #default="scope">
          {{ getCategoryName(scope.row.categoryId) }}
        </template>
      </el-table-column>
      <el-table-column label="开发者" align="center" prop="developer" width="120" />
      <el-table-column label="下载量" align="center" prop="downloadCount" width="100" />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template #default="scope">
          <el-tag :type="getAppStatusType(scope.row.status)">
            {{ getAppStatusLabel(scope.row.status) }}
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
      <el-table-column label="操作" align="center" width="260" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['appstore:app:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="primary"
            @click="openVersionDrawer(scope.row)"
          >
            版本
          </el-button>
          <el-button
            v-if="scope.row.status !== 1"
            link
            type="success"
            @click="handleUpdateStatus(scope.row.id, 1)"
            v-hasPermi="['appstore:app:update']"
          >
            上架
          </el-button>
          <el-button
            v-if="scope.row.status === 1"
            link
            type="warning"
            @click="handleUpdateStatus(scope.row.id, 2)"
            v-hasPermi="['appstore:app:update']"
          >
            下架
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['appstore:app:delete']"
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
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <AppForm ref="formRef" :category-list="categoryList" @success="getList" />

  <!-- 版本管理抽屉 -->
  <AppVersionDrawer ref="versionDrawerRef" />
</template>
<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import * as AppApi from '@/api/appstore/app'
import * as CategoryApi from '@/api/appstore/category'
import AppForm from './AppForm.vue'
import AppVersionDrawer from './AppVersionDrawer.vue'

defineOptions({ name: 'AppstoreApp' })

const message = useMessage()
const { t } = useI18n()

const loading = ref(true)
const total = ref(0)
const list = ref([])
const categoryList = ref<CategoryApi.CategorySimpleVO[]>([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  name: '',
  packageName: '',
  categoryId: undefined,
  status: undefined
})
const queryFormRef = ref()

const appStatusOptions = [
  { label: '草稿', value: 0 },
  { label: '已上架', value: 1 },
  { label: '已下架', value: 2 }
]

const getAppStatusLabel = (status: number) => {
  return appStatusOptions.find((item) => item.value === status)?.label ?? '未知'
}

const getAppStatusType = (status: number) => {
  const map: Record<number, string> = { 0: 'info', 1: 'success', 2: 'danger' }
  return map[status] ?? 'info'
}

const getCategoryName = (categoryId: number) => {
  return categoryList.value.find((item) => item.id === categoryId)?.name ?? categoryId
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await AppApi.getAppPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 更新状态操作 */
const handleUpdateStatus = async (id: number, status: number) => {
  const statusLabel = status === 1 ? '上架' : '下架'
  try {
    await message.confirm(`确认要${statusLabel}该应用吗？`)
    await AppApi.updateAppStatus(id, status)
    message.success(`${statusLabel}成功`)
    await getList()
  } catch {}
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await AppApi.deleteApp(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

/** 版本管理抽屉 */
const versionDrawerRef = ref()
const openVersionDrawer = (row: any) => {
  versionDrawerRef.value.open(row.id, row.name)
}

/** 初始化 */
onMounted(async () => {
  await getList()
  categoryList.value = await CategoryApi.getCategorySimpleList()
})
</script>

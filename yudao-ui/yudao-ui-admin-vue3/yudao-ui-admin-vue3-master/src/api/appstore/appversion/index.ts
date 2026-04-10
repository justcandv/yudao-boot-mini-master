import request from '@/config/axios'

export interface AppVersionVO {
  id?: number
  appId: number
  versionName: string
  versionCode: number
  apkUrl: string
  apkSize?: number
  updateDescription?: string
  status?: number
  createTime?: Date
}

// 查询应用版本分页
export const getAppVersionPage = async (params: PageParam) => {
  return await request.get({ url: '/appstore/app-version/page', params })
}

// 查询应用版本详情
export const getAppVersion = async (id: number) => {
  return await request.get({ url: '/appstore/app-version/get?id=' + id })
}

// 新增应用版本
export const createAppVersion = async (data: AppVersionVO) => {
  return await request.post({ url: '/appstore/app-version/create', data })
}

// 修改应用版本
export const updateAppVersion = async (data: AppVersionVO) => {
  return await request.put({ url: '/appstore/app-version/update', data })
}

// 删除应用版本
export const deleteAppVersion = async (id: number) => {
  return await request.delete({ url: '/appstore/app-version/delete?id=' + id })
}

// 上传 APK 文件（须用 upload，避免默认 application/json 导致后端无法解析 multipart）
export const uploadApk = async (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  const res: any = await request.upload({
    url: '/appstore/app-version/upload-apk',
    data: formData,
    timeout: 600000 // 大 APK 上传，避免 30s 默认超时误判为网络错误
  })
  return res.data as string
}

import request from '@/config/axios'

export interface AppVO {
  id?: number
  categoryId: number
  name: string
  packageName: string
  iconUrl?: string
  description?: string
  screenshots?: string
  developer?: string
  status?: number
  downloadCount?: number
  latestVersionId?: number
  createTime?: Date
}

// 查询应用分页
export const getAppPage = async (params: PageParam) => {
  return await request.get({ url: '/appstore/app/page', params })
}

// 查询应用详情
export const getApp = async (id: number) => {
  return await request.get({ url: '/appstore/app/get?id=' + id })
}

// 新增应用
export const createApp = async (data: AppVO) => {
  return await request.post({ url: '/appstore/app/create', data })
}

// 修改应用
export const updateApp = async (data: AppVO) => {
  return await request.put({ url: '/appstore/app/update', data })
}

// 更新应用状态
export const updateAppStatus = async (id: number, status: number) => {
  return await request.put({ url: '/appstore/app/update-status', params: { id, status } })
}

// 删除应用
export const deleteApp = async (id: number) => {
  return await request.delete({ url: '/appstore/app/delete?id=' + id })
}

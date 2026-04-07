import request from '@/config/axios'

export interface CategoryVO {
  id?: number
  name: string
  iconUrl?: string
  sort: number
  status: number
  createTime?: Date
}

export interface CategorySimpleVO {
  id: number
  name: string
}

// 查询应用分类分页
export const getCategoryPage = async (params: PageParam) => {
  return await request.get({ url: '/appstore/category/page', params })
}

// 查询应用分类详情
export const getCategory = async (id: number) => {
  return await request.get({ url: '/appstore/category/get?id=' + id })
}

// 查询应用分类精简列表
export const getCategorySimpleList = async (): Promise<CategorySimpleVO[]> => {
  return await request.get({ url: '/appstore/category/simple-list' })
}

// 新增应用分类
export const createCategory = async (data: CategoryVO) => {
  return await request.post({ url: '/appstore/category/create', data })
}

// 修改应用分类
export const updateCategory = async (data: CategoryVO) => {
  return await request.put({ url: '/appstore/category/update', data })
}

// 删除应用分类
export const deleteCategory = async (id: number) => {
  return await request.delete({ url: '/appstore/category/delete?id=' + id })
}

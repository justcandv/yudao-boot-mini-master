package cn.iocoder.yudao.module.appstore.service.category;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.appstore.controller.admin.category.vo.CategoryPageReqVO;
import cn.iocoder.yudao.module.appstore.controller.admin.category.vo.CategorySaveReqVO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.category.CategoryDO;

import java.util.List;

/**
 * 应用分类 Service 接口
 *
 * @author appstore
 */
public interface CategoryService {

    /**
     * 创建应用分类
     *
     * @param createReqVO 创建信息
     * @return 分类编号
     */
    Long createCategory(CategorySaveReqVO createReqVO);

    /**
     * 更新应用分类
     *
     * @param updateReqVO 更新信息
     */
    void updateCategory(CategorySaveReqVO updateReqVO);

    /**
     * 删除应用分类
     *
     * @param id 分类编号
     */
    void deleteCategory(Long id);

    /**
     * 获得应用分类
     *
     * @param id 分类编号
     * @return 应用分类
     */
    CategoryDO getCategory(Long id);

    /**
     * 获得应用分类分页
     *
     * @param pageReqVO 分页查询
     * @return 应用分类分页
     */
    PageResult<CategoryDO> getCategoryPage(CategoryPageReqVO pageReqVO);

    /**
     * 获得已启用的分类列表（精简）
     *
     * @return 分类列表
     */
    List<CategoryDO> getCategorySimpleList();

}

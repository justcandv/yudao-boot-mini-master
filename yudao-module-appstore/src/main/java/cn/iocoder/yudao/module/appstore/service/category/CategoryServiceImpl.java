package cn.iocoder.yudao.module.appstore.service.category;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.appstore.controller.admin.category.vo.CategoryPageReqVO;
import cn.iocoder.yudao.module.appstore.controller.admin.category.vo.CategorySaveReqVO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.category.CategoryDO;
import cn.iocoder.yudao.module.appstore.dal.mysql.app.AppMapper;
import cn.iocoder.yudao.module.appstore.dal.mysql.category.CategoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.appstore.enums.ErrorCodeConstants.CATEGORY_HAS_APP;
import static cn.iocoder.yudao.module.appstore.enums.ErrorCodeConstants.CATEGORY_NOT_EXISTS;

/**
 * 应用分类 Service 实现类
 *
 * @author appstore
 */
@Service
@Validated
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private AppMapper appMapper;

    @Override
    public Long createCategory(CategorySaveReqVO createReqVO) {
        CategoryDO category = BeanUtils.toBean(createReqVO, CategoryDO.class);
        categoryMapper.insert(category);
        return category.getId();
    }

    @Override
    public void updateCategory(CategorySaveReqVO updateReqVO) {
        validateCategoryExists(updateReqVO.getId());
        CategoryDO updateObj = BeanUtils.toBean(updateReqVO, CategoryDO.class);
        categoryMapper.updateById(updateObj);
    }

    @Override
    public void deleteCategory(Long id) {
        validateCategoryExists(id);
        // 校验分类下是否存在应用
        if (appMapper.selectCountByCategoryId(id) > 0) {
            throw exception(CATEGORY_HAS_APP);
        }
        categoryMapper.deleteById(id);
    }

    @Override
    public CategoryDO getCategory(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public PageResult<CategoryDO> getCategoryPage(CategoryPageReqVO pageReqVO) {
        return categoryMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CategoryDO> getCategorySimpleList() {
        return categoryMapper.selectListByStatus(CommonStatusEnum.ENABLE.getStatus());
    }

    private void validateCategoryExists(Long id) {
        if (categoryMapper.selectById(id) == null) {
            throw exception(CATEGORY_NOT_EXISTS);
        }
    }

}

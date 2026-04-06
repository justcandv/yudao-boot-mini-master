package cn.iocoder.yudao.module.appstore.controller.admin.category;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.appstore.controller.admin.category.vo.CategoryPageReqVO;
import cn.iocoder.yudao.module.appstore.controller.admin.category.vo.CategoryRespVO;
import cn.iocoder.yudao.module.appstore.controller.admin.category.vo.CategorySaveReqVO;
import cn.iocoder.yudao.module.appstore.controller.admin.category.vo.CategorySimpleRespVO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.category.CategoryDO;
import cn.iocoder.yudao.module.appstore.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 应用分类")
@RestController
@RequestMapping("/appstore/category")
@Validated
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping("/create")
    @Operation(summary = "创建应用分类")
    @PreAuthorize("@ss.hasPermission('appstore:category:create')")
    public CommonResult<Long> createCategory(@Valid @RequestBody CategorySaveReqVO createReqVO) {
        return success(categoryService.createCategory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新应用分类")
    @PreAuthorize("@ss.hasPermission('appstore:category:update')")
    public CommonResult<Boolean> updateCategory(@Valid @RequestBody CategorySaveReqVO updateReqVO) {
        categoryService.updateCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除应用分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('appstore:category:delete')")
    public CommonResult<Boolean> deleteCategory(@RequestParam("id") Long id) {
        categoryService.deleteCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得应用分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('appstore:category:query')")
    public CommonResult<CategoryRespVO> getCategory(@RequestParam("id") Long id) {
        CategoryDO category = categoryService.getCategory(id);
        return success(BeanUtils.toBean(category, CategoryRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得应用分类分页")
    @PreAuthorize("@ss.hasPermission('appstore:category:query')")
    public CommonResult<PageResult<CategoryRespVO>> getCategoryPage(@Valid CategoryPageReqVO pageReqVO) {
        PageResult<CategoryDO> pageResult = categoryService.getCategoryPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CategoryRespVO.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得应用分类精简列表", description = "只包含已开启的分类，用于前端下拉选项")
    public CommonResult<List<CategorySimpleRespVO>> getCategorySimpleList() {
        List<CategoryDO> list = categoryService.getCategorySimpleList();
        return success(BeanUtils.toBean(list, CategorySimpleRespVO.class));
    }

}

package cn.iocoder.yudao.module.appstore.controller.app.category;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.appstore.controller.admin.category.vo.CategorySimpleRespVO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.category.CategoryDO;
import cn.iocoder.yudao.module.appstore.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "TV 端 - 应用分类")
@RestController
@RequestMapping("/appstore/category")
@Validated
public class TvCategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "获得已启用的应用分类列表")
    @PermitAll
    public CommonResult<List<CategorySimpleRespVO>> getCategoryList() {
        List<CategoryDO> list = categoryService.getCategorySimpleList();
        return success(BeanUtils.toBean(list, CategorySimpleRespVO.class));
    }

}

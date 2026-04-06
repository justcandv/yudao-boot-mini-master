package cn.iocoder.yudao.module.appstore.controller.admin.app;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.appstore.controller.admin.app.vo.AppPageReqVO;
import cn.iocoder.yudao.module.appstore.controller.admin.app.vo.AppRespVO;
import cn.iocoder.yudao.module.appstore.controller.admin.app.vo.AppSaveReqVO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.app.AppDO;
import cn.iocoder.yudao.module.appstore.service.app.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 应用管理")
@RestController
@RequestMapping("/appstore/app")
@Validated
public class AppController {

    @Resource
    private AppService appService;

    @PostMapping("/create")
    @Operation(summary = "创建应用")
    @PreAuthorize("@ss.hasPermission('appstore:app:create')")
    public CommonResult<Long> createApp(@Valid @RequestBody AppSaveReqVO createReqVO) {
        return success(appService.createApp(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新应用")
    @PreAuthorize("@ss.hasPermission('appstore:app:update')")
    public CommonResult<Boolean> updateApp(@Valid @RequestBody AppSaveReqVO updateReqVO) {
        appService.updateApp(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新应用状态（上架/下架）")
    @Parameters({
            @Parameter(name = "id", description = "应用编号", required = true, example = "1"),
            @Parameter(name = "status", description = "状态（0 草稿 1 已上架 2 已下架）", required = true, example = "1")
    })
    @PreAuthorize("@ss.hasPermission('appstore:app:update')")
    public CommonResult<Boolean> updateAppStatus(@RequestParam("id") Long id,
                                                 @RequestParam("status") Integer status) {
        appService.updateAppStatus(id, status);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除应用")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('appstore:app:delete')")
    public CommonResult<Boolean> deleteApp(@RequestParam("id") Long id) {
        appService.deleteApp(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得应用信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('appstore:app:query')")
    public CommonResult<AppRespVO> getApp(@RequestParam("id") Long id) {
        AppDO app = appService.getApp(id);
        return success(BeanUtils.toBean(app, AppRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得应用分页")
    @PreAuthorize("@ss.hasPermission('appstore:app:query')")
    public CommonResult<PageResult<AppRespVO>> getAppPage(@Valid AppPageReqVO pageReqVO) {
        PageResult<AppDO> pageResult = appService.getAppPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppRespVO.class));
    }

}

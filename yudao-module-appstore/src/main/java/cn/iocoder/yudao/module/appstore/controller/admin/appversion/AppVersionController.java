package cn.iocoder.yudao.module.appstore.controller.admin.appversion;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.appstore.controller.admin.appversion.vo.AppVersionPageReqVO;
import cn.iocoder.yudao.module.appstore.controller.admin.appversion.vo.AppVersionRespVO;
import cn.iocoder.yudao.module.appstore.controller.admin.appversion.vo.AppVersionSaveReqVO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.app.AppVersionDO;
import cn.iocoder.yudao.module.appstore.service.appversion.AppVersionService;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 应用版本")
@RestController
@RequestMapping("/appstore/app-version")
@Validated
public class AppVersionController {

    @Resource
    private AppVersionService appVersionService;

    @Resource
    private FileApi fileApi;

    @PostMapping("/upload-apk")
    @Operation(summary = "上传 APK 文件", description = "上传 APK 并返回文件访问 URL")
    @PreAuthorize("@ss.hasPermission('appstore:app-version:create')")
    public CommonResult<String> uploadApk(@RequestParam("file") MultipartFile file) throws Exception {
        String url = fileApi.createFile(file.getBytes(), file.getOriginalFilename(),
                "appstore/apk", file.getContentType());
        return success(url);
    }

    @PostMapping("/create")
    @Operation(summary = "创建应用版本")
    @PreAuthorize("@ss.hasPermission('appstore:app-version:create')")
    public CommonResult<Long> createAppVersion(@Valid @RequestBody AppVersionSaveReqVO createReqVO) {
        return success(appVersionService.createAppVersion(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新应用版本")
    @PreAuthorize("@ss.hasPermission('appstore:app-version:update')")
    public CommonResult<Boolean> updateAppVersion(@Valid @RequestBody AppVersionSaveReqVO updateReqVO) {
        appVersionService.updateAppVersion(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除应用版本")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('appstore:app-version:delete')")
    public CommonResult<Boolean> deleteAppVersion(@RequestParam("id") Long id) {
        appVersionService.deleteAppVersion(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得应用版本")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('appstore:app-version:query')")
    public CommonResult<AppVersionRespVO> getAppVersion(@RequestParam("id") Long id) {
        AppVersionDO version = appVersionService.getAppVersion(id);
        return success(BeanUtils.toBean(version, AppVersionRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得应用版本分页")
    @PreAuthorize("@ss.hasPermission('appstore:app-version:query')")
    public CommonResult<PageResult<AppVersionRespVO>> getAppVersionPage(@Valid AppVersionPageReqVO pageReqVO) {
        PageResult<AppVersionDO> pageResult = appVersionService.getAppVersionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppVersionRespVO.class));
    }

}

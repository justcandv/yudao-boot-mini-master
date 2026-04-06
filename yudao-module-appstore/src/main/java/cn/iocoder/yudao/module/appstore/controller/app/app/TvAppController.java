package cn.iocoder.yudao.module.appstore.controller.app.app;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.appstore.controller.admin.app.vo.AppRespVO;
import cn.iocoder.yudao.module.appstore.controller.app.app.vo.TvAppDetailRespVO;
import cn.iocoder.yudao.module.appstore.controller.app.app.vo.TvAppListReqVO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.app.AppDO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.app.AppVersionDO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.category.CategoryDO;
import cn.iocoder.yudao.module.appstore.service.app.AppService;
import cn.iocoder.yudao.module.appstore.service.appversion.AppVersionService;
import cn.iocoder.yudao.module.appstore.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "TV 端 - 应用")
@RestController
@RequestMapping("/appstore/app")
@Validated
public class TvAppController {

    @Resource
    private AppService appService;

    @Resource
    private AppVersionService appVersionService;

    @Resource
    private CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "获得已上架应用列表")
    @PermitAll
    public CommonResult<List<AppRespVO>> getAppList(TvAppListReqVO reqVO) {
        List<AppDO> list;
        if (StrUtil.isNotBlank(reqVO.getKeyword())) {
            list = appService.searchPublishedApp(reqVO.getKeyword());
        } else {
            list = appService.getPublishedAppList(reqVO.getCategoryId());
        }
        return success(BeanUtils.toBean(list, AppRespVO.class));
    }

    @GetMapping("/get")
    @Operation(summary = "获得应用详情（含最新版本信息）")
    @Parameter(name = "id", description = "应用编号", required = true, example = "1")
    @PermitAll
    public CommonResult<TvAppDetailRespVO> getAppDetail(@RequestParam("id") Long id) {
        AppDO app = appService.getApp(id);
        if (app == null) {
            return success(null);
        }
        TvAppDetailRespVO respVO = BeanUtils.toBean(app, TvAppDetailRespVO.class);
        // 解析截图 JSON
        if (StrUtil.isNotBlank(app.getScreenshots())) {
            respVO.setScreenshots(JSONUtil.toList(app.getScreenshots(), String.class));
        }
        // 填充分类名称
        CategoryDO category = categoryService.getCategory(app.getCategoryId());
        if (category != null) {
            respVO.setCategoryName(category.getName());
        }
        // 填充最新版本信息
        AppVersionDO latestVersion = appVersionService.getLatestVersion(app.getId());
        if (latestVersion != null) {
            respVO.setVersionName(latestVersion.getVersionName());
            respVO.setVersionCode(latestVersion.getVersionCode());
            respVO.setApkUrl(latestVersion.getApkUrl());
            respVO.setApkSize(latestVersion.getApkSize());
            respVO.setUpdateDescription(latestVersion.getUpdateDescription());
        }
        return success(respVO);
    }

    @PostMapping("/download")
    @Operation(summary = "记录下载并返回 APK 下载地址")
    @Parameter(name = "id", description = "应用编号", required = true, example = "1")
    @PermitAll
    public CommonResult<String> downloadApp(@RequestParam("id") Long id) {
        appService.incrementDownloadCount(id);
        AppVersionDO latestVersion = appVersionService.getLatestVersion(id);
        return success(latestVersion != null ? latestVersion.getApkUrl() : null);
    }

}

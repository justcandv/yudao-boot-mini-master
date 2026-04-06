package cn.iocoder.yudao.module.appstore.controller.app.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "TV 端 - 应用详情 Response VO")
@Data
public class TvAppDetailRespVO {

    @Schema(description = "应用编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "应用名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芒果TV")
    private String name;

    @Schema(description = "安卓包名", requiredMode = Schema.RequiredMode.REQUIRED, example = "com.mgtv.tv")
    private String packageName;

    @Schema(description = "应用图标 URL", example = "https://example.com/icon.png")
    private String iconUrl;

    @Schema(description = "应用描述", example = "一款热门的视频应用")
    private String description;

    @Schema(description = "截图 URL 列表")
    private List<String> screenshots;

    @Schema(description = "开发者名称", example = "芒果TV")
    private String developer;

    @Schema(description = "累计下载次数", example = "10000")
    private Long downloadCount;

    @Schema(description = "分类名称", example = "影音娱乐")
    private String categoryName;

    // ----- 最新版本信息 -----

    @Schema(description = "最新版本号", example = "1.2.0")
    private String versionName;

    @Schema(description = "最新版本 versionCode", example = "120")
    private Integer versionCode;

    @Schema(description = "APK 下载地址", example = "https://example.com/app.apk")
    private String apkUrl;

    @Schema(description = "APK 文件大小（字节）", example = "52428800")
    private Long apkSize;

    @Schema(description = "更新说明", example = "修复已知问题，优化性能")
    private String updateDescription;

}

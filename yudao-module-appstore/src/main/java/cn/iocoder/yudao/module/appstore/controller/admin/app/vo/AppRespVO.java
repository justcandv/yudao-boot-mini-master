package cn.iocoder.yudao.module.appstore.controller.admin.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 应用信息 Response VO")
@Data
public class AppRespVO {

    @Schema(description = "应用编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "所属分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long categoryId;

    @Schema(description = "应用名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芒果TV")
    private String name;

    @Schema(description = "安卓包名", requiredMode = Schema.RequiredMode.REQUIRED, example = "com.mgtv.tv")
    private String packageName;

    @Schema(description = "应用图标 URL", example = "https://example.com/icon.png")
    private String iconUrl;

    @Schema(description = "应用描述", example = "一款热门的视频应用")
    private String description;

    @Schema(description = "截图 URL（JSON 数组）", example = "[\"https://example.com/s1.png\"]")
    private String screenshots;

    @Schema(description = "开发者名称", example = "芒果TV")
    private String developer;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "累计下载次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10000")
    private Long downloadCount;

    @Schema(description = "最新版本编号", example = "5")
    private Long latestVersionId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

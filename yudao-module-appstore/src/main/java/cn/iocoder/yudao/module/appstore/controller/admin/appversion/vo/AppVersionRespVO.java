package cn.iocoder.yudao.module.appstore.controller.admin.appversion.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 应用版本 Response VO")
@Data
public class AppVersionRespVO {

    @Schema(description = "版本编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "关联应用编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long appId;

    @Schema(description = "版本号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.0.0")
    private String versionName;

    @Schema(description = "安卓 versionCode", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Integer versionCode;

    @Schema(description = "APK 下载地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://example.com/app.apk")
    private String apkUrl;

    @Schema(description = "APK 文件大小（字节）", example = "52428800")
    private Long apkSize;

    @Schema(description = "更新说明", example = "修复已知问题，优化性能")
    private String updateDescription;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

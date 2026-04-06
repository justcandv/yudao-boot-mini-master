package cn.iocoder.yudao.module.appstore.controller.admin.appversion.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 应用版本创建/修改 Request VO")
@Data
public class AppVersionSaveReqVO {

    @Schema(description = "版本编号", example = "1")
    private Long id;

    @Schema(description = "关联应用编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "关联应用编号不能为空")
    private Long appId;

    @Schema(description = "版本号（如 1.2.0）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.0.0")
    @NotBlank(message = "版本号不能为空")
    private String versionName;

    @Schema(description = "安卓 versionCode", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "versionCode 不能为空")
    private Integer versionCode;

    @Schema(description = "APK 下载地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://example.com/app.apk")
    @NotBlank(message = "APK 下载地址不能为空")
    private String apkUrl;

    @Schema(description = "APK 文件大小（字节）", example = "52428800")
    private Long apkSize;

    @Schema(description = "更新说明", example = "修复已知问题，优化性能")
    private String updateDescription;

    @Schema(description = "状态（0 草稿 1 已发布）", example = "0")
    private Integer status;

}

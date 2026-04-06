package cn.iocoder.yudao.module.appstore.controller.admin.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "管理后台 - 应用创建/修改 Request VO")
@Data
public class AppSaveReqVO {

    @Schema(description = "应用编号", example = "1")
    private Long id;

    @Schema(description = "所属分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "所属分类不能为空")
    private Long categoryId;

    @Schema(description = "应用名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芒果TV")
    @NotBlank(message = "应用名称不能为空")
    @Size(max = 128, message = "应用名称长度不能超过 128 个字符")
    private String name;

    @Schema(description = "安卓包名", requiredMode = Schema.RequiredMode.REQUIRED, example = "com.mgtv.tv")
    @NotBlank(message = "安卓包名不能为空")
    @Size(max = 256, message = "安卓包名长度不能超过 256 个字符")
    private String packageName;

    @Schema(description = "应用图标 URL", example = "https://example.com/icon.png")
    private String iconUrl;

    @Schema(description = "应用描述", example = "一款热门的视频应用")
    private String description;

    @Schema(description = "截图 URL（JSON 数组）", example = "[\"https://example.com/s1.png\"]")
    private String screenshots;

    @Schema(description = "开发者名称", example = "芒果TV")
    private String developer;

}

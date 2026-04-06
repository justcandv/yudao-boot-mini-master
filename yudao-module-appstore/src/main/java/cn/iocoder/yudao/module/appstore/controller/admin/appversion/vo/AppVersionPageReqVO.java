package cn.iocoder.yudao.module.appstore.controller.admin.appversion.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 应用版本分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppVersionPageReqVO extends PageParam {

    @Schema(description = "关联应用编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "关联应用编号不能为空")
    private Long appId;

    @Schema(description = "状态（0 草稿 1 已发布）", example = "1")
    private Integer status;

}

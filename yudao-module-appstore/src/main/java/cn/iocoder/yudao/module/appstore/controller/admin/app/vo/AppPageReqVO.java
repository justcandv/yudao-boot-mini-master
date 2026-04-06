package cn.iocoder.yudao.module.appstore.controller.admin.app.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 应用分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppPageReqVO extends PageParam {

    @Schema(description = "应用名称，模糊匹配", example = "芒果")
    private String name;

    @Schema(description = "安卓包名，模糊匹配", example = "com.mgtv")
    private String packageName;

    @Schema(description = "所属分类编号", example = "1")
    private Long categoryId;

    @Schema(description = "状态", example = "1")
    private Integer status;

}

package cn.iocoder.yudao.module.appstore.controller.admin.category.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 应用分类分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryPageReqVO extends PageParam {

    @Schema(description = "分类名称，模糊匹配", example = "影音")
    private String name;

    @Schema(description = "状态，见 CommonStatusEnum 枚举", example = "0")
    private Integer status;

}

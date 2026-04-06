package cn.iocoder.yudao.module.appstore.controller.app.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "TV 端 - 应用列表 Request VO")
@Data
public class TvAppListReqVO {

    @Schema(description = "分类编号", example = "1")
    private Long categoryId;

    @Schema(description = "搜索关键词", example = "芒果")
    private String keyword;

}

package cn.iocoder.yudao.module.appstore.controller.app.device.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "TV 端 - 刷新 Token Request VO")
@Data
public class RefreshTokenReqVO {

    @Schema(description = "刷新令牌", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "刷新令牌不能为空")
    @JsonProperty("refresh_token")
    private String refreshToken;

}

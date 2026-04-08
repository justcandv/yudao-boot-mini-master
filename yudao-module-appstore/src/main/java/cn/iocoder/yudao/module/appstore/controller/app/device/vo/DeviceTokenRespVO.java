package cn.iocoder.yudao.module.appstore.controller.app.device.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "TV 端 - 设备授权轮询 Response VO")
@Data
public class DeviceTokenRespVO {

    @Schema(description = "访问令牌")
    @JsonProperty("access_token")
    private String accessToken;

    @Schema(description = "刷新令牌")
    @JsonProperty("refresh_token")
    private String refreshToken;

    @Schema(description = "访问令牌有效期（秒）")
    @JsonProperty("expires_in")
    private Integer expiresIn;

    @Schema(description = "设备名称")
    @JsonProperty("device_name")
    private String deviceName;

}

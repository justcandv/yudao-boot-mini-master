package cn.iocoder.yudao.module.appstore.controller.app.device.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "TV 端 - 设备注册 Response VO")
@Data
public class DeviceRegisterRespVO {

    @Schema(description = "设备授权码")
    @JsonProperty("device_code")
    private String deviceCode;

    @Schema(description = "激活链接")
    @JsonProperty("activation_url")
    private String activationUrl;

    @Schema(description = "设备码过期时间（秒）")
    @JsonProperty("expires_in")
    private Integer expiresIn;

    @Schema(description = "建议轮询间隔（秒）")
    @JsonProperty("poll_interval")
    private Integer pollInterval;

}

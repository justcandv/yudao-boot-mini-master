package cn.iocoder.yudao.module.appstore.controller.app.device.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "TV 端 - 轮询设备授权 Request VO")
@Data
public class DeviceTokenReqVO {

    @Schema(description = "设备唯一标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "设备唯一标识不能为空")
    @JsonProperty("device_id")
    private String deviceId;

    @Schema(description = "设备授权码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "设备授权码不能为空")
    @JsonProperty("device_code")
    private String deviceCode;

}

package cn.iocoder.yudao.module.appstore.controller.app.device.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "TV 端 - 设备注册 Request VO")
@Data
public class DeviceRegisterReqVO {

    @Schema(description = "设备唯一标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "设备唯一标识不能为空")
    @JsonProperty("device_id")
    private String deviceId;

    @Schema(description = "设备型号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "设备型号不能为空")
    @JsonProperty("device_model")
    private String deviceModel;

    @Schema(description = "系统版本", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "系统版本不能为空")
    @JsonProperty("os_version")
    private String osVersion;

    @Schema(description = "应用版本号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "应用版本号不能为空")
    @JsonProperty("app_version")
    private String appVersion;

}

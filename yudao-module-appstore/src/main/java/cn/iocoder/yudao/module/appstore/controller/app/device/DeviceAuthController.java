package cn.iocoder.yudao.module.appstore.controller.app.device;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.appstore.controller.app.device.vo.*;
import cn.iocoder.yudao.module.appstore.service.deviceauth.DeviceAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "TV 端 - 设备认证")
@RestController
@RequestMapping("/device")
@Validated
public class DeviceAuthController {

    @Resource
    private DeviceAuthService deviceAuthService;

    @PostMapping("/register")
    @Operation(summary = "设备注册")
    @PermitAll
    public CommonResult<DeviceRegisterRespVO> register(@Valid @RequestBody DeviceRegisterReqVO reqVO) {
        return success(deviceAuthService.register(reqVO));
    }

    @PostMapping("/token")
    @Operation(summary = "轮询设备授权状态")
    @PermitAll
    public CommonResult<DeviceTokenRespVO> pollToken(@Valid @RequestBody DeviceTokenReqVO reqVO) {
        return success(deviceAuthService.pollToken(reqVO));
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新 Token")
    @PermitAll
    public CommonResult<RefreshTokenRespVO> refreshToken(@Valid @RequestBody RefreshTokenReqVO reqVO) {
        return success(deviceAuthService.refreshToken(reqVO));
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    @PermitAll
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String accessToken = SecurityFrameworkUtils.obtainAuthorization(request, "Authorization", "access_token");
        deviceAuthService.logout(accessToken);
        return success(true);
    }

}

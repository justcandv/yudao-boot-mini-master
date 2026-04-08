package cn.iocoder.yudao.module.appstore.service.deviceauth;

import cn.iocoder.yudao.module.appstore.controller.app.device.vo.*;

public interface DeviceAuthService {

    DeviceRegisterRespVO register(DeviceRegisterReqVO reqVO);

    DeviceTokenRespVO pollToken(DeviceTokenReqVO reqVO);

    RefreshTokenRespVO refreshToken(RefreshTokenReqVO reqVO);

    void logout(String accessToken);

}

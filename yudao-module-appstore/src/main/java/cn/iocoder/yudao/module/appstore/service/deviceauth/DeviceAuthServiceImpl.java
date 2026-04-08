package cn.iocoder.yudao.module.appstore.service.deviceauth;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.appstore.controller.app.device.vo.*;
import cn.iocoder.yudao.module.appstore.dal.dataobject.deviceauth.DeviceAuthDO;
import cn.iocoder.yudao.module.appstore.dal.mysql.deviceauth.DeviceAuthMapper;
import cn.iocoder.yudao.module.appstore.enums.DeviceAuthStatusEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.appstore.enums.ErrorCodeConstants.*;

/**
 * 设备认证 Service 实现类
 *
 * @author appstore
 */
@Service
@Validated
public class DeviceAuthServiceImpl implements DeviceAuthService {

    private static final int DEVICE_CODE_EXPIRES_SECONDS = 15 * 60;
    private static final int POLL_INTERVAL_SECONDS = 5;
    private static final int ACCESS_TOKEN_EXPIRES_SECONDS = 60 * 60;
    private static final int REFRESH_TOKEN_EXPIRES_SECONDS = (int) TimeUnit.DAYS.toSeconds(30);

    @Resource
    private DeviceAuthMapper deviceAuthMapper;

    @Override
    public DeviceRegisterRespVO register(DeviceRegisterReqVO reqVO) {
        DeviceAuthDO record = deviceAuthMapper.selectByDeviceId(reqVO.getDeviceId());
        LocalDateTime now = LocalDateTime.now();
        String deviceCode = generateDeviceCode();
        if (record == null) {
            record = new DeviceAuthDO();
            record.setDeviceId(reqVO.getDeviceId());
            record.setDeviceModel(reqVO.getDeviceModel());
            record.setOsVersion(reqVO.getOsVersion());
            record.setAppVersion(reqVO.getAppVersion());
            record.setDeviceCode(deviceCode);
            record.setCodeExpiresTime(now.plusSeconds(DEVICE_CODE_EXPIRES_SECONDS));
            record.setPollInterval(POLL_INTERVAL_SECONDS);
            record.setStatus(DeviceAuthStatusEnum.AUTHORIZED.getStatus());
            record.setDeviceName(reqVO.getDeviceModel());
            record.setAuthorizedUserId(0L);
            clearToken(record);
            deviceAuthMapper.insert(record);
        } else {
            record.setDeviceModel(reqVO.getDeviceModel());
            record.setOsVersion(reqVO.getOsVersion());
            record.setAppVersion(reqVO.getAppVersion());
            record.setDeviceCode(deviceCode);
            record.setCodeExpiresTime(now.plusSeconds(DEVICE_CODE_EXPIRES_SECONDS));
            record.setPollInterval(POLL_INTERVAL_SECONDS);
            record.setStatus(DeviceAuthStatusEnum.AUTHORIZED.getStatus());
            record.setDeviceName(reqVO.getDeviceModel());
            record.setAuthorizedUserId(0L);
            clearToken(record);
            deviceAuthMapper.updateById(record);
        }
        return buildRegisterResp(record);
    }

    @Override
    public DeviceTokenRespVO pollToken(DeviceTokenReqVO reqVO) {
        DeviceAuthDO record = deviceAuthMapper.selectByDeviceCode(reqVO.getDeviceCode());
        if (record == null || !StrUtil.equals(record.getDeviceId(), reqVO.getDeviceId())) {
            throw exception(DEVICE_CODE_INVALID);
        }
        LocalDateTime now = LocalDateTime.now();

        record.setLastPollTime(now);
        if (ObjectUtil.notEqual(record.getStatus(), DeviceAuthStatusEnum.AUTHORIZED.getStatus())) {
            if (now.isAfter(record.getCodeExpiresTime())) {
                throw exception(DEVICE_CODE_EXPIRED);
            }
            deviceAuthMapper.updateById(record);
            throw exception(DEVICE_NOT_AUTHORIZED);
        }

        if (StrUtil.isBlank(record.getAccessToken()) || now.isAfter(record.getAccessTokenExpiresTime())) {
            issueToken(record, now);
        }
        record.setLastLoginTime(now);
        deviceAuthMapper.updateById(record);
        return buildTokenResp(record);
    }

    @Override
    public RefreshTokenRespVO refreshToken(RefreshTokenReqVO reqVO) {
        DeviceAuthDO record = deviceAuthMapper.selectByRefreshToken(reqVO.getRefreshToken());
        if (record == null) {
            throw exception(DEVICE_REFRESH_TOKEN_INVALID);
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(record.getRefreshTokenExpiresTime())) {
            throw exception(DEVICE_REFRESH_TOKEN_EXPIRED);
        }
        if (ObjectUtil.notEqual(record.getStatus(), DeviceAuthStatusEnum.AUTHORIZED.getStatus())) {
            throw exception(DEVICE_NOT_AUTHORIZED);
        }
        issueToken(record, now);
        record.setLastLoginTime(now);
        deviceAuthMapper.updateById(record);
        return buildRefreshResp(record);
    }

    @Override
    public void logout(String accessToken) {
        if (StrUtil.isBlank(accessToken)) {
            throw exception(DEVICE_ACCESS_TOKEN_INVALID);
        }
        DeviceAuthDO record = deviceAuthMapper.selectByAccessToken(accessToken);
        if (record == null) {
            throw exception(DEVICE_ACCESS_TOKEN_INVALID);
        }
        record.setStatus(DeviceAuthStatusEnum.LOGOUT.getStatus());
        clearToken(record);
        deviceAuthMapper.updateById(record);
    }

    private static void issueToken(DeviceAuthDO record, LocalDateTime now) {
        record.setAccessToken(IdUtil.fastSimpleUUID());
        record.setRefreshToken(IdUtil.fastSimpleUUID());
        record.setAccessTokenExpiresTime(now.plusSeconds(ACCESS_TOKEN_EXPIRES_SECONDS));
        record.setRefreshTokenExpiresTime(now.plusSeconds(REFRESH_TOKEN_EXPIRES_SECONDS));
    }

    private static void clearToken(DeviceAuthDO record) {
        record.setAccessToken(null);
        record.setRefreshToken(null);
        record.setAccessTokenExpiresTime(null);
        record.setRefreshTokenExpiresTime(null);
    }

    private static String generateDeviceCode() {
        return RandomUtil.randomStringUpper(4) + "-" + RandomUtil.randomStringUpper(4);
    }

    private static DeviceRegisterRespVO buildRegisterResp(DeviceAuthDO record) {
        DeviceRegisterRespVO respVO = new DeviceRegisterRespVO();
        respVO.setDeviceCode(record.getDeviceCode());
        respVO.setActivationUrl("https://example.com/activate?code=" + record.getDeviceCode());
        respVO.setExpiresIn(DEVICE_CODE_EXPIRES_SECONDS);
        respVO.setPollInterval(record.getPollInterval());
        return respVO;
    }

    private static DeviceTokenRespVO buildTokenResp(DeviceAuthDO record) {
        DeviceTokenRespVO respVO = new DeviceTokenRespVO();
        respVO.setAccessToken(record.getAccessToken());
        respVO.setRefreshToken(record.getRefreshToken());
        respVO.setExpiresIn(ACCESS_TOKEN_EXPIRES_SECONDS);
        respVO.setDeviceName(record.getDeviceName());
        return respVO;
    }

    private static RefreshTokenRespVO buildRefreshResp(DeviceAuthDO record) {
        RefreshTokenRespVO respVO = new RefreshTokenRespVO();
        respVO.setAccessToken(record.getAccessToken());
        respVO.setRefreshToken(record.getRefreshToken());
        respVO.setExpiresIn(ACCESS_TOKEN_EXPIRES_SECONDS);
        return respVO;
    }

}

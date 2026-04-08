package cn.iocoder.yudao.module.appstore.dal.mysql.deviceauth;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.appstore.dal.dataobject.deviceauth.DeviceAuthDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceAuthMapper extends BaseMapperX<DeviceAuthDO> {

    default DeviceAuthDO selectByDeviceId(String deviceId) {
        return selectOne(DeviceAuthDO::getDeviceId, deviceId);
    }

    default DeviceAuthDO selectByDeviceCode(String deviceCode) {
        return selectOne(DeviceAuthDO::getDeviceCode, deviceCode);
    }

    default DeviceAuthDO selectByRefreshToken(String refreshToken) {
        return selectOne(DeviceAuthDO::getRefreshToken, refreshToken);
    }

    default DeviceAuthDO selectByAccessToken(String accessToken) {
        return selectOne(DeviceAuthDO::getAccessToken, accessToken);
    }

}

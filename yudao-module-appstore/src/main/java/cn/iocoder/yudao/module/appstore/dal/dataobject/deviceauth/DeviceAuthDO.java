package cn.iocoder.yudao.module.appstore.dal.dataobject.deviceauth;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备认证 DO
 *
 * @author appstore
 */
@TableName("appstore_device_auth")
@KeySequence("appstore_device_auth_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceAuthDO extends BaseDO {

    @TableId
    private Long id;

    private String deviceId;
    private String deviceModel;
    private String osVersion;
    private String appVersion;

    private String deviceCode;
    private LocalDateTime codeExpiresTime;
    private Integer pollInterval;

    private Integer status;
    private Long authorizedUserId;
    private String deviceName;

    private String accessToken;
    private LocalDateTime accessTokenExpiresTime;
    private String refreshToken;
    private LocalDateTime refreshTokenExpiresTime;

    private LocalDateTime lastPollTime;
    private LocalDateTime lastLoginTime;

}

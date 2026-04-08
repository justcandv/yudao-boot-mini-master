package cn.iocoder.yudao.module.appstore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备认证状态枚举
 *
 * @author appstore
 */
@Getter
@AllArgsConstructor
public enum DeviceAuthStatusEnum {

    PENDING(0, "待授权"),
    AUTHORIZED(1, "已授权"),
    LOGOUT(2, "已登出");

    private final Integer status;
    private final String name;

}

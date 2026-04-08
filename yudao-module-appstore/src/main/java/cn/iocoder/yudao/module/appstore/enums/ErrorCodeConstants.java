package cn.iocoder.yudao.module.appstore.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * appstore 模块错误码枚举类
 *
 * appstore 系统，使用 1-015-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 应用分类 1-015-001-000 ==========
    ErrorCode CATEGORY_NOT_EXISTS = new ErrorCode(1_015_001_000, "应用分类不存在");
    ErrorCode CATEGORY_HAS_APP = new ErrorCode(1_015_001_001, "该分类下存在应用，无法删除");

    // ========== 应用信息 1-015-002-000 ==========
    ErrorCode APP_NOT_EXISTS = new ErrorCode(1_015_002_000, "应用不存在");
    ErrorCode APP_PACKAGE_NAME_DUPLICATE = new ErrorCode(1_015_002_001, "应用包名已存在");

    // ========== 应用版本 1-015-003-000 ==========
    ErrorCode APP_VERSION_NOT_EXISTS = new ErrorCode(1_015_003_000, "应用版本不存在");
    ErrorCode APP_VERSION_CODE_DUPLICATE = new ErrorCode(1_015_003_001, "该应用下版本号已存在");

    // ========== 设备认证 1-015-004-000 ==========
    ErrorCode DEVICE_AUTH_NOT_EXISTS = new ErrorCode(1_015_004_000, "设备认证记录不存在");
    ErrorCode DEVICE_CODE_INVALID = new ErrorCode(1_015_004_001, "设备码无效");
    ErrorCode DEVICE_CODE_EXPIRED = new ErrorCode(1_015_004_002, "设备码已过期");
    ErrorCode DEVICE_NOT_AUTHORIZED = new ErrorCode(1_015_004_003, "设备尚未完成授权");
    ErrorCode DEVICE_REFRESH_TOKEN_INVALID = new ErrorCode(1_015_004_004, "刷新令牌无效");
    ErrorCode DEVICE_REFRESH_TOKEN_EXPIRED = new ErrorCode(1_015_004_005, "刷新令牌已过期");
    ErrorCode DEVICE_ACCESS_TOKEN_INVALID = new ErrorCode(1_015_004_006, "访问令牌无效");
}

package cn.iocoder.yudao.module.appstore.dal.dataobject.app;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.appstore.enums.AppVersionStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用版本 DO
 *
 * @author appstore
 */
@TableName("appstore_app_version")
@KeySequence("appstore_app_version_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppVersionDO extends BaseDO {

    /**
     * 版本编号
     */
    @TableId
    private Long id;
    /**
     * 关联应用编号
     *
     * 关联 {@link AppDO#getId()}
     */
    private Long appId;
    /**
     * 版本号（如 1.2.0）
     */
    private String versionName;
    /**
     * 安卓 versionCode
     */
    private Integer versionCode;
    /**
     * APK 下载地址
     */
    private String apkUrl;
    /**
     * APK 文件大小（字节）
     */
    private Long apkSize;
    /**
     * 更新说明
     */
    private String updateDescription;
    /**
     * 状态
     *
     * 枚举 {@link AppVersionStatusEnum}
     */
    private Integer status;

}

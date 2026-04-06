package cn.iocoder.yudao.module.appstore.dal.dataobject.app;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.appstore.enums.AppStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用信息 DO
 *
 * @author appstore
 */
@TableName("appstore_app")
@KeySequence("appstore_app_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppDO extends BaseDO {

    /**
     * 应用编号
     */
    @TableId
    private Long id;
    /**
     * 所属分类编号
     */
    private Long categoryId;
    /**
     * 应用名称
     */
    private String name;
    /**
     * 安卓包名（唯一）
     */
    private String packageName;
    /**
     * 应用图标 URL
     */
    private String iconUrl;
    /**
     * 应用描述
     */
    private String description;
    /**
     * 截图 URL（JSON 数组）
     */
    private String screenshots;
    /**
     * 开发者名称
     */
    private String developer;
    /**
     * 状态
     *
     * 枚举 {@link AppStatusEnum}
     */
    private Integer status;
    /**
     * 累计下载次数
     */
    private Long downloadCount;
    /**
     * 最新版本编号
     *
     * 关联 {@link AppVersionDO#getId()}
     */
    private Long latestVersionId;

}

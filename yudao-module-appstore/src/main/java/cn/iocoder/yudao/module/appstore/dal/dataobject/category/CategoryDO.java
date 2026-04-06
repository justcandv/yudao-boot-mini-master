package cn.iocoder.yudao.module.appstore.dal.dataobject.category;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用分类 DO
 *
 * @author appstore
 */
@TableName("appstore_category")
@KeySequence("appstore_category_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryDO extends BaseDO {

    /**
     * 分类编号
     */
    @TableId
    private Long id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类图标 URL
     */
    private String iconUrl;
    /**
     * 排序权重
     */
    private Integer sort;
    /**
     * 状态（0 开启 1 关闭）
     *
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;

}

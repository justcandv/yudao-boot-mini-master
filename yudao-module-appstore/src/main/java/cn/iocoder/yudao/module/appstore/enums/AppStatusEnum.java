package cn.iocoder.yudao.module.appstore.enums;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 应用状态枚举
 *
 * @author appstore
 */
@Getter
@AllArgsConstructor
public enum AppStatusEnum implements ArrayValuable<Integer> {

    DRAFT(0, "草稿"),
    PUBLISHED(1, "已上架"),
    REMOVED(2, "已下架");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(AppStatusEnum::getStatus).toArray();

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return Arrays.stream(values()).map(AppStatusEnum::getStatus).toArray(Integer[]::new);
    }
}

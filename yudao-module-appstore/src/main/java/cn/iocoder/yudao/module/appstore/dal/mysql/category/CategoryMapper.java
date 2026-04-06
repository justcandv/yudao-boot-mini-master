package cn.iocoder.yudao.module.appstore.dal.mysql.category;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.appstore.controller.admin.category.vo.CategoryPageReqVO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.category.CategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapperX<CategoryDO> {

    default PageResult<CategoryDO> selectPage(CategoryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CategoryDO>()
                .likeIfPresent(CategoryDO::getName, reqVO.getName())
                .eqIfPresent(CategoryDO::getStatus, reqVO.getStatus())
                .orderByAsc(CategoryDO::getSort));
    }

    default List<CategoryDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<CategoryDO>()
                .eqIfPresent(CategoryDO::getStatus, status)
                .orderByAsc(CategoryDO::getSort));
    }

}

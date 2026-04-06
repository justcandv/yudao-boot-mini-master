package cn.iocoder.yudao.module.appstore.dal.mysql.app;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.appstore.controller.admin.app.vo.AppPageReqVO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.app.AppDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AppMapper extends BaseMapperX<AppDO> {

    default PageResult<AppDO> selectPage(AppPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AppDO>()
                .likeIfPresent(AppDO::getName, reqVO.getName())
                .likeIfPresent(AppDO::getPackageName, reqVO.getPackageName())
                .eqIfPresent(AppDO::getCategoryId, reqVO.getCategoryId())
                .eqIfPresent(AppDO::getStatus, reqVO.getStatus())
                .orderByDesc(AppDO::getId));
    }

    default AppDO selectByPackageName(String packageName) {
        return selectOne(AppDO::getPackageName, packageName);
    }

    default List<AppDO> selectListByCategoryIdAndStatus(Long categoryId, Integer status) {
        return selectList(new LambdaQueryWrapperX<AppDO>()
                .eqIfPresent(AppDO::getCategoryId, categoryId)
                .eqIfPresent(AppDO::getStatus, status)
                .orderByDesc(AppDO::getDownloadCount));
    }

    default List<AppDO> selectListByNameLikeAndStatus(String name, Integer status) {
        return selectList(new LambdaQueryWrapperX<AppDO>()
                .likeIfPresent(AppDO::getName, name)
                .eqIfPresent(AppDO::getStatus, status)
                .orderByDesc(AppDO::getDownloadCount));
    }

    default Long selectCountByCategoryId(Long categoryId) {
        return selectCount(AppDO::getCategoryId, categoryId);
    }

}

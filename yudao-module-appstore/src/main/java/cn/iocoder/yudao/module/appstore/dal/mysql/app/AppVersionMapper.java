package cn.iocoder.yudao.module.appstore.dal.mysql.app;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.appstore.controller.admin.appversion.vo.AppVersionPageReqVO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.app.AppVersionDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AppVersionMapper extends BaseMapperX<AppVersionDO> {

    default PageResult<AppVersionDO> selectPage(AppVersionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AppVersionDO>()
                .eqIfPresent(AppVersionDO::getAppId, reqVO.getAppId())
                .eqIfPresent(AppVersionDO::getStatus, reqVO.getStatus())
                .orderByDesc(AppVersionDO::getVersionCode));
    }

    default AppVersionDO selectByAppIdAndVersionCode(Long appId, Integer versionCode) {
        return selectOne(new LambdaQueryWrapperX<AppVersionDO>()
                .eq(AppVersionDO::getAppId, appId)
                .eq(AppVersionDO::getVersionCode, versionCode));
    }

    default List<AppVersionDO> selectListByAppId(Long appId) {
        return selectList(new LambdaQueryWrapperX<AppVersionDO>()
                .eq(AppVersionDO::getAppId, appId)
                .orderByDesc(AppVersionDO::getVersionCode));
    }

}

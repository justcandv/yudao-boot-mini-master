package cn.iocoder.yudao.module.appstore.service.appversion;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.appstore.controller.admin.appversion.vo.AppVersionPageReqVO;
import cn.iocoder.yudao.module.appstore.controller.admin.appversion.vo.AppVersionSaveReqVO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.app.AppVersionDO;

/**
 * 应用版本 Service 接口
 *
 * @author appstore
 */
public interface AppVersionService {

    /**
     * 创建应用版本
     *
     * @param createReqVO 创建信息
     * @return 版本编号
     */
    Long createAppVersion(AppVersionSaveReqVO createReqVO);

    /**
     * 更新应用版本
     *
     * @param updateReqVO 更新信息
     */
    void updateAppVersion(AppVersionSaveReqVO updateReqVO);

    /**
     * 删除应用版本
     *
     * @param id 版本编号
     */
    void deleteAppVersion(Long id);

    /**
     * 获得应用版本
     *
     * @param id 版本编号
     * @return 应用版本
     */
    AppVersionDO getAppVersion(Long id);

    /**
     * 获得应用版本分页
     *
     * @param pageReqVO 分页查询
     * @return 应用版本分页
     */
    PageResult<AppVersionDO> getAppVersionPage(AppVersionPageReqVO pageReqVO);

    /**
     * 获取指定应用的最新版本
     *
     * @param appId 应用编号
     * @return 最新版本
     */
    AppVersionDO getLatestVersion(Long appId);

}

package cn.iocoder.yudao.module.appstore.service.app;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.appstore.controller.admin.app.vo.AppPageReqVO;
import cn.iocoder.yudao.module.appstore.controller.admin.app.vo.AppSaveReqVO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.app.AppDO;

import java.util.List;

/**
 * 应用信息 Service 接口
 *
 * @author appstore
 */
public interface AppService {

    /**
     * 创建应用
     *
     * @param createReqVO 创建信息
     * @return 应用编号
     */
    Long createApp(AppSaveReqVO createReqVO);

    /**
     * 更新应用
     *
     * @param updateReqVO 更新信息
     */
    void updateApp(AppSaveReqVO updateReqVO);

    /**
     * 删除应用
     *
     * @param id 应用编号
     */
    void deleteApp(Long id);

    /**
     * 获得应用
     *
     * @param id 应用编号
     * @return 应用信息
     */
    AppDO getApp(Long id);

    /**
     * 获得应用分页
     *
     * @param pageReqVO 分页查询
     * @return 应用分页
     */
    PageResult<AppDO> getAppPage(AppPageReqVO pageReqVO);

    /**
     * 更新应用状态（上架 / 下架）
     *
     * @param id     应用编号
     * @param status 目标状态
     */
    void updateAppStatus(Long id, Integer status);

    /**
     * 按分类获取已上架应用列表（TV 端）
     *
     * @param categoryId 分类编号（可选）
     * @return 应用列表
     */
    List<AppDO> getPublishedAppList(Long categoryId);

    /**
     * 按关键词搜索已上架应用（TV 端）
     *
     * @param keyword 搜索关键词
     * @return 应用列表
     */
    List<AppDO> searchPublishedApp(String keyword);

    /**
     * 递增下载次数
     *
     * @param id 应用编号
     */
    void incrementDownloadCount(Long id);

}

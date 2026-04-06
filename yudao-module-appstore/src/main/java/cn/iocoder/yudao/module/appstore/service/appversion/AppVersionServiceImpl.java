package cn.iocoder.yudao.module.appstore.service.appversion;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.appstore.controller.admin.appversion.vo.AppVersionSaveReqVO;
import cn.iocoder.yudao.module.appstore.controller.admin.appversion.vo.AppVersionPageReqVO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.app.AppDO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.app.AppVersionDO;
import cn.iocoder.yudao.module.appstore.dal.mysql.app.AppMapper;
import cn.iocoder.yudao.module.appstore.dal.mysql.app.AppVersionMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.appstore.enums.ErrorCodeConstants.*;

/**
 * 应用版本 Service 实现类
 *
 * @author appstore
 */
@Service
@Validated
public class AppVersionServiceImpl implements AppVersionService {

    @Resource
    private AppVersionMapper appVersionMapper;

    @Resource
    private AppMapper appMapper;

    @Override
    public Long createAppVersion(AppVersionSaveReqVO createReqVO) {
        // 校验应用存在
        if (appMapper.selectById(createReqVO.getAppId()) == null) {
            throw exception(APP_NOT_EXISTS);
        }
        // 校验 versionCode 唯一
        if (appVersionMapper.selectByAppIdAndVersionCode(createReqVO.getAppId(), createReqVO.getVersionCode()) != null) {
            throw exception(APP_VERSION_CODE_DUPLICATE);
        }
        // 插入版本
        AppVersionDO version = BeanUtils.toBean(createReqVO, AppVersionDO.class);
        appVersionMapper.insert(version);

        // 更新应用的最新版本
        updateAppLatestVersion(createReqVO.getAppId());
        return version.getId();
    }

    @Override
    public void updateAppVersion(AppVersionSaveReqVO updateReqVO) {
        AppVersionDO existVersion = validateAppVersionExists(updateReqVO.getId());
        // 校验 versionCode 唯一（排除自身）
        AppVersionDO sameCodeVersion = appVersionMapper.selectByAppIdAndVersionCode(
                existVersion.getAppId(), updateReqVO.getVersionCode());
        if (sameCodeVersion != null && !sameCodeVersion.getId().equals(updateReqVO.getId())) {
            throw exception(APP_VERSION_CODE_DUPLICATE);
        }
        AppVersionDO updateObj = BeanUtils.toBean(updateReqVO, AppVersionDO.class);
        appVersionMapper.updateById(updateObj);

        // 更新应用的最新版本
        updateAppLatestVersion(existVersion.getAppId());
    }

    @Override
    public void deleteAppVersion(Long id) {
        AppVersionDO version = validateAppVersionExists(id);
        appVersionMapper.deleteById(id);
        // 更新应用的最新版本
        updateAppLatestVersion(version.getAppId());
    }

    @Override
    public AppVersionDO getAppVersion(Long id) {
        return appVersionMapper.selectById(id);
    }

    @Override
    public PageResult<AppVersionDO> getAppVersionPage(AppVersionPageReqVO pageReqVO) {
        return appVersionMapper.selectPage(pageReqVO);
    }

    @Override
    public AppVersionDO getLatestVersion(Long appId) {
        List<AppVersionDO> versions = appVersionMapper.selectListByAppId(appId);
        return CollUtil.isEmpty(versions) ? null : versions.get(0);
    }

    private AppVersionDO validateAppVersionExists(Long id) {
        AppVersionDO version = appVersionMapper.selectById(id);
        if (version == null) {
            throw exception(APP_VERSION_NOT_EXISTS);
        }
        return version;
    }

    /**
     * 更新应用表的 latestVersionId（取 versionCode 最大的版本）
     */
    private void updateAppLatestVersion(Long appId) {
        List<AppVersionDO> versions = appVersionMapper.selectListByAppId(appId);
        Long latestVersionId = CollUtil.isEmpty(versions) ? null : versions.get(0).getId();
        AppDO updateApp = new AppDO();
        updateApp.setId(appId);
        updateApp.setLatestVersionId(latestVersionId);
        appMapper.updateById(updateApp);
    }

}

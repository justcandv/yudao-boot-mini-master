package cn.iocoder.yudao.module.appstore.service.app;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.appstore.controller.admin.app.vo.AppPageReqVO;
import cn.iocoder.yudao.module.appstore.controller.admin.app.vo.AppSaveReqVO;
import cn.iocoder.yudao.module.appstore.dal.dataobject.app.AppDO;
import cn.iocoder.yudao.module.appstore.dal.mysql.app.AppMapper;
import cn.iocoder.yudao.module.appstore.enums.AppStatusEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.appstore.enums.ErrorCodeConstants.APP_NOT_EXISTS;
import static cn.iocoder.yudao.module.appstore.enums.ErrorCodeConstants.APP_PACKAGE_NAME_DUPLICATE;

/**
 * 应用信息 Service 实现类
 *
 * @author appstore
 */
@Service
@Validated
public class AppServiceImpl implements AppService {

    @Resource
    private AppMapper appMapper;

    @Override
    public Long createApp(AppSaveReqVO createReqVO) {
        validatePackageNameUnique(null, createReqVO.getPackageName());
        AppDO app = BeanUtils.toBean(createReqVO, AppDO.class);
        app.setDownloadCount(0L);
        app.setStatus(AppStatusEnum.DRAFT.getStatus());
        appMapper.insert(app);
        return app.getId();
    }

    @Override
    public void updateApp(AppSaveReqVO updateReqVO) {
        validateAppExists(updateReqVO.getId());
        validatePackageNameUnique(updateReqVO.getId(), updateReqVO.getPackageName());
        AppDO updateObj = BeanUtils.toBean(updateReqVO, AppDO.class);
        appMapper.updateById(updateObj);
    }

    @Override
    public void deleteApp(Long id) {
        validateAppExists(id);
        appMapper.deleteById(id);
    }

    @Override
    public AppDO getApp(Long id) {
        return appMapper.selectById(id);
    }

    @Override
    public PageResult<AppDO> getAppPage(AppPageReqVO pageReqVO) {
        return appMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateAppStatus(Long id, Integer status) {
        validateAppExists(id);
        AppDO updateObj = new AppDO();
        updateObj.setId(id);
        updateObj.setStatus(status);
        appMapper.updateById(updateObj);
    }

    @Override
    public List<AppDO> getPublishedAppList(Long categoryId) {
        return appMapper.selectListByCategoryIdAndStatus(categoryId, AppStatusEnum.PUBLISHED.getStatus());
    }

    @Override
    public List<AppDO> searchPublishedApp(String keyword) {
        return appMapper.selectListByNameLikeAndStatus(keyword, AppStatusEnum.PUBLISHED.getStatus());
    }

    @Override
    public void incrementDownloadCount(Long id) {
        AppDO app = validateAppExists(id);
        AppDO updateObj = new AppDO();
        updateObj.setId(id);
        updateObj.setDownloadCount(app.getDownloadCount() + 1);
        appMapper.updateById(updateObj);
    }

    private AppDO validateAppExists(Long id) {
        AppDO app = appMapper.selectById(id);
        if (app == null) {
            throw exception(APP_NOT_EXISTS);
        }
        return app;
    }

    private void validatePackageNameUnique(Long id, String packageName) {
        AppDO app = appMapper.selectByPackageName(packageName);
        if (app == null) {
            return;
        }
        if (id == null || ObjectUtil.notEqual(app.getId(), id)) {
            throw exception(APP_PACKAGE_NAME_DUPLICATE);
        }
    }

}

package zhe84300975.baseframe.core;


import zhe84300975.baseframe.moudles.structure.BaseStructureModel;

/**
 * Created by zhaowencong on 16/8/24.
 * Describe: 业务
 */
public interface BaseIBiz {

    void initUI(BaseStructureModel BaseView);

    /**
     * 清空
     */
    void detach();
}

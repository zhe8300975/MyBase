package zhe84300975.baseframe.view.common;

import android.support.annotation.LayoutRes;

/**
 * Created by zhaowencong on 16/8/17.
 * Describe: 公共视图接口
 */
public interface BaseIViewCommon {
    /**
     * 进度布局
     *
     * @return
     */
    @LayoutRes int layoutLoading();

    /**
     * 空布局
     *
     * @return
     */
    @LayoutRes int layoutEmpty();

    /**
     * 网络业务错误
     *
     * @return
     */
    @LayoutRes int layoutBizError();

    /**
     * 网络错误
     *
     * @return
     */
    @LayoutRes int layoutHttpError();
}

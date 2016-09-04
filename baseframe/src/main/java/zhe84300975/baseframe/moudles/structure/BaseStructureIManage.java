package zhe84300975.baseframe.moudles.structure;

import android.support.v4.app.FragmentManager;

import java.util.List;

import zhe84300975.baseframe.core.BaseIBiz;
import zhe84300975.baseframe.core.BaseICommonBiz;
import zhe84300975.baseframe.display.BaseIDisplay;
import zhe84300975.baseframe.view.BaseActivity;

/**
 * Created by zhaowencong on 16/8/18.
 * Describe: 结构管理器
 */
public interface BaseStructureIManage {

    void attach(BaseStructureModel view);

    void detach(BaseStructureModel view);

    <D extends BaseIDisplay> D display(Class<D> displayClazz);

    <B extends BaseIBiz> B biz(Class<B> bizClazz);

    <B extends BaseIBiz> boolean isExist(Class<B> bizClazz);

    <B extends BaseICommonBiz> B common(Class<B> service);

    <B extends BaseIBiz> List<B> bizList(Class<B> service);

    <H> H http(Class<H> httpClazz);

    <P> P impl(Class<P> implClazz);

    <T> T createMainLooper(final Class<T> service, Object ui);

    <U> U createNullService(final Class<U> service);
    /**
     * 拦截back 交给 fragment onKeyBack
     *
     * @param keyCode
     * @param fragmentManager
     * @param baseActivity
     * @return
     */
    boolean onKeyBack(int keyCode, FragmentManager fragmentManager, BaseActivity baseActivity);

    /**
     * 打印堆栈内容
     *
     * @param fragmentManager
     */
    void printBackStackEntry(FragmentManager fragmentManager);

}

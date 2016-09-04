package zhe84300975.baseframe.core;

import zhe84300975.baseframe.BaseHelper;
import zhe84300975.baseframe.display.BaseIDisplay;

/**
 * Created by zhaowencong on 16/8/24.
 * Describe: 公共接口
 */
public class BaseCommonBiz implements BaseICommonBiz{

    protected <H> H http(Class<H> hClass) {
        return BaseHelper.http(hClass);
    }

    protected <I> I impl(Class<I> inter) {
        return BaseHelper.impl(inter);
    }

    protected <D extends BaseIDisplay> D display(Class<D> eClass) {
        return BaseHelper.display(eClass);
    }

    public <C extends BaseIBiz> C biz(Class<C> service) {
        return BaseHelper.biz(service);
    }
}

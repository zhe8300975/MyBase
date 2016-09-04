package zhe84300975.baseframe.core;

import zhe84300975.baseframe.BaseHelper;
import zhe84300975.baseframe.display.BaseIDisplay;
import zhe84300975.baseframe.moudles.structure.BaseStructureModel;
import zhe84300975.baseframe.utils.BaseAppUtils;

/**
 * Created by zhaowencong on 16/8/24.
 * Describe:
 */
public abstract class BaseBiz<U>  implements BaseIBiz{

    private U					u;

    private Class				ui;

    private BaseStructureModel mBaseStructureModel;

    protected <H> H http(Class<H> hClass) {
        if (mBaseStructureModel == null || mBaseStructureModel.getView() == null) {
            return BaseHelper.http(hClass);
        }
        return mBaseStructureModel.http(hClass);
    }

    protected <I> I impl(Class<I> inter) {
        if (mBaseStructureModel == null || mBaseStructureModel.getView() == null) {
            return BaseHelper.impl(inter);
        }
        return mBaseStructureModel.impl(inter);
    }

    protected <D extends BaseIDisplay> D display(Class<D> eClass) {
        if (mBaseStructureModel == null || mBaseStructureModel.getView() == null) {
            return BaseHelper.display(eClass);
        }
        return mBaseStructureModel.display(eClass);
    }

    public <C extends BaseIBiz> C biz(Class<C> service) {
        if (mBaseStructureModel != null && mBaseStructureModel.isSupterClass(service)) {
            if (mBaseStructureModel.getBaseProxy() == null || mBaseStructureModel.getBaseProxy().proxy == null) {
                return BaseHelper.structureHelper().createNullService(service);
            }
            return (C) mBaseStructureModel.getBaseProxy().proxy;
        } else if (mBaseStructureModel != null && service.equals(mBaseStructureModel.getService())) {
            if (mBaseStructureModel.getBaseProxy() == null || mBaseStructureModel.getBaseProxy().proxy == null) {
                return BaseHelper.structureHelper().createNullService(service);
            }
            return (C) mBaseStructureModel.getBaseProxy().proxy;
        } else {
            return BaseHelper.biz(service);
        }
    }

    /**
     * View层 回调引用
     *
     * @return
     */
    protected U ui() {
        if (u == null) {
            Class ui = BaseAppUtils.getSuperClassGenricType(this.getClass(), 0);
            return (U) BaseHelper.structureHelper().createNullService(ui);
        }
        return u;
    }

    /**
     * View层 回调
     *
     * @param clazz
     * @param <V>
     * @return
     */
    protected <V> V ui(Class<V> clazz) {
        if (clazz.equals(ui)) {
            return (V) ui();
        } else {
            return BaseHelper.structureHelper().createNullService(clazz);
        }
    }

    /**
     * View层 是否存在
     *
     * @return
     */
    public boolean isUI() {
        return u != null;
    }

    @Override public void initUI(BaseStructureModel baseStructureModel) {
        this.mBaseStructureModel = baseStructureModel;
        ui = BaseAppUtils.getSuperClassGenricType(this.getClass(), 0);
        u = (U) BaseHelper.structureHelper().createMainLooper(ui, baseStructureModel.getView());
    }

    @Override public void detach() {
        u = null;
        ui = null;
        mBaseStructureModel = null;
    }


}

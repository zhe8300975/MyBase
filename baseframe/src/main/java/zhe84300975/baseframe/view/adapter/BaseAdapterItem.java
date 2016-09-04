package zhe84300975.baseframe.view.adapter;

import android.view.View;

import zhe84300975.baseframe.display.BaseIDisplay;
import zhe84300975.baseframe.utils.BaseCheckUtils;
import zhe84300975.baseframe.view.BaseActivity;
import zhe84300975.baseframe.view.BaseDialogFragment;
import zhe84300975.baseframe.view.BaseFragment;
import zhe84300975.baseframe.view.BaseView;

/**
 * Created by zhaowencong on 16/8/25.
 * Describe: 适配器
 */
public abstract class BaseAdapterItem <T> implements Cloneable{
    private BaseView mBaseiew;

    void setBaseView(BaseView baseView) {
        this.mBaseiew = baseView;
    }

    /**
     * 设置布局
     *
     * @return 布局ID
     */
    public abstract int getItemLayout();

    /**
     * 初始化控件
     *
     * @param contentView
     *            ItemView
     */
    public abstract void init(View contentView);

    /**
     * 绑定数据
     *
     * @param t
     *            数据类型泛型
     * @param position
     *            下标
     * @param count
     *            数量
     */
    public abstract void bindData(T t, int position, int count);


    public <V extends BaseFragment> V fragment() {
        return mBaseiew.fragment();
    }

    public <A extends BaseActivity> A activity() {
        return mBaseiew.activity();
    }

    public <D extends BaseDialogFragment> D dialogFragment() {
        return mBaseiew.dialogFragment();
    }

    public BaseView getUI() {
        return mBaseiew;
    }

    /**
     * 获取调度
     *
     * @param e
     * @param <E>
     * @return
     */
    protected <E extends BaseIDisplay> E display(Class<E> e) {
        return mBaseiew.display(e);
    }

    /**
     * 获取fragment
     *
     * @param clazz
     * @return
     */
    public <T> T findFragment(Class<T> clazz) {
        BaseCheckUtils.checkNotNull(clazz, "class不能为空");
        return (T) mBaseiew.manager().findFragmentByTag(clazz.getSimpleName());
    }

    /**
     * 克隆
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override protected final Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

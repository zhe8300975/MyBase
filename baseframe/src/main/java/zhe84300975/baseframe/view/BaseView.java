package zhe84300975.baseframe.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import zhe84300975.baseframe.BaseHelper;
import zhe84300975.baseframe.core.BaseBiz;
import zhe84300975.baseframe.core.BaseIBiz;
import zhe84300975.baseframe.display.BaseIDisplay;
import zhe84300975.baseframe.utils.BaseCheckUtils;

/**
 * Created by zhaowencong on 16/8/25.
 * Describe: UI层引用
 */
public class BaseView {

    /**
     * 常量
     */
    public static final int		STATE_ACTIVITY			= 99999;

    public static final int		STATE_FRAGMENT			= 88888;

    public static final int		STATE_DIALOGFRAGMENT	= 77777;

    public static final int		STATE_NOTVIEW			= 66666;

    /** 类型 **/
    private int					mState;

    private BaseActivity mBaseActivity;

    private Context mContext;

    private BaseFragment mBaseFragment;

    private BaseDialogFragment mBaseDialogFragment;

    private FragmentManager mFragmentManager;

    /** 初始化 **/
    public void initUI(BaseActivity mBaseActivity) {
        this.mState = STATE_ACTIVITY;
        this.mBaseActivity = mBaseActivity;
        this.mContext = mBaseActivity;
    }

    public void initUI(BaseFragment mBaseFragment) {
        initUI((BaseActivity) mBaseFragment.getActivity());
        this.mState = STATE_FRAGMENT;
        this.mBaseFragment = mBaseFragment;
    }

    public void initUI(BaseDialogFragment mBaseDialogFragment) {
        initUI((BaseActivity) mBaseDialogFragment.getActivity());
        this.mState = STATE_DIALOGFRAGMENT;
        this.mBaseDialogFragment = mBaseDialogFragment;
    }

    public void initUI(Context context) {
        this.mContext = context;
        this.mState = STATE_NOTVIEW;
    }

    public Context context() {
        return mContext;
    }

    public <A extends BaseActivity> A activity() {
        return (A) mBaseActivity;
    }

    public FragmentManager manager() {
        return BaseHelper.screenHelper().getCurrentActivity().getSupportFragmentManager();
    }



    public Object getView() {
        Object obj = null;
        switch (mState) {
            case STATE_ACTIVITY:
                obj = mBaseActivity;
                break;
            case STATE_FRAGMENT:
                obj = mBaseFragment;
                break;
            case STATE_DIALOGFRAGMENT:
                obj = mBaseDialogFragment;
                break;
        }
        return obj;
    }

    public int getState() {
        return mState;
    }

    public <F extends BaseFragment> F fragment() {
        return (F) mBaseFragment;
    }

    public <D extends BaseDialogFragment> D dialogFragment() {
        return (D) mBaseDialogFragment;
    }

    public <B extends BaseIBiz> B biz() {
        B b = null;
        switch (mState) {
            case STATE_ACTIVITY:
                b = (B) mBaseActivity.biz();
                break;
            case STATE_FRAGMENT:
                b = (B) mBaseFragment.biz();
                break;
            case STATE_DIALOGFRAGMENT:
                b = (B) mBaseDialogFragment.biz();
                break;
        }
        return b;
    }

    public <B extends BaseIBiz> B biz(Class<B> service) {
        B b = null;
        switch (mState) {
            case STATE_ACTIVITY:
                b = (B) mBaseActivity.biz(service);
                break;
            case STATE_FRAGMENT:
                b = (B) mBaseFragment.biz(service);
                break;
            case STATE_DIALOGFRAGMENT:
                b = (B) mBaseDialogFragment.biz(service);
                break;
        }
        return b;
    }

    public <E extends BaseIDisplay> E display(Class<E> display) {
        E e = null;
        switch (mState) {
            case STATE_ACTIVITY:
                e = (E) mBaseActivity.display(display);
                break;
            case STATE_FRAGMENT:
                e = (E) mBaseFragment.display(display);
                break;
            case STATE_DIALOGFRAGMENT:
                e = (E) mBaseDialogFragment.display(display);
                break;
        }
        return e;
    }

    public Toolbar toolbar(int... types) {
        int type = mState;
        if (types.length > 0) {
            type = types[0];
        }
        Toolbar toolbar = null;
        switch (type) {
            case STATE_ACTIVITY:
                toolbar = mBaseActivity.toolbar();
                break;
            case STATE_FRAGMENT:
                toolbar = mBaseFragment.toolbar();
                toolbar = toolbar == null ? mBaseActivity.toolbar() : toolbar;
                break;
            case STATE_DIALOGFRAGMENT:
                toolbar = mBaseDialogFragment.toolbar();
                toolbar = toolbar == null ? mBaseActivity.toolbar() : toolbar;
                break;
        }

        BaseCheckUtils.checkNotNull(toolbar, "标题栏没有打开，无法调用");
        return toolbar;
    }

    /**
     * 消除引用
     */
    public void detach() {
        this.mState = 0;
        this.mBaseActivity = null;
        this.mBaseFragment = null;
        this.mBaseDialogFragment = null;
        this.mContext = null;
        this.mFragmentManager = null;
    }

}

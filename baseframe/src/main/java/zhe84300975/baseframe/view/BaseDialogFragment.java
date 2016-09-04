package zhe84300975.baseframe.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zhe84300975.baseframe.BaseHelper;
import zhe84300975.baseframe.core.BaseIBiz;
import zhe84300975.baseframe.display.BaseIDisplay;
import zhe84300975.baseframe.moudles.structure.BaseStructureModel;
import zhe84300975.baseframe.utils.BaseAppUtils;
import zhe84300975.baseframe.utils.BaseCheckUtils;
import zhe84300975.baseframe.utils.BaseKeyboardUtils;
import zhe84300975.baseframe.view.adapter.BaseListAdapter;
import zhe84300975.baseframe.view.adapter.recycleview.BaseRVAdapter;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhaowencong on 16/8/25.
 * Describe: dialog
 */
public abstract class BaseDialogFragment <B extends BaseIBiz> extends DialogFragment implements BaseIDialogFragment, DialogInterface.OnKeyListener {

    private boolean				mTargetActivity;

    /** 请求编码 **/
    protected int				mRequestCode		= 2013 << 5;

    /** 请求默认值 **/
    public final static String	ARG_REQUEST_CODE	= "Base_request_code";

    /** View层编辑器 **/
    private BaseBuilder mBaseBuilder;

    BaseStructureModel mBaseStructureModel;

    private Unbinder unbinder;

    /**
     * 定制
     *
     * @param initialBaseBuilder
     * @return
     **/
    protected abstract BaseBuilder build(BaseBuilder initialBaseBuilder);

    /**
     * 数据
     *
     * @param savedInstanceState
     */
    protected void createData(Bundle savedInstanceState) {

    }

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     *            数据
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 自定义样式
     *
     * @return
     */
    protected abstract int getBaseStyle();

    /**
     * 是否可取消
     *
     * @return
     */
    protected boolean isCancel() {
        return false;
    }

    protected void setDialogCancel(boolean flg) {
        getDialog().setCanceledOnTouchOutside(flg);
    }

    protected boolean isFull() {
        return false;
    }

    /**
     * 是否设置目标活动
     *
     * @return
     */
    public boolean isTargetActivity() {
        return mTargetActivity;
    }

    /**
     * 创建Dialog
     *
     * @param savedInstanceState
     * @return
     */
    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 创建对话框
        Dialog dialog = new Dialog(getActivity(), getBaseStyle());
        return dialog;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 打开开关触发菜单项 **/
        setHasOptionsMenu(true);
        // 获取指定碎片
        final Fragment targetFragment = getTargetFragment();
        // 如果有指定碎片 从指定碎片里获取请求码，反之既然
        if (targetFragment != null) {
            mRequestCode = getTargetRequestCode();
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** 初始化结构 **/
        mBaseStructureModel = new BaseStructureModel(this);
        BaseHelper.structureHelper().attach(mBaseStructureModel);
        /** 初始化视图 **/
        mBaseBuilder = new BaseBuilder(this, inflater);
        View view = build(mBaseBuilder).create();
        /** 初始化所有组建 **/
        unbinder = ButterKnife.bind(this, view);
        // 获取参数-设置是否可取消
        setDialogCancel(isCancel());
        getDialog().setOnKeyListener(this);
        return view;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFull()) {
            Window window = getDialog().getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        createData(savedInstanceState);
        initData(getArguments());
    }

    @Override public void onResume() {
        super.onResume();
        BaseHelper.structureHelper().printBackStackEntry(getFragmentManager());
    }

    @Override public void onPause() {
        super.onPause();
        // 恢复初始化
        listRefreshing(false);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }

    @Override public void onDestroy() {
        super.onDestroy();
        detach();
        /** 移除builder **/
        mBaseBuilder.detach();
        mBaseBuilder = null;

        BaseHelper.structureHelper().detach(mBaseStructureModel);
        /** 清空注解view **/
        unbinder.unbind();
        /** 关闭键盘 **/
        BaseKeyboardUtils.hideSoftInput(getActivity());
        // 销毁
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
    }
    /**
     * 清空
     */
    protected void detach() {}

    /**
     * 设置输入法
     *
     * @param mode
     */
    public void setSoftInputMode(int mode) {
        getActivity().getWindow().setSoftInputMode(mode);
    }

    public <D extends BaseIDisplay> D display(Class<D> eClass) {
        if (mBaseStructureModel == null || mBaseStructureModel.getView() == null) {
            return BaseHelper.display(eClass);
        }
        return mBaseStructureModel.display(eClass);
    }

    public B biz() {
        if (mBaseStructureModel == null || mBaseStructureModel.getBaseProxy() == null || mBaseStructureModel.getBaseProxy().proxy == null) {
            Class service = BaseAppUtils.getSuperClassGenricType(getClass(), 0);
            return (B) BaseHelper.structureHelper().createNullService(service);
        }
        return (B) mBaseStructureModel.getBaseProxy().proxy;
    }

    public <C extends BaseIBiz> C biz(Class<C> service) {
        if (mBaseStructureModel != null && service.equals(mBaseStructureModel.getService())) {
            if (mBaseStructureModel == null || mBaseStructureModel.getBaseProxy() == null || mBaseStructureModel.getBaseProxy().proxy == null) {
                return BaseHelper.structureHelper().createNullService(service);
            }
            return (C) mBaseStructureModel.getBaseProxy().proxy;
        }
        return BaseHelper.biz(service);
    }

    /**
     * 创建menu
     *
     * @param menu
     * @return
     */
    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mBaseBuilder.getToolbarMenuId() > 0) {
            menu.clear();
            this.getActivity().getMenuInflater().inflate(mBaseBuilder.getToolbarMenuId(), menu);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseHelper.methodsProxy().activityInterceptor().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 获取fragment
     *
     * @param clazz
     * @return
     */
    public <T> T findFragment(Class<T> clazz) {
        BaseCheckUtils.checkNotNull(clazz, "class不能为空");
        return (T) getFragmentManager().findFragmentByTag(clazz.getName());
    }

    /********************** Actionbar业务代码 *********************/

    protected void showContent() {
        if (mBaseBuilder != null) {
            mBaseBuilder.layoutContent();
        }
    }

    protected void showLoading() {
        if (mBaseBuilder != null) {
            mBaseBuilder.layoutLoading();
        }
    }

    protected void showBizError() {
        if (mBaseBuilder != null) {
            mBaseBuilder.layoutBizError();
        }
    }

    protected void showEmpty() {
        if (mBaseBuilder != null) {
            mBaseBuilder.layoutEmpty();
        }
    }

    protected void showHttpError() {
        if (mBaseBuilder != null) {
            mBaseBuilder.layoutHttpError();
        }
    }

    /********************** Actionbar业务代码 *********************/
    public Toolbar toolbar() {
        return mBaseBuilder == null ? null : mBaseBuilder.getToolbar();
    }

    /********************** RecyclerView业务代码 *********************/

    protected BaseRVAdapter recyclerAdapter() {
        return mBaseBuilder == null ? null : mBaseBuilder.getBaseRVAdapterItem2();
    }

    protected RecyclerView.LayoutManager recyclerLayoutManager() {
        return mBaseBuilder == null ? null : mBaseBuilder.getLayoutManager();
    }

    protected RecyclerView recyclerView() {
        return mBaseBuilder == null ? null : mBaseBuilder.getRecyclerView();
    }

    /********************** ListView业务代码 *********************/

    protected void addListHeader() {
        if (mBaseBuilder != null) {
            mBaseBuilder.addListHeader();
        }
    }

    protected void addListFooter() {
        if (mBaseBuilder != null) {
            mBaseBuilder.addListFooter();
        }
    }

    protected void removeListHeader() {
        if (mBaseBuilder != null) {
            mBaseBuilder.removeListHeader();
        }
    }

    protected void removeListFooter() {
        if (mBaseBuilder != null) {
            mBaseBuilder.removeListFooter();
        }

    }

    protected void listRefreshing(boolean bool) {
        if (mBaseBuilder != null) {
            mBaseBuilder.listRefreshing(bool);
        }
    }

    protected void listLoadMoreOpen() {
        if (mBaseBuilder != null) {
            mBaseBuilder.loadMoreOpen();
        }
    }

    protected BaseListAdapter adapter() {
        return mBaseBuilder == null ? null : mBaseBuilder.getAdapter();
    }

    protected ListView listView() {
        return mBaseBuilder == null ? null : mBaseBuilder.getListView();
    }

    /********************** View业务代码 *********************/

    public BaseView baseView() {
        return mBaseBuilder == null ? null : mBaseBuilder.getBaseView();
    }

    /**
     * 可见
     */
    protected void onVisible() {}

    /**
     * 不可见
     */
    protected void onInvisible() {}

    /**
     * 返回键
     */
    public boolean onKeyBack() {
        getActivity().onBackPressed();
        return true;
    }

    /********************** Dialog业务代码 *********************/
    /**
     * 获取某种类型的所有侦听器
     */
    protected <T> List<T> getDialogListeners(Class<T> listenerInterface) {
        final Fragment targetFragment = getTargetFragment();
        List<T> listeners = new ArrayList<>(2);
        if (targetFragment != null && listenerInterface.isAssignableFrom(targetFragment.getClass())) {
            listeners.add((T) targetFragment);
        }
        if (getActivity() != null && listenerInterface.isAssignableFrom(getActivity().getClass())) {
            listeners.add((T) getActivity());
        }
        return Collections.unmodifiableList(listeners);
    }

    /**
     * 取消
     *
     * @param dialog
     */
    @Override public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        for (IDialogCancelListener listener : getCancelListeners()) {
            listener.onCancelled(mRequestCode);
        }
    }

    /**
     * 获取取消的所有事件
     *
     * @return
     */
    protected List<IDialogCancelListener> getCancelListeners() {
        return getDialogListeners(IDialogCancelListener.class);
    }

    /**
     * 显示碎片
     *
     * @return
     */
    @Override public DialogFragment show(FragmentManager fragmentManager) {
        show(fragmentManager, this.getClass().getSimpleName());
        return this;
    }

    @Override public DialogFragment show(FragmentManager fragmentManager, int mRequestCode) {
        this.mRequestCode = mRequestCode;
        show(fragmentManager, this.getClass().getSimpleName());
        return this;
    }

    @Override public DialogFragment show(FragmentManager fragmentManager, Fragment mTargetFragment) {
        this.setTargetFragment(mTargetFragment, mRequestCode);
        show(fragmentManager, this.getClass().getSimpleName());
        return this;
    }

    @Override public DialogFragment show(FragmentManager fragmentManager, Fragment mTargetFragment, int mRequestCode) {
        this.setTargetFragment(mTargetFragment, mRequestCode);
        show(fragmentManager, this.getClass().getSimpleName());
        return this;
    }

    @Override public DialogFragment show(FragmentManager fragmentManager, Activity activity) {
        this.mTargetActivity = true;
        show(fragmentManager, this.getClass().getSimpleName());
        return this;
    }

    @Override public DialogFragment show(FragmentManager fragmentManager, Activity activity, int mRequestCode) {
        this.mTargetActivity = true;
        this.mRequestCode = mRequestCode;
        show(fragmentManager, this.getClass().getSimpleName());
        return this;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示碎片-不保存activity状态
     *
     * @return
     */
    @Override public DialogFragment showAllowingStateLoss(FragmentManager fragmentManager) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, this.getClass().getName());
        ft.commitAllowingStateLoss();
        return this;
    }

    @Override public DialogFragment showAllowingStateLoss(FragmentManager fragmentManager, int mRequestCode) {
        this.mRequestCode = mRequestCode;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, this.getClass().getName());
        ft.commitAllowingStateLoss();
        return this;
    }

    @Override public DialogFragment showAllowingStateLoss(FragmentManager fragmentManager, Fragment mTargetFragment) {
        if (mTargetFragment != null) {
            this.setTargetFragment(mTargetFragment, mRequestCode);
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, this.getClass().getName());
        ft.commitAllowingStateLoss();
        return this;
    }

    @Override public DialogFragment showAllowingStateLoss(FragmentManager fragmentManager, Fragment mTargetFragment, int mRequestCode) {
        if (mTargetFragment != null) {
            this.setTargetFragment(mTargetFragment, mRequestCode);
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, this.getClass().getName());
        ft.commitAllowingStateLoss();
        return this;
    }

    @Override public DialogFragment showAllowingStateLoss(FragmentManager fragmentManager, Activity activity) {
        this.mTargetActivity = true;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, this.getClass().getName());
        ft.commitAllowingStateLoss();
        return this;
    }

    @Override public DialogFragment showAllowingStateLoss(FragmentManager fragmentManager, Activity activity, int mRequestCode) {
        this.mTargetActivity = true;
        this.mRequestCode = mRequestCode;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, this.getClass().getName());
        ft.commitAllowingStateLoss();
        return this;
    }

    @Override public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return onKeyBack();
        } else {
            return false;
        }
    }

}

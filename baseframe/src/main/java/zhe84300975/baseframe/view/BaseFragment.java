package zhe84300975.baseframe.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
 * Describe: View层碎片
 */
public abstract class BaseFragment <B extends BaseIBiz> extends Fragment implements View.OnTouchListener{

    private boolean		targetActivity;

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

    /** View层编辑器 **/
    private BaseBuilder mBaseBuilder;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 打开开关触发菜单项 **/
        setHasOptionsMenu(true);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** 初始化结构 **/
        mBaseStructureModel = new BaseStructureModel(this);

        BaseHelper.structureHelper().attach(mBaseStructureModel);
        /** 初始化视图 **/
        mBaseBuilder = new BaseBuilder(this, inflater);
        View view = build(mBaseBuilder).create();
        /** 初始化所有组建 **/
        unbinder  = ButterKnife.bind(this, view);
        /** 初始化点击事件 **/
        view.setOnTouchListener(this);// 设置点击事件
        return view;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BaseHelper.methodsProxy().fragmentInterceptor().onFragmentCreated(this, getArguments(), savedInstanceState);
        createData(savedInstanceState);
        initData(getArguments());
    }

    @Override public void onStart() {
        super.onStart();
        BaseHelper.methodsProxy().fragmentInterceptor().onFragmentStart(this);
    }

    @Override public void onResume() {
        super.onResume();
        BaseHelper.methodsProxy().fragmentInterceptor().onFragmentResume(this);
        BaseHelper.structureHelper().printBackStackEntry(getFragmentManager());
        listLoadMoreOpen();
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseHelper.methodsProxy().activityInterceptor().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override public void onPause() {
        super.onPause();
        BaseHelper.methodsProxy().fragmentInterceptor().onFragmentPause(this);
        // 恢复初始化
        listRefreshing(false);
    }

    @Override public void onStop() {
        super.onStop();
        BaseHelper.methodsProxy().fragmentInterceptor().onFragmentStop(this);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onDetach() {
        super.onDetach();
        detach();
        /** 移除builder **/
        mBaseBuilder.detach();
        mBaseBuilder = null;
        BaseHelper.structureHelper().detach(mBaseStructureModel);
        /** 清空注解view **/
        unbinder.unbind();
        /** 关闭键盘 **/
        BaseKeyboardUtils.hideSoftInput(getActivity());
    }

    /**
     * 清空
     */
    protected void detach() {}

    @Override public void onDestroy() {
        super.onDestroy();
        BaseHelper.methodsProxy().fragmentInterceptor().onFragmentDestroy(this);
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
     * 是否设置目标活动
     *
     * @return
     */
    public boolean isTargetActivity() {
        return targetActivity;
    }

    /**
     * 设置目标活动
     *
     * @param targetActivity
     */
    public void setTargetActivity(boolean targetActivity) {
        this.targetActivity = targetActivity;
    }

    /**
     * 防止事件穿透
     *
     * @param v
     *            View
     * @param event
     *            事件
     * @return true 拦截 false 不拦截
     */
    @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    /**
     * 返回键
     */
    public boolean onKeyBack() {
        getActivity().onBackPressed();
        return true;
    }

    /**
     * 设置输入法
     *
     * @param mode
     */
    public void setSoftInputMode(int mode) {
        getActivity().getWindow().setSoftInputMode(mode);
    }

    /********************** View业务代码 *********************/
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

    /**
     * 获取activity
     *
     * @param <A>
     * @return
     */
    protected <A extends BaseActivity> A activity() {
        return (A) getActivity();
    }

    public BaseView baseView() {
        return mBaseBuilder == null ? null : mBaseBuilder.getBaseView();
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
        return mBaseBuilder.getToolbar();

    }

    /********************** RecyclerView业务代码 *********************/

    protected BaseRVAdapter recyclerAdapter() {
        return mBaseBuilder == null ? null : mBaseBuilder.getBaseRVAdapterItem2();
    }

    protected RecyclerView.LayoutManager recyclerLayoutManager() {
        return mBaseBuilder == null ? null : mBaseBuilder.getLayoutManager();
    }

    public RecyclerView recyclerView() {
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

    /********************** ViewPager业务代码 *********************/

    /**
     * 可见
     */
    public void onVisible() {}

    /**
     * 不可见
     */
    public void onInvisible() {}
}

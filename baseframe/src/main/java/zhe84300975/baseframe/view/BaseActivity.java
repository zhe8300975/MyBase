package zhe84300975.baseframe.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

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

/**
 * Created by zhaowencong on 16/8/25.
 * Describe:
 */
public abstract class BaseActivity <B extends BaseIBiz> extends AppCompatActivity {

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
     * View层编辑器
     **/
    private BaseBuilder mBaseBuilder;

    BaseStructureModel mBaseStructureModel;

    /**
     * 初始化
     *
     * @param savedInstanceState
     */
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 初始化结构 **/
        mBaseStructureModel = new BaseStructureModel(this);
        BaseHelper.structureHelper().attach(mBaseStructureModel);
        /** 初始化堆栈 **/
        BaseHelper.screenHelper().onCreate(this);
        /** 活动拦截器 **/
        BaseHelper.methodsProxy().activityInterceptor().onCreate(this, getIntent().getExtras(), savedInstanceState);
        /** 初始化视图 **/
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBaseBuilder = new BaseBuilder(this, inflater);
        /** 拦截Builder **/
        BaseHelper.methodsProxy().activityInterceptor().build(mBaseBuilder);
        setContentView(build(mBaseBuilder).create());
        /** 状态栏高度 **/
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }
        /** 状态栏颜色 **/
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(mBaseBuilder.getStatusBarTintEnabled());
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(mBaseBuilder.getNavigationBarTintEnabled());
        tintManager.setStatusBarTintResource(mBaseBuilder.getTintColor());
        /** 初始化所有组建 **/
        ButterKnife.bind(this);
        /** 初始化数据 **/
        createData(savedInstanceState);
        /** 初始化数据 **/
        initData(getIntent().getExtras());
    }

    @Override protected void onStart() {
        super.onStart();
        BaseHelper.methodsProxy().activityInterceptor().onStart(this);
    }

    @Override protected void onResume() {
        super.onResume();
        BaseHelper.screenHelper().onResume(this);
        BaseHelper.methodsProxy().activityInterceptor().onResume(this);
        listLoadMoreOpen();
    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BaseHelper.screenHelper().onActivityResult(this);
    }

    /**
     * 设置输入法
     *
     * @param mode
     */
    public void setSoftInputMode(int mode) {
        getWindow().setSoftInputMode(mode);
    }

    @Override protected void onPause() {
        super.onPause();
        BaseHelper.screenHelper().onPause(this);
        BaseHelper.methodsProxy().activityInterceptor().onPause(this);
        // 恢复初始化
        listRefreshing(false);
    }

    @Override protected void onRestart() {
        super.onRestart();
        BaseHelper.methodsProxy().activityInterceptor().onRestart(this);
    }

    @Override protected void onStop() {
        super.onStop();
        BaseHelper.methodsProxy().activityInterceptor().onStop(this);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        detach();
        /** 移除builder **/
        mBaseBuilder.detach();
        mBaseBuilder = null;
        BaseHelper.structureHelper().detach(mBaseStructureModel);
        BaseHelper.screenHelper().onDestroy(this);
        BaseHelper.methodsProxy().activityInterceptor().onDestroy(this);
        /** 关闭键盘 **/
        BaseKeyboardUtils.hideSoftInput(this);
    }

    /**
     * 清空
     */
    protected void detach() {}

    public void setLanding() {
        BaseHelper.screenHelper().setAsLanding(this);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 创建menu
     *
     * @param menu
     * @return
     */
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        if (mBaseBuilder != null && mBaseBuilder.getToolbarMenuId() > 0) {
            getMenuInflater().inflate(mBaseBuilder.getToolbarMenuId(), menu);
        }
        return super.onCreateOptionsMenu(menu);
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
            if(mBaseStructureModel == null || mBaseStructureModel.getBaseProxy() == null || mBaseStructureModel.getBaseProxy().proxy == null) {
                return BaseHelper.structureHelper().createNullService(service);
            }
            return (C) mBaseStructureModel.getBaseProxy().proxy;
        }
        return BaseHelper.biz(service);
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (BaseHelper.structureHelper().onKeyBack(keyCode, getSupportFragmentManager(), this)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**********************
     * View业务代码
     *********************/

    public <T> T findFragment(Class<T> clazz) {
        BaseCheckUtils.checkNotNull(clazz, "class不能为空");
        return (T) getSupportFragmentManager().findFragmentByTag(clazz.getName());
    }

    public BaseView baseView() {
        return mBaseBuilder == null ? null : mBaseBuilder.getBaseView();
    }

    /**********************
     * Actionbar业务代码
     *********************/

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



    /**********************
     * Actionbar业务代码
     *********************/
    public Toolbar toolbar() {
        return mBaseBuilder == null ? null : mBaseBuilder.getToolbar();
    }

    /**********************
     * RecyclerView业务代码
     *********************/

    protected BaseRVAdapter recyclerAdapter() {
        return mBaseBuilder == null ? null : mBaseBuilder.getBaseRVAdapterItem2();
    }

    protected RecyclerView.LayoutManager recyclerLayoutManager() {
        return mBaseBuilder == null ? null : mBaseBuilder.getLayoutManager();
    }

    protected RecyclerView recyclerView() {
        return mBaseBuilder == null ? null : mBaseBuilder.getRecyclerView();
    }

    /**********************
     * ListView业务代码
     *********************/

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

    public boolean onKeyBack() {
        onBackPressed();
        return true;
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseHelper.methodsProxy().activityInterceptor().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

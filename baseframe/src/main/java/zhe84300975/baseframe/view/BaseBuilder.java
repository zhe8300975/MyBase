package zhe84300975.baseframe.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import zhe84300975.baseframe.BaseHelper;
import zhe84300975.baseframe.R;
import zhe84300975.baseframe.moudles.log.L;
import zhe84300975.baseframe.utils.BaseCheckUtils;
import zhe84300975.baseframe.utils.BaseKeyboardUtils;
import zhe84300975.baseframe.view.adapter.BaseAdapterItem;
import zhe84300975.baseframe.view.adapter.BaseListAdapter;
import zhe84300975.baseframe.view.adapter.BaseListViewMultiLayout;
import zhe84300975.baseframe.view.adapter.recycleview.BaseRVAdapter;
import zhe84300975.baseframe.view.adapter.recycleview.stickyheader.BaseStickyHeaders;
import zhe84300975.baseframe.view.adapter.recycleview.stickyheader.StickyRecyclerHeadersDecoration;
import zhe84300975.baseframe.view.adapter.recycleview.stickyheader.StickyRecyclerHeadersTouchListener;
import zhe84300975.baseframe.view.common.BaseFooterListener;
import zhe84300975.baseframe.view.common.BaseRefreshListener;
import butterknife.ButterKnife;

/**
 * Created by zhaowencong on 16/8/24.
 * Describe: 编辑
 */
public class BaseBuilder implements AbsListView.OnScrollListener{
    /**
     * UI
     **/
    private BaseView mBaseView;

    /**
     * 布局加载器
     **/
    private LayoutInflater mInflater;

    /**
     * 布局ID
     */
    private int mLayoutId;

    private FrameLayout mContentRoot;

    private int mContentRootColor;


    /**
     * 显示状态切换
     */

    private @LayoutRes int mLayoutLoadingId;

    private @LayoutRes int mLayoutEmptyId;

    private @LayoutRes int mLayoutBizErrorId;

    private @LayoutRes int mLayoutHttpErrorId;

    private View mLayoutContent;

    private ViewStub mVsLoading;

    private View mLayoutLoading;

    private View mLayoutEmpty;

    private View mLayoutBizError;

    private View mLayoutHttpError;

    /**
     * 键盘
     */
    private boolean mAutoShouldHideInput = true;
    /**
     * swipback
     */

    private boolean mIsOpenSwipBackLayout;

    private ImageView mIvShadow;

    /**
     * TintManger
     */
    private int mTintColor;

    private boolean mStatusBarEnabled = true;

    private boolean mNavigationBarTintEnabled = true;


    /**
     * actionbar
     */

    private Toolbar mToolbar;

    private Toolbar.OnMenuItemClickListener mMenuListener;

    private int mToolbarLayoutId = R.layout.base_include_toolbar;

    private int mToolbarId = R.id.toolbar;

    private int mToolbarMenuId;

    private int mToolbarDrawerId;

    private boolean mIsOpenToolbar;

    private boolean mIsOpenCustomToolbar;

    private boolean mIsOpenToolbarBack;

    /**
     * ListView
     */
    private BaseListAdapter mBaseListAdapter;

    private ListView mListView;

    private View mHeader;

    private View mFooter;

    private SwipeRefreshLayout mSwipeContainer;

    private BaseRefreshListener mBaseRefreshListener;

    private boolean mLoadMoreIsAtBottom;            // 加载更多

    // 开关

    private int mLoadMoreRequestedItemCount;    // 加载更多

    // 数量

    private int mColorResIds[];

    private int mSwipRefreshId;

    private int mListId;

    private int mListHeaderLayoutId;

    private int mListFooterLayoutId;

    BaseAdapterItem mBaseAdapterItem;

    BaseListViewMultiLayout mBaseListViewMultiLayout;

    AdapterView.OnItemClickListener mItemListener;

    AdapterView.OnItemLongClickListener mItemLongListener;



    /**
     * RecyclerView 替代ListView GradView 可以实现瀑布流
     */

    private int mRecyclerviewId;

    private int mRecyclerviewColorResIds[];

    private int mRecyclerviewSwipRefreshId;

    private BaseFooterListener mBaseFooterListener;

    private RecyclerView recyclerView;

    private BaseRVAdapter mBaseRVAdapter;

    private RecyclerView.LayoutManager mLayoutManager;					// 布局管理器

    private RecyclerView.ItemAnimator mItemAnimator;					// 动画

    private RecyclerView.ItemDecoration mItemDecoration;					// 分割线

    private SwipeRefreshLayout mRecyclerviewSwipeContainer;

    private BaseRefreshListener mRecyclerviewBaseRefreshListener;

    private StickyRecyclerHeadersTouchListener.OnHeaderClickListener mOnHeaderClickListener;

    private boolean mIsHeaderFooter;


    /**
     * 构造器
     *
     * @param baseActivity
     * @param inflater
     */
    public BaseBuilder(@NonNull BaseActivity baseActivity, @NonNull LayoutInflater inflater) {
        mBaseView = new BaseView();
        mBaseView.initUI(baseActivity);
        this.mInflater = inflater;
    }

    /**
     * 构造器
     *
     * @param baseFragment
     * @param inflater
     */
    public BaseBuilder(@NonNull BaseFragment baseFragment, @NonNull LayoutInflater inflater) {
        mBaseView = new BaseView();
        mBaseView.initUI(baseFragment);
        this.mInflater = inflater;
    }

    /**
     * 构造器
     *
     * @param baseDialogFragment
     * @param inflater
     */
    public BaseBuilder(@NonNull BaseDialogFragment baseDialogFragment, @NonNull LayoutInflater inflater) {
        mBaseView = new BaseView();
        mBaseView.initUI(baseDialogFragment);
        this.mInflater = inflater;
    }

    @Nullable
    public BaseView getBaseView() {
        return mBaseView;
    }

    /**
     * 布局ID
     */

    int getLayoutId() {
        return mLayoutId;
    }

    public void layoutId(@LayoutRes int layoutId) {
        this.mLayoutId = layoutId;
    }

    public void layoutColor(@ColorRes int color) {
        this.mContentRootColor = color;
    }


    /**
     * 显示状态切换
     */


    // 设置
    public void layoutLoadingId(@LayoutRes int layoutLoadingId) {
        this.mLayoutLoadingId = layoutLoadingId;
    }

    public void layoutEmptyId(@LayoutRes int layoutEmptyId) {
        this.mLayoutEmptyId = layoutEmptyId;
    }

    public void layoutBizErrorId(@LayoutRes int layoutBizErrorId) {
        this.mLayoutBizErrorId = layoutBizErrorId;
    }

    public void layoutHttpErrorId(@LayoutRes int layoutHttpErrorId) {
        this.mLayoutHttpErrorId = layoutHttpErrorId;
    }

    // 功能
    void layoutContent() {
        if (mLayoutContent == null) {
            return;
        }
        changeShowAnimation(mLayoutLoading, false);
        changeShowAnimation(mLayoutEmpty, false);
        changeShowAnimation(mLayoutBizError, false);
        changeShowAnimation(mLayoutHttpError, false);
        changeShowAnimation(mLayoutContent, true);
    }

    void layoutLoading() {
        if (mLayoutLoadingId < 1) {
            return;
        }
        changeShowAnimation(mLayoutEmpty, false);
        changeShowAnimation(mLayoutBizError, false);
        changeShowAnimation(mLayoutHttpError, false);
        changeShowAnimation(mLayoutContent, false);
        if (mLayoutLoading == null && mVsLoading != null) {
            mLayoutLoading = mVsLoading.inflate();
            BaseCheckUtils.checkNotNull(mLayoutLoading, "无法根据布局文件ID,获取layoutLoading");
        }
        changeShowAnimation(mLayoutLoading, true);
    }

    void layoutEmpty() {
        if (mLayoutEmpty == null) {
            return;
        }
        changeShowAnimation(mLayoutBizError, false);
        changeShowAnimation(mLayoutHttpError, false);
        changeShowAnimation(mLayoutContent, false);
        changeShowAnimation(mLayoutLoading, false);
        changeShowAnimation(mLayoutEmpty, true);
    }

    void layoutBizError() {
        if (mLayoutBizError == null) {
            return;
        }
        changeShowAnimation(mLayoutEmpty, false);
        changeShowAnimation(mLayoutHttpError, false);
        changeShowAnimation(mLayoutContent, false);
        changeShowAnimation(mLayoutLoading, false);
        changeShowAnimation(mLayoutBizError, true);
    }

    void layoutHttpError() {
        if (mLayoutHttpError == null) {
            return;
        }
        changeShowAnimation(mLayoutEmpty, false);
        changeShowAnimation(mLayoutBizError, false);
        changeShowAnimation(mLayoutContent, false);
        changeShowAnimation(mLayoutLoading, false);
        changeShowAnimation(mLayoutHttpError, true);
    }

    void changeShowAnimation(@NonNull View view, boolean visible) {
        if (view == null) {
            return;
        }
        Animation anim;
        if (visible) {
            if (view.getVisibility() == View.VISIBLE) {
                return;
            }
            view.setVisibility(View.VISIBLE);
            anim = AnimationUtils.loadAnimation(mBaseView.activity(), android.R.anim.fade_in);
        } else {
            if (view.getVisibility() == View.GONE) {
                return;
            }
            view.setVisibility(View.GONE);
            anim = AnimationUtils.loadAnimation(mBaseView.activity(), android.R.anim.fade_out);
        }

        anim.setDuration(mBaseView.activity().getResources().getInteger(android.R.integer.config_shortAnimTime));
        view.startAnimation(anim);
    }

    /**
     * 键盘
     */
    public void autoKeyBoard(boolean auto) {
        this.mAutoShouldHideInput = auto;
    }

    boolean isAutoKeyBoard() {
        return this.mAutoShouldHideInput;
    }

    /**
     * swipback
     */
    public void swipBackIsOpen(boolean isOpenSwipBackLayout) {
        this.mIsOpenSwipBackLayout = isOpenSwipBackLayout;
    }

    // 获取
    boolean isOpenSwipBackLayout() {
        return mIsOpenSwipBackLayout;
    }


    /**
     * TintManger
     */

    int getTintColor() {
        return mTintColor;
    }

    boolean isTintColor() {
        return mTintColor > 0;
    }

    public boolean getStatusBarTintEnabled() {
        return mStatusBarEnabled;
    }

    public boolean getNavigationBarTintEnabled() {
        return mNavigationBarTintEnabled;
    }

    public void tintColor(@ColorRes int tintColor) {
        this.mTintColor = tintColor;
    }

    public void tintStatusBarEnabled(boolean isStatusBar) {
        this.mStatusBarEnabled = isStatusBar;
    }

    public void tintNavigationBarEnabled(boolean isNavigationBar) {
        this.mNavigationBarTintEnabled = isNavigationBar;
    }

    /**
     * actionbar
     */


    // 获取
    int getToolbarLayoutId() {
        return mToolbarLayoutId;
    }

    public boolean isOpenCustomToolbar() {
        return mIsOpenCustomToolbar;
    }

    boolean isOpenToolbar() {
        return mIsOpenToolbar;
    }

    boolean isOpenToolbarBack() {
        return mIsOpenToolbarBack;
    }

    int getToolbarId() {
        return mToolbarId;
    }

    int getToolbarMenuId() {
        return mToolbarMenuId;
    }

    int getToolbarDrawerId() {
        return mToolbarDrawerId;
    }

    @Nullable
    Toolbar getToolbar() {
        return mToolbar;
    }

    @Nullable
    Toolbar.OnMenuItemClickListener getMenuListener() {
        return mMenuListener;
    }

    // 设置

    public void toolbarId(@IdRes int toolbarId) {
        this.mToolbarId = toolbarId;
        this.mIsOpenCustomToolbar = true;
    }

    public void toolbarLayoutId(@LayoutRes int toolbarLayoutId) {
        this.mToolbarLayoutId = toolbarLayoutId;
    }

    public void toolbarDrawerId(@DrawableRes int toolbarDrawerId) {
        this.mToolbarDrawerId = toolbarDrawerId;
    }

    public void toolbarMenuListener(@NonNull Toolbar.OnMenuItemClickListener menuListener) {
        this.mMenuListener = menuListener;
    }

    public void toolbarIsBack(@NonNull boolean isOpenToolbarBack) {
        this.mIsOpenToolbarBack = isOpenToolbarBack;
    }

    public void toolbarIsOpen(@NonNull boolean isOpenToolbar) {
        this.mIsOpenToolbar = isOpenToolbar;
    }

    public void toolbarMenuId(@MenuRes int toolbarMenuId) {
        this.mToolbarMenuId = toolbarMenuId;
    }


    /**
     * ListView
     */

    // 获取
    int getListId() {
        return mListId;
    }

    @Nullable
    BaseAdapterItem getBaseAdapterItem() {
        return mBaseAdapterItem;
    }

    @Nullable
    BaseListViewMultiLayout getBaseListViewMultiLayout() {
        return mBaseListViewMultiLayout;
    }

    @Nullable
    AdapterView.OnItemClickListener getItemListener() {
        return mItemListener;
    }

    @Nullable
    AdapterView.OnItemLongClickListener getItemLongListener() {
        return mItemLongListener;
    }

    int getListHeaderLayoutId() {
        return mListHeaderLayoutId;
    }

    int getListFooterLayoutId() {
        return mListFooterLayoutId;
    }

    @Nullable
    BaseListAdapter getAdapter() {
        BaseCheckUtils.checkNotNull(mBaseListAdapter, "适配器没有初始化");
        return mBaseListAdapter;
    }

    @Nullable
    ListView getListView() {
        BaseCheckUtils.checkNotNull(mListView, "没有设置布局文件ID,无法获取ListView");
        return mListView;
    }

    int getSwipRefreshId() {
        return mSwipRefreshId;
    }

    int[] getSwipeColorResIds() {
        return mColorResIds;
    }

    // 设置
    public void listHeaderLayoutId(@LayoutRes int listHeaderLayoutId) {
        this.mListHeaderLayoutId = listHeaderLayoutId;
    }

    public void listFooterLayoutId(@LayoutRes int listFooterLayoutId) {
        this.mListFooterLayoutId = listFooterLayoutId;
    }

    public void listViewOnItemClick(@NonNull AdapterView.OnItemClickListener itemListener) {
        this.mItemListener = itemListener;
    }

    public void listViewOnItemLongClick(@NonNull AdapterView.OnItemLongClickListener itemLongListener) {
        this.mItemLongListener = itemLongListener;
    }

    public void listViewId(@IdRes int listId, @NonNull BaseAdapterItem baseAdapterItem) {
        this.mListId = listId;
        this.mBaseAdapterItem = baseAdapterItem;
    }

    public void listViewId(@IdRes int listId, @NonNull BaseListViewMultiLayout baseListViewMultiLayout) {
        this.mListId = listId;
        this.mBaseListViewMultiLayout = baseListViewMultiLayout;
    }

    public void listSwipRefreshId(@IdRes int swipRefreshId, @NonNull BaseRefreshListener baseRefreshListener) {
        this.mSwipRefreshId = swipRefreshId;
        this.mBaseRefreshListener = baseRefreshListener;
    }

    public void listSwipeColorResIds(int... colorResIds) {
        this.mColorResIds = colorResIds;
    }

    // 功能
    void addListHeader() {
        if (mListView != null && mHeader != null) {
            mListView.addHeaderView(mHeader);
        }
    }

    void addListFooter() {
        if (mListView != null && mFooter != null) {
            mListView.addFooterView(mFooter);
        }
    }

    void removeListHeader() {
        if (mListView != null && mHeader != null) {
            mListView.removeHeaderView(mHeader);
        }
    }

    void removeListFooter() {
        if (mListView != null && mFooter != null) {
            mListView.removeFooterView(mFooter);
        }
    }

    void listRefreshing(boolean bool) {
        if (mSwipeContainer != null) {
            mSwipeContainer.setRefreshing(bool);
        }
        if (mRecyclerviewSwipeContainer != null) {
            mRecyclerviewSwipeContainer.setRefreshing(bool);
        }
    }

    void loadMoreOpen() {
        mLoadMoreIsAtBottom = true;
        mLoadMoreRequestedItemCount = 0;
    }


    /**
     * RecyclerView 替代ListView GradView 可以实现瀑布流
     */


    // 获取
    int getRecyclerviewId() {
        return mRecyclerviewId;
    }

    RecyclerView getRecyclerView() {
        BaseCheckUtils.checkNotNull(recyclerView, "RecyclerView没有找到，查看布局里是否存在~");
        return recyclerView;
    }

    @Nullable
    BaseRVAdapter getBaseRVAdapterItem2() {
        return mBaseRVAdapter;
    }

    @Nullable public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    @Nullable RecyclerView.ItemAnimator getItemAnimator() {
        return mItemAnimator;
    }

    @Nullable RecyclerView.ItemDecoration getItemDecoration() {
        return mItemDecoration;
    }

    int[] getRecyclerviewColorResIds() {
        return mRecyclerviewColorResIds;
    }

    int getRecyclerviewSwipRefreshId() {
        return mRecyclerviewSwipRefreshId;
    }

    public void recyclerviewGridOpenHeaderFooter(boolean bool) {
        this.mIsHeaderFooter = bool;
    }

    // 设置
    public void recyclerviewId(@IdRes int recyclerviewId) {
        this.mRecyclerviewId = recyclerviewId;
    }

    public void recyclerviewLoadingMore(@NonNull BaseFooterListener baseFooterListener) {
        this.mBaseFooterListener = baseFooterListener;
    }

    public void recyclerviewStickyHeaderClick(@NonNull StickyRecyclerHeadersTouchListener.OnHeaderClickListener onHeaderClickListener) {
        this.mOnHeaderClickListener = onHeaderClickListener;
    }

    public void recyclerviewAdapter(@NonNull BaseRVAdapter baseRVAdapter) {
        this.mBaseRVAdapter = baseRVAdapter;
    }

    public void recyclerviewGridManager(@NonNull GridLayoutManager gridLayoutManager) {
        this.mLayoutManager = gridLayoutManager;
    }

    public void recyclerviewLinearManager(@NonNull LinearLayoutManager linearLayoutManager) {
        this.mLayoutManager = linearLayoutManager;
    }

    public void recyclerviewAnimator(@NonNull RecyclerView.ItemAnimator itemAnimator) {
        this.mItemAnimator = itemAnimator;
    }

    public void recyclerviewLinearLayoutManager(int direction, RecyclerView.ItemDecoration itemDecoration, RecyclerView.ItemAnimator itemAnimator, boolean... reverseLayout) {
        boolean reverse = false;
        if (reverseLayout != null && reverseLayout.length > 0) {
            reverse = reverseLayout[0];
        }
        this.mLayoutManager = new LinearLayoutManager(mBaseView.activity(), direction, reverse);
        this.mItemDecoration = itemDecoration;
        this.mItemAnimator = itemAnimator;
    }

    public void recyclerviewGridLayoutManager(int direction, int spanCount, RecyclerView.ItemDecoration itemDecoration, RecyclerView.ItemAnimator itemAnimator, boolean... reverseLayout) {
        boolean reverse = false;
        if (reverseLayout != null && reverseLayout.length > 0) {
            reverse = reverseLayout[0];
        }
        this.mLayoutManager = new GridLayoutManager(mBaseView.activity(), spanCount, direction, reverse);
        this.mItemDecoration = itemDecoration;
        this.mItemAnimator = itemAnimator == null ? new DefaultItemAnimator() : itemAnimator;
    }

    public void recyclerviewStaggeredGridyoutManager(int direction, int spanCount, RecyclerView.ItemDecoration itemDecoration, RecyclerView.ItemAnimator itemAnimator, boolean... reverseLayout) {
        this.mLayoutManager = new StaggeredGridLayoutManager(spanCount, direction);
        this.mItemDecoration = itemDecoration;
        this.mItemAnimator = itemAnimator == null ? new DefaultItemAnimator() : itemAnimator;
    }

    public void recyclerviewColorResIds(int... recyclerviewColorResIds) {
        this.mRecyclerviewColorResIds = recyclerviewColorResIds;
    }

    public void recyclerviewSwipRefreshId(@IdRes int recyclerviewSwipRefreshId, @NonNull BaseRefreshListener recyclerviewBaseRefreshListener) {
        this.mRecyclerviewSwipRefreshId = recyclerviewSwipRefreshId;
        this.mRecyclerviewBaseRefreshListener = recyclerviewBaseRefreshListener;
    }


    /**
     * 创建
     *
     * @return
     */
    View create() {
        L.i("BaseBuilder.create()");
        /** layout **/
        createLayout();
        /** listview **/
        createListView(mContentRoot);
        /** recyclerview **/
        createRecyclerView(mContentRoot);
        /** actoinbar **/
        View view = createActionbar(mContentRoot);
        return view;
    }

    /**
     * 清空所有
     */
    void detach() {
        // 清楚
        if (mBaseView != null) {
            mBaseView.detach();
            mBaseView = null;
        }
        // 基础清除
        detachLayout();
        // actionbar清除
        detachActionbar();
        // listview清除
        detachListView();
        // recyclerview清楚
        detachRecyclerView();
    }

    /**
     * 布局
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) private void createLayout() {
        mContentRoot = new FrameLayout(mBaseView.context());
        if (mContentRootColor > 0) {
            mContentRoot.setBackgroundColor(mContentRootColor);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        // 内容
        if (getLayoutId() > 0) {
            mLayoutContent = mInflater.inflate(getLayoutId(), null, false);
            BaseCheckUtils.checkNotNull(mLayoutContent, "无法根据布局文件ID,获取layoutContent");
            mContentRoot.addView(mLayoutContent, layoutParams);
        }

        // 进度条
        mLayoutLoadingId = mLayoutLoadingId > 0 ? mLayoutLoadingId : BaseHelper.getInstance().layoutLoading();
        if (mLayoutLoadingId > 0) {
            mVsLoading = new ViewStub(mBaseView.activity());
            mVsLoading.setLayoutResource(mLayoutLoadingId);
            mContentRoot.addView(mVsLoading, layoutParams);
        }

        // 空布局
        mLayoutEmptyId = mLayoutEmptyId > 0 ? mLayoutEmptyId : BaseHelper.getInstance().layoutEmpty();
        if (mLayoutEmptyId > 0) {
            mLayoutEmpty = mInflater.inflate(mLayoutEmptyId, null, false);
            BaseCheckUtils.checkNotNull(mLayoutEmpty, "无法根据布局文件ID,获取layoutEmpty");
            mContentRoot.addView(mLayoutEmpty, layoutParams);
            mLayoutEmpty.setVisibility(View.GONE);
        }

        // 业务错误布局
        mLayoutBizErrorId = mLayoutBizErrorId > 0 ? mLayoutBizErrorId : BaseHelper.getInstance().layoutBizError();
        if (mLayoutBizErrorId > 0) {
            mLayoutBizError = mInflater.inflate(mLayoutBizErrorId, null, false);
            BaseCheckUtils.checkNotNull(mLayoutBizError, "无法根据布局文件ID,获取layoutBizError");
            mContentRoot.addView(mLayoutBizError, layoutParams);
            mLayoutBizError.setVisibility(View.GONE);
        }

        // 网络错误布局
        mLayoutHttpErrorId = mLayoutHttpErrorId > 0 ? mLayoutHttpErrorId : BaseHelper.getInstance().layoutHttpError();
        if (mLayoutHttpErrorId > 0) {
            BaseCheckUtils.checkArgument(mLayoutHttpErrorId > 0, "网络错误布局Id不能为空,重写公共布局Application.layoutBizError 或者 在Buider.layout里设置");
            mLayoutHttpError = mInflater.inflate(mLayoutHttpErrorId, null, false);
            BaseCheckUtils.checkNotNull(mLayoutHttpError, "无法根据布局文件ID,获取layoutHttpError");
            mContentRoot.addView(mLayoutHttpError, layoutParams);
            mLayoutHttpError.setVisibility(View.GONE);
        }
    }

    private void detachLayout() {
        mContentRoot = null;
        mInflater = null;
        mLayoutContent = null;
        mLayoutBizError = null;
        mLayoutHttpError = null;
        mLayoutEmpty = null;
        mVsLoading = null;
        mLayoutLoading = null;
        mIvShadow = null;
    }
    /**
     * 标题栏
     *
     * @param view
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) private View createActionbar(View view) {
        if (isOpenToolbar()) {
            final RelativeLayout toolbarRoot = new RelativeLayout(mBaseView.context());
            toolbarRoot.setId(R.id.base_home);
            toolbarRoot.setFitsSystemWindows(true);
            // 添加toolbar布局
            mInflater.inflate(getToolbarLayoutId(), toolbarRoot, true);
            // 添加内容布局
            RelativeLayout.LayoutParams contentLayoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            contentLayoutParams.addRule(RelativeLayout.BELOW, R.id.toolbar);
            toolbarRoot.addView(view, contentLayoutParams);
            mToolbar = ButterKnife.findById(toolbarRoot, getToolbarId());

            BaseCheckUtils.checkNotNull(mToolbar, "无法根据布局文件ID,获取Toolbar");

            if (getToolbarDrawerId() > 0) {
                DrawerLayout drawerLayout = ButterKnife.findById(view, getToolbarDrawerId());
                BaseCheckUtils.checkNotNull(drawerLayout, "无法根据布局文件ID,获取DrawerLayout");
                ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(mBaseView.activity(), drawerLayout, mToolbar, R.string.app_name, R.string.app_name);
                mDrawerToggle.syncState();
                drawerLayout.setDrawerListener(mDrawerToggle);
            }
            // 添加点击事件
            if (getMenuListener() != null) {
                mToolbar.setOnMenuItemClickListener(getMenuListener());
            }
            if (getToolbarMenuId() > 0) {
                mToolbar.inflateMenu(getToolbarMenuId());
            }
            if (isOpenToolbarBack()) {
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

                    @Override public void onClick(View v) {
                        BaseKeyboardUtils.hideSoftInput(mBaseView.activity());
                        switch (mBaseView.getState()) {
                            case BaseView.STATE_ACTIVITY:
                                mBaseView.activity().onKeyBack();
                                break;
                            case BaseView.STATE_FRAGMENT:
                                mBaseView.fragment().onKeyBack();
                                break;
                            case BaseView.STATE_DIALOGFRAGMENT:
                                mBaseView.dialogFragment().onKeyBack();
                                break;
                        }
                    }
                });
            } else {
            }

            toolbarRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            return toolbarRoot;
        } else if (isOpenCustomToolbar()) {
            view.setId(R.id.base_home);
            view.setFitsSystemWindows(true);
            mToolbar = ButterKnife.findById(view, getToolbarId());

            BaseCheckUtils.checkNotNull(mToolbar, "无法根据布局文件ID,获取Toolbar");
            if (getToolbarDrawerId() > 0) {
                DrawerLayout drawerLayout = ButterKnife.findById(view, getToolbarDrawerId());
                BaseCheckUtils.checkNotNull(drawerLayout, "无法根据布局文件ID,获取DrawerLayout");
                ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(mBaseView.activity(), drawerLayout, mToolbar, R.string.app_name, R.string.app_name);
                mDrawerToggle.syncState();
                drawerLayout.setDrawerListener(mDrawerToggle);
            }
            if (isOpenToolbarBack()) {
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

                    @Override public void onClick(View v) {
                        BaseKeyboardUtils.hideSoftInput(mBaseView.activity());
                        switch (mBaseView.getState()) {
                            case BaseView.STATE_ACTIVITY:
                                mBaseView.activity().onKeyBack();
                                break;
                            case BaseView.STATE_FRAGMENT:
                                mBaseView.fragment().onKeyBack();
                                break;
                            case BaseView.STATE_DIALOGFRAGMENT:
                                mBaseView.dialogFragment().onKeyBack();
                                break;
                        }
                    }
                });
            }
            // 添加点击事件
            if (getMenuListener() != null) {
                mToolbar.setOnMenuItemClickListener(getMenuListener());
            }
            if (getToolbarMenuId() > 0) {
                mToolbar.inflateMenu(getToolbarMenuId());
            }

            return view;
        } else {
            view.setId(R.id.base_home);
            view.setFitsSystemWindows(true);
            return view;
        }
    }

    private void detachActionbar() {
        mMenuListener = null;
        mToolbar = null;
        mMenuListener = null;
    }

    /**
     * 列表
     *
     * @param view
     */
    private void createListView(View view) {
        if (getListId() > 0) {
            mListView = ButterKnife.findById(view, getListId());
            BaseCheckUtils.checkNotNull(mListView, "无法根据布局文件ID,获取ListView");
            // 添加头布局
            if (getListHeaderLayoutId() != 0) {
                mHeader = mInflater.inflate(getListHeaderLayoutId(), null, false);
                BaseCheckUtils.checkNotNull(mHeader, "无法根据布局文件ID,获取ListView 头布局");
                addListHeader();
            }
            // 添加尾布局
            if (getListFooterLayoutId() != 0) {
                mFooter = mInflater.inflate(getListFooterLayoutId(), null, false);
                BaseCheckUtils.checkNotNull(mFooter, "无法根据布局文件ID,获取ListView 尾布局");
                addListFooter();
            }
            // 设置上拉和下拉事件
            if (getSwipRefreshId() != 0) {
                mSwipeContainer= ButterKnife.findById(view, getSwipRefreshId());
                BaseCheckUtils.checkNotNull(mSwipeContainer, "无法根据布局文件ID,获取ListView的SwipRefresh下载刷新布局");
                BaseCheckUtils.checkNotNull(mBaseRefreshListener, " ListView的SwipRefresh 下拉刷新和上拉加载事件没有设置");
                mSwipeContainer.setOnRefreshListener(mBaseRefreshListener);// 下载刷新
                mListView.setOnScrollListener(this);// 加载更多
            }
            // 设置进度颜色
            if (getSwipeColorResIds() != null) {
                BaseCheckUtils.checkNotNull(mSwipeContainer, "无法根据布局文件ID,获取ListView的SwipRefresh下载刷新布局");
                mSwipeContainer.setColorSchemeResources(getSwipeColorResIds());
            }
            // 添加点击事件
            if (getItemListener() != null) {
                mListView.setOnItemClickListener(getItemListener());
            }
            if (getItemLongListener() != null) {
                mListView.setOnItemLongClickListener(getItemLongListener());
            }
            // 创建适配器
            mBaseListAdapter = mBaseListViewMultiLayout == null ? new BaseListAdapter(mBaseView, getBaseAdapterItem()) : new BaseListAdapter(mBaseView, mBaseListViewMultiLayout);
            BaseCheckUtils.checkNotNull(mBaseListAdapter, "适配器创建失败");
            // 设置适配器
            mListView.setAdapter(mBaseListAdapter);
        }
    }

    private void detachListView() {
        if (mBaseListAdapter != null) {
            mBaseListAdapter.detach();
            mBaseListAdapter = null;
        }
        mListView = null;
        mHeader = null;
        mFooter = null;
        mBaseAdapterItem = null;
        mBaseListViewMultiLayout = null;
        mItemListener = null;
        mItemLongListener = null;
        mSwipeContainer = null;
        mColorResIds = null;
        mBaseRefreshListener = null;
    }

    /**
     * 列表
     *
     * @param view
     */
    private void createRecyclerView(View view) {
        if (getRecyclerviewId() > 0) {
            recyclerView = ButterKnife.findById(view, getRecyclerviewId());
            BaseCheckUtils.checkNotNull(recyclerView, "无法根据布局文件ID,获取recyclerView");
            BaseCheckUtils.checkNotNull(mLayoutManager, "LayoutManger不能为空");
            recyclerView.setLayoutManager(mLayoutManager);
            if (mBaseRVAdapter != null) {
                // 扩展适配器
                if (mBaseRVAdapter instanceof BaseStickyHeaders) {
                    BaseStickyHeaders baseStickyHeaders = (BaseStickyHeaders) mBaseRVAdapter;
                    final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(baseStickyHeaders);
                    recyclerView.addItemDecoration(headersDecor);

                    if (mOnHeaderClickListener != null) {
                        StickyRecyclerHeadersTouchListener touchListener = new StickyRecyclerHeadersTouchListener(recyclerView, headersDecor);
                        touchListener.setOnHeaderClickListener(mOnHeaderClickListener);
                        recyclerView.addOnItemTouchListener(touchListener);

                    }
                    mBaseRVAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

                        @Override public void onChanged() {
                            headersDecor.invalidateHeaders();
                        }
                    });
                }
                recyclerView.setAdapter(mBaseRVAdapter);
                if (mIsHeaderFooter) {
                    final GridLayoutManager gridLayoutManager = (GridLayoutManager) mLayoutManager;
                    BaseCheckUtils.checkNotNull(gridLayoutManager, "LayoutManger，不是GridLayoutManager");
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                        @Override public int getSpanSize(int position) {
                            return mBaseRVAdapter.isHeaderAndFooter(position) ? gridLayoutManager.getSpanCount() : 1;
                        }
                    });
                }
                // 设置Item增加、移除动画
                recyclerView.setItemAnimator(getItemAnimator());
                // 添加分割线
                if (getItemDecoration() != null) {
                    recyclerView.addItemDecoration(getItemDecoration());
                }
                // 优化
                recyclerView.setHasFixedSize(true);
                // 设置上拉和下拉事件
                if (getRecyclerviewSwipRefreshId() != 0) {
                    mRecyclerviewSwipeContainer = ButterKnife.findById(view, getRecyclerviewSwipRefreshId());
                    BaseCheckUtils.checkNotNull(mRecyclerviewSwipeContainer, "无法根据布局文件ID,获取recyclerview的SwipRefresh下载刷新布局");
                    BaseCheckUtils.checkNotNull(mRecyclerviewBaseRefreshListener, " recyclerview的SwipRefresh 下拉刷新和上拉加载事件没有设置");
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                        @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (newState == RecyclerView.SCROLL_STATE_IDLE && mLoadMoreIsAtBottom) {
                                if (mRecyclerviewBaseRefreshListener.onScrolledToBottom()) {
                                    mLoadMoreRequestedItemCount = mBaseRVAdapter.getItemCount();
                                    mLoadMoreIsAtBottom = false;
                                }
                            }
                        }

                        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            if (mLayoutManager instanceof LinearLayoutManager) {
                                int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition();
                                mLoadMoreIsAtBottom = mBaseRVAdapter.getItemCount() > mLoadMoreRequestedItemCount && lastVisibleItem + 1 == mBaseRVAdapter.getItemCount();
                            }
                        }
                    });// 加载更多
                    mRecyclerviewSwipeContainer.setOnRefreshListener(mRecyclerviewBaseRefreshListener);// 下载刷新
                } else {
                    if (mBaseFooterListener != null) {
                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLoadMoreIsAtBottom) {
                                    if (mBaseFooterListener.onScrolledToBottom()) {
                                        mLoadMoreRequestedItemCount = mBaseRVAdapter.getItemCount();
                                        mLoadMoreIsAtBottom = false;
                                    }
                                }
                            }

                            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if (mLayoutManager instanceof LinearLayoutManager) {
                                    int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition();
                                    mLoadMoreIsAtBottom = mBaseRVAdapter.getItemCount() > mLoadMoreRequestedItemCount && lastVisibleItem + 1 == mBaseRVAdapter.getItemCount();
                                }
                            }
                        });
                    }
                }
            } else {
                BaseCheckUtils.checkNotNull(null, "BaseRVAdapter适配器不能为空");
            }

            // 设置进度颜色
            if (getRecyclerviewColorResIds() != null) {
                BaseCheckUtils.checkNotNull(mRecyclerviewSwipeContainer, "无法根据布局文件ID,获取recyclerview的SwipRefresh下载刷新布局");
                mRecyclerviewSwipeContainer.setColorSchemeResources(getRecyclerviewColorResIds());
            }
        }
    }

    private void detachRecyclerView() {
        recyclerView = null;
        if (mBaseRVAdapter != null) {
            mBaseRVAdapter.clearCache();
            mBaseRVAdapter = null;
        }
        mOnHeaderClickListener = null;
        mLayoutManager = null;
        mItemAnimator = null;
        mItemDecoration = null;
        mRecyclerviewSwipeContainer = null;
        mRecyclerviewBaseRefreshListener = null;
    }

    /**
     * 自动加载更多
     **/
    @Override public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mLoadMoreIsAtBottom) {
            if (mBaseRefreshListener.onScrolledToBottom()) {
                mLoadMoreRequestedItemCount = view.getCount();
                mLoadMoreIsAtBottom = false;
            }
        }
    }

    @Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mLoadMoreIsAtBottom = totalItemCount > mLoadMoreRequestedItemCount && firstVisibleItem + visibleItemCount == totalItemCount;
    }


}

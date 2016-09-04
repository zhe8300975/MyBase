package zhe84300975.baseframe.view.adapter.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import zhe84300975.baseframe.core.BaseIBiz;
import zhe84300975.baseframe.display.BaseIDisplay;
import zhe84300975.baseframe.utils.BaseCheckUtils;
import zhe84300975.baseframe.view.BaseActivity;
import zhe84300975.baseframe.view.BaseDialogFragment;
import zhe84300975.baseframe.view.BaseFragment;
import zhe84300975.baseframe.view.BaseView;

/**
 * Created by zhaowencong on 16/8/25.
 * Describe: RecyclerView 适配器
 */
public abstract class BaseRVAdapter <T, V extends BaseHolder> extends RecyclerView.Adapter<V> {

    public abstract V newViewHolder(ViewGroup viewGroup, int type);

    private BaseRVAdapter() {}

    /**
     * 数据
     */
    private List mItems;

    private BaseView mBaseView;

    public BaseRVAdapter(BaseActivity baseActivity) {
        BaseCheckUtils.checkNotNull(baseActivity, "View层不存在");
        this.mBaseView = baseActivity.baseView();
    }

    public BaseRVAdapter(BaseFragment baseFragment) {
        BaseCheckUtils.checkNotNull(baseFragment, "View层不存在");
        this.mBaseView = baseFragment.baseView();
    }

    public BaseRVAdapter(BaseDialogFragment baseDialogFragment) {
        BaseCheckUtils.checkNotNull(baseDialogFragment, "View层不存在");
        this.mBaseView = baseDialogFragment.baseView();
    }

    @Override public V onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        V holder = newViewHolder(viewGroup, viewType);
        return holder;
    }

    @Override public void onBindViewHolder(V v, int position) {
        v.bindData(getItem(position), getItemCount());
    }

    public List<T> getItems() {
        return mItems;
    }

    public void setItems(List items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public void add(int position, Object object) {
        if (object == null || getItems() == null || position < 0 || position > getItems().size()) {
            return;
        }
        mItems.add(position, object);
        notifyItemInserted(position);
    }

    public void add(Object object) {
        if (object == null || getItems() == null) {
            return;
        }
        mItems.add(object);
        notifyItemInserted(mItems.size());

    }

    public void addList(int position, List list) {
        if (list == null || list.size() < 1 || getItems() == null || position < 0 || position > getItems().size()) {
            return;
        }
        mItems.addAll(position, list);
        notifyItemRangeInserted(position, list.size());

    }

    public void addList(List list) {
        if (list == null || list.size() < 1 || getItems() == null) {
            return;
        }
        int postion = getItemCount();
        mItems.addAll(list);
        notifyItemRangeInserted(postion, list.size());
    }

    public void delete(int position) {
        if (getItems() == null || position < 0 || getItems().size() < position) {
            return;
        }
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public void delete(List list) {
        if (list == null || list.size() < 1 || getItems() == null) {
            return;
        }
        int position = getItemCount();
        mItems.removeAll(list);
        notifyItemRangeRemoved(position, list.size());
    }

    public void delete(int position, List list) {
        if (list == null || list.size() < 1 || getItems() == null) {
            return;
        }
        mItems.removeAll(list);
        notifyItemRangeRemoved(position, list.size());
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return (T) mItems.get(position);
    }

    public void updateData() {
        notifyDataSetChanged();
    }

    public <V extends BaseFragment> V fragment() {
        return mBaseView.fragment();
    }

    public <A extends BaseActivity> A activity() {
        return mBaseView.activity();
    }

    public <D extends BaseDialogFragment> D dialogFragment() {
        return mBaseView.dialogFragment();
    }

    /**
     * 获取适配器
     *
     * @return
     */
    protected BaseRVAdapter getAdapter() {
        return this;
    }

    /**
     * 获取fragment
     *
     * @param clazz
     * @return
     */
    public <T> T findFragment(Class<T> clazz) {
        BaseCheckUtils.checkNotNull(clazz, "class不能为空");
        return (T) mBaseView.manager().findFragmentByTag(clazz.getSimpleName());
    }

    public BaseView getUI() {
        return mBaseView;
    }

    public <B extends BaseIBiz> B biz(Class<B> service) {
        return mBaseView.biz(service);
    }

    /**
     * 获取调度
     *
     * @param e
     * @param <E>
     * @return
     */
    protected <E extends BaseIDisplay> E display(Class<E> e) {
        return mBaseView.display(e);
    }

    @Override public int getItemCount() {
        if (mItems == null) {
            return 0;
        }
        return mItems.size();
    }

    public boolean isHeaderAndFooter(int position) {
        return false;
    }

    public void clearCache() {
        if (mItems != null) {
            mItems.clear();
            mItems = null;
        }
    }
}

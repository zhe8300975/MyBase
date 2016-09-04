package zhe84300975.baseframe.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import zhe84300975.baseframe.display.BaseIDisplay;
import zhe84300975.baseframe.utils.BaseCheckUtils;
import butterknife.ButterKnife;
import zhe84300975.baseframe.view.BaseActivity;
import zhe84300975.baseframe.view.BaseDialogFragment;
import zhe84300975.baseframe.view.BaseFragment;
import zhe84300975.baseframe.view.BaseView;

/**
 * Created by zhaowencong on 16/8/25.
 * Describe: 列表适配器
 */
public class BaseListAdapter extends BaseAdapter {

    private BaseListAdapter() {}

    /**
     * 数据
     */
    private List mItems;

    /**
     * View
     */
    BaseView mBaseView;

    /**
     * 适配器Item
     */
    private BaseAdapterItem mBaseAdapterItem;

    /**
     * 多布局接口
     */
    private BaseListViewMultiLayout mBaseListViewMultiLayout;

    public BaseListAdapter(BaseView baseView, BaseAdapterItem baseAdapterItem) {
        BaseCheckUtils.checkNotNull(baseView, "View层不存在");
        BaseCheckUtils.checkNotNull(baseAdapterItem, "ListView Item类不存在");
        this.mBaseView = baseView;
        this.mBaseAdapterItem = baseAdapterItem;
    }

    public BaseListAdapter(BaseView baseView, BaseListViewMultiLayout baseListViewMultiLayout) {
        BaseCheckUtils.checkNotNull(baseView, "View层不存在");
        BaseCheckUtils.checkNotNull(baseListViewMultiLayout, "ListView 多布局接口不存在");
        this.mBaseView = baseView;
        this.mBaseListViewMultiLayout = baseListViewMultiLayout;
    }

    public void setItems(List items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public void add(int position, Object object) {
        if (object == null || mItems == null || position < 0 || position > mItems.size()) {
            return;
        }
        mItems.add(position, object);
        notifyDataSetChanged();
    }

    public void add(Object object) {
        if (object == null || mItems == null) {
            return;
        }
        mItems.add(object);
        notifyDataSetChanged();
    }

    public void addList(int position, List list) {
        if (list == null || list.size() < 1 || mItems == null || position < 0 || position > mItems.size()) {
            return;
        }
        mItems.addAll(position, list);
        notifyDataSetChanged();
    }

    public void addList(List list) {
        if (list == null || list.size() < 1 || mItems == null) {
            return;
        }
        mItems.addAll(list);
        notifyDataSetChanged();
    }

    public void delete(int position) {
        if (mItems == null || position < 0 || mItems.size() < position) {
            return;
        }
        mItems.remove(position);
        notifyDataSetChanged();
    }

    public void delete(Object object) {
        if (mItems == null || mItems.size() < 1) {
            return;
        }
        mItems.remove(object);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mItems == null) {
            return;
        }
        mItems.clear();
        notifyDataSetChanged();
    }

    public List getItems() {
        return mItems;
    }

    @Override public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public int getViewTypeCount() {
        return mBaseListViewMultiLayout == null ? 1 : mBaseListViewMultiLayout.getBaseViewTypeCount();
    }

    @Override public int getItemViewType(int position) {
        return mBaseListViewMultiLayout == null ? 0 : mBaseListViewMultiLayout.getBaseViewType(position);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        BaseAdapterItem item = null;
        if (convertView == null) {
            if (mBaseListViewMultiLayout == null) {
                item = createItem(); // 单类型
            } else {
                item = createMultiItem(position);// 多类型
            }
            convertView = LayoutInflater.from(parent.getContext()).inflate(item.getItemLayout(), null, false);
            // 初始化
            ButterKnife.bind(item, convertView);
            // 初始化布局
            item.init(convertView);
            // 设置Tag标记
            convertView.setTag(item);
        }
        // 获取item
        item = item == null ? (BaseAdapterItem) convertView.getTag() : item;
        // 绑定数据
        item.bindData(getItem(position), position, getCount());
        return convertView;
    }

    public BaseView getUI() {
        return mBaseView;
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
     * 获取调度
     *
     * @param e
     * @param <E>
     * @return
     */
    protected <E extends BaseIDisplay> E display(Class<E> e) {
        return mBaseView.display(e);
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

    /**
     * 单类型
     *
     * @return
     */
    private BaseAdapterItem createItem() {
        BaseAdapterItem itemClone = (BaseAdapterItem) this.mBaseAdapterItem.clone();
        itemClone.setBaseView(mBaseView);
        return itemClone;
    }

    /**
     * 多类型
     *
     * @param position
     * @return
     */
    private BaseAdapterItem createMultiItem(int position) {
        int type = getItemViewType(position);
        return mBaseListViewMultiLayout.getBaseAdapterItem(type);
    }

    public void detach() {
        if (mItems != null) {
            mItems.clear();
            mItems = null;
        }
        mBaseView = null;
        mBaseAdapterItem = null;
        mBaseListViewMultiLayout = null;
    }

}

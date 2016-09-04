package zhe84300975.baseframe.view.adapter;

/**
 * Created by zhaowencong on 16/8/25.
 * Describe: ListView 多布局接口
 */
public interface BaseListViewMultiLayout {

    /**
     * 类型
     *
     * @param position
     * @return
     */
    int getBaseViewType(int position);

    /**
     * 类型数量
     *
     * @return
     */
    int getBaseViewTypeCount();

    /**
     * 根据类型获取适配器Item
     *
     * @param type
     * @return
     */
    BaseAdapterItem getBaseAdapterItem(int type);

}

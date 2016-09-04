package zhe84300975.baseframe.view.common;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by zhaowencong on 16/8/25.
 * Describe: 下拉和加载更多接口
 */
public interface BaseRefreshListener extends SwipeRefreshLayout.OnRefreshListener{
    boolean onScrolledToBottom();// 到底部
}

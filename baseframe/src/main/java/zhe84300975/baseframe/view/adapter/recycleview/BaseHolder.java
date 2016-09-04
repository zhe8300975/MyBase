package zhe84300975.baseframe.view.adapter.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by zhaowencong on 16/8/25.
 * Describe: 适配器优化holder
 */
public abstract class BaseHolder <T> extends RecyclerView.ViewHolder{

    public BaseHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public abstract void bindData(T t, int count);
}

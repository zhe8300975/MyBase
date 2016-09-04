package zhe84300975.baseframe.moudles.toast;

import android.os.Looper;
import android.widget.Toast;

import zhe84300975.baseframe.BaseHelper;

/**
 * Created by zhaowencong on 16/8/18.
 * Describe: Toast
 */
public class BaseToast {                //TODO 之后要更改为只弹出一次

    private Toast mToast = null;

    /**
     * 简单Toast 消息弹出
     *
     * @param msg
     */
    public void show(final String msg) {
        // 判断是否在主线程
        boolean isMainLooper = Looper.getMainLooper().getThread() != Thread.currentThread();

        if (isMainLooper) {
            BaseHelper.mainLooper().execute(new Runnable() {

                @Override public void run() {
                    showToast(msg, Toast.LENGTH_SHORT);
                }
            });
        } else {
            showToast(msg, Toast.LENGTH_SHORT);
        }
    }


    /**
     * 简单Toast 消息弹出
     *
     * @param msg
     */
    public void show(final int msg) {
        // 判断是否在主线程
        boolean isMainLooper = Looper.getMainLooper().getThread() != Thread.currentThread();

        if (isMainLooper) {
            BaseHelper.mainLooper().execute(new Runnable() {

                @Override public void run() {
                    showToast(BaseHelper.getInstance().getString(msg), Toast.LENGTH_SHORT);
                }
            });
        } else {
            showToast(BaseHelper.getInstance().getString(msg), Toast.LENGTH_SHORT);
        }
    }


    /**
     * 简单Toast 消息弹出
     *
     * @param msg
     */
    public void show(final int msg,final int duration) {
        // 判断是否在主线程
        boolean isMainLooper = Looper.getMainLooper().getThread() != Thread.currentThread();

        if (isMainLooper) {
            BaseHelper.mainLooper().execute(new Runnable() {

                @Override public void run() {
                    showToast(BaseHelper.getInstance().getString(msg), duration);
                }
            });
        } else {
            showToast(BaseHelper.getInstance().getString(msg), duration);
        }
    }
    /**
     * 弹出提示
     *
     * @param text
     * @param duration
     */
    protected void showToast(String text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseHelper.getInstance(), text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }

        mToast.show();
    }

    public void clear() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

}

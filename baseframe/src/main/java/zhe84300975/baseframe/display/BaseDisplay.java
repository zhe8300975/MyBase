package zhe84300975.baseframe.display;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import zhe84300975.baseframe.BaseHelper;
import zhe84300975.baseframe.utils.BaseCheckUtils;

/**
 * Created by zhaowencong on 16/8/19.
 * Describe:统一控制TitleBar、Drawer以及所有Activity和Fragment跳转(TODO 跳转改为AndRutor)
 */
public class BaseDisplay implements BaseIDisplay{

    @Override public Context context() {
        return BaseHelper.screenHelper().getCurrentActivity();
    }

    @Override public <T extends FragmentActivity> T activity() {
        T baseActivity = BaseHelper.screenHelper().getCurrentIsRunningActivity();
        if (baseActivity != null) {
            return baseActivity;
        } else {
            return BaseHelper.screenHelper().getCurrentActivity();
        }
    }

    @Override public void intentFromFragment(Class clazz, Fragment fragment, int requestCode) {
        Intent intent = new Intent();
        if (activity() == null) {
            return;
        }
        intent.setClass(activity(), clazz);
        intentFromFragment(intent, fragment, requestCode);
    }

    @Override public void intentFromFragment(Intent intent, Fragment fragment, int requestCode) {
        if (activity() == null) {
            return;
        }
        activity().startActivityFromFragment(fragment, intent, requestCode);
    }

    /** 跳转fragment **/

    @Override public void commitAdd(int layoutId, Fragment fragment) {
        BaseCheckUtils.checkArgument(layoutId > 0, "布局ID 不能为空~");
        BaseCheckUtils.checkNotNull(fragment, "fragment不能为空~");
        if (activity() == null) {
            return;
        }
        activity().getSupportFragmentManager().beginTransaction().add(layoutId, fragment, fragment.getClass().getName()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
    }

    @Override public void commitChildReplace(Fragment srcFragment, int layoutId, Fragment fragment) {
        BaseCheckUtils.checkArgument(layoutId > 0, "提交布局ID 不能为空~");
        BaseCheckUtils.checkNotNull(fragment, "fragment不能为空~");
        if (activity() == null) {
            return;
        }
        srcFragment.getChildFragmentManager().beginTransaction().replace(layoutId, fragment, fragment.getClass().getName()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
    }

    @Override public void commitReplace(int layoutId, Fragment fragment) {
        BaseCheckUtils.checkArgument(layoutId > 0, "提交布局ID 不能为空~");
        BaseCheckUtils.checkNotNull(fragment, "fragment不能为空~");
        if (activity() == null) {
            return;
        }
        activity().getSupportFragmentManager().beginTransaction().replace(layoutId, fragment, fragment.getClass().getName()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
    }




    @Override public void commitBackStack(int layoutId, Fragment fragment) {
        BaseCheckUtils.checkArgument(layoutId > 0, "提交布局ID 不能为空~");
        BaseCheckUtils.checkNotNull(fragment, "fragment不能为空~");
        if (activity() == null) {
            return;
        }
        activity().getSupportFragmentManager().beginTransaction().add(layoutId, fragment, fragment.getClass().getName()).addToBackStack(fragment.getClass().getName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commitAllowingStateLoss();

    }

    @Override public void commitBackStack(int layoutId, Fragment fragment, int animation) {
        BaseCheckUtils.checkArgument(layoutId > 0, "提交布局ID 不能为空~");
        BaseCheckUtils.checkArgument(animation > 0, "动画 不能为空~");
        BaseCheckUtils.checkNotNull(fragment, "fragment不能为空~");
        if (activity() == null) {
            return;
        }
        activity().getSupportFragmentManager().beginTransaction().add(layoutId, fragment, fragment.getClass().getName()).addToBackStack(fragment.getClass().getName())
                .setTransition(animation != 0 ? animation : FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commitAllowingStateLoss();
    }

    /** 跳转intent **/
    @Override public void intent(Class clazz) {
        intent(clazz, null);
    }

    @Override public void intent(String clazzName) {
        if (activity() == null || clazzName == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(BaseHelper.getInstance(), clazzName);
        activity().startActivity(intent);
    }

    @Override public void intentNotAnimation(Class clazz) {
        intentNotAnimation(clazz, null);
    }

    @Override public void intent(Class clazz, Bundle bundle) {
        if (activity() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(activity(), clazz);
        intent(intent, bundle);
    }

    @Override public void intentNotAnimation(Class clazz, Bundle bundle) {
        if (activity() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setClass(activity(), clazz);
        intent(intent, bundle);
    }

    @Override public void intent(Intent intent) {
        intent(intent, null);
    }

    @Override public void intent(Intent intent, Bundle options) {
        intentForResult(intent, options, -1);
    }

    @Override public void intentForResult(Class clazz, int requestCode) {
        intentForResult(clazz, null, requestCode);
    }

    @Override public void intentForResultFromFragment(Class clazz, Bundle bundle, int requestCode, Fragment fragment) {
        Intent intent = new Intent();
        intent.setClass(activity(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (activity() == null) {
            return;
        }
        activity().startActivityFromFragment(fragment, intent, requestCode);
    }

    @Override public void intentForResult(Class clazz, Bundle bundle, int requestCode) {
        if (activity() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(activity(), clazz);
        intentForResult(intent, bundle, requestCode);
    }

    @Override public void intentForResult(Intent intent, int requestCod) {
        intentForResult(intent, null, requestCod);
    }

    /** 根据某个View 位置 启动跳转动画 **/

    @Override public void intentAnimation(Class clazz, View view, Bundle bundle) {
        if (activity() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(activity(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ActivityCompat.startActivity(activity(), intent, ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight()).toBundle());
    }

    @Override public void intentAnimation(Class clazz, int in, int out) {
        intent(clazz);
        if (activity() == null) {
            return;
        }
        activity().overridePendingTransition(in, out);
    }

    @Override public void intentAnimation(Class clazz, int in, int out, Bundle bundle) {
        intent(clazz, bundle);
        if (activity() == null) {
            return;
        }
        activity().overridePendingTransition(in, out);
    }

    @Override public void intentForResultAnimation(Class clazz, View view, int requestCode) {
        intentForResultAnimation(clazz, view, null, requestCode);
    }

    @Override public void intentForResultAnimation(Class clazz, View view, Bundle bundle, int requestCode) {
        if (activity() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(activity(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ActivityCompat.startActivityForResult(activity(), intent, requestCode, ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight()).toBundle());

    }

    @Override public void intentForResultAnimation(Class clazz, int in, int out, int requestCode) {
        intentForResultAnimation(clazz, in, out, null, requestCode);
    }

    @Override public void intentForResultAnimation(Class clazz, int in, int out, Bundle bundle, int requestCode) {
        intentForResult(clazz, bundle, requestCode);
        if (activity() == null) {
            return;
        }
        activity().overridePendingTransition(in, out);
    }

    @Override @TargetApi(Build.VERSION_CODES.JELLY_BEAN) public void intentForResult(Intent intent, Bundle options, int requestCode) {
        BaseCheckUtils.checkNotNull(intent, "intent不能为空～");
        if (options != null) {
            intent.putExtras(options);
        }
        if (activity() == null) {
            return;
        }
        activity().startActivityForResult(intent, requestCode);
    }

    @Override public void onKeyHome() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
        intent.addCategory(Intent.CATEGORY_HOME);
        if (activity() == null) {
            return;
        }
        context().startActivity(intent);
    }

    @Override public void popBackStack() {
        if (activity() == null) {
            return;
        }
        activity().getSupportFragmentManager().popBackStackImmediate();
    }

    @Override public void popBackStack(Class clazz) {
        if (activity() == null) {
            return;
        }
        activity().getSupportFragmentManager().popBackStackImmediate(clazz.getName(), 0);
    }

    @Override public void popBackStack(String clazzName) {
        if (activity() == null) {
            return;
        }
        activity().getSupportFragmentManager().popBackStackImmediate(clazzName, 0);
    }

    @Override public void popBackStackAll() {
        if (activity() == null) {
            return;
        }
        activity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}

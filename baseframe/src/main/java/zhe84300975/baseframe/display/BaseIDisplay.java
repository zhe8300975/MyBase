package zhe84300975.baseframe.display;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import org.jetbrains.annotations.NotNull;

/**
 * Created by zhaowencong on 16/8/19.
 * Describe: 统一控制TitleBar、Drawer以及所有Activity和Fragment跳转
 */
public interface BaseIDisplay {

    /**
     * 获取上下文
     *
     * @return
     */
    Context context();

    <T extends FragmentActivity> T activity();

    /**
     * 跳转
     *
     * @param clazz
     * @param fragment
     * @param requestCode
     */
    void intentFromFragment(@NotNull Class clazz, @NotNull Fragment fragment, int requestCode);

    /**
     * 跳转
     *
     * @param intent
     * @param fragment
     * @param requestCode
     */
    void intentFromFragment(@NotNull Intent intent, @NotNull Fragment fragment, int requestCode);

    /**
     * home键
     */
    void onKeyHome();


    void popBackStack();

    void popBackStack(@NotNull Class clazz);

    void popBackStack(@NotNull String clazzName);

    void popBackStackAll();

    void commitAdd(@IdRes int layoutId, @NotNull Fragment fragment);

    void commitChildReplace(@NotNull Fragment srcFragment, @IdRes int layoutId, @NotNull Fragment fragment);

    void commitReplace(@IdRes int layoutId, @NotNull Fragment fragment);


    void commitBackStack(@IdRes int layoutId, @NotNull Fragment fragment);

    void commitBackStack(@IdRes int layoutId, @NotNull Fragment fragment, int animation);

    /** 跳转intent **/

    void intent(@NotNull Class clazz);

    void intent(@NotNull String clazzName);

    void intentNotAnimation(@NotNull Class clazz);

    void intent(@NotNull Class clazz, Bundle bundle);

    void intentNotAnimation(@NotNull Class clazz, @NotNull Bundle bundle);

    void intent(@NotNull Intent intent);

    void intent(@NotNull Intent intent, @NotNull Bundle options);

    void intentForResult(@NotNull Class clazz, int requestCode);

    void intentForResultFromFragment(@NotNull Class clazz, Bundle bundle, int requestCode, @NotNull Fragment fragment);

    void intentForResult(@NotNull Class clazz, @NotNull Bundle bundle, int requestCode);

    void intentForResult(@NotNull Intent intent, int requestCod);

    void intentForResult(@NotNull Intent intent, @NotNull Bundle options, int requestCode);

    void intentAnimation(@NotNull Class clazz, @NotNull View view, Bundle bundle);

    void intentAnimation(@NotNull Class clazz, @AnimRes int in, @AnimRes int out);

    void intentAnimation(@NotNull Class clazz, @AnimRes int in, @AnimRes int out, @NonNull Bundle bundle);

    void intentForResultAnimation(@NotNull Class clazz, @NotNull View view, int requestCode);

    void intentForResultAnimation(@NotNull Class clazz, @NotNull View view, @NotNull Bundle bundle, int requestCode);

    void intentForResultAnimation(@NotNull Class clazz, @AnimRes int in, @AnimRes int out, int requestCode);

    void intentForResultAnimation(@NotNull Class clazz, @AnimRes int in, @AnimRes int out, @NonNull Bundle bundle, int requestCode);

}

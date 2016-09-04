package zhe84300975.baseframe.core.plugin;

import android.os.Bundle;
import android.support.annotation.NonNull;

import zhe84300975.baseframe.view.BaseActivity;
import zhe84300975.baseframe.view.BaseBuilder;


/**
 * Created by zhaowencong on 16/8/24.
 * Describe:activty 拦截器
 */
public interface BaseActivityInterceptor {

    void build(BaseBuilder initialBaseBuilder);

    void onCreate(BaseActivity baseIView, Bundle bundle, Bundle savedInstanceState);

    void onStart(BaseActivity baseIView);

    void onResume(BaseActivity baseIView);

    void onPause(BaseActivity baseIView);

    void onStop(BaseActivity baseIView);

    void onDestroy(BaseActivity baseIView);

    void onRestart(BaseActivity baseIView);

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    BaseActivityInterceptor NONE	= new BaseActivityInterceptor() {

        @Override public void build(BaseBuilder initialBaseBuilder) {

        }

        @Override public void onCreate(BaseActivity baseIView, Bundle bundle, Bundle savedInstanceState) {

        }

        @Override public void onStart(BaseActivity baseIView) {

        }

        @Override public void onResume(BaseActivity baseIView) {

        }

        @Override public void onPause(BaseActivity BaseIView) {

        }

        @Override public void onStop(BaseActivity baseIView) {

        }

        @Override public void onDestroy(BaseActivity baseIView) {

        }

        @Override public void onRestart(BaseActivity BaseIView) {

        }

        @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        }
    };

}

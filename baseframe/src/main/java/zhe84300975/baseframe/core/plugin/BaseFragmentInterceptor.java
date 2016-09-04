package zhe84300975.baseframe.core.plugin;

import android.os.Bundle;

import zhe84300975.baseframe.view.BaseFragment;


/**
 * Created by zhaowencong on 16/8/24.
 * Describe: fragment拦截器
 */
public interface BaseFragmentInterceptor {

    void onFragmentCreated(BaseFragment baseeFragment, Bundle bundle, Bundle savedInstanceState);

    void onFragmentStart(BaseFragment baseFragment);

    void onFragmentResume(BaseFragment baseFragment);

    void onFragmentPause(BaseFragment baseFragment);

    void onFragmentStop(BaseFragment baseFragment);

    void onFragmentDestroy(BaseFragment baseFragment);

    BaseFragmentInterceptor NONE = new BaseFragmentInterceptor() {
        @Override
        public void onFragmentCreated(BaseFragment baseFragment, Bundle bundle, Bundle savedInstanceState) {

        }

        @Override
        public void onFragmentStart(BaseFragment baseFragment) {

        }

        @Override
        public void onFragmentResume(BaseFragment baseFragment) {

        }

        @Override
        public void onFragmentPause(BaseFragment baseFragment) {

        }

        @Override
        public void onFragmentStop(BaseFragment baseFragment) {

        }

        @Override
        public void onFragmentDestroy(BaseFragment baseFragment) {

        }
    };

}

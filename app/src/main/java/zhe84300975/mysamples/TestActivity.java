package zhe84300975.mysamples;

import android.os.Bundle;

import zhe84300975.baseframe.core.BaseIBiz;
import zhe84300975.baseframe.core.Impl;
import zhe84300975.baseframe.view.BaseActivity;
import zhe84300975.baseframe.view.BaseBuilder;


/**
 * Created by zhaowencong on 16/8/16.
 */

@Impl(TestActivity.class)
interface ITestActivity  {

    void lala();

    /**
     * 展示文案
     */
    void showText();
}


public class TestActivity extends BaseActivity<ITestBiz> implements ITestActivity{


    @Override
    protected BaseBuilder build(BaseBuilder initialBaseBuilder) {
        initialBaseBuilder.layoutId(R.layout.testlayout);
        return initialBaseBuilder;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        biz().sdf();
    }

    @Override
    public void lala() {

    }

    @Override
    public void showText() {

    }
}

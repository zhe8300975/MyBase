package zhe84300975.mysamples;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import zhe84300975.baseframe.core.BaseBiz;
import zhe84300975.baseframe.core.BaseIBiz;
import zhe84300975.baseframe.core.Impl;
import zhe84300975.baseframe.moudles.log.L;
import zhe84300975.baseframe.moudles.methodProxy.Background;

/**
 * Created by zhaowencong on 16/8/16.
 */
@Impl(TextBiz.class)
public interface ITestBiz extends BaseIBiz {

    @Background()
    void sdf();

}


class TextBiz extends BaseBiz<ITestActivity> implements ITestBiz {

    Call<LoginBean> loginBeanCall;


    @Override
    public void sdf() {
        ui().lala();
        try {
            LoginBean body;
            if (loginBeanCall == null) {
                HashMap<String,String> hashMap=new HashMap<String, String>();
                hashMap.put("grant_type", "password");
                hashMap.put("client_id","yYotOH8iXeuleN8Ab4hRAPI3bVndMa");
                hashMap.put("client_secret","X9iC2FSzPDRid9rX7jtGwgpfkDfNfy");
                hashMap.put("username","17801056327");
                hashMap.put("password", "zhaoxiaoying1");
                hashMap.put("scope","basic user_info savings_r savings_w wallet_r wallet_w");
                loginBeanCall = http(ITestService.class).goLogin(hashMap);
                body = loginBeanCall.execute().body();
            } else {
                body=loginBeanCall.clone().execute().body();
            }

            ui().showText();

            if (body != null) {
                L.i("zwc----------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}


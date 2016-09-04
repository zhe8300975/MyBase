package zhe84300975.mysamples;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by zhaowencong on 16/8/17.
 */
public interface ITestService {

    @FormUrlEncoded
    @POST("/oauth/token")                                                                           //登录
    Call<LoginBean> goLogin(@FieldMap HashMap<String, String> map);
}

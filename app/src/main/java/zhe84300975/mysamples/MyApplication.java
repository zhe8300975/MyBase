package zhe84300975.mysamples;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import zhe84300975.baseframe.BaseApplication;
import zhe84300975.baseframe.moudles.methodProxy.BaseMethods;

/**
 * Created by zhaowencong on 16/8/16.
 */
public class MyApplication extends BaseApplication {
    @Override
    public boolean isLogOpen() {
        return true;
    }

    @Override
    public Retrofit getRestAdapter(Retrofit.Builder builder) {
        builder.baseUrl("https://www.guihua.com");
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
        okhttpBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        okhttpBuilder.connectTimeout(20, TimeUnit.SECONDS);
        okhttpBuilder.readTimeout(20, TimeUnit.SECONDS);
        okhttpBuilder.writeTimeout(20, TimeUnit.SECONDS);
        builder.client(okhttpBuilder.build());
        builder.addConverterFactory(GsonConverterFactory.create());

        return builder.build();

    }

    @Override
    public BaseMethods getMethodInterceptor(BaseMethods.Builder builder) {
        return builder.build();
    }

    @Override
    public int layoutLoading() {
        return 0;
    }

    @Override
    public int layoutEmpty() {
        return 0;
    }

    @Override
    public int layoutBizError() {
        return 0;
    }

    @Override
    public int layoutHttpError() {
        return 0;
    }
}

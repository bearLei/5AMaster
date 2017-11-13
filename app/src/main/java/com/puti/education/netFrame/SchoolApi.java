package com.puti.education.netFrame;

import android.util.Log;

import com.puti.education.bean.Schools;
import com.puti.education.listener.BaseListener;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/8/31 0031.
 */

public class SchoolApi {
    private static OkHttpClient client = null;
    private static Retrofit retrofit = null;
    private static SchoolService mServiceApi = null;
    private static SchoolApi gSchoolApi = null;
    //获取学校列表
    public static String BASE_URL = "http://54.223.26.249:925/";

    interface SchoolService {
        @GET("schoollist.xml" )
        Call<Schools> getSchoolUrlList();
    }



    public static SchoolApi getInstance(){
        if (gSchoolApi == null){
            gSchoolApi = new SchoolApi();
            return gSchoolApi;
        }
        return  gSchoolApi;
    }
    private SchoolApi(){
        initRetrofit();
    }

    public static Retrofit initRetrofit() {
        client = setDefaultBuilder();
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client).build();

        return retrofit;
    }

    public static void getSchoolUrls(final BaseListener listener){
        mServiceApi = retrofit.create(SchoolService.class);
        Call<Schools> call = mServiceApi.getSchoolUrlList();
        call.enqueue(new Callback<Schools>() {
            @Override
            public void onResponse(Call<Schools> call, Response<Schools> response) {
                //请求成功操作
                Schools data = response.body();
                Log.d("", "获取学校列表数据 " + data);
                if (data != null){
                    listener.responseResult(data, null, 1, true);
                }
            }
            @Override
            public void onFailure(Call<Schools> call, Throwable t) {
                //请求失败操作
                Log.d("", "获取学校列表出错 " + t.getMessage());
            }
        });
    }

    private static OkHttpClient setDefaultBuilder() {
        OkHttpClient.Builder mBuilder = new OkHttpClient().newBuilder();
        //设置超时
        mBuilder.connectTimeout(RetrofitUtil.DEFAULT_ITMEOUT, TimeUnit.SECONDS);
        mBuilder.writeTimeout(RetrofitUtil.DEFAULT_ITMEOUT, TimeUnit.SECONDS);
        mBuilder.readTimeout(RetrofitUtil.DEFAULT_ITMEOUT, TimeUnit.SECONDS);
        if (RetrofitUtil.DEBUG) {
            mBuilder.addNetworkInterceptor(new NetWorkInterceptor());
        }
        return mBuilder.build();
    }

}

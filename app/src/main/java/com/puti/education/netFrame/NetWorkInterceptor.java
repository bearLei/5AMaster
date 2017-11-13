package com.puti.education.netFrame;

import android.text.TextUtils;
import android.util.Log;
import com.puti.education.util.Key;
import com.puti.education.util.LogUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 *  请求拦截器
 *
 *  通过添加token修改请求
 *
 */
public class NetWorkInterceptor implements Interceptor{

    public static String gToken;
    public static int gType = 1;    //1 一般数据接口，2 文件上传接口

    public NetWorkInterceptor(){

    }

    public static void setToken(String token){
        gToken = "Basic " + token;
    }
    public static void resetToken(){
        gToken = "";
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            Request.Builder builder = request.newBuilder();

            if (!TextUtils.isEmpty(gToken)){
                //request = builder.addHeader(Key.TOKEN, token).build();
                request = builder.addHeader(Key.AUTHORIZATION, gToken).build();
                Log.d("", "gToken:" + gToken);
            }

            if (gType == 1) {
                request = builder.addHeader(Key.CONTENT_TYPE, "application/json; charset=utf-8").build();
            }

            String url = request.url().toString();
            Log.i("url",url);
            String method = request.method();
            long t1 = System.nanoTime();


            RequestBody requestBody = request.body();


            Response response = chain.proceed(request);
            long t2 = System.nanoTime();

            ResponseBody body = response.body();

            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = Charset.defaultCharset();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String bodyString = buffer.clone().readString(charset);
            LogUtil.i("body",bodyString);


        return response;
    }

}

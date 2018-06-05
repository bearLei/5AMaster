package com.puti.education.netFrame;

import android.text.TextUtils;
import android.util.Log;

import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.PatriarchModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.netFrame.netModel.UploadModel;
import com.puti.education.util.LogUtil;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import unit.api.PutiCommonModel;

public class RetrofitUtil {

    //正式环境
    public static String BASE_URL = "http://54.223.26.249:8083";
    //测试环境
    //public static String BASE_URL  = "http://54.223.26.249:8090/ms/api/";
    public static String PHOTO_URL = "http://54.223.26.249:8083";

    //113.98.235.212
    public final static int DEFAULT_ITMEOUT = 30;
    public final static boolean DEBUG = true;

    private static OkHttpClient client = null;
    private static Retrofit retrofit = null;

    private static Retrofit uploadRetrofit = null;

    public static void setSchoolBaseUrl(String preUrl){
        if (!TextUtils.isEmpty(preUrl)){
            //BASE_URL = preUrl + "/ms/api/";
            BASE_URL = preUrl;
            PHOTO_URL= preUrl + "";
        }
        retrofit = null;
        CommonModel.clearInstance();
        PatriarchModel.clearInstance();
        TeacherModel.clearInstance();
        StudentModel.clearInstance();
        UploadModel.clearInstance();
        PutiCommonModel.clearInstance();
    }

    public static Retrofit initRetrofit() {
        Log.d("lei","initRetrofit");
        if (!isHttps(BASE_URL)) {
            client = setDefaultBuilder();
        } else {
            client = setWrapTLSClient();
        }

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(client).build();

        return retrofit;
    }

    public static Retrofit initUploadRetrofit() {

        OkHttpClient uploadclient;
        if (!isHttps(BASE_URL)) {
            uploadclient = setUploadBuilder();
        } else {
            uploadclient = setWrapTLSClient();
        }

        uploadRetrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(uploadclient).build();

        return retrofit;
    }


    public static synchronized Retrofit getRetrofit(){
        Log.d("lei","getRetrofit");
        if (retrofit == null){
            synchronized (RetrofitUtil.class) {
                if (retrofit == null) {
                    initRetrofit();
                }
            }
        }
       return retrofit;
    }

    public static synchronized Retrofit getUploadRetrofit(){
        if (uploadRetrofit == null){
            synchronized (RetrofitUtil.class) {
                if (uploadRetrofit == null) {
                    initUploadRetrofit();
                }
            }
        }
        return uploadRetrofit;
    }


    public static boolean isHttps(String url) {
        if (url.startsWith("https")) {
            return true;
        }
        return false;
    }

    private static OkHttpClient setDefaultBuilder() {
        OkHttpClient.Builder mBuilder = new OkHttpClient().newBuilder();
        //设置超时
        mBuilder.connectTimeout(DEFAULT_ITMEOUT, TimeUnit.SECONDS);
        mBuilder.writeTimeout(DEFAULT_ITMEOUT, TimeUnit.SECONDS);
        mBuilder.readTimeout(DEFAULT_ITMEOUT, TimeUnit.SECONDS);
        if (DEBUG) {
            mBuilder.addNetworkInterceptor(new NetWorkInterceptor());
        }
        Log.d("lei","Buider-->"+mBuilder.toString());
        return mBuilder.build();
    }

    private static OkHttpClient setUploadBuilder() {
        OkHttpClient.Builder mBuilder = new OkHttpClient().newBuilder();
        //设置超时
        mBuilder.connectTimeout(DEFAULT_ITMEOUT, TimeUnit.SECONDS);
        mBuilder.writeTimeout(DEFAULT_ITMEOUT, TimeUnit.SECONDS);
        mBuilder.readTimeout(DEFAULT_ITMEOUT, TimeUnit.SECONDS);

        mBuilder.addNetworkInterceptor(new NetWorkUploadInterceptor());
        Log.d("lei","Buider--->"+mBuilder);
        return mBuilder.build();
    }


    public static OkHttpClient setWrapTLSClient() {

        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        }};

        final HostnameVerifier hostnameVerifier = new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        OkHttpClient sslHttpClient = null;
        try {

            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder mBuilder = new OkHttpClient.Builder()
                    .readTimeout(DEFAULT_ITMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(DEFAULT_ITMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_ITMEOUT, TimeUnit.SECONDS)
                    .addNetworkInterceptor(new NetWorkInterceptor())
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(hostnameVerifier);
                    sslHttpClient = mBuilder.build();

        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return null;
        } catch (KeyManagementException kme) {
            kme.printStackTrace();
            return null;
        }
        return sslHttpClient;
    }
}

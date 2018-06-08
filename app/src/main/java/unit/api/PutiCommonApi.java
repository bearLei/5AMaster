package unit.api;

import com.puti.education.netFrame.response.ResponseInfo;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import unit.base.BaseResponseInfo;
import unit.base.PutiBaseModel;

/**
 * Created by lei on 2018/6/4.
 */

public interface PutiCommonApi {
    @POST("Common/Login")//登录
    Observable<BaseResponseInfo> login(@Body RequestBody route);

    @GET("Common/verify")//请求验证码
    Observable<BaseResponseInfo> queryVerify(@Query("refer")int refer);

    @GET("Common/getConfig")//获取APP配置信息
    Observable<BaseResponseInfo> getAppConfig(@Query("Uid") String uid);

    @GET("Teacher/queryCount")//查询首页统计信息
    Observable<BaseResponseInfo>getHomeCountInfo(@Query("Uid") String uid);

    @GET("Teacher/getMessageList")//获取消息列表
    Observable<BaseResponseInfo>getMessageList(
            @Query("Uid") String uid,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize);

    @POST("Common/updatePassword")//修改密码
    Observable<BaseResponseInfo> updatePsw(@Body RequestBody route );

    @GET("/Common/Logout")//退出登录
    Observable<BaseResponseInfo> logout();

}

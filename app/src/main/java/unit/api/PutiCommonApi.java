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
    @POST("Common/Login")
    Observable<BaseResponseInfo> login(@Body RequestBody route);

    @GET("Common/verify")
    Observable<BaseResponseInfo> queryVerify(@Query("refer")int refer);

    @GET("Common/getConfig")
    Observable<BaseResponseInfo> getAppConfig(@Query("Uid") String uid);

    @GET("Teacher/queryCount")
    Observable<BaseResponseInfo>getHomeCOuntInfo(@Query("Uid") String uid);
}

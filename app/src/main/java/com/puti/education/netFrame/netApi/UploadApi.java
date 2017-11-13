package com.puti.education.netFrame.netApi;


import com.puti.education.netFrame.response.ResponseInfo;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * GET , POSET 请求传参方式不同
 * 若有参数是可选的，当参数不需要传时，设置参数值为null，则框架会自动忽略
 *
 *  @FormUrlEncoded
    @POST("warehouse/productsend.json")
    Observable<ResponseInfo> outSku(@Field("id") String id, @Field("warehouseId") int warehouseId, @Field("storeId") int storeId, @Field("items") String items);
 */
public interface UploadApi {

    /*------上传文件相关-------*/
    /**
     * 上传一张图片
     * @param description
     * @param imgs
     * @return
     */
    @Multipart
    @POST("/upload")
    Call<String> uploadImage(@Part("fileName") String description,
                             @Part("file\"; filename=\"image.png\"") RequestBody imgs);

    /**
     * 上传多张图片
     * @param imgs1
     * @return
     */
    @Multipart
    @POST("teeke/upload")
    Observable<ResponseInfo> uploadImage(@PartMap
                                                 Map<String, RequestBody> imgs1);

    /**
     * 上传多个音频文件
     * @param imgs1
     * @return
     */
    @Multipart
    @POST("teeke/upload")
    Observable<ResponseInfo> uploadAudio(@PartMap
                                                 Map<String, RequestBody> imgs1);

    /**
     * 上传多个视频文件
     * @param imgs1
     * @return
     */
    @Multipart
    @POST("teeke/upload")
    Observable<ResponseInfo> uploadVideo(@PartMap
                                                 Map<String, RequestBody> imgs1);

 /**
  * 上传多张文件
  * @param imgs1
  * @return
  */
  @Multipart
  @POST
  Observable<ResponseInfo> uploadImageTest(@Url String url, @PartMap
          Map<String, RequestBody> imgs1);


}
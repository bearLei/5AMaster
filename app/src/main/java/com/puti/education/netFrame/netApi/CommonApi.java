package com.puti.education.netFrame.netApi;


import com.puti.education.netFrame.response.ResponseInfo;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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
public interface CommonApi {

    @POST("user/applogin")
    Observable<ResponseInfo> loginEx(@Body RequestBody route);

    @POST("base/sendVerifyCode")
    Observable<ResponseInfo> getVerifyCode(@Body RequestBody route);

    @POST()
    Observable<ResponseInfo> getVerifyCodeEx(@Url String url, @Body RequestBody route);

    @POST("user/logout")
    Observable<ResponseInfo> logout();

    @POST("user/forgetPassword")
    Observable<ResponseInfo> forgetPwd(@Body RequestBody route);

    @GET("schoollist.xml")
    Observable<String> schoolUrl();

    @GET("base/schoollist")
    Observable<ResponseInfo> schoolList();

    @GET("base/teacherlist")
    Observable<ResponseInfo> teacherList(@Query("schoolUid") String schoolId, @Query("keyword") String keyword);
    @GET("base/studentlist")
    Observable<ResponseInfo> studentList(@Query("classUid") String classId,@Query("keyword") String keyword);
    //班级列表
    @GET("base/gradeclass")
    Observable<ResponseInfo> getClassList(@Query("schoolUid") String schoolId);

    @GET("base/headteacherlist")
    Observable<ResponseInfo> getHeadTeacherList(@Query("schoolId") int schoolId);

    @GET("base/expertlist")
    Observable<ResponseInfo> getProfessorList();

    @GET("rate/teacherlist")
    Observable<ResponseInfo> rateTeacherList();

    @GET("rate/studentlist")
    Observable<ResponseInfo> rateStudentList();

    @GET("rate/parentslist")
    Observable<ResponseInfo> rateParentList();

    @GET("rate/studentdetail")
    Observable<ResponseInfo> ratePeopleDetail(@Query("uid") String peopleId);

    @GET("rate/teacherdetail")
    Observable<ResponseInfo> rateTeacherDetail(@Query("uid") String peopleId);

    @GET("rate/parentdetail")
    Observable<ResponseInfo> rateParentDetail(@Query("uid") String peopleId);

    @GET("rate/eventdetail")
    Observable<ResponseInfo> rateEventDetail();

    @POST("rate/student")
    Observable<ResponseInfo> ratePeople(@Body RequestBody route);

    @POST("rate/event")
    Observable<ResponseInfo> rateEvent(@Body RequestBody route);

    @POST("rate/teacher")
    Observable<ResponseInfo> rateTeacher(@Body RequestBody route);

    @POST("rate/parent")
    Observable<ResponseInfo> rateParent(@Body RequestBody route);

    @GET("base/studentinfo")
    Observable<ResponseInfo> getStudentDetail(@Query("uid") String id);

    @GET("base/teacherinfo")
    Observable<ResponseInfo> getTeacherDetail(@Query("uid") String id);

    //通知中心
    @GET("notice/pagelist")
    Observable<ResponseInfo> getMsgList(@Query("pageIndex") int pageIndex,@Query("pageSize") int pageSize);

    //获取学校课程
    @GET("base/courselist")
    Observable<ResponseInfo> getCourseList(@Query("schoolUid") String schoolUid);

    //通知详情
    @GET("notice/{uid}")
    Observable<ResponseInfo> getMsgDetail(@Path("uid") String uid);

    //事件地点
    @GET("teacher/address")
    Observable<ResponseInfo> getEventAddressList();

    /**
     * 返回问卷调查列表
     * @return
     */
    @GET("questionnaire/list")
    Observable<ResponseInfo> questionnaireList();

    //获取事件类型
    @GET("event/type")
    Observable<ResponseInfo> getEventTypeList();

    /**
     * 返回问卷调查详情
     * @return
     */
    @GET("questionnaire/{uid}")
    Observable<ResponseInfo> questionnaireDetail(@Path("uid") String uid);


    /**
     * 提交问卷
     * @return
     */
    @POST("questionnaire/commit")
    Observable<ResponseInfo> questionnaireCommit(@Body RequestBody route);

    /**
     * 获取最新通告
     */
    @GET("base/newnotice")
    Observable<ResponseInfo> getLatestNotice();


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


    /**
     * 上报当前位置
     */
    @POST("base/latlng")
    Observable<ResponseInfo> reportLocation(@Body RequestBody route);

    //通知中心
    @GET("event/evaluationlist")
    Observable<ResponseInfo> getEventEvaluationList(@Query("pageIndex") int pageIndex,@Query("pageSize") int pageSize);

    //互评事件
    @POST("event/evaluation")
    Observable<ResponseInfo> evaluationEvent(@Body RequestBody route);

}
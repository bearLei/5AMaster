package com.puti.education.netFrame.netApi;


import com.puti.education.netFrame.response.ResponseInfo;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface TeacherApi {

    //教师首页数据
    @GET("teacher/index")
    Observable<ResponseInfo> getTeacherHomeData();

    //添加异常事件
    @POST("teacher/abnormalevent")
    Observable<ResponseInfo> addAbnormalEvent(@Body RequestBody requestBody);

    //添加普通事件
    @POST("teacher/normalevent")
    Observable<ResponseInfo> addNormalEvent(@Body RequestBody requestBody);

    //事件列表
    @GET("teacher/eventlist")
    Observable<ResponseInfo> getEventList(@Query("type") String type, @Query("status") String status,@Query("pageIndex") int pageIndex,@Query("pageSize") int pageSize);

    //教师事件列表
    @GET("teacher/involStudentList")
    Observable<ResponseInfo> getTeacherEventList(@Query("eventType") int type, @Query("status") String status,@Query("pageIndex") int pageIndex,@Query("pageSize") int pageSize);


    @GET("involvedstudent/eventdetail")
    Observable<ResponseInfo> eventDetail(@Query("eventUID") String eventid, @Query("studentUID") String studentid);

    //新增事件
    @FormUrlEncoded
    @POST("teacher/questionnaireadd")
    Observable<ResponseInfo> addEvent(@Field("title") String title,@Field("question") String question);

    //事件问卷
    @GET("teacher/questionnairelist")
    Observable<ResponseInfo> getTeacherQuestionnaireList();

    //新增事件问券
    @POST("teacher/questionnaireadd")
    Observable<ResponseInfo> addEventQuestionaire(@Body RequestBody requestBody);

    //个人资料
    @GET("teacher/info")
    Observable<ResponseInfo> getTeacherInfo();

    //个人资料修改
    @POST("teacher/update")
    Observable<ResponseInfo> editTeacherPersonInfo(@Body RequestBody requestBody);

    //责任等级
    @GET("teacher/involvedtypelist")
    Observable<ResponseInfo> dutyWarningList();

    //警告等级
    @GET("teacher/warninglist")
    Observable<ResponseInfo> warningList();

    //处置标准（包括加减分，责任等级等）
    @GET("teacher/dealstandard")
    Observable<ResponseInfo> getDealstandard(@Query("eventType") int type);

    //事件确认
    @POST("headteacher/involStudentConfirm")
    Observable<ResponseInfo> commitConfirm(@Body RequestBody body);

    //事件处理
    @POST("headteacher/involStudentDeal")
    Observable<ResponseInfo> commitDeal(@Body RequestBody body);

    //提交审核
    @POST("studentaffairs/involStudentValid")
    Observable<ResponseInfo> commitAudit(@Body RequestBody body);

    //事件跟进
    @POST("event/eventtrack")
    Observable<ResponseInfo> eventTrack(@Body RequestBody body);

    //结束事件
    @POST("headteacher/involStudentEnd")
    Observable<ResponseInfo> eventFinished(@Body RequestBody body);

    //教师评论其他教师处理的事件
    @POST("event/opinion")
    Observable<ResponseInfo> commentOpinion(@Body RequestBody body);

    //事件地点
    @GET("teacher/address")
    Observable<ResponseInfo> eventAddressList();

    //推荐课程
    @GET("teacher/tutoring")
    Observable<ResponseInfo> recommandTutor();

    //获取事件处理模板（个案分析和措施对策）
    @GET("teacher/dealtemplate")
    Observable<ResponseInfo> getDealTemplate(@Query("type") int type);

    //获取家长培训实践记录）
    @GET("teacher/parentTrainlist")
    Observable<ResponseInfo> getParentTrainList(@Query("type") int type, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    //获取学生培训实践记录）
    @GET("teacher/studentTrainlist")
    Observable<ResponseInfo> getStudentTrainList(@Query("type") int type, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    //学生处老师获取巡检记录）
    @GET("studentaffairs/patrolRecords")
    Observable<ResponseInfo> getDetectiveList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    //学生处老师确认巡检记录）
    @POST("studentaffairs/confirmPatrol")
    Observable<ResponseInfo> confirmDetective(@Body RequestBody body);

    //学生处老师获取学生举报记录）
    @GET("studentaffairs/reportList")
    Observable<ResponseInfo> getStudentReportList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    //学生处老师确认举报记录）
    @POST("studentaffairs/confirmReport")
    Observable<ResponseInfo> confirmReport(@Body RequestBody body);

}
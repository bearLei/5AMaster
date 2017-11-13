package com.puti.education.netFrame.netModel;


import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.CommonSubscriber;
import com.puti.education.netFrame.RetrofitUtil;
import com.puti.education.netFrame.netApi.TeacherApi;
import com.puti.education.netFrame.response.ResponseInfo;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TeacherModel extends BaseModel {
    private TeacherApi mTeacherApi = null;
    private static TeacherModel gTeacherModel = null;
    private MediaType mMediaType;

    public TeacherModel() {
        mTeacherApi = RetrofitUtil.getRetrofit().create(TeacherApi.class);
        mMediaType = okhttp3.MediaType.parse("application/json; charset=utf-8");
    }

    public static TeacherModel getInstance() {
        if (gTeacherModel == null) {
            gTeacherModel = new TeacherModel();
            return gTeacherModel;
        }
        return gTeacherModel;
    }

    public static void clearInstance(){
        gTeacherModel = null;
    }

    //教师首页数据
    public void getTeacherHomeData(final BaseListener baseListener){
        mTeacherApi.getTeacherHomeData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    //添加异常事件
    public void addAbnormalEvent(String bodyStr,final BaseListener baseListener) {

        RequestBody body=RequestBody.create(mMediaType,bodyStr);
        mTeacherApi.addAbnormalEvent(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //添加异常事件
    public void addNormalEvent(String bodyStr,final BaseListener baseListener) {

        RequestBody body=RequestBody.create(mMediaType,bodyStr);
        mTeacherApi.addNormalEvent(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }


    //教师事件问券
    public void getQuestionNaireaList(final BaseListener baseListener){

        mTeacherApi.getTeacherQuestionnaireList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //事件列表
    public void getEventList(String type,String status,int pageIndex,int pageSize,final BaseListener baseListener){

        mTeacherApi.getEventList(type,  status,  pageIndex,  pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });

    }

    //教师学生事件列表
    public void getTeacherEventList(int type,String status,int pageIndex,int pageSize,final BaseListener baseListener){

        mTeacherApi.getTeacherEventList(type,  status,  pageIndex,  pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });

    }

    //事件详情
    public void eventDetail(String eventid,String studentuid, final BaseListener baseListener){

        mTeacherApi.eventDetail(eventid, studentuid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }


    //新增问券
    public void addNetQuestionaire(String paramStr,final BaseListener baseListener){

        RequestBody body = RequestBody.create(mMediaType,paramStr);

        mTeacherApi.addEventQuestionaire(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //教师个人信息
    public void getTeacherInfo(final BaseListener baseListener){

        mTeacherApi.getTeacherInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //修改教师个人信息
    public void modifyTeacherInfo(String paramStr,final BaseListener baseListener){

        RequestBody body = RequestBody.create(mMediaType,paramStr);

        mTeacherApi.editTeacherPersonInfo(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //责任等级
    public void getDutyWarnList(final BaseListener baseListener){

        mTeacherApi.dutyWarningList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //警告等级
    public void getWarnList(final BaseListener baseListener){
        mTeacherApi.warningList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    //处置标准
    public void getDealstandard(int type, final BaseListener baseListener){
        mTeacherApi.getDealstandard(type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    //事件确认
    public void commitEventConfirm(String bodyStr,final BaseListener baseListener) {

        RequestBody body=RequestBody.create(mMediaType,bodyStr);
        mTeacherApi.commitConfirm(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });

    }

    //提交处理
    public void commitEventDeal(String bodyStr,final BaseListener baseListener) {

        RequestBody body=RequestBody.create(mMediaType,bodyStr);
        mTeacherApi.commitDeal(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });

    }

    //提交审核
    public void commitEventAudit(String bodyStr,final BaseListener baseListener) {

        RequestBody body=RequestBody.create(mMediaType,bodyStr);
        mTeacherApi.commitAudit(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });

    }

    //事件跟进
    public void eventTrack(String bodyStr,final BaseListener baseListener){

        RequestBody body=RequestBody.create(mMediaType,bodyStr);
        mTeacherApi.eventTrack(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });
    }

    //事件结束
    public void eventFinished(String bodyStr,final BaseListener baseListener){

        RequestBody body=RequestBody.create(mMediaType,bodyStr);
        mTeacherApi.eventFinished(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });

    }

    //事件地点
    public void getEventAddressList(final BaseListener baseListener){

        mTeacherApi.eventAddressList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //教师评论
    public void commentOpinion(String bodyStr,final BaseListener baseListener){

        RequestBody body=RequestBody.create(mMediaType,bodyStr);

        mTeacherApi.commentOpinion(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });

    }

    //推荐课程
    public void recommandCourse(final BaseListener baseListener){

        mTeacherApi.recommandTutor().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });

    }

    //获取事件处理模板
    public void getDealTemplate(int type,final BaseListener baseListener){

        mTeacherApi.getDealTemplate(type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });

    }

    //获取家长培训实践记录
    public void getParentTrainList(int type, int pageIndex, int pageSize, final BaseListener baseListener){
        mTeacherApi.getParentTrainList(type, pageIndex, pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });
    }

    //获取学生培训实践记录
    public void getStudentTrainList(int type, int pageIndex, int pageSize, final BaseListener baseListener){
        mTeacherApi.getStudentTrainList(type, pageIndex, pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });
    }


    //获取巡检记录
    public void getDetectiveList(int pageIndex, int pageSize, final BaseListener baseListener){
        mTeacherApi.getDetectiveList(pageIndex, pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });
    }

    //获取学生举报记录
    public void getStudentReportList(int pageIndex, int pageSize, final BaseListener baseListener){
        mTeacherApi.getStudentReportList(pageIndex, pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });
    }

    //确认巡检为事件
    public void confirmDetective(String bodyStr,final BaseListener baseListener){

        RequestBody body=RequestBody.create(mMediaType,bodyStr);

        mTeacherApi.confirmDetective(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });

    }

    //确认举报为事件
    public void confirmReport(String bodyStr,final BaseListener baseListener){

        RequestBody body=RequestBody.create(mMediaType,bodyStr);

        mTeacherApi.confirmReport(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });

    }
}


package unit.api;

import android.os.Build;

import com.puti.education.App;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.CommonSubscriber;
import com.puti.education.netFrame.RetrofitUtil;
import com.puti.education.netFrame.netApi.CommonApi;
import com.puti.education.netFrame.netModel.BaseModel;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.response.ResponseInfo;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import unit.base.BaseResponseInfo;
import unit.base.PutiBaseModel;
import unit.base.PutiCommonSubscriber;
import unit.entity.EventBase;
import unit.entity.VerifyPostInfo;
import unit.util.UserInfoUtils;

/**
 * Created by lei on 2018/6/4.
 */

public class PutiCommonModel extends PutiBaseModel{

    private PutiCommonApi mCommonApi = null;
    private static PutiCommonModel gCommonModel = null;
    private MediaType mMediaType;

    public PutiCommonModel(){
        mCommonApi = RetrofitUtil.getRetrofit().create(PutiCommonApi.class);
        mMediaType = okhttp3.MediaType.parse("application/json; charset=utf-8");
    }

    public static PutiCommonModel getInstance(){
        if (gCommonModel == null){
            gCommonModel = new PutiCommonModel();
            return gCommonModel;
        }
        return  gCommonModel;
    }

    public static void clearInstance(){
        gCommonModel = null;
    }

        /******************api调用开始***********************************/

    /**
     *
     * @param userName 登录账号
     * @param psd 登录密码
      * @param verifyInfo 验证码信息
     * @param deviceInfo 设备信息
     * @param baseListener 回调
     */
    public void login(String userName, String psd, VerifyPostInfo verifyInfo, String deviceInfo, final BaseListener baseListener){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"LoginName\":\"").append(userName).append("\"");
        sb.append(",\"PassWord\":\"").append(psd).append("\"");
        sb.append("}");
//        sb.append(",\"verifyCode\":{").append("\"uuidKey\":\"").append(verifyInfo.getUuidKey());
//        sb.append(",\"vericode\":\""+verifyInfo.getVericode()+"}");
//        sb.append(",\"deviceInfo\":{");
//        sb.append("\"type\":10,");
//        sb.append("\"deviceDesc\":\"" + Build.MODEL+ "\",");
//        sb.append("\"pushId\":\"" + App.mJPushRegId + "\",");
//        sb.append("\"deviceToken\":\" \"}}");

        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),sb.toString());
        mCommonApi.login(body).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new PutiCommonSubscriber(baseListener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,baseListener);
                    }
                });
    }

    /**
     *
     * @param refer 请求来源，1 登录
     * @param listener
     */
    public void queryVerify(int refer,final BaseListener listener){
        mCommonApi.queryVerify(refer).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }

    /**
     * 教师端查询消息列表
     * @param uid 查询的id
     * @param pageIndex 查询下标
     * @param pageSize 查询个数
     * @param listener 回调
     */
    public void queryMessageList(String uid,int pageIndex,int pageSize,final BaseListener listener){
        mCommonApi.getMessageList(uid, pageIndex, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }

    /**
     * 教师端查询首页统计信息
     * @param uid 查询id
     * @param listener 回调
     */
    public void queryCountInfo(String uid,final BaseListener listener){
        mCommonApi.getHomeCountInfo(uid)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }


    /**
     * 修改密码
     * @param uid 请求人id
     * @param oldPsw 旧密码
     * @param newPsw 新密码
     * @param listener 回调
     */
    public void updatePsw(String uid,String oldPsw,String newPsw,final BaseListener listener){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"Uid\":\"").append(uid).append("\"");
        sb.append(",\"oldPsw\":\"").append(oldPsw).append("\"");
        sb.append(",\"newPsw\":\"").append(newPsw).append("\"");
        sb.append("}");
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),sb.toString());

        mCommonApi.updatePsw(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }

    //退出登录
    public void logout(final BaseListener listener){
        mCommonApi.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }

    /**
     * 提交有奖反馈
     * @param suggestion 意见
     * @param listener
     */
    public void commitSuggestion(String suggestion,final  BaseListener listener){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"Suggestion\":\"").append(suggestion).append("\"");
        sb.append("}");
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),sb.toString());

        mCommonApi.commitSuggestion(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }

    /**
     * 获取事件类型
     * @param listener
     */
    public void getEventType(final BaseListener listener){
        if (UserInfoUtils.isInLoginStata()){
            String areaUid = UserInfoUtils.getAreaUid();

            mCommonApi.getEventType(areaUid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new PutiCommonSubscriber(listener){
                        @Override
                        public void onNext(BaseResponseInfo responseInfo) {
                            dealJson(responseInfo,listener);
                        }
                    });
        }
    }

    /**
     * 获取地址列表
     * @param type 0不限 1宿舍 2操场 3办公室 4教师 5食堂
     * @param listener
     */
    public void getAddress(int type,final BaseListener listener){
        mCommonApi.getAddress(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }

    /**
     * 教师端新增事件
     * @param eventStr
     * @param listener
     */
    public void addEvent(String eventStr, final BaseListener listener){
        RequestBody body=RequestBody.create(mMediaType,eventStr);
        mCommonApi.addEvent(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }

    /**
     * \
     * @param classUID 班级主键,为空时代表有权查看的班级（校管取所有班级，教师取所带班级
//     * @param studentName 学生名称，模糊查询
//     * @param eventTypeName 事件名称，模糊查询
     * @param status -1不限 0已拒绝 1处理中 2审核中 3追踪中 4已完结
     * @param pageIndex
     * @param pageSize
     * @param listener
     */
    public void queryEvent(String classUID, int status,
                           int pageIndex,int pageSize,final BaseListener listener
                           ){
        mCommonApi.queryEvent(classUID,  status, pageIndex, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }

    /**
     *
     * @param studentUid
     * @param termUID termUID
     * @param listener
     */
    public void queryPortrait(String studentUid,String termUID,final BaseListener listener){
        mCommonApi.queryPortrait(studentUid,termUID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }

    /**
     *
     * @param studentUid
     * @param termUID termUID
     * @param listener
     */
    public void queryStuInfo(String studentUid,String termUID,final BaseListener listener){
        mCommonApi.queryStuInfo(studentUid,termUID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }

    /**
     * 查询教师基础信息
     * @param teacherUID 教师id
     * @param listener
     */
    public void queryTeaInfo(String teacherUID ,final BaseListener listener){
        mCommonApi.queryTeacherInfo(teacherUID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                       dealJson(responseInfo,listener);
                    }
                });
    }

    /**
     *查询某学期的课表
     * @param classUId 班级Id
     * @param termUid 学期id
     */
    public void queryCoursInfo(String classUId,String termUid,final BaseListener listener){
        mCommonApi.queryCoursInfo(classUId,termUid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }


    /**
     * 获取事件详情
     * @param eventId 事件id
     * @param listener
     */
    public void queryEventDetail(String eventId,final BaseListener listener){
        mCommonApi.getEventDetail(eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }

}

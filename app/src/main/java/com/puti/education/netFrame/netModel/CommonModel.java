package com.puti.education.netFrame.netModel;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puti.education.App;
import com.puti.education.bean.Question;
import com.puti.education.bean.Questionnaire;
import com.puti.education.bean.RateEventItem;
import com.puti.education.bean.RatePeopleItem;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.CommonSubscriber;
import com.puti.education.netFrame.NetWorkInterceptor;
import com.puti.education.netFrame.RetrofitUtil;
import com.puti.education.netFrame.netApi.CommonApi;
import com.puti.education.netFrame.response.ResponseInfo;
import com.puti.education.util.Constant;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 家长Presenter
 */
public class CommonModel extends BaseModel{

    private CommonApi mCommonApi = null;
    private static CommonModel gCommonModel = null;
    private MediaType mMediaType;

    public CommonModel(){
        mCommonApi = RetrofitUtil.getRetrofit().create(CommonApi.class);
        mMediaType = okhttp3.MediaType.parse("application/json; charset=utf-8");
    }

    public static CommonModel getInstance(){
        if (gCommonModel == null){
            gCommonModel = new CommonModel();
            return gCommonModel;
        }
        return  gCommonModel;
    }

    public static void clearInstance(){
        gCommonModel = null;
    }

    public void getVerifyCode(String mobile, final BaseListener baseListener){
        String sb = "{\"mobile\":\"" + mobile + "\"}";
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),sb.toString());
        mCommonApi.getVerifyCode(body).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    public void login(String userName, String psd,String verifyCode, String deviceInfo, final BaseListener baseListener){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"loginName\":\"").append(userName).append("\"");
        sb.append(",\"password\":\"").append(psd).append("\"");
        sb.append(",\"verifyCode\":\"").append(verifyCode).append("\"");
        sb.append(",\"deviceInfo\":{");
             sb.append("\"type\":10,");
             sb.append("\"deviceDesc\":\"" + Build.MODEL+ "\",");
             sb.append("\"pushId\":\"" + App.mJPushRegId + "\",");
             sb.append("\"deviceToken\":\" \"}}");


        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),sb.toString());
        mCommonApi.loginEx(body).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    public void checkVersion(final BaseListener baseListener){
//        int versioncode = 78;
//       JSONObject jsonObject  = new JSONObject();
//        jsonObject.put("versioncode",78);
//        jsonObject.put("device",1);
//        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),jsonObject.toString());
        mCommonApi.checkVersion(78,1).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }
    public void logout(final BaseListener baseListener){

        mCommonApi.logout().subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    public void forgetPwd(String userName, String mobile, String verifyCode, String psd, final BaseListener baseListener){
        Map<String, Object> param = new HashMap<>();
        param.put("loginName", userName);
        param.put("mobile", mobile);
        param.put("verifyCode", verifyCode);
        param.put("newPwd", psd);

        String sb = JSONObject.toJSONString(param);

        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),sb.toString());
        mCommonApi.forgetPwd(body).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    public void getSchoolList(final BaseListener baseListener){
        mCommonApi.schoolList().subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }


    public void getTeacherList(String schoolId, String keyword, final BaseListener baseListener){
        mCommonApi.teacherList(schoolId, keyword).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    public void getStudentList(String classId,String keyword,final BaseListener baseListener){

        mCommonApi.studentList(classId, keyword).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    public void getClassList(String schoolId,final BaseListener baseListener){

        mCommonApi.getClassList(schoolId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //获取学校班主任列表
    public void getHeadTeacherList(int schoolId, final BaseListener baseListener){
        mCommonApi.getHeadTeacherList(schoolId).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    //专家
    public void getProfessorList(final BaseListener baseListener){
        mCommonApi.getProfessorList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    public void rateTeacherList(final BaseListener baseListener){

        mCommonApi.rateTeacherList().subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    public void rateStudentList(final BaseListener baseListener){

        mCommonApi.rateStudentList().subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    public void rateParentList(final BaseListener baseListener){

        mCommonApi.rateParentList().subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    public void ratePeopleDetail(String peopleId,final BaseListener baseListener){

        mCommonApi.ratePeopleDetail(peopleId).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    public void getRateTeacherDetail(String peopleId, final BaseListener baseListener){

        mCommonApi.rateTeacherDetail(peopleId).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    public void getRateParentDetail(String peopleId, final BaseListener baseListener){

        mCommonApi.rateParentDetail(peopleId).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    public void rateEventDetail(final BaseListener baseListener){
        mCommonApi.rateEventDetail().subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    public void ratePeople(String peopleItem, final BaseListener baseListener){
        //String sb = JSONObject.toJSONString(peopleItem);
        LogUtil.d("", "param: " + peopleItem);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),peopleItem);
        mCommonApi.ratePeople(body).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    public void rateTeacher(String peopleItem, final BaseListener baseListener){
        //String sb = JSONObject.toJSONString(peopleItem);
        LogUtil.d("", "param: " + peopleItem);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),peopleItem);
        mCommonApi.rateTeacher(body).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    public void rateParent(String peopleItem, final BaseListener baseListener){
        //String sb = JSONObject.toJSONString(peopleItem);
        LogUtil.d("", "param: " + peopleItem);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),peopleItem);
        mCommonApi.rateParent(body).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    public void rateLatestEvent(RateEventItem eventItem, final BaseListener baseListener){
        String sb = JSONObject.toJSONString(eventItem);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),sb.toString());
        mCommonApi.rateEvent(body).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //事件类型
    public void getEventTypeList(final BaseListener baseListener){

        mCommonApi.getEventTypeList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    //学生详情
    public void getStudentDetail(String studentId,final BaseListener baseListener){

        mCommonApi.getStudentDetail(studentId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //教师详情
    public void getTeacherDetail(String studentId,final BaseListener baseListener){

        mCommonApi.getTeacherDetail(studentId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }


    /**
     * 获取问卷列表
     */
    public void getQuestionnaireList(final BaseListener baseListener){

        mCommonApi.questionnaireList().subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    /**
     * 获取问卷详情
     */
    public void getQuestionnaireDetail(String uid, final BaseListener baseListener){

        mCommonApi.questionnaireDetail(uid).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    /*
     * 获取最新通知
     */
    public void getLatestNotice(final BaseListener baseListener){
        mCommonApi.getLatestNotice().subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    /**
     * 提交问卷
     */
    public void questionnaireCommit(String uid, ArrayList<Question> items, ArrayList<String> photos, final BaseListener baseListener){

        String photosStr = "";
        String result = "";
        StringBuilder subsb = new StringBuilder();
        subsb.append("{\"uid\":" + "\"" + uid + "\"");
        subsb.append(",\"items\":");
        subsb.append("[");

        if (items != null && items.size() > 0){
            for (Question qtitem: items){
                subsb.append("{\"uid\":" +  "\"" + qtitem.uid+ "\"");
                subsb.append(",\"optionAnswer\":" + "\"" + qtitem.answerd + "\"},");
            }
            subsb.deleteCharAt(subsb.length()-1);
        }

        subsb.append("]");

        if (photos != null){
            photosStr  = JSONArray.toJSONString(photos);
            subsb.append(",\"photos\":" + "["+ photosStr + "]}");
        }else{
            subsb.append("}");
        }




        Log.d("", "result:" + subsb.toString());


        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),subsb.toString());
        mCommonApi.questionnaireCommit(body).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    /**
     * 发单个图片或者文件
     * @param onePath
     * @param type  1: 图片  2：音频文件
     */
    public void uploadOneFile(String onePath, int type, final BaseListener baseListener){
        ArrayList<String> audioPath = new ArrayList<String>();
        audioPath.add(onePath);
        uploadMany(audioPath, type, baseListener);
    }

    /**
     * 发图片或者文件
     * @param paths
     * @param type  0: 图片  1：音频文件 2视频文件
     */
    public void uploadMany(ArrayList<String> paths, int type, final BaseListener baseListener){
        Map<String,RequestBody> uploadfiles = new HashMap<>();
        File upfile = null;
        if (paths.size()>0) {
            for (int i = 0; i < paths.size(); i++) {
                upfile = new File(paths.get(i));
                uploadfiles.put("file\"; filename=\"" + upfile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), upfile));
            }
        }

        NetWorkInterceptor.gType = 2;
        if (type == 0) {
            mCommonApi.uploadImage(uploadfiles).subscribeOn(Schedulers.io())//请求在子线程
                    .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                    .subscribe(new CommonSubscriber(baseListener) {
                        @Override
                        public void onNext(ResponseInfo responseInfo) {
                            dealJsonStr(responseInfo, baseListener);
                            NetWorkInterceptor.gType = 1;
                        }
                    });
        }else if (type == 1){
            mCommonApi.uploadAudio(uploadfiles).subscribeOn(Schedulers.io())//请求在子线程
                    .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                    .subscribe(new CommonSubscriber(baseListener) {
                        @Override
                        public void onNext(ResponseInfo responseInfo) {
                            dealJsonStr(responseInfo, baseListener);
                            NetWorkInterceptor.gType = 1;
                        }
                    });
        }else if(type == 2){
            mCommonApi.uploadVideo(uploadfiles).subscribeOn(Schedulers.io())//请求在子线程
                    .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                    .subscribe(new CommonSubscriber(baseListener) {
                        @Override
                        public void onNext(ResponseInfo responseInfo) {
                            dealJsonStr(responseInfo, baseListener);
                            NetWorkInterceptor.gType = 1;
                        }
                    });
        }else{
            NetWorkInterceptor.gType = 1;
        }

    }

    /**
     * 发图片（鲁班压缩算法）
     * @param paths
     * @param type  1: 图片  2：音频文件
     */


    public void uploadManyWithLuBan(final Context context, final Handler handler, final ArrayList<String> paths, final int type, final BaseListener baseListener){

        final List<File> files = new ArrayList<>(9);
        final LinkedList<Runnable> taskList = new LinkedList<>();

        if (paths.size()>0) {
            class  LoopFileTask implements Runnable{
                String onepath;
                LoopFileTask(String path){
                    this.onepath = path;
                }
                @Override
                public void run() {
                    File animFile = new File(onepath);
                    LogUtil.i("old file name ", animFile.getAbsolutePath());
                    //压缩算法
                    Luban.get(context).load(animFile).putGear(Luban.THIRD_GEAR).setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            LogUtil.i("开始压缩","start");
                        }

                        @Override
                        public void onSuccess(File file) {

                            synchronized (files) {
                                files.add(file);

                                LogUtil.i("tag file index ", files.size() + "");
                                LogUtil.i("tag compress file", file.getName());
                                LogUtil.i("tag file size ", file.length() + "");

                                    if(!taskList.isEmpty()){
                                        Runnable runnable = taskList.pop();
                                        handler.post(runnable);
                                    }else {
                                        //完成之后的操作
                                        //组装上传数据
                                        if (files.size() == paths.size()) {
                                            int fileSize = files.size();

                                            //检验是否符合上传条件
                                            long allSize = 0;
                                            for (int i = 0; i < fileSize; i++) {
                                                allSize = allSize + files.get(i).length();
                                                if (allSize > 4 * 1024 * 1024) {
                                                    ToastUtil.show("图片上传大小不能超过4M");
                                                    baseListener.requestFailed(false, -1, "图片超过大小");
                                                    return;
                                                }
                                            }

                                        Map<String, RequestBody> uploadfiles = new HashMap<>();
                                        for (int i = 0; i < fileSize; i++) {
                                            allSize = allSize + files.get(i).length();
                                            LogUtil.i("tag file--", files.get(i).getName() + "   " + files.get(i).getPath());
                                            uploadfiles.put("file\"; filename=\"" + files.get(i).getName(), RequestBody.create(MediaType.parse("multipart/form-data"), files.get(i)));
                                        }
                                        uploadRequest(uploadfiles, type, baseListener);
                                        }
                                    }
                                }
                            }
                        @Override
                        public void onError(Throwable throwable) {
                            LogUtil.i("compress error","error");
                            baseListener.requestFailed(false, -1, "图片压缩出错");
                        }
                    }).launch();
                }
            }

            for (String path :paths){
                taskList.add(new LoopFileTask(path));
            }
            handler.post(taskList.pop());
        }

    }



    private void uploadRequest(Map<String,RequestBody> uploadfiles,int type,final BaseListener baseListener){

        NetWorkInterceptor.gType = 2;
        if (type == 0) {
            mCommonApi.uploadImage(uploadfiles).subscribeOn(Schedulers.io())//请求在子线程
                    .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                    .subscribe(new CommonSubscriber(baseListener) {
                        @Override
                        public void onNext(ResponseInfo responseInfo) {
                            NetWorkInterceptor.gType = 1;
                            dealJsonStr(responseInfo, baseListener);
                        }
                    });
        }else if (type == 1){
            mCommonApi.uploadAudio(uploadfiles).subscribeOn(Schedulers.io())//请求在子线程
                    .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                    .subscribe(new CommonSubscriber(baseListener) {
                        @Override
                        public void onNext(ResponseInfo responseInfo) {
                            dealJsonStr(responseInfo, baseListener);
                            NetWorkInterceptor.gType = 1;
                        }
                    });
        }else{
            NetWorkInterceptor.gType = 1;
        }

    }

    //通知中心
    public void getMsgList(int pageIndex,int pageSize,final BaseListener baseListener){

        mCommonApi.getMsgList(pageIndex,pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });

    }

    //消息详情
    public void getMsgDetail(String messageId, final BaseListener baseListener){

        mCommonApi.getMsgDetail(messageId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //获取学校课程
    public void getCourseList(String schoolUid,final BaseListener baseListener){

        mCommonApi.getCourseList(schoolUid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });

    }

    //获取事件地点
    public void getEventAddressList(final BaseListener baseListener){

        mCommonApi.getEventAddressList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //定期上传经纬度
    public void reportLocation(double lat, double lng, final BaseListener baseListener){
        Map<String, Object> param = new HashMap<>();
        param.put("lat", lat);
        param.put("lng", lng);

        String sb = JSONObject.toJSONString(param);

        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),sb.toString());
        mCommonApi.reportLocation(body).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //获取事件互评列表
    public void getEventEvaluationList(int pageIndex,int pageSize,final BaseListener baseListener){

        mCommonApi.getEventEvaluationList(pageIndex,pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });

    }


    //互评事件
    public void evaluationEvent(String bodyStr,final BaseListener baseListener){

        RequestBody body=RequestBody.create(mMediaType,bodyStr);
        mCommonApi.evaluationEvent(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });

    }


}

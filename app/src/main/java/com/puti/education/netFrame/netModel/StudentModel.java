package com.puti.education.netFrame.netModel;


import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.CommonSubscriber;
import com.puti.education.netFrame.RetrofitUtil;
import com.puti.education.netFrame.netApi.StudentApi;
import com.puti.education.netFrame.response.ResponseInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StudentModel extends BaseModel{

    private StudentApi mStudentApi = null;
    private static StudentModel gStudentModel = null;
    private MediaType mMediaType;

    public StudentModel(){
        mStudentApi = RetrofitUtil.getRetrofit().create(StudentApi.class);
        mMediaType = okhttp3.MediaType.parse("application/json; charset=utf-8");

    }

    public static StudentModel getInstance(){
        if (gStudentModel == null){
            gStudentModel = new StudentModel();
            return gStudentModel;
        }
        return  gStudentModel;
    }

    public static void clearInstance(){
        gStudentModel = null;
    }

    public void studentIndex(final BaseListener baseListener){
        mStudentApi.studentIndex().subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    public void studentEventList(int type, String status, int pageIndex, int pageSize, final BaseListener baseListener){
        mStudentApi.actionEventList(type, status, pageIndex, pageSize).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });
    }

    //学生详情
    public void getStudentDetail(final BaseListener baseListener){

        mStudentApi.getStudentDetail().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //修改学生详情
    public void modifyStudentInfo(String paramStr ,final BaseListener baseListener){
        RequestBody body = RequestBody.create(mMediaType,paramStr);
        mStudentApi.modifyStudentInfo(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });
    }

    //求救
    public void launchSos(double lat, double lng, String adddress, String audioUrl, String mac, final BaseListener baseListener){
        Map<String, Object> paramJsonStr = new HashMap<String, Object>();
        paramJsonStr.put("lat", lat);
        paramJsonStr.put("lng", lng);
        paramJsonStr.put("url", audioUrl);
        paramJsonStr.put("address", adddress);
        paramJsonStr.put("mac", (TextUtils.isEmpty(mac)?"":mac));
        String str = JSONObject.toJSONString(paramJsonStr);
        RequestBody body = RequestBody.create(mMediaType,str);
        mStudentApi.launchSos(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });
    }

    //举报
    public void reportEvent(List<EventAboutPeople> involvers, int type, String desc, List<String> photoUrls,
                            List<String> knownStudentIds, List<String> knownTeacherIds, final BaseListener baseListener){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("desc",desc);
        jsonObject.put("useruid",involvers.get(0).uid);

        JSONObject subjsonObject;
        JSONArray subPhotos = new JSONArray();
        if (photoUrls != null && photoUrls.size() > 0){
            for (String one: photoUrls) {
                subjsonObject = new JSONObject();
                subjsonObject.put("name", one);
                subjsonObject.put("description", one);
                subjsonObject.put("url", one);
                subjsonObject.put("type", "0");
                subPhotos.add(subjsonObject);
            }
        }

        jsonObject.put("photoUrl",subPhotos);


        String str = jsonObject.toString();

        RequestBody body = RequestBody.create(mMediaType,str);
        mStudentApi.reportEvent(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });
    }

    //实践记录
    public void getPracticeList(String type, String pageIndex,String pageSize,final BaseListener baseListener){
        mStudentApi.praticeList(type, pageIndex,pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });

    }

    //添加实践记录
    public void addPracticeRecord(String name, String result, String time, String content, List<String> photoUrls,
                             final BaseListener baseListener){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",name);
        jsonObject.put("result",result);
        jsonObject.put("time",time);
        jsonObject.put("desc",content);

        JSONArray subPhotos = new JSONArray();
        if (photoUrls != null && photoUrls.size() > 0){
            subPhotos.addAll(photoUrls);
        }
        jsonObject.put("photo",subPhotos);

        String str = jsonObject.toString();

        RequestBody body = RequestBody.create(mMediaType,str);
        mStudentApi.addPracticeRecord(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });
    }

    //添加培训或实践记录(新接口)
    public void addTrain(int type, String theme, String address, String time, String content, String result, ArrayList<String> photos,
                         final BaseListener baseListener){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("type",type);
        jsonObject.put("title", theme);
        jsonObject.put("address",address);
        jsonObject.put("time",time);
        jsonObject.put("content",content);
        jsonObject.put("result", result);

        JSONArray subPhotos = new JSONArray();
        if (photos != null && photos.size() > 0){
            subPhotos.addAll(photos);
        }

        jsonObject.put("photo",subPhotos);

        String str = jsonObject.toString();
        RequestBody body = RequestBody.create(mMediaType,str);
        mStudentApi.addPracticeRecord(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });
    }


    //查看实践记录详情
    public void getPracticeDetail(String id, final BaseListener baseListener){

        mStudentApi.getPracticeDetail(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    //添加小孩行为普通事件
    public void addNormalEvent(int type, int level, String time, String address, String lng, String lat, String desc,
                               String imageText, ArrayList<UploadFileBean> mUploadedImages, ArrayList<UploadFileBean> mUploadAudios, ArrayList<UploadFileBean> mUploadVideos,
                               final BaseListener baseListener){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventType",type);
        jsonObject.put("eventLevel", level);

        jsonObject.put("bizTime",time);
        jsonObject.put("address", address);
        jsonObject.put("longitude", lng);
        jsonObject.put("latitude", lat);

        jsonObject.put("desc",desc);

        JSONObject subjsonObject;

        //提交佐证记录  //0. 图文，　1.音频, 2.视频, 3.文档
        //提交图片
        JSONArray subRecords = new JSONArray();
        if (mUploadedImages != null && mUploadedImages.size() > 0) {
            for (UploadFileBean ufile : mUploadedImages) {
                if (ufile != null) {
                    subjsonObject = new JSONObject();
                    subjsonObject.put("name", ufile.localName);
                    subjsonObject.put("description", imageText);
                    subjsonObject.put("url", ufile.fileuid);
                    subjsonObject.put("type", "0");
                    subRecords.add(subjsonObject);
                }
            }
        }

        //提交音视频文件
        if (mUploadAudios != null && mUploadAudios.size() > 0) {
            for (UploadFileBean ufile : mUploadAudios) {
                if (ufile != null) {
                    subjsonObject = new JSONObject();
                    subjsonObject.put("name", ufile.localName);
                    subjsonObject.put("description", imageText);
                    subjsonObject.put("url", ufile.fileuid);
                    subjsonObject.put("type", "1");
                    subRecords.add(subjsonObject);
                }
            }
        }

        //提交视频文件
        if (mUploadVideos != null && mUploadVideos.size() > 0) {
            for (UploadFileBean ufile : mUploadVideos) {
                if (ufile != null) {
                    subjsonObject = new JSONObject();
                    subjsonObject.put("name", ufile.localName);
                    subjsonObject.put("description", imageText);
                    subjsonObject.put("url", ufile.fileuid);
                    subjsonObject.put("type", "2");
                    subRecords.add(subjsonObject);
                }
            }
        }


        jsonObject.put("evidence", subRecords);

        String str = jsonObject.toString();
        RequestBody body = RequestBody.create(mMediaType,str);
        mStudentApi.addNormalEvent(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });
    }

    //举报记录
    public void getReportList(int pageIndex, int pageSize,final BaseListener baseListener){
        mStudentApi.getReportList(pageIndex,pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });

    }



}

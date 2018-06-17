package com.puti.education.netFrame.netModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.CommonSubscriber;
import com.puti.education.netFrame.RetrofitUtil;
import com.puti.education.netFrame.netApi.PatriarchApi;
import com.puti.education.netFrame.response.ResponseInfo;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 家长Presenter
 */
public class PatriarchModel extends BaseModel{

    private PatriarchApi patriarchApi = null;
    private static PatriarchModel patriarchModel = null;
    private MediaType mMediaType;

    public PatriarchModel(){
        patriarchApi = RetrofitUtil.getRetrofit().create(PatriarchApi.class);
        mMediaType = okhttp3.MediaType.parse("application/json; charset=utf-8");
    }

    public static PatriarchModel getInstance(){
        if (patriarchModel == null){
            patriarchModel = new PatriarchModel();
            return patriarchModel;
        }
        return  patriarchModel;
    }

    public static void clearInstance(){
        patriarchModel = null;
    }


    //添加培训或实践记录
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
        patriarchApi.addTrain(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });
    }

    //添加小孩行为普通事件
    public void addActionEventRecord(int type, int level, String studentuid, String time, String address, String lng, String lat, String desc,
                                     String imageText, ArrayList<UploadFileBean> mUploadedImages, ArrayList<UploadFileBean> mUploadAudios,ArrayList<UploadFileBean> mUploadVideos,
                                     final BaseListener baseListener){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventType",type);
        jsonObject.put("eventLevel", level);

        jsonObject.put("bizTime",time);
        jsonObject.put("address", address);
        jsonObject.put("longitude", lng);
        jsonObject.put("latitude", lat);

        jsonObject.put("desc",desc);
        jsonObject.put("studentuid", studentuid);
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
        patriarchApi.addActionEventRecord(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });
    }

    //根据学生获取班主任
    public void getTeacherByStudent(int studentId, final BaseListener baseListener){
        patriarchApi.getTeacherByStudent(studentId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });
    }

    //家长数据
    public void getParentHomeData(final BaseListener baseListener){

        patriarchApi.getParentHomeData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }



    //获取培训记录
    public void getTrainList(String type, int pageIndex, int pageSize, final BaseListener baseListener){
        patriarchApi.getTrainList(type, pageIndex, pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });
    }


    //获取成长记录
    public void getGrowthTrackList(String date, int pageIndex, int pageSize, final BaseListener baseListener){
        patriarchApi.getGrowthTrackList(date, pageIndex, pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });
    }

    //获取培训实践记录详情
    public void getPracticeTrainDetail(String uid, final BaseListener baseListener){
        patriarchApi.getPracticeTrainDetail(uid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    //获取成长轨迹详情
    public void getGrowthTrackDetail(String id, final BaseListener baseListener){
        patriarchApi.getGrowthTrackDetail(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    //获取行为事件详情
    public void getActionEventDetail(int id, final BaseListener baseListener){
        patriarchApi.getActionEventDetail(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }


    //家人个人详情
    public void getParnetInfo(final BaseListener baseListener){

        patriarchApi.getParentInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });

    }

    //修改家长信息
    public void modifyParentInfo(String paramStr,final BaseListener baseListener){
        RequestBody body = RequestBody.create(mMediaType,paramStr);
        patriarchApi.modifyParentInfo(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });

    }

    //书信
    public void writeLetter(String paramStr,final BaseListener baseListener){

        RequestBody body = RequestBody.create(mMediaType,paramStr);
        patriarchApi.writeLetter(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealSimpleStr(responseInfo, baseListener);
                    }
                });
    }

    //书信列表
    public void getAnonymityList(int pageIndex, int pageSize, final BaseListener baseListener){
        patriarchApi.getAnonymityList(pageIndex, pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });
    }

    //获取家长小孩的事件行为
    public void parentChildEventList(String uid, int type, String status, int pageIndex, int pageSize, final BaseListener baseListener){
        patriarchApi.actionEventList(uid, type, status, pageIndex, pageSize).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new CommonSubscriber(baseListener){
                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        dealJsonArrayStr(responseInfo, baseListener);
                    }
                });
    }



}

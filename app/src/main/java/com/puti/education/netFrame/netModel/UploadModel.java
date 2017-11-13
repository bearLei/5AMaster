package com.puti.education.netFrame.netModel;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puti.education.App;
import com.puti.education.bean.Question;
import com.puti.education.bean.RateEventItem;
import com.puti.education.bean.RatePeopleItem;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.CommonSubscriber;
import com.puti.education.netFrame.NetWorkInterceptor;
import com.puti.education.netFrame.RetrofitUtil;
import com.puti.education.netFrame.netApi.CommonApi;
import com.puti.education.netFrame.netApi.UploadApi;
import com.puti.education.netFrame.response.ResponseInfo;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 家长Presenter
 */
public class UploadModel extends BaseModel{

    private UploadApi mUploadApi = null;
    private static UploadModel gUploadModel = null;

    public UploadModel(){
        mUploadApi = RetrofitUtil.getUploadRetrofit().create(UploadApi.class);
    }

    public static UploadModel getInstance(){
        if (gUploadModel == null){
            gUploadModel = new UploadModel();
            return gUploadModel;
        }
        return  gUploadModel;
    }

    public static void clearInstance(){
        gUploadModel = null;
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
            mUploadApi.uploadImage(uploadfiles).subscribeOn(Schedulers.io())//请求在子线程
                    .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                    .subscribe(new CommonSubscriber(baseListener) {
                        @Override
                        public void onNext(ResponseInfo responseInfo) {
                            dealJsonStr(responseInfo, baseListener);
                            NetWorkInterceptor.gType = 1;
                        }
                    });
        }else if (type == 1){
            mUploadApi.uploadAudio(uploadfiles).subscribeOn(Schedulers.io())//请求在子线程
                    .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                    .subscribe(new CommonSubscriber(baseListener) {
                        @Override
                        public void onNext(ResponseInfo responseInfo) {
                            dealJsonStr(responseInfo, baseListener);
                            NetWorkInterceptor.gType = 1;
                        }
                    });
        }else if(type == 2){
            mUploadApi.uploadVideo(uploadfiles).subscribeOn(Schedulers.io())//请求在子线程
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
            mUploadApi.uploadImage(uploadfiles).subscribeOn(Schedulers.io())//请求在子线程
                    .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                    .subscribe(new CommonSubscriber(baseListener) {
                        @Override
                        public void onNext(ResponseInfo responseInfo) {
                            NetWorkInterceptor.gType = 1;
                            dealJsonStr(responseInfo, baseListener);
                        }
                    });
        }else if (type == 1){
            mUploadApi.uploadAudio(uploadfiles).subscribeOn(Schedulers.io())//请求在子线程
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

}

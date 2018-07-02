package unit.api;

import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.CommonSubscriber;
import com.puti.education.netFrame.NetWorkInterceptor;
import com.puti.education.netFrame.RetrofitUtil;
import com.puti.education.netFrame.netModel.BaseModel;
import com.puti.education.netFrame.response.ResponseInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import unit.base.BaseResponseInfo;
import unit.base.PutiBaseModel;
import unit.base.PutiCommonSubscriber;

/**
 * Created by lei on 2018/6/14.
 */

public class PutiUploadModel extends PutiBaseModel{

    private PutiUploadApi mUploadApi = null;
    private static PutiUploadModel gUploadModel = null;
    private MediaType mMediaType;

    public PutiUploadModel(){
        mUploadApi= RetrofitUtil.getRetrofit().create(PutiUploadApi.class);
        mMediaType = okhttp3.MediaType.parse("application/json; charset=utf-8");
    }

    public static PutiUploadModel getInstance(){
        if (gUploadModel == null){
            gUploadModel = new PutiUploadModel();
            return gUploadModel;
        }
        return  gUploadModel;
    }

    public static void clearInstance(){
        gUploadModel = null;
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
                    .subscribe(new PutiCommonSubscriber(baseListener){
                        @Override
                        public void onNext(BaseResponseInfo responseInfo) {
                            dealJson(responseInfo, baseListener);
                            NetWorkInterceptor.gType = 1;
                        }
                    });
        }else if (type == 1){
            mUploadApi.uploadAudio(uploadfiles).subscribeOn(Schedulers.io())//请求在子线程
                    .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                    .subscribe(new PutiCommonSubscriber(baseListener) {
                        @Override
                        public void onNext(BaseResponseInfo responseInfo) {
                            dealJson(responseInfo, baseListener);
                            NetWorkInterceptor.gType = 1;
                        }
                    });
        }else if(type == 2){
            mUploadApi.uploadVideo(uploadfiles).subscribeOn(Schedulers.io())//请求在子线程
                    .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                    .subscribe(new PutiCommonSubscriber(baseListener) {
                        @Override
                        public void onNext(BaseResponseInfo responseInfo) {
                            dealJson(responseInfo, baseListener);
                            NetWorkInterceptor.gType = 1;
                        }
                    });
        }else{
            NetWorkInterceptor.gType = 1;
        }

    }


    /**
     * 发图片或者文件
     * @param paths
     */
    public void changeAvatar(ArrayList<String> paths,int type, final BaseListener baseListener){
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
                    .subscribe(new PutiCommonSubscriber(baseListener){
                        @Override
                        public void onNext(BaseResponseInfo responseInfo) {
                            dealJson(responseInfo, baseListener);
                            NetWorkInterceptor.gType = 1;
                        }
                    });
        }else{
            NetWorkInterceptor.gType = 1;
        }

    }
}

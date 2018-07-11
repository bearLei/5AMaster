package unit.moudle.eventregist.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.bean.LocalFile;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.ui.uiCommon.VideoRecordActivity;
import com.puti.education.ui.uiTeacher.AddEventWithPictrueTextActivity;
import com.puti.education.ui.uiTeacher.SystemAudioListChooseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.api.PutiUploadModel;
import unit.entity.Student;
import unit.entity.UpLoadInfo;

/**
 * Created by lei on 2018/6/11.
 * 事件登记详情-选择佐证holder
 */

public class EventEvidenceHolder extends BaseHolder<Object> implements View.OnClickListener {
    @BindView(R.id.choose_image)
    LinearLayout VChooseImage;
    @BindView(R.id.choose_music)
    LinearLayout VChooseMusic;
    @BindView(R.id.choose_video)
    LinearLayout VChooseVideo;


    private ArrayList<String> mImagePaths;
    private String mImageTextStr;
    private String mVideoPaths;
    private ArrayList<LocalFile> mAudioLocalFileList;
    private boolean mNeedUploadImage;
    private boolean mNeedUploadVideo;
    private boolean mNeedUploadAudios;

    //下载完成后承载的集合
    private ArrayList<UpLoadInfo> mUploadedImages;
    private ArrayList<UpLoadInfo> mUploadAudios;
    private ArrayList<UpLoadInfo> mUploadVideos;
    public EventEvidenceHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_event_detail_evidence_holder);
        ButterKnife.bind(this, mRootView);
        if (mImagePaths == null) {
            mImagePaths = new ArrayList<>();
        }
        if (mAudioLocalFileList == null) {
            mAudioLocalFileList = new ArrayList<>();
        }

        VChooseImage.setOnClickListener(this);
        VChooseMusic.setOnClickListener(this);
        VChooseVideo.setOnClickListener(this);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Object data) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_image:
                Intent picIntent = new Intent(mContext, AddEventWithPictrueTextActivity.class);
                picIntent.putStringArrayListExtra(Key.BEAN, mImagePaths);
                picIntent.putExtra(Key.RECORD_IMG_TEXT, TextUtils.isEmpty(mImageTextStr) ? "" : mImageTextStr);
                ((Activity) mContext).startActivityForResult(picIntent, Constant.CODE_REQUEST_IMG_TEXT);
                break;
            case R.id.choose_music:
                Intent intent = new Intent(mContext, SystemAudioListChooseActivity.class);
                intent.putParcelableArrayListExtra(Key.BEAN, mAudioLocalFileList);
                ((Activity) mContext).startActivityForResult(intent, Constant.CODE_REQUEST_MEDIA);
                break;
            case R.id.choose_video:
                Intent videoIntent = new Intent(mContext, VideoRecordActivity.class);
                videoIntent.putExtra(Key.RECORD_VIDEO, mVideoPaths);
                ((Activity) mContext).startActivityForResult(videoIntent, Constant.CODE_REQUEST_VIDEO);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (resultCode) {
            case Constant.CODE_RESULT_VIDEO: {
                ToastUtil.show("成功添加视频");
                mVideoPaths = intent.getStringExtra(Key.RECORD_VIDEO);
                if (!TextUtils.isEmpty(mVideoPaths)){
                    mNeedUploadVideo = true;
                }else {
                    mNeedUploadVideo = false;
                }
            }
            break;
            case Constant.CODE_RESULT_IMG_TEXT: {
                ToastUtil.show("成功添加图片");
                mImagePaths.clear();
                mImageTextStr = intent.getStringExtra(Key.RECORD_IMG_TEXT);
                List<String> tempImgList = intent.getStringArrayListExtra(Key.BEAN);
                mImagePaths.addAll(tempImgList);
                if (mImagePaths.size() > 0 ){
                    mNeedUploadImage = true;
                }else {
                    mNeedUploadImage = false;
                }
            }
            break;
            case Constant.CODE_RESULT_MEDIA: {
                ToastUtil.show("成功添加音频");
                mAudioLocalFileList.clear();
                List<LocalFile> temImgList = intent.getParcelableArrayListExtra(Key.BEAN);
                mAudioLocalFileList.addAll(temImgList);
                if (mAudioLocalFileList.size() > 0){
                    mNeedUploadAudios = true;
                }else {
                    mNeedUploadAudios = false;
                }
            }
        }
    }

    //上传图片
    private void uploadImages() {
        PutiUploadModel.getInstance().uploadMany(mImagePaths, 0, new BaseListener(UpLoadInfo.class) {
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
            }

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
                mUploadedImages = (ArrayList<UpLoadInfo>) listObj;
                if (mNeedUploadAudios){
                    uploadAudio();
                }else if (mNeedUploadVideo){
                    uploadVideo();
                }else if (uploadFinishCallBack != null){
                    uploadFinishCallBack.finish();
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("图片上传失败");
            }
        });
    }

    //上传
    private void uploadAudio() {
        ArrayList<String> tempAudioList = new ArrayList<>();
        for (LocalFile localFile :mAudioLocalFileList){
            tempAudioList.add(localFile.localPath);
        }
        PutiUploadModel.getInstance().uploadMany(tempAudioList, 1, new BaseListener(UpLoadInfo.class) {
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
            }

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
                mUploadAudios = (ArrayList<UpLoadInfo>) listObj;
                if (mNeedUploadVideo){
                    uploadVideo();
                }else if (uploadFinishCallBack != null){
                    uploadFinishCallBack.finish();
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("音频上传失败");
            }
        });

    }

    //上传视频
    private void uploadVideo() {
        ArrayList<String> tempVideoList = new ArrayList<>();
        if (!TextUtils.isEmpty(mVideoPaths)){
            tempVideoList.add(mVideoPaths);
        }

        PutiUploadModel.getInstance().uploadMany(tempVideoList, 2, new BaseListener(UpLoadInfo.class) {
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
            }

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
                mUploadVideos = (ArrayList<UpLoadInfo>) listObj;
                if (uploadFinishCallBack != null){
                    uploadFinishCallBack.finish();
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("视频上传失败");
            }
        });
    }


    public JSONArray getEvidenceJson(){
        JSONArray array = new JSONArray();
        if (mUploadedImages != null && mUploadedImages.size() > 0) {
            for (UpLoadInfo upLoadInfo : mUploadedImages) {
                array.add(upLoadInfo.getFileuid());
            }
        }
        if (mUploadAudios!= null && mUploadAudios.size() > 0) {
            for (UpLoadInfo upLoadInfo : mUploadAudios) {
                array.add(upLoadInfo.getFileuid());
            }
        }
        if (mUploadVideos != null && mUploadVideos.size() > 0) {
            for (UpLoadInfo upLoadInfo : mUploadVideos) {
                array.add(upLoadInfo.getFileuid());
            }
        }
        return array;
    }

    public void doUpload(UploadFinishCallBack callBack){
        this.uploadFinishCallBack = callBack;
        if (mNeedUploadImage){
            uploadImages();
        } else if (mNeedUploadAudios){
            uploadAudio();
        } else if (mNeedUploadVideo){
            uploadVideo();
        }
    }

    private UploadFinishCallBack uploadFinishCallBack;

    public interface  UploadFinishCallBack{
        void finish();
    }
}

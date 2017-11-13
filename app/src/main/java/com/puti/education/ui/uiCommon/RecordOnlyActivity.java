package com.puti.education.ui.uiCommon;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.puti.education.R;
import com.puti.education.core.audio.AudioManagerFactory;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.PopupWindowFactory;
import com.puti.education.util.TimeUtils;

import butterknife.BindView;

public class RecordOnlyActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.tv_right)
    TextView mRightTv;
    @BindView(R.id.layout_frame)
    FrameLayout mFramelayout;
    @BindView(R.id.btn_sos)
    Button mSosBtn;

    private AudioManagerFactory mAudioRecoderUtils;
    private Context mContext;
    private PopupWindowFactory mPop;
    private ImageView mImageView;
    private TextView mTextView;

    private String mAudioPath;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_record_only;
    }

    @Override
    public void initVariables() {
        mContext = this;
        //6.0以上需要录音权限申请
        requestPermissions();
        initPopIcon();

        mAudioRecoderUtils = new AudioManagerFactory();
        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioManagerFactory.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                Toast.makeText(mContext, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                mTextView.setText(TimeUtils.long2String(0));
                //noticeAudioFileUpdate(filePath);
                setResult(Constant.VOICE_RECORD_RESULT_CODE);
                finish();
            }
        });
    }

    @Override
    public void initViews() {
        mTitleTv.setText("录音");
        mSosBtn.setText("按住录音取证");
    }

    @Override
    public void loadData() {

    }

    public void finishActivity(View view) {
        finish();
    }

    private void noticeAudioFileUpdate(String filePath){
        Intent intent = new  Intent();
        //设置intent的动作为com.example.broadcast，可以任意定义
        intent.putExtra("file", filePath);
        intent.setAction(Constant.BROADCAST_REFRESH_AUDIO_FILE);
        //发送无序广播
        sendBroadcast(intent);
    }

    private void initPopIcon(){
        //PopupWindow的布局文件
        final View view = View.inflate(mContext, R.layout.layout_microphone, null);

        mPop = new PopupWindowFactory(mContext,view);

        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);
    }
    /**
     * 开启扫描之前判断权限是否打开
     */
    private void requestPermissions() {
        //判断是否开启录音权限
        if ((ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                ) {
            StartListener();

            //判断是否开启语音权限
        } else {
            //请求获取语音权限
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, Constant.VOICE_REQUEST_CODE);
        }

    }

    private float mDownY = 0;
    public void StartListener(){
        //Button的touch监听
        mSosBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:
                        mDownY = event.getY();
                        mPop.showAtLocation(mFramelayout, Gravity.CENTER, 0, 0);
                        mSosBtn.setText("松开保存");
                        mAudioRecoderUtils.startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
                        mPop.dismiss();
                        mSosBtn.setText("按住说话");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mAudioRecoderUtils.cancelRecord();    //取消录音（不保存录音文件）
                        mPop.dismiss();
                        mSosBtn.setText("按住说话");
                        break;
                    case MotionEvent.ACTION_MOVE:              // 滑动手指取消录音功能
                        float moveY = event.getY();
//                        if (mDownY - moveY > 100) {
//                            isCanceled = true;
//                            mTvNotice.setText("松开手指可取消录音");
//                            mIvRecord.setImageDrawable(getResources().getDrawable(R.drawable.record));
//                        }
//                        if (mDownY - moveY < 20) {
//                            isCanceled = false;
//                            mIvRecord.setImageDrawable(getResources().getDrawable(R.drawable.record_pressed));
//                            mTvNotice.setText("向上滑动取消发送");
//                        }
                        break;
                }
                return true;
            }
        });
    }


}

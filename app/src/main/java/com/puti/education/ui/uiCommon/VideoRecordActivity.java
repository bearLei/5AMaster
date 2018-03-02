package com.puti.education.ui.uiCommon;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.puti.education.R;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.ToastUtil;
import com.puti.education.util.ViewUtils;
import com.puti.education.widget.VideoProgressBar;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Administrator on 2017/9/7 0007.
 */

public class VideoRecordActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnTouchListener, VideoProgressBar.OnProgressEndListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    @BindView(R.id.main_surface_view)
    SurfaceView mSurfaceView;

    private static final int LISTENER_START = 200;
    private final String TAG = VideoRecordActivity.class.toString();
    private Camera mCamera;
    //底部"按住拍"按钮
    private View mStartButton;
    //进度条
    private VideoProgressBar mProgressBar;
    //进度条线程
    private Thread mProgressThread;
    //录制视频
    private MediaRecorder mMediaRecorder;
    private SurfaceHolder mSurfaceHolder;
    //屏幕分辨率
    private int videoWidth, videoHeight;
    //判断是否正在录制
    private boolean isRecording;
    //段视频保存的目录
    private File mTargetFile;
    //当前进度/时间
    private int mProgress;
    //录制最大时间
    public static final int MAX_TIME = 10;
    //是否上滑取消
    private boolean isCancel;
    //手势处理, 主要用于变焦 (双击放大缩小)
    private GestureDetector mDetector;
    //是否放大
    private boolean isZoomIn = false;

    private MyHandler mHandler;
    private TextView mTvTip;
    private boolean isRunning;

    private static final int REQUEST_VIDEO_PERMISSIONS = 1;


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_video_record;
    }

    @Override
    public void initVariables() {
        videoWidth = 640;
        videoHeight = 480;
        mDetector = new GestureDetector(this, new ZoomGestureListener());
    }

    @Override
    public void initViews() {

        mStartButton = findViewById(R.id.main_press_control);
        mTvTip = (TextView) findViewById(R.id.main_tv_tip);

        mStartButton.setOnTouchListener(this);
        //自定义双向进度条
        mProgressBar = (VideoProgressBar) findViewById(R.
                id.main_progress_bar);
        mProgressBar.setOnProgressEndListener(this);
        mHandler = new MyHandler(this);
        mMediaRecorder = new MediaRecorder();
        requestPermissions();

    }

    public void initSurface(){
        /**
         * 单独处理mSurfaceView的双击事件
         */
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });
        mSurfaceHolder = mSurfaceView.getHolder();
        //设置屏幕分辨率
        mSurfaceHolder.setFixedSize(videoWidth, videoHeight);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        startPreView(holder);
    }


    /**
     * 开启录制之前判断权限是否打开
     */
    private void requestPermissions() {
        //判断是否开启录音权限
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                ) {
            initSurface();

            //判断是否开启音视频权限
        } else {
            //请求获取音视频权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA
                    }, REQUEST_VIDEO_PERMISSIONS);
        }

    }

    /**
     * 开启预览
     *
     * @param holder
     */
    private void startPreView(SurfaceHolder holder) {
        Log.d(TAG, "startPreView: ");

        if (mCamera == null) {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        if (mCamera != null) {
            mCamera.setDisplayOrientation(90);
            try {
                mCamera.setPreviewDisplay(holder);
                Camera.Parameters parameters = mCamera.getParameters();
                //实现Camera自动对焦
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes != null) {
                    for (String mode : focusModes) {
                        mode.contains("continuous-video");
                        parameters.setFocusMode("continuous-video");
                    }
                }
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            Log.d(TAG, "surfaceDestroyed: ");
            //停止预览并释放摄像头资源
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // 进度条结束后的回调方法
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onProgressEndListener() {
        //视频停止录制
        stopRecordSave();
    }

    /**
     * 开始录制
     */
    private void startRecord() {
        if (mMediaRecorder != null) {
            //没有外置存储, 直接停止录制
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return;
            }
            try {
                mMediaRecorder.reset();
                mCamera.unlock();
                mMediaRecorder.setCamera(mCamera);
                //从相机采集视频
                mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                // 从麦克采集音频信息
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                // TODO: 设置视频格式
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mMediaRecorder.setVideoSize(videoWidth, videoHeight);
                //每秒的帧数
//                mMediaRecorder.setVideoFrameRate(24);
                //编码格式
                mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                // 设置帧频率，然后就清晰了
                mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 1024 * 100);
                // TODO: 临时写个文件地址, 稍候该!!!
                String folderPath = Environment.getExternalStorageDirectory()+"/ms_record";

                File dir = new File(folderPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                mTargetFile = new File(folderPath,
                        SystemClock.currentThreadTimeMillis() + ".mp4");

                if (!mTargetFile.exists()) {
                    mTargetFile.createNewFile();
                }

                mMediaRecorder.setOutputFile(mTargetFile.getAbsolutePath());
                mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
                //解决录制视频, 播放器横向问题
                mMediaRecorder.setOrientationHint(90);

                mMediaRecorder.prepare();
                //正式录制
                mMediaRecorder.start();
                isRecording = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 停止录制 并且保存
     */
    private void stopRecordSave() {
        if (isRecording) {
            isRunning = false;
            try {
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
                // TODO: handle exception
                Log.i("Exception", Log.getStackTraceString(e));
            }catch (RuntimeException e) {
                // TODO: handle exception
                Log.i("Exception", Log.getStackTraceString(e));
            }catch (Exception e) {
                // TODO: handle exception
                Log.i("Exception", Log.getStackTraceString(e));
            }

            isRecording = false;
            String fileurl = mTargetFile.getAbsolutePath();
            Toast.makeText(this, "视频已经放至" + fileurl, Toast.LENGTH_SHORT).show();
            if (!TextUtils.isEmpty(fileurl)){
                Intent intent = new Intent();
                intent.putExtra(Key.RECORD_VIDEO, mTargetFile.getAbsolutePath());
                this.setResult(Constant.CODE_RESULT_VIDEO, intent);
                finish();
            }
        }
    }

    /**
     * 停止录制, 不保存
     */
    private void stopRecordUnSave() {
        if (mTargetFile.exists()) {
            //不保存直接删掉
            mTargetFile.delete();
        }
//        mCamera.lock();
//        if (isRecording) {
//            isRunning = false;
//            try {
////                mMediaRecorder.setOnErrorListener(null);
////                mMediaRecorder.setOnInfoListener(null);
////                mMediaRecorder.setPreviewDisplay(null);
////                mMediaRecorder.stop();
//            } catch (IllegalStateException e) {
//                // TODO: handle exception
//                Log.i("Exception", Log.getStackTraceString(e));
//            }catch (RuntimeException e) {
//                // TODO: handle exception
//                Log.i("Exception", Log.getStackTraceString(e));
//            }catch (Exception e) {
//                // TODO: handle exception
//                Log.i("Exception", Log.getStackTraceString(e));
//            }
//
//            isRecording = false;
//            if (mTargetFile.exists()) {
//                //不保存直接删掉
//                mTargetFile.delete();
//            }
//
//        }
    }

    /**
     * 相机变焦
     *
     * @param zoomValue
     */
    public void setZoom(int zoomValue) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.isZoomSupported()) {//判断是否支持
                int maxZoom = parameters.getMaxZoom();
                if (maxZoom == 0) {
                    return;
                }
                if (zoomValue > maxZoom) {
                    zoomValue = maxZoom;
                }
                parameters.setZoom(zoomValue);
                mCamera.setParameters(parameters);
            }
        }

    }


    ///////////////////////////////////////////////////////////////////////////
    // Handler处理
    ///////////////////////////////////////////////////////////////////////////
    private static class MyHandler extends Handler {
        private WeakReference<VideoRecordActivity> mReference;
        private VideoRecordActivity mActivity;

        public MyHandler(VideoRecordActivity activity) {
            mReference = new WeakReference<VideoRecordActivity>(activity);
            mActivity = mReference.get();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mActivity.mProgressBar.setProgress(mActivity.mProgress);
                    break;
            }

        }
    }

    /**
     * 触摸事件的触发
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean ret = false;
        int action = event.getAction();
        float ey = event.getY();
        float ex = event.getX();
        //只监听中间的按钮处
        int vW = v.getWidth();
        int left = vW/2 - ViewUtils.dip2px(this,72);
        int right = vW/2 + ViewUtils.dip2px(this,72);

        float downY = 0;

        switch (v.getId()) {
            case R.id.main_press_control: {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (ex > left && ex < right) {
                            isCancel = false;
                            mProgressBar.setCancel(false);
                            //显示上滑取消
                            mTvTip.setVisibility(View.VISIBLE);
                            mTvTip.setText("↑ 上滑取消");
                            //记录按下的Y坐标
                            downY = ey;
                            // TODO: 2016/10/20 开始录制视频, 进度条开始走
                            mProgressBar.setVisibility(View.VISIBLE);
                            //开始录制
                            Toast.makeText(this, "开始录制", Toast.LENGTH_SHORT).show();
                            startRecord();
                            if (mProgressThread == null) {
                                mProgressThread = new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        try {
                                            mProgress = 0;
                                            isRunning = true;
                                            while (isRunning) {
                                                mProgress++;
                                                mHandler.obtainMessage(0).sendToTarget();
                                                Thread.sleep(20);
                                            }
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                mProgressThread.start();
                            }
                            ret = true;
                        }


                        break;
                    case MotionEvent.ACTION_UP:
                        if (ex > left && ex < right) {
                            mTvTip.setVisibility(View.INVISIBLE);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            //判断是否为录制结束, 或者为成功录制(时间过短)
                            if (!isCancel) {
                                if (mProgress < 50) {
                                    //时间太短不保存
                                    stopRecordUnSave();
                                    if (mProgressThread != null && mProgressThread.isAlive()) {
                                        mProgressThread.interrupt();
                                        mProgressThread = null;
                                    }
                                    Toast.makeText(this, "时间太短", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                //停止录制
                                stopRecordSave();
                            } else {
                                //现在是取消状态,不保存
                                stopRecordUnSave();
                                if (mProgressThread != null && mProgressThread.isAlive()) {
                                    mProgressThread.interrupt();
                                    mProgressThread = null;
                                }
                                isCancel = false;
                                Toast.makeText(this, "取消录制", Toast.LENGTH_SHORT).show();
                                mProgressBar.setCancel(false);
                                finish();
                            }
                            ret = false;
                        }
                    {
//                        stopRecordUnSave();
                        if (isCancel) {
                            if (mProgressThread != null && mProgressThread.isAlive()) {
                                mProgressThread.interrupt();
                                mProgressThread = null;
                            }
                            mProgressBar.setCancel(true);
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(this, "取消录制", Toast.LENGTH_SHORT).show();
                        }
                    }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (ex > left && ex < right) {
                            float currentY = event.getY();
                            if (downY - currentY > 10) {
                                isCancel = true;
                                mProgressBar.setCancel(true);
                            }
                        }else {
                            isCancel = true;
                            mProgressBar.setCancel(true);
                        }
                        break;
                }
                break;

            }

        }
        return ret;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 变焦手势处理类
    ///////////////////////////////////////////////////////////////////////////
    class ZoomGestureListener extends GestureDetector.SimpleOnGestureListener {
        //双击手势事件
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            super.onDoubleTap(e);
            Log.d(TAG, "onDoubleTap: 双击事件");
            if (mMediaRecorder != null) {
                if (!isZoomIn) {
                    setZoom(20);
                    isZoomIn = true;
                } else {
                    setZoom(0);
                    isZoomIn = false;
                }
            }
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        if (requestCode == REQUEST_VIDEO_PERMISSIONS) {
            int i = 0;
            boolean allAllow = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allAllow = false;
                    break;
                }
            }
            if (allAllow){
                initSurface();
            }else{
                ToastUtil.show("请在应用管理中打开“相机”访问权限！");
                finish();
            }
        } else {
           //TODO
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressThread != null){
            mProgressThread = null;
        }
    }
}

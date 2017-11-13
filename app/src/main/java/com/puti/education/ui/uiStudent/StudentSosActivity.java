package com.puti.education.ui.uiStudent;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.puti.education.R;
import com.puti.education.bean.ResponseBean;
import com.puti.education.bean.Role;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.core.audio.AudioManagerFactory;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.RetrofitUtil;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiCommon.BlueBoundActivity;
import com.puti.education.ui.uiCommon.LoginActivity;
import com.puti.education.util.PopupWindowFactory;
import com.puti.education.util.ThreadUtil;
import com.puti.education.util.TimeUtils;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.SchoolSpinnerPopWindow;
import com.puti.education.widget.SpinnerPopWindow;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/22 0022.
 */

public class StudentSosActivity extends BaseActivity {

    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.btn_sos)
    Button mSosBtn;
    @BindView(R.id.layout_frame)
    FrameLayout mFramelayout;
    @BindView(R.id.img_menu)
    ImageView mImgMenu;

    static final int VOICE_REQUEST_CODE = 66;

    private BaiduMap mBaiduMap;
    public BDLocation mMyLocation;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    private AudioManagerFactory mAudioRecoderUtils;
    private Context mContext;
    private PopupWindowFactory mPop;
    private ImageView mImageView;
    private TextView mTextView;

    private double mLat,mLng;
    private boolean mIsSelfSos;
    private String mAudioPath;
    private boolean mIsLocated = false;

    @OnClick(R.id.back_frame)
    public void finishActivity(){
        this.finish();
    }
    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_student_sos;
    }

    @Override
    public void initVariables() {
        int sosType = this.getIntent().getIntExtra("type", -1);
        if (sosType == 1){
            mIsSelfSos = true;
        }else{
            mIsSelfSos = false;
        }

        mBaiduMap = mMapView.getMap();

        requestGpsPermissions();


        mContext = this;

        //PopupWindow的布局文件
        final View view = View.inflate(this, R.layout.layout_microphone, null);

        mPop = new PopupWindowFactory(this,view);

        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);

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
                Toast.makeText(StudentSosActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                mTextView.setText(TimeUtils.long2String(0));
                uploadAudio(filePath);
            }
        });


        //6.0以上需要权限申请
        requestPermissions();
    }


    public void uploadAudio(String filePath){
        disLoading();
        CommonModel.getInstance().uploadOneFile(filePath, 2, new BaseListener(UploadFileBean.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                if (listObj != null){
                    ArrayList<UploadFileBean> uploadFiles = (ArrayList<UploadFileBean>)listObj;
                    mAudioPath = uploadFiles.get(0).fileuid;
                    if (mMyLocation == null){
                        ToastUtil.show("没有获取当前位置,求助失败");
                        return;
                    }
                    launchSos(mMyLocation.getLatitude(), mMyLocation.getLongitude(), "", mAudioPath);
                }

            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("上传音频文件失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
                hideLoading();
            }
        });
    }

    public void launchSos(double lat, double lng, String address, String audioUrl){
        StudentModel.getInstance().launchSos(lat, lng, address, audioUrl, null, new BaseListener(ResponseBean.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();
                ToastUtil.show("发送求助信号成功");
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show("发送求助信号失败"+ (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }


    @Override
    public void initViews() {

    }

    @Override
    public void loadData() {
       // mLocationClient.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null){
            mLocationClient.stop();
        }
        mMapView.onDestroy();
    }


    private void initLocation(){
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );
        //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=2000;
        option.setScanSpan(0);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        //option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(false);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            mMyLocation = location;

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation){

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            Log.i("BaiduLocationApiDem", sb.toString());

            //if (mMyLocation.getLatitude() > 0 && !mIsLocated){
            //    mIsLocated = true;
                ThreadUtil.runAtMain(new Runnable() {
                    @Override
                    public void run() {
                        displayAddr();
                    }
                });
           // }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }


    public void displayAddr(){
        //定义Maker坐标点
        LatLng point = new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude());
        //构建Marker图标

        View view = LayoutInflater.from(this).inflate(R.layout.baidu_marker_view, null);

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                 .fromView(view);

        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .title("这里？")
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        float f = mBaiduMap.getMaxZoomLevel();//获取最小比例级别，然后再
        float zoomLevel = f-5;  //

        MapStatusUpdate status = MapStatusUpdateFactory.newLatLngZoom(point, zoomLevel);
        mBaiduMap.animateMapStatus(status);

    }

    /**
     * 开启扫描之前判断权限是否打开
     */
    private void requestPermissions() {
        //判断是否开启录音权限
        if ((ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                ) {
            StartListener();

            //判断是否开启语音权限
        } else {
            //请求获取语音权限
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, VOICE_REQUEST_CODE);
        }

    }


    /**
     * 开启GPS权限
     */
    private void requestGpsPermissions() {
        if (Build.VERSION.SDK_INT>=23){
            AndPermission.with(this)
                    .requestCode(200)
                    .permission(Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_PHONE_STATE)
                    .rationale(rationaleListener)
                    .callback(listener)
                    .start();
        }else{
            initLocation();
            mLocationClient.start();
        }



    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if(requestCode == 200) {
                initLocation();
                mLocationClient.start();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if(requestCode == 200) {
                ToastUtil.show("获取定位权限失败");
            }
        }
    };

    private RationaleListener rationaleListener = new RationaleListener(){
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            AlertDialog.newBuilder(StudentSosActivity.this)
                    .setTitle("友好提醒")
                    .setMessage("你已拒绝过定位权限，沒有定位定位权限无法精确发送求助信息！")
                    .setPositiveButton("打开", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            rationale.resume();
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            rationale.resume();
                        }
                    }).show();
        }

    };

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

    private SpinnerPopWindow mMenuPop;
    private List<Role> mMenuList;

    @OnClick(R.id.img_menu)
    public void openMenu(){
        mMenuList = new ArrayList<Role>();
        Role r1 = new Role();
        r1.name = "蓝牙绑定";
        Role r2 = new Role();
        r2.name = "解除蓝牙绑定";
        mMenuList.add(r1);
        mMenuList.add(r2);

        if (mMenuPop == null){
            mMenuPop = new SpinnerPopWindow(this);
            mMenuPop.setWidth(300);
            mMenuPop.refreshData(mMenuList);
            mMenuPop.showAsDropDown(mImgMenu);
            mMenuPop.setMyOnItemClickListener(new SpinnerPopWindow.MyOnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    mMenuPop.dismiss();
                    Role ro = mMenuList.get(position);
                    if (ro.name.equals("蓝牙绑定")){
                        goBlueBound();
                    }
                }
            });
        }else{
            mMenuPop.showAsDropDown(mImgMenu);
        }
    }

    private void goBlueBound(){
        Intent intent = new Intent();
        if (mMyLocation != null){
            intent.putExtra("lat", mMyLocation.getLatitude());
            intent.putExtra("lng", mMyLocation.getLongitude());
        }
        intent.putExtra("isSelfSos", mIsSelfSos);
        intent.setClass(this, BlueBoundActivity.class);
        startActivity(intent);
    }



}

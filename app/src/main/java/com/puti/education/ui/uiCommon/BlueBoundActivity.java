package com.puti.education.ui.uiCommon;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanRecord;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.progressindicator.AVLoadingIndicatorView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.DeviceListAdapter;
import com.puti.education.bean.BtDeviceBean;
import com.puti.education.bean.ResponseBean;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.core.audio.AudioManagerFactory;
import com.puti.education.core.bluetooth.BleDefinedUUIDs;
import com.puti.education.core.bluetooth.BleWrapperUiCallbacks;
import com.puti.education.core.service.SosService;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by icebery on 2017/5/11 0015.
 * 蓝牙绑定
 */

public class BlueBoundActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mUiTitle;
    @BindView(R.id.lrv_devices)
    LRecyclerView mEtRecyclerView;
    @BindView(R.id.empty_rel)
    RelativeLayout mEmptyRealtive;

    private static final int ENABLE_BT_REQUEST_ID = 1;
    private static final long SCANNING_TIMEOUT = 8 * 1000; /* 5 seconds */

    private Handler mHandler = new Handler();
    private boolean mScanning = false;
    private BluetoothGattService        mBTService = null;
    private BluetoothGattCharacteristic mBTValueCharacteristic = null;

    private BtDeviceBean mConnectDevice;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private DeviceListAdapter mDeviceAdapter;
    private double mLat, mLng;
    private String mAddress;
    private boolean mIsSelfSos;
    private SosServiceConn mConn;
    private SosService.BtBinder mBtBinder;

    private String mAudioPath;
    private AudioManagerFactory mAudioRecoderUtils;
    private Runnable mRecordTimeout;


    private ArrayList<BtDeviceBean> mDeviceList  = new ArrayList<BtDeviceBean>();

    private int refer;//1 绑定 2 解绑

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_blue_bound_layout;
    }

    @Override
    public void initVariables() {
        mLat =  this.getIntent().getDoubleExtra("lat", 0);
        mLng =  this.getIntent().getDoubleExtra("lng", 0);
        mAddress= this.getIntent().getStringExtra("address");
        mIsSelfSos = this.getIntent().getBooleanExtra("isSelfSos", false);
        refer = getIntent().getIntExtra("refer",0);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //TODO
        }

        disLoading();
        bindBlueService();
        initRecord();
    }

    private void operateBind(){
        if (refer == 2){
            mBtBinder.disConnect();
        }
    }

    public void bindBlueService(){
        if (mConn == null){
            mConn=new SosServiceConn();
        }

        Intent intent = new Intent();
        intent.putExtra("lat", mLat);
        intent.putExtra("lng", mLng);
        intent.putExtra("address", mAddress);
        intent.setClass(this, SosService.class);
        startService(intent);
        bindService(intent, mConn, BIND_AUTO_CREATE);

    }

    public void unbindBlueService(){
        this.unbindService(mConn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAudioRecordTask();
        if (mBtBinder != null){
            mBtBinder.unregisterCallback(mBleCallback);
            //mBtBinder.disConnect();
        }
        unbindBlueService();
    }

    public class SosServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            hideLoading();
            mBtBinder = ((SosService.BtBinder) binder);
            mBtBinder.registerCallback(mBleCallback);

            boolean ret = openBt();
            if (ret){
                BluetoothDevice connectedDevice = mBtBinder.getConnectDevice();
                if (connectedDevice != null){
                    displayConnectedDevice(connectedDevice);
                    return;
                }else{
                    startScaning();
                }
            }else{
                LogUtil.d("", "open blue failed! please retry!!");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            mBtBinder = null;
            hideLoading();
        }
    }


    public void displayConnectedDevice(BluetoothDevice device){
        BtDeviceBean btdevice = new BtDeviceBean();
        String tempName = device.getName();
        if (TextUtils.isEmpty(tempName)){
            btdevice.deviceName = "Unknown device";
        }else{
            btdevice.deviceName = tempName;
        }

        btdevice.deviceMac  = device.getAddress();
        btdevice.deviceRssi = 0;
        if (refer == 2) {
            btdevice.deviceStatus = 2;
        }else {
            btdevice.deviceStatus = 1;
        }
        mDeviceList.add(btdevice);
        mConnectDevice = btdevice;
        mDeviceAdapter.setDataList(mDeviceList);
        if (mDeviceAdapter.mList.size() > 0) {
            if (mEmptyRealtive.getVisibility() == View.VISIBLE) {
                mEmptyRealtive.setVisibility(View.GONE);
            }
        } else {
            if (mEmptyRealtive.getVisibility() == View.GONE) {
                mEmptyRealtive.setVisibility(View.VISIBLE);
            }
        }
        mDeviceAdapter.notifyDataSetChanged();

        if (refer == 2){
            mBtBinder.disConnect();
        }
    }


    @Override
    public void initViews() {
        mUiTitle.setText("蓝牙绑定");

        mDeviceAdapter = new DeviceListAdapter(this);
        mDeviceAdapter.setDataList(mDeviceList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mEtRecyclerView.setLayoutManager(layoutManager);
        mEtRecyclerView.setRefreshProgressStyle(AVLoadingIndicatorView.BallSpinFadeLoader);
        mEtRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, mDeviceAdapter);
        mEtRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mEtRecyclerView.setRefreshing(true);

        mDeviceAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                if (position >= 0 && position <mDeviceList.size()){

                    if (mScanning ) {
                        mScanning = false;
                        if (mBtBinder != null){
                            mBtBinder.stopSearch();
                        }
                    }

                    mConnectDevice = mDeviceList.get(position);
                    boolean ret = false;
                    if (mBtBinder != null){
                        disLoading();
                        ret = mBtBinder.connectDevice(mConnectDevice.deviceMac);
                        if (!ret){
                            ToastUtil.show("连接失败");
                            hideLoading();
                        }
                    }


                }

            }
        });
    }

    @Override
    public void loadData() {
    }

    public void finishActivity(View v){
        this.finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private boolean openBt(){
        //        // first check if BT/BLE is available and enabled
        if(mBtBinder.initBt() == false) return false;

        // on every Resume check if BT is enabled (user could turn it off while app was in background etc.)
        if(mBtBinder.isEnable() == false) {
            // BT is not turned on - ask user to make it enabled
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BT_REQUEST_ID);
            // see onActivityResult to check what is the status of our request
            return false;
        }

        return mBtBinder.isBleAvaible() != false;
    }


    @OnClick(R.id.btn_blue_search)
    public void startScaning(){

        if (!mScanning && mBtBinder != null){
            if (!mBtBinder.isEnable()){
                ToastUtil.show("请打开蓝牙设备");
                return;
            }
            mScanning = true;
            mBtBinder.startSearch();
            disLoading();
            addScanningTimeout();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /* check if user agreed to enable BT */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // user didn't want to turn on BT
        if (requestCode == ENABLE_BT_REQUEST_ID) {
            if(resultCode == Activity.RESULT_CANCELED) {
                btDisabled();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void btDisabled() {
        ToastUtil.show("蓝牙需要打开才能工作");
        //finish();
    }


    /* make sure that potential scanning will take no longer
    * than <SCANNING_TIMEOUT> seconds from now on */
    private void addScanningTimeout() {
        Runnable timeout = new Runnable() {
            @Override
            public void run() {
                if(mBtBinder == null) {
                    return;
                }
                mScanning = false;
                mBtBinder.stopSearch();
                hideLoading();
            }
        };
        mHandler.postDelayed(timeout, SCANNING_TIMEOUT);
    }


    private void getAndDisplayHrValue(BluetoothGattCharacteristic characteristic) {
        byte[] raw = characteristic.getValue();
        final String strRaw = bytesToHexString(raw);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.show(strRaw);
                LogUtil.e("", "bytesToHexString: " + strRaw);
                if (strRaw.equalsIgnoreCase(Constant.BLUETOOTH_SHORT_CLICK_CMD)){
                    ToastUtil.show("收到求救指令");
                    //launchSosByBluetooth();

                    if (mConnectDevice == null){
                        ToastUtil.show("还没有连接设备");
                        return;
                    }
                    if (!mAudioRecoderUtils.isRecording()){
                        ToastUtil.show("开始录音");
                        mAudioRecoderUtils.startRecord();
                        startAudioRecordTask();
                    }else{
                        ToastUtil.show("正在录音");
                    }
                }

            }
        });
    }

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    private void launchSosByBluetooth(){
        if (mConnectDevice != null){
            launchSos(mLat,mLng,mAddress,  mAudioPath, mConnectDevice.deviceMac);
        }else{
            ToastUtil.show("还没有连接设备");
        }

    }


    private BleWrapperUiCallbacks mBleCallback = new BleWrapperUiCallbacks.Null(){

        @Override
        public void uiDeviceConnected(BluetoothGatt gatt, final BluetoothDevice device) {
            super.uiDeviceConnected(gatt, device);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoading();

                    if (device.getAddress().equals(mConnectDevice.deviceMac)){
                        mConnectDevice.deviceStatus = 1;

                        for (BtDeviceBean temBt: mDeviceList){
                            if (!TextUtils.isEmpty(temBt.deviceMac) && temBt.deviceMac.equals(device.getAddress())){
                                temBt.deviceStatus = 1;
                            }else{
                                temBt.deviceStatus = 0;
                            }
                        }

                        mDeviceAdapter.notifyDataSetChanged();
                    }
                    //mBtMgr.startServicesDiscovery();
                    if (mBtBinder != null){
                        mBtBinder.statrServiceDiscovery();
                    }
                }
            });


        }

        @Override
        public void uiDeviceDisconnected(final BluetoothGatt gatt, final BluetoothDevice device) {
            super.uiDeviceDisconnected(gatt, device);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BluetoothGatt bluetoothGatt = gatt;
                    BluetoothDevice bluetoothDevice = device;
                    LogUtil.d("lei","解除绑定");
                    if (null != device
                            &&null != mConnectDevice
                            && device.getAddress().equals(mConnectDevice.deviceMac)){
                        mConnectDevice.deviceStatus = 0;
                        mDeviceAdapter.notifyDataSetChanged();
                    }
                }
            });

        }

        @Override
        public void uiAvailableServices(BluetoothGatt gatt, BluetoothDevice device, List<BluetoothGattService> services) {
            super.uiAvailableServices(gatt, device, services);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //getSosService();
                }
            });
        }

        @Override
        public void uiCharacteristicForService(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, List<BluetoothGattCharacteristic> chars) {
            super.uiCharacteristicForService(gatt, device, service, chars);
        }

        @Override
        public void uiCharacteristicsDetails(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, BluetoothGattCharacteristic characteristic) {
            super.uiCharacteristicsDetails(gatt, device, service, characteristic);
        }

        @Override
        public void uiNewValueForCharacteristic(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, BluetoothGattCharacteristic ch, String strValue, int intValue, byte[] rawValue, String timestamp) {
            super.uiNewValueForCharacteristic(gatt, device, service, ch, strValue, intValue, rawValue, timestamp);
        }

        @Override
        public void uiGotNotification(BluetoothGattCharacteristic characteristic) {
            super.uiGotNotification(characteristic);
                getAndDisplayHrValue(characteristic);

        }

        @Override
        public void uiSuccessfulWrite(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, BluetoothGattCharacteristic ch, String description) {
            super.uiSuccessfulWrite(gatt, device, service, ch, description);
        }

        @Override
        public void uiFailedWrite(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, BluetoothGattCharacteristic ch, String description) {
            super.uiFailedWrite(gatt, device, service, ch, description);
        }

        @Override
        public void uiNewRssiAvailable(BluetoothGatt gatt, BluetoothDevice device, int rssi) {
            super.uiNewRssiAvailable(gatt, device, rssi);
        }

        @Override
        public void uiDeviceFound(final BluetoothDevice device, final int rssi, final ScanRecord record) {
            super.uiDeviceFound(device, rssi, record);
            LogUtil.d("uiDeviceFound", "device: " + device.getName());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addBtDevice(device, rssi, record);
                }
            });
        }
    };

    private void addBtDevice(BluetoothDevice device, int rssi, ScanRecord record){

        if (mDeviceList.size() <= 0){
            BtDeviceBean btdevice = new BtDeviceBean();
            String tempName = device.getName();
            if (TextUtils.isEmpty(tempName)){
                btdevice.deviceName = "Unknown device";
            }else{
                btdevice.deviceName = tempName;
            }

            btdevice.deviceMac  = device.getAddress();
            btdevice.deviceRssi = rssi;
            mDeviceList.add(btdevice);
        }else{
            boolean isFound = false;
            for (BtDeviceBean temBt: mDeviceList){
                if (!TextUtils.isEmpty(temBt.deviceMac) && temBt.deviceMac.equals(device.getAddress())){
                    isFound = true;
                    break;
                }
            }
            if (!isFound){
                BtDeviceBean btdevice = new BtDeviceBean();
                String tempName = device.getName();
                if (TextUtils.isEmpty(tempName)){
                    btdevice.deviceName = "Unknown device";
                }else{
                    btdevice.deviceName = tempName;
                }
                btdevice.deviceMac  = device.getAddress();
                btdevice.deviceRssi = rssi;
                mDeviceList.add(btdevice);
            }
        }

        mDeviceAdapter.setDataList(mDeviceList);
        mEtRecyclerView.refreshComplete();

        if (mDeviceAdapter.mList.size() > 0) {
            if (mEmptyRealtive.getVisibility() == View.VISIBLE) {
                mEmptyRealtive.setVisibility(View.GONE);
            }
        } else {
            if (mEmptyRealtive.getVisibility() == View.GONE) {
                mEmptyRealtive.setVisibility(View.VISIBLE);
            }
        }
        mDeviceAdapter.notifyDataSetChanged();
    }


    public void launchSos(double lat, double lng, String address, String audioUrl, String mac){
        disLoading();
        StudentModel.getInstance().launchSos(lat, lng, address, audioUrl, mac, new BaseListener(ResponseBean.class){

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
                ToastUtil.show("发送求助信号失败");
            }
        });
    }



    public void initRecord(){
        mAudioRecoderUtils = new AudioManagerFactory();

        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioManagerFactory.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
//                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
//                mTextView.setText(TimeUtils.long2String(time));
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                Toast.makeText(BlueBoundActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                //mTextView.setText(TimeUtils.long2String(0));
                uploadAudio(filePath);
            }
        });
    }

    private void startAudioRecordTask(){
        if (mHandler == null){
            return;
        }

        mRecordTimeout = new Runnable() {
            @Override
            public void run() {
                if (mAudioRecoderUtils != null && mAudioRecoderUtils.isRecording()){
                    mAudioRecoderUtils.stopRecord();
                }
            }
        };
        mHandler.postDelayed(mRecordTimeout, 10000);

    }

    private void stopAudioRecordTask(){
        if (mHandler != null){
            mHandler.removeCallbacks(mRecordTimeout);
            mAudioRecoderUtils.cancelRecord();
        }

    }

    public void uploadAudio(String filePath){
        CommonModel.getInstance().uploadOneFile(filePath, 2, new BaseListener(UploadFileBean.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                if (listObj != null){
                    ArrayList<UploadFileBean> uploadFiles = (ArrayList<UploadFileBean>)listObj;
                    mAudioPath = uploadFiles.get(0).fileuid;
                    launchSosByBluetooth();
                }

            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("上传音频文件失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }



}

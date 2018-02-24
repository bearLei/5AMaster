package com.puti.education.core.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanRecord;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.puti.education.bean.ResponseBean;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.core.audio.AudioManagerFactory;
import com.puti.education.core.baidu.GlobeLocation;
import com.puti.education.core.bluetooth.BleWrapperUiCallbacks;
import com.puti.education.core.bluetooth.BtManager;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.ui.uiStudent.StudentSosActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ThreadUtil;
import com.puti.education.util.TimeUtils;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by icebery on 2017/5/5 0005.
 * 后台监听蓝牙求助信号并及时发送到后台
 */

public class SosService extends Service {

    private boolean mIsUploadLoc = false;
    private Timer mTimer = new Timer(true);

    private BtManager mBtMgr;
    private String mDeviceMac;
    private BluetoothDevice mConnectedDevice = null;
    private BluetoothGattService        mBTService = null;
    private BluetoothGattCharacteristic mBTValueCharacteristic = null;
    private boolean mIsBtInit = false;
    private double mLat = 0,mLng = 0;
    private String mAddress ="";

    private Handler mHandler = new Handler();
    private String mAudioPath;
    private AudioManagerFactory mAudioRecoderUtils;
    private Runnable mRecordTimeout;

    private List<BleWrapperUiCallbacks> mBleCallbackList = new ArrayList<>();

    private GlobeLocation.GlobeLocationCb mCb = new GlobeLocation.GlobeLocationCb() {
        @Override
        public void callback(double lng, double lat, String address) {
            LogUtil.d("", "SosService  lat: " + lat + " ,lng: " + lng);
            if (lng > 0 && lat > 0){
                CommonModel.getInstance().reportLocation(lat, lng, new BaseListener(ResponseBean.class){

                    @Override
                    public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                        super.responseResult(infoObj, listObj, code, status);
                        //ToastUtil.show("上报位置成功");
                    }

                    @Override
                    public void requestFailed(boolean status, int code, String errorMessage) {
                        super.requestFailed(status, code, errorMessage);
                        //ToastUtil.show("上报位置失败");
                    }
                });
            }
        }
    };


    public class BtBinder extends android.os.Binder{
        public boolean initBt(){
            boolean ret = false;
            if (mIsBtInit){
                ret = true;
            }else{
                ret = mBtMgr.initialize();
                mIsBtInit = ret;
            }
            return ret;
        }

        public boolean isEnable(){
            boolean ret = false;
            if (!mIsBtInit){
                return false;
            }
            ret = mBtMgr.isBtEnabled();
            return ret;
        }

        public boolean isBleAvaible(){
            boolean ret = false;
            if (!mIsBtInit){
                return false;
            }
            ret = mBtMgr.isBleAvailable();
            return ret;
        }

        public boolean connectDevice(String mac){
            boolean ret = false;
            if (!mIsBtInit){
                return false;
            }
            ret = mBtMgr.connect(mac);
            return ret;
        }

        public void disConnect(){
            if (!mIsBtInit){
                return ;
            }
            mBtMgr.disConnect();
            return;
        }

        public BluetoothDevice getConnectDevice(){
            if (mConnectedDevice != null){
                return mConnectedDevice;
            }
            return null;
        }


        public void startSearch(){
            mBtMgr.startSearchingForSos();
        }

        public void stopSearch(){
            mBtMgr.stopScanning();
        }

        public void statrServiceDiscovery(){
            mBtMgr.startServicesDiscovery();
        }

        public BluetoothGattService getSpeciService(final UUID uuid){
            BluetoothGattService innerService = mBtMgr.getSpeciService(uuid);
            return innerService;
        }

        public void enableNotification(BluetoothGattCharacteristic charac, boolean value){
            mBtMgr.setNotificationForCharacteristic(charac, value);

        }

        public void registerCallback(BleWrapperUiCallbacks cb){
            mBleCallbackList.add(cb);
        }

        public void unregisterCallback(BleWrapperUiCallbacks cb){
            mBleCallbackList.remove(cb);
        }
    }




    @Override
    public void onCreate() {
        super.onCreate();

        String volunterNumber = ConfigUtil.getInstance(this).get(Constant.KEY_VT_NUMBER, "");
        if (!TextUtils.isEmpty(volunterNumber)){
            mIsUploadLoc = true;
        }
        mIsUploadLoc= false;
        if (mIsUploadLoc){
            GlobeLocation.getInstance().initialize(this.getApplicationContext());

            GlobeLocation.getInstance().addCallback(mCb);
            GlobeLocation.getInstance().startLocation();
        }

        mBtMgr = new BtManager(this, mBleCallback);

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
                Toast.makeText(SosService.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                //mTextView.setText(TimeUtils.long2String(0));
                uploadAudio(filePath);
            }
        });
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAudioRecordTask();
        if (mBtMgr != null){
            mBtMgr.disConnect();
            mBtMgr = null;
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mLat = intent.getDoubleExtra("lat", 0);
        mLng = intent.getDoubleExtra("lng", 0);
        mAddress= intent.getStringExtra("address");

        return new BtBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    private BleWrapperUiCallbacks mBleCallback = new BleWrapperUiCallbacks.Null(){

        @Override
        public void uiDeviceConnected(BluetoothGatt gatt, final BluetoothDevice device) {
            super.uiDeviceConnected(gatt, device);
            if (device != null) {
                mConnectedDevice = device;
                mDeviceMac = device.getAddress();
            }
            if (mBleCallbackList != null && mBleCallbackList.size() > 0){
                for (BleWrapperUiCallbacks cb: mBleCallbackList){
                    cb.uiDeviceConnected(gatt,device);
                }
            }


        }

        @Override
        public void uiDeviceDisconnected(BluetoothGatt gatt, final BluetoothDevice device) {
            super.uiDeviceDisconnected(gatt, device);
            if (mBleCallbackList != null && mBleCallbackList.size() > 0){
                for (BleWrapperUiCallbacks cb: mBleCallbackList){
                    cb.uiDeviceDisconnected(gatt,device);
                }
            }
            mConnectedDevice = null;
        }

        @Override
        public void uiAvailableServices(BluetoothGatt gatt, BluetoothDevice device, List<BluetoothGattService> services) {
            super.uiAvailableServices(gatt, device, services);
            ThreadUtil.runAtMain(new Runnable() {
                    @Override
                    public void run() {
                        getSosService();
                    }
                });

        }

        @Override
        public void uiCharacteristicForService(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, List<BluetoothGattCharacteristic> chars) {
            super.uiCharacteristicForService(gatt, device, service, chars);
//            if (mBleCallbackList != null && mBleCallbackList.size() > 0){
//                for (BleWrapperUiCallbacks cb: mBleCallbackList){
//                    cb.uiCharacteristicForService(gatt,device, service, chars);
//                }
//            }
        }

        @Override
        public void uiCharacteristicsDetails(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, BluetoothGattCharacteristic characteristic) {
            super.uiCharacteristicsDetails(gatt, device, service, characteristic);
//            if (mBleCallbackList != null && mBleCallbackList.size() > 0){
//                for (BleWrapperUiCallbacks cb: mBleCallbackList){
//                    cb.uiCharacteristicsDetails(gatt,device, service, characteristic);
//                }
//            }
        }

        @Override
        public void uiNewValueForCharacteristic(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, BluetoothGattCharacteristic ch, String strValue, int intValue, byte[] rawValue, String timestamp) {
            super.uiNewValueForCharacteristic(gatt, device, service, ch, strValue, intValue, rawValue, timestamp);
        }

        @Override
        public void uiGotNotification(BluetoothGattCharacteristic characteristic) {
            super.uiGotNotification(characteristic);
            //当UI层有绑定的监听者时，则把收到的指令发到UI层去处理，　否则的话，就在service里处理
                if(characteristic.equals(mBTValueCharacteristic)) {
                    getAndDisplayHrValue();
                }



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
        public void uiDeviceFound(final BluetoothDevice device, final int rssi, ScanRecord record) {
            super.uiDeviceFound(device, rssi, record);
            LogUtil.d("uiDeviceFound", "device: " + device.getName());
            if (mBleCallbackList != null && mBleCallbackList.size() > 0){
                for (BleWrapperUiCallbacks cb: mBleCallbackList){
                    cb.uiDeviceFound(device,rssi, record);
                }
            }
        }
    };



    private void getSosService() {
        LogUtil.d("", "Getting SOS Service");
        mBTService = mBtMgr.getSpeciService(BtManager.SOS_SERVICE_UUID);

        if(mBTService == null) {
            LogUtil.d("", "Could not get SOS Service");
        }
        else {
            LogUtil.d("", "SOS Service successfully retrieved");
            getHrCharacteristic();
        }
    }

    private void getHrCharacteristic() {
        LogUtil.d("", "Getting SOS notify characteristic");
        mBTValueCharacteristic = mBTService.getCharacteristic(BtManager.mSosCharacteristicUuid);
        if(mBTValueCharacteristic == null) {
            LogUtil.d("", "Could not find SOS notify Characteristic");
        }
        else {
            LogUtil.d("", "SOS notify characteristic retrieved properly");
            enableNotificationForHr();
        }
    }

    private void enableNotificationForHr() {
        LogUtil.d("", "Enabling notification for SOS");
        mBtMgr.setNotificationForCharacteristic(mBTValueCharacteristic, true);
    }

    private void getAndDisplayHrValue() {
        byte[] raw = mBTValueCharacteristic.getValue();
        final String strRaw = bytesToHexString(raw);

        if (mBleCallbackList != null && mBleCallbackList.size() > 0){
            for (BleWrapperUiCallbacks cb: mBleCallbackList){
                cb.uiGotNotification(mBTValueCharacteristic);
            }
        }else{
            ThreadUtil.runAtMain(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.show(strRaw);
                    if (strRaw.equalsIgnoreCase(Constant.BLUETOOTH_SHORT_CLICK_CMD)){
                        ToastUtil.show("收到求救指令");
                        //launchSosByBluetooth();
                        if (!mAudioRecoderUtils.isRecording()){
                            mAudioRecoderUtils.startRecord();
                            startAudioRecordTask();
                        }else{
                            ToastUtil.show("正在录音");
                        }
                    }
                }
            });
        }
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

    private void launchSosByBluetooth(){
        launchSos(mLat,mLng, mAddress, mAudioPath, mDeviceMac);
    }

    public void launchSos(double lat, double lng, String address, String audioUrl, String mac){

        StudentModel.getInstance().launchSos(lat, lng, address, audioUrl, mac, new BaseListener(ResponseBean.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                ToastUtil.show("发送求助信号成功");
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("发送求助信号失败");
            }
        });
    }
}

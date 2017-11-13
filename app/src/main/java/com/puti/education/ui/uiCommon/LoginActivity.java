package com.puti.education.ui.uiCommon;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.puti.education.App;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.LoginUser;
import com.puti.education.bean.ResponseBean;
import com.puti.education.bean.Role;
import com.puti.education.bean.Schools;
import com.puti.education.bean.StudentDetailBean;
import com.puti.education.bean.TeacherPersonInfo;
import com.puti.education.core.service.SosService;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.NetWorkInterceptor;
import com.puti.education.netFrame.RetrofitUtil;
import com.puti.education.netFrame.SchoolApi;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.R;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.fragment.StudentSosFragment;
import com.puti.education.ui.uiPatriarch.PatriarchMainActivity;
import com.puti.education.ui.uiStudent.StudentMainActivity;
import com.puti.education.ui.uiTeacher.ChoosePersonListActivity;
import com.puti.education.ui.uiTeacher.TeacherMainActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.NumberUtils;
import com.puti.education.util.ThreadUtil;
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
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by icebery on 2017/4/15 0015.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv_role_choose)
    TextView mRoleTv;
    @BindView(R.id.tv_school_choose)
    TextView mSchoolTv;
    @BindView(R.id.tv_login_name)
    EditText mLoginNameTv;
    @BindView(R.id.tv_verify_code)
    TextView mBtnVerifyCode;
    @BindView(R.id.tv_login_pwd)
    EditText mLoginPwd;
    @BindView(R.id.tv_pwd_forget)
    TextView mPwdForget;

    private final String TAG = LoginActivity.class.getName();

    private Role mRole;
    private Schools.School mSchool;
    private String mLoginName, mPwd;
    private SpinnerPopWindow mSpinnerPopWindow;
    private SchoolSpinnerPopWindow mSchoolPop;
    private List<Role> mRoleList;
    private List<Schools.School> mSchoolList;
    private int mSecondCount = 60;
    private int mType = 1; //1,登录，　2,忘记密码


    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            App.mMyLocation = location;

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

            //mLocAddress = location.getLocationDescribe();

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


        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    Runnable mUpdateCall = new Runnable() {

        @Override
        public void run() {
            if (mSecondCount > 0) {
                mSecondCount--;
                mBtnVerifyCode.setEnabled(false);
                mBtnVerifyCode.setText("" +mSecondCount+ "秒");
                mBtnVerifyCode.postDelayed(this, 1000);
            }else
            {
                mSecondCount = 60;
                mBtnVerifyCode.setEnabled(true);
                mBtnVerifyCode.setText("获取验证码");
            }

        }
    };

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_login;
    }
    @Override
    public void initVariables() {
        NetWorkInterceptor.resetToken();
        RetrofitUtil.setSchoolBaseUrl(Constant.BASE_IP);
        setRoleList();


        requestGpsPermissions();
    }

    private void test(){

            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "thread1 run1 ");
                    Thread thread2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("", "thread2 run1 ");
                            for (int a = 0; a< 50000; a++){
                                Log.e("", "thread2 a: " + a);
                            }
                            Log.e("", "thread2 run2 ");
                        }
                    });
                    thread2.start();
                    try {
                        thread2.wait();
                    }catch (InterruptedException e){
                        Log.e("", "thread1 run2 ");
                    }

                    Log.e("", "thread1 run2 ");
                }
            });

            thread1.start();
        Log.e("", "thread1 run3 ");

    }

    @Override
    public void initViews() {
        //setRoleView(1);

        int trole = ConfigUtil.getInstance(this).get(Constant.KEY_ROLE_TYPE, -1);
        if (trole > 0){
            String tname = getRoleName(trole);
            mRoleTv.setText(tname);
            setRoleView(trole);
            mRole = new Role();
            mRole.type = trole;
            mRole.name = tname;
        }

        String schoolUid = ConfigUtil.getInstance(this).get(Constant.KEY_SCHOOL_ID, "");
        if (!TextUtils.isEmpty(schoolUid)){
            String schoolName = ConfigUtil.getInstance(this).get(Constant.KEY_SCHOOL_NAME, null);
            if (!TextUtils.isEmpty(schoolName)){
                mSchoolTv.setText(schoolName);
                mSchool = new Schools.School();
                mSchool.name = schoolName;
                mSchool.school_uid = schoolUid;
                mSchool.server_url = ConfigUtil.getInstance(this).get(Constant.KEY_SCHOOL_URL, "");

                RetrofitUtil.setSchoolBaseUrl(mSchool.server_url);
            }
        }


        String tname = ConfigUtil.getInstance(this).get(Constant.KEY_LOGIN_NAME, "");
        if (!TextUtils.isEmpty(tname)){
            mLoginNameTv.setText(tname);
        }

        String tpwd = ConfigUtil.getInstance(this).get(Constant.KEY_LOGIN_PWD, "");
        if (!TextUtils.isEmpty(tpwd)){
            mLoginPwd.setText(tpwd);
        }
    }

    @Override
    public void loadData() {
    }



    @OnClick(R.id.tv_role_choose)
    public void roleChoose(){
        createPopWindow(mRoleList);
    }

    @OnClick(R.id.tv_school_choose)
    public void schoolChoose(){
        RetrofitUtil.setSchoolBaseUrl(Constant.BASE_IP);
        getSchoolList();

    }

    @OnClick(R.id.tv_verify_code)
    public void getVerifyCode(){
        String mobile = mLoginNameTv.getText().toString();
        if (TextUtils.isEmpty(mobile.trim()) ){
            ToastUtil.show("手机号不能为空");
            return;
        }
        getVerifyCode(mobile);

    }

    @OnClick(R.id.btn_login)
    public void login(){
        mType = 1;
        mLoginName = mLoginNameTv.getText().toString();
        if (TextUtils.isEmpty(mLoginName)){
            ToastUtil.show("请输入登录名");
            return;
        }

        mPwd = mLoginPwd.getText().toString();
        if (TextUtils.isEmpty(mPwd)){
            ToastUtil.show("请输入登录密码");
            return;
        }


        App.mJPushRegId = JPushInterface.getRegistrationID(this.getApplication());
        getSchoolUrl();

    }

    private void appLogin(){

        CommonModel.getInstance().login(mLoginName, mPwd, mPwd, null, new BaseListener(LoginUser.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                LoginUser user = (LoginUser)infoObj;
                NetWorkInterceptor.setToken(user.token);
                saveSp(user);


                if (user.type == Constant.ROLE_STUDENT){
                    startStudentUI();
                }else if (user.type == Constant.ROLE_TEACHER){
                    starTeacherUI();
                }else if (user.type == Constant.ROLE_PARENTS){
                    starParentUI();
                }

                //startService();

            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("登录失败" + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }



    private void startService(){
        Intent intent = new Intent();
        intent.setClass(this, SosService.class);
        startService(intent);
    }

    public void saveSp(LoginUser user){
        if (user != null){
            ConfigUtil.getInstance(this).put(Constant.KEY_ROLE_TYPE, user.type);
        }
        if (mSchool != null){
            ConfigUtil.getInstance(this).put(Constant.KEY_SCHOOL_ID, mSchool.school_uid);
            ConfigUtil.getInstance(this).put(Constant.KEY_SCHOOL_NAME, mSchool.name);
            ConfigUtil.getInstance(this).put(Constant.KEY_SCHOOL_URL, mSchool.server_url);
        }

        ConfigUtil.getInstance(this).put(Constant.KEY_LOGIN_NAME, mLoginName);
        ConfigUtil.getInstance(this).put(Constant.KEY_LOGIN_PWD, mPwd);
        ConfigUtil.getInstance(this).put(Constant.KEY_USER_ID, user.uid);
        ConfigUtil.getInstance(this).put(Constant.KEY_USER_NAME, user.name);
        ConfigUtil.getInstance(this).put(Constant.KEY_USER_AVATAR, user.avatar);
        ConfigUtil.getInstance(this).put(Constant.KEY_VT_NUMBER, user.volunteerNo);

        ConfigUtil.getInstance(this).put("classid", "");
        ConfigUtil.getInstance(this).put("classname", "");

        ConfigUtil.getInstance(this).commit();
    }

    @OnClick(R.id.tv_pwd_forget)
    public void pwdForget(){
        mLoginName = mLoginNameTv.getText().toString();
        if (TextUtils.isEmpty(mLoginName)){
            ToastUtil.show("请输入登录名");
            return;
        }

        mType = 2;
        getSchoolUrl();

    }

    private void startPwdForgetUI(){
        String userName = mLoginNameTv.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("schoolid", mSchool.school_uid);
        intent.putExtra("loginname", userName);
        intent.setClass(LoginActivity.this, ForgetPwdActivity.class);
        startActivity(intent);
    }

    private void startStudentUI(){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, StudentMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void starTeacherUI(){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, TeacherMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void starParentUI(){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, PatriarchMainActivity.class);
        startActivity(intent);
        finish();
    }

    public void setRoleList(){
        mRoleList = new ArrayList<Role>();
        Role role1 = new Role();
        role1.type = 1;
        role1.name = "教师";
        Role role2 = new Role();
        role2.type = 2;
        role2.name = "学生";
        Role role4 = new Role();
        role4.type = 4;
        role4.name = "家长";
        mRoleList.add(role1);
        mRoleList.add(role2);
        mRoleList.add(role4);
    }

    private String getRoleName(int type){
        String result = null;
        if (mRoleList != null && mRoleList.size() > 0){
            for (Role trole: mRoleList){
                if (trole.type == type){
                    result = trole.name;
                    break;
                }
            }
        }
        return result;
    };


    //创建角色选择popwindow
    public void createPopWindow(List<Role> list){

        if (mSpinnerPopWindow == null){
            mSpinnerPopWindow = new SpinnerPopWindow(LoginActivity.this);
            mSpinnerPopWindow.setWidth(mRoleTv.getMeasuredWidth());
            mSpinnerPopWindow.refreshData(list);
            mSpinnerPopWindow.showAsDropDown(mRoleTv);
        }else{
            mSpinnerPopWindow.showAsDropDown(mRoleTv);
        }
        mSpinnerPopWindow.setMyOnItemClickListener(new SpinnerPopWindow.MyOnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mSpinnerPopWindow.dismiss();
                mRole = mRoleList.get(position);
                mRoleTv.setText(mRole.name);
                setRoleView(mRole.type);
                Log.i(TAG,"角色名称: "+ mRole.name);

                mLoginNameTv.setText("");
                mLoginPwd.setText("");

            }
        });

    }

    public void setRoleView(int type){
        switch (type){
            case 1: //教师
                //mSchoolTv.setVisibility(View.VISIBLE);
                mPwdForget.setVisibility(View.VISIBLE);
                mLoginNameTv.setHint("账号");
                mLoginPwd.setHint("密码");
                mLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mBtnVerifyCode.setVisibility(View.GONE);
                break;
            case 2://学生
                //mSchoolTv.setVisibility(View.VISIBLE);
                mPwdForget.setVisibility(View.VISIBLE);
                mLoginNameTv.setHint("账号");
                mLoginPwd.setHint("密码");
                mBtnVerifyCode.setVisibility(View.GONE);
                mLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case 4://家长
                //mSchoolTv.setVisibility(View.VISIBLE);
                mBtnVerifyCode.setVisibility(View.GONE);
                mPwdForget.setVisibility(View.VISIBLE);
                mLoginNameTv.setHint("账号");
                mLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mLoginPwd.setHint("密码");
                break;
        }
    }

    private void getVerifyCode(String mobile){
        CommonModel.getInstance().getVerifyCode(mobile, new BaseListener(ResponseBean.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                mBtnVerifyCode.postDelayed(mUpdateCall, 0);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("获取手机验证码出错");
            }
        });
    }

    private void getSchoolUrl(){
        disLoading();
        SchoolApi.getInstance().getSchoolUrls(new BaseListener(Schools.class) {

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                Schools schoollist = (Schools)infoObj;
                hideLoading();

                if (schoollist == null || schoollist.school == null || schoollist.school.size() <= 0){
                    ToastUtil.show("学校列表为空");
                    return;
                }

                matchSchoolIp(schoollist.school);


                //createSchoolPopWindow(mSchoolList);

            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show("获取学校出错 " +(TextUtils.isEmpty(errorMessage)?"":errorMessage));

            }
        });
    }

    private void matchSchoolIp(List<Schools.School> list){
        boolean isFound = false;
        for (Schools.School one: list){
            if (mLoginName.startsWith(one.school_prefix)){
                isFound = true;
                mSchool = one;
                RetrofitUtil.setSchoolBaseUrl(one.server_url);
                break;
            }
        }
        if (!isFound){
            ToastUtil.show("登录用户没有匹配到相关联的学校 " );
            return;
        }

        if  (mType == 1){
            appLogin();
        }else{
            startPwdForgetUI();
        }


    }

    private void getSchoolList() {
        disLoading();
        CommonModel.getInstance().getSchoolList(new BaseListener(Schools.School.class) {

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                mSchoolList = (List<Schools.School>)listObj;
                hideLoading();

                if (mSchoolList == null || mSchoolList.size() <= 0){
                    ToastUtil.show("学校列表为空");
                    return;
                }
                //createSchoolPopWindow(mSchoolList);

            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show("获取学校出错 " +(TextUtils.isEmpty(errorMessage)?"":errorMessage));

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBtnVerifyCode.removeCallbacks(mUpdateCall);
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
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.RECORD_AUDIO)
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
            AlertDialog.newBuilder(LoginActivity.this)
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

    private void initLocation(){
        mLocationClient = new LocationClient(this.getApplicationContext());
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
}

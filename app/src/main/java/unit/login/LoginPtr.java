package unit.login;

import android.app.Activity;
import android.content.Context;

import com.baidu.location.BDLocation;
import com.puti.education.base.BaseMvpPtr;
import com.puti.education.listener.BaseListener;
import com.puti.education.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import unit.api.PutiCommonModel;
import unit.entity.VerifyInfo;
import unit.entity.VerifyPostInfo;
import unit.location.LocationManager;
import unit.location.MapEvent;
import unit.entity.UserInfo;
import unit.util.UserInfoUtils;


/**
 * Created by ${lei} on 2018/6/2.
 */
public class LoginPtr implements BaseMvpPtr {

    private static final int Need_verify = 999;//返回状态码999需要验证码


    private Context mContext;
    private LoginView mView;
    private LocationManager manager;
    private VerifyInfo verifyInfo;//图形验证码信息
    public LoginPtr(Context mContext, LoginView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void star() {
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        getLocation();
    }

    @Override
    public void stop() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
    //获取定位
    public void getLocation(){
        if (manager ==  null){
            manager = LocationManager.g();
        }
        manager.requestGpsPermissions((Activity) mContext);
    }

    //登录
    public void login(){
        PutiCommonModel.getInstance().login(mView.getEditAccount(),mView.getEditPsw(),getPostVerify(),null,new BaseListener(UserInfo.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                UserInfo userInfo = (UserInfo) infoObj;
                handleResult(userInfo);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                //处理下验证码的逻辑
                switch (code){
                    case Need_verify:
                        queryVerify();
                        break;
                }
            }
        });
    }

    //登录成功后的操作
    private void handleResult(UserInfo userInfo){
        //sp存储
        UserInfoUtils.setUserInfo(userInfo);
        //跳转首页
    }

    private void queryVerify(){
        PutiCommonModel.getInstance().queryVerify(1,new BaseListener(){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                verifyInfo = (VerifyInfo) infoObj;
                mView.showVerify(true,verifyInfo);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show(errorMessage);
                mView.showVerify(false,null);
            }
        });
    }

    //组装用户登录的时候上传的验证码信息
    private VerifyPostInfo getPostVerify(){
        VerifyPostInfo info = new VerifyPostInfo();
        if (verifyInfo != null){
            info.setUuidKey(verifyInfo.getUuidKey());
            info.setVericode(mView.getVerify());
        }
        return info;
    }


    /**
     * 定位信息返回
     *
     * @param mapEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on3EventMainThread(MapEvent mapEvent) {
        if (mapEvent.isSuccess){
            BDLocation bdLocation = mapEvent.bdLocation;
            String city = bdLocation.getCity();
            mView.setLocationDesc(city);
        }
    }

}

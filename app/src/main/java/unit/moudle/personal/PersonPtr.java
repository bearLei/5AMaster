package unit.moudle.personal;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.puti.education.base.BaseMvpPtr;

import unit.eventbus.LogoutEvent;
import unit.eventbus.PutiEventBus;
import unit.util.UserInfoUtils;

/**
 * Created by lei on 2018/6/7.
 * 个人中心Ptr
 */

public class PersonPtr implements BaseMvpPtr {

    private Context mContext;

    public PersonPtr(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void star() {
        // TODO: 2018/6/7 后续可以加点初始化操作
    }

    @Override
    public void stop() {

    }

    /**
     * 更换头像
     */
    public void updateAvatar(){

    }

    /**
     * 获取我的二维码
     */
    public void getQrCode(){

    }

    /**
     * 邀请使用
     */
    public void inviteUse(){

    }

    /**
     * 意见反馈
     */
    public void feedBack(){

    }

    /**
     * 去赏个好评
     */
    public void  evaluate(){

    }

    /**
     * 修改密码
     */
    public void updatePsw(){

    }

    /**
     * 故障帮助
     */
    public void troubleHelp(){

    }

    /**
     * 当前版本
     */
    public void currentCode(){

    }

    /**
     * 退出登录
     */
    public void logOut(){
        // TODO: 2018/6/7 登出登录请求

        //清除个人信息
        UserInfoUtils.setUserInfo(null);
        PutiEventBus.post(new LogoutEvent());

    }

    /**
     *
     * @return 当前版本名
     */
    public String getVersionName(){
        PackageManager packageManager = mContext.getPackageManager();
        String versionName = "";
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
             versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
       return versionName;
    }

}

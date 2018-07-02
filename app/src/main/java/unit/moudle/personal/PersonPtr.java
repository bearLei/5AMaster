package unit.moudle.personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.FileUtils;
import com.puti.education.util.SimpleTextWatcher;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.EduDialog;

import java.util.ArrayList;
import java.util.List;

import unit.api.PutiCommonModel;
import unit.api.PutiUploadModel;
import unit.entity.PutiAvatarInfo;
import unit.entity.UpLoadInfo;
import unit.eventbus.LogoutEvent;
import unit.eventbus.PutiEventBus;
import unit.moudle.login.LoginActivity;
import unit.moudle.personal.feedback.FeedBackActivity;
import unit.moudle.personal.qrcode.MyQrCodeActivity;
import unit.moudle.personal.updatepsw.UpdatePswDialog;
import unit.util.MarketUtils;
import unit.util.UserInfoUtils;

/**
 * Created by lei on 2018/6/7.
 * 个人中心Ptr
 */

public class PersonPtr implements BaseMvpPtr {

    private Context mContext;
    private PersonView mView;
    private PhotoChooser chooser;


    public PersonPtr(Context mContext, PersonView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void star() {
        // TODO: 2018/6/7 后续可以加点初始化操作
    }

    @Override
    public void stop() {
        if (chooser != null){
            chooser = null;
        }
    }

    private Bitmap getTempBitmap(String path){
       Bitmap bitmap = FileUtils.getImageThumbnail(path, 720, 1280);
        return bitmap;
    }
    /**
     * 更换头像
     */
    public void updateAvatar() {
            chooser = new PhotoChooser((Activity) mContext,1);
            chooser.chooseDialog(new PhotoChooser.ChooseCallBack() {
                @Override
                public void chooseCamer(String path) {
                    uploadFile(path);
                }

                @Override
                public void chooseAlbum(ArrayList<String> list) {
                    if (list != null && list.size() > 0){
                        uploadFile(list.get(0));
                    }
                }
            });
    }

    private void uploadFile(String path){
        ArrayList<String> tempList = new ArrayList<String>();
        tempList.add(path);
        PutiUploadModel.getInstance().changeAvatar(tempList, 0, new BaseListener(PutiAvatarInfo.class) {
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                PutiAvatarInfo info = (PutiAvatarInfo) infoObj;
                if (info != null){
                    mView.updateAvatar(info.getPhoto());
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("图片上传失败");
            }
        });
    }

    public void ReqlogoutDialog(){
        final EduDialog dialog = new EduDialog(mContext,"确定退出登录吗？");
        dialog.setOnButtonClickListener(new EduDialog.OnButtonClickListener() {
            @Override
            public void onNegativeButtonClick(View view) {
                logOut();
                dialog.dismiss();
            }

            @Override
            public void onPositiveButtonClick(View view) {
                dialog.dismiss();
            }
        },"确定","取消");
        dialog.show();
    }

    /**
     * 获取我的二维码
     */
    public void getQrCode(){
        Intent intent = new Intent(mContext, MyQrCodeActivity.class);
        mContext.startActivity(intent);
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
        // TODO: 2018/6/8 反馈页面
        Intent intent = new Intent(mContext, FeedBackActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 去赏个好评
     */
    public void  evaluate(){
        MarketUtils.goScore(mContext);
    }

    /**
     * 修改密码
     */
    public void updatePsw(){
        UpdatePswDialog dialog = new UpdatePswDialog(mContext);
        dialog.show();
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
            PutiCommonModel.getInstance().logout(new BaseListener(){
                @Override
                public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                    super.responseResult(infoObj, listObj, code, status);
                    UserInfoUtils.setUserInfo(null);
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                }

                @Override
                public void requestFailed(boolean status, int code, String errorMessage) {
                    super.requestFailed(status, code, errorMessage);
                    ToastUtil.show(errorMessage);
                }
            });
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

    public void onActivityResult(int requestCode, int resultCode, Intent data, ImageView imageView) {
        if (chooser != null){
            chooser.onActivityResult((Activity) mContext, requestCode, resultCode, data, imageView);
        }
    }

}

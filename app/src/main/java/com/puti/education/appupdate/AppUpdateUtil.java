package com.puti.education.appupdate;//package com.puti.education.appupdate;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.VersionInfo;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.util.LogUtil;
import com.puti.education.zxing.ZxingUtil;

import java.io.File;
import java.util.Random;

/**
 * Created by lei on 2017/11/11.
 *检查更新的类
 */
public class AppUpdateUtil {


    private static final String TAG = "APPAPDATE" ;
    private String upload_url;
    private static class SingleInstance{
        private static final AppUpdateUtil Instance = new AppUpdateUtil();
    }
    public static AppUpdateUtil g(){
        return AppUpdateUtil.SingleInstance.Instance;
    }

    public static void update(Context context, String StrFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri= FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", new File(StrFile));
        }else {
            uri = Uri.fromFile(new File(StrFile));
        }
        intent.setDataAndType(uri,
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    public void queryVersion(final Activity activity){
        int code = 1;
        if (activity.getPackageManager() != null){
            try {
                PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(),0);
                if (info != null){
                    code = info.versionCode;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        CommonModel.getInstance().checkVersion(code,new BaseListener(VersionInfo.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                VersionInfo versionInfo = (VersionInfo) infoObj;
                if (versionInfo == null) return;
                 upload_url = versionInfo.update_url;
                if (versionInfo.isNeedUpdate){
                  creatDialog(activity);
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
            }
        });
    }

    private UpdateDialog dialog;
    private void creatDialog(Activity activity){
        if (dialog == null){
            dialog = new UpdateDialog(activity);
            dialog.setUpdate_url(upload_url);
        }
        dialog.show(activity.getFragmentManager(), TAG);
    }



}

package com.puti.education.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.lidong.photopicker.PhotoPickerActivity;

/**
 * Created by xjbin on 2017/6/5 0005.
 *
 * 使用方法参考 PhotoPickerActivity.java文件
 */

public class PermissionUtil {

    Activity activity;

    public PermissionUtil(Activity activity){
        this.activity = activity;
    }

    /**
     *
     * @return -1没有权限 1有权限
     */
    private int requestPermissions(Activity activity,String permissionName,int requestCode,int dialogMsgId) {

        if (Build.VERSION.SDK_INT>=23){

            if (ContextCompat.checkSelfPermission(activity,permissionName) != PackageManager.PERMISSION_GRANTED){

                //用户上次拒绝过
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,permissionName)){

                    rationaleDialog(dialogMsgId);

                    return -1;

                }else{
                    ActivityCompat.requestPermissions(activity, new String[]{permissionName},requestCode);
                    return -1;
                }

            }else{

                return 1;
            }

        }else{
            return 1;
        }
    }


    //针对用户已经禁止过权限弹出的提示框
    private void rationaleDialog(int msgId){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getResources().getString(msgId));
        builder.setTitle("提示");
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                startToSettingActivity();
                activity.finish();
            }
        });
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
            }
        });
        builder.show();
    }

    //跳转到权限设置界面（可能不是很全面）
    private void startToSettingActivity(){
        Uri packageURI = Uri.parse("package:" + activity.getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        activity.startActivity(intent);
    }
}

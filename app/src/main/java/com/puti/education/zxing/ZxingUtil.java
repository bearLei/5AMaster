package com.puti.education.zxing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.formax.lib_zxing.activity.CaptureActivity;
import com.formax.lib_zxing.activity.CodeUtils;
import com.formax.lib_zxing.activity.ZXingLibrary;
import com.puti.education.bean.oldStudent;
import com.puti.education.util.FileUtils;


/**
 * Created by lei on 2017/11/12.
 * 扫一扫的耦合类
 */
public class ZxingUtil {
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
   private static class SingleInstance{
       private static final ZxingUtil  Instance = new ZxingUtil();
    }
    public static ZxingUtil g(){
        return SingleInstance.Instance;
    }
    public  void startZxing(Activity context){
        ZXingLibrary.initDisplayOpinion(context);
        Intent intent = new Intent(context, CaptureActivity.class);
        context.startActivityForResult(intent,REQUEST_CODE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data,ZxingCallBack zxingCallBack) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    if (zxingCallBack != null) {
                        zxingCallBack.fail();
                    }
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (zxingCallBack != null) {
                        zxingCallBack.result(result);
                    }
//                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                    Toast.makeText(, "解析二维码失败", Toast.LENGTH_LONG).show();
                    if (zxingCallBack != null) {
                        zxingCallBack.fail();
                    }
                }
            }
        }

    }

    public void createImage(String msg){
        oldStudent student = new oldStudent();
        student.setUid("123456789");
        student.setAvatar("");
        student.setClassName("初二十班");
        student.setMajor("化学");
        student.setName("赵日天");
        student.setNumber("007");
        student.setSex("不男女");
       JSONObject jsonObject = new JSONObject();
        jsonObject.put("uid",student.getUid());
        jsonObject.put("avatar",student.getAvatar());
        jsonObject.put("className",student.getClassName());
        jsonObject.put("name",student.getName());
        jsonObject.put("number",student.getNumber());
        jsonObject.put("sex",student.getSex());

        Bitmap image = CodeUtils.createImage(jsonObject.toString(), 100, 100, null);
        // TODO: 2017/11/14 暂时不需要
        FileUtils.saveBitmap(image,FileUtils.getAppFilePath()+"zxing.jpg",1);
    }


    private ZxingCallBack zxingCallBack;

    public interface ZxingCallBack{
        void result(String result);
        void fail();
    }
}

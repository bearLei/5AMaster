package com.puti.education.util;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.puti.education.netFrame.RetrofitUtil;

import java.io.File;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * glide 图片框架封装
 */
public class ImgLoadUtil {

    public static void displayPic(int res, String imageUrl, ImageView imageView) {
        String relativeUrl = "";
        if (!TextUtils.isEmpty(imageUrl)){
            relativeUrl = imageUrl.replace("\\", "/");
        }
        //String fullUrl = RetrofitUtil.PHOTO_URL +  relativeUrl;
        String fullUrl = relativeUrl;
        Glide.with(imageView.getContext())
                .load(fullUrl)
                .placeholder(res)
                .error(res)
                .crossFade(200)
                .into(imageView);
    }

    public static void displayCirclePic(int res, String imageUrl, ImageView imageView) {
        String relativeUrl = "";
        if (!TextUtils.isEmpty(imageUrl)){
            relativeUrl = imageUrl.replace("\\", "/");
        }

        //String fullUrl = RetrofitUtil.PHOTO_URL +  relativeUrl;
        String fullUrl = relativeUrl;
        Glide.with(imageView.getContext())
                .load(fullUrl)
                .placeholder(res)
                .error(res)
                .transform(new GlideCircleTransform(imageView.getContext()))
                .crossFade(200)
                .into(imageView);
    }



    /**
     * 显示高斯模糊效果
     */
    private static void displayBlushPic(Context context,int res, String url, ImageView imageView) {
        String relativeUrl = "";
        if (!TextUtils.isEmpty(url)){
            relativeUrl = url.replace("\\", "/");
        }

        String fullUrl = RetrofitUtil.PHOTO_URL +  relativeUrl;
        Glide.with(context)
                .load(fullUrl)
                .error(res)
                .placeholder(res)
                .crossFade(500)
                .bitmapTransform(new BlurTransformation(context, 23, 4))// "23":模糊度；"4":图片缩放4倍后再进行模糊
                .into(imageView);
    }


    public static  void displayLocalPictrue(Context context,int res,File file,ImageView imageView){

        Glide.with(context)
                .load(Uri.fromFile(file))
                .error(res)
                .placeholder(res)
                .crossFade(200)
                .into(imageView);

    }
}

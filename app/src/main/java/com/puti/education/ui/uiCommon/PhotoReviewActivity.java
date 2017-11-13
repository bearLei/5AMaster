package com.puti.education.ui.uiCommon;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.puti.education.R;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.ToastUtil;

import java.io.File;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class PhotoReviewActivity extends BaseActivity {

    @BindView(R.id.iv_view)
    ImageView mPhotoView;

    private int mType   = -1;  //1, 表示从网络获取，　2.表示从本地获取
    private String mUrl = null;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_photo_preview;
    }

    @Override
    public void initVariables() {
        mType   = this.getIntent().getIntExtra("type", -1);
        mUrl    = this.getIntent().getStringExtra("url");
    }

    @Override
    public void initViews() {
        Toolbar mToolbar = (Toolbar) findViewById(com.lidong.photopicker.R.id.pickerToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadData() {
        if (mType == 1 && !TextUtils.isEmpty(mUrl)){
            ImgLoadUtil.displayPic(R.mipmap.empty_content, mUrl, mPhotoView);
        }else if (mType == 2 && !TextUtils.isEmpty(mUrl)){
            File f= new File(mUrl);
            ImgLoadUtil.displayLocalPictrue(this, R.mipmap.empty_content, f, mPhotoView);
        }else{
            ToastUtil.show("图片类型或地址为空，请重新确认");
        }

    }

    @Override
    public void finishActivity(View v) {
        super.finishActivity(v);
    }
}

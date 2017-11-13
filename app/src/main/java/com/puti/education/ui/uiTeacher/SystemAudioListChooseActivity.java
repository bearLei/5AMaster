package com.puti.education.ui.uiTeacher;

import android.content.Entity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.adapter.SystemAudioListAdapter;
import com.puti.education.bean.LocalFile;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiCommon.RecordOnlyActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.LogUtil;
import com.puti.education.util.SortByDate;
import com.puti.education.util.ThreadUtil;
import com.puti.education.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xjbin on 2017/5/23 0023.
 *
 * 系统音频选择
 */

public class SystemAudioListChooseActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.tv_right)
    TextView mRightTv;
    @BindView(R.id.event_commit_btn)
    Button mCommit;

    SystemAudioListAdapter adapter;
    ArrayList<LocalFile> localFiles;
    ArrayList<LocalFile> tempLocalFiles;

    private boolean mIsExit = false;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_system_audiolist;
    }

    @Override
    public void initVariables() {
        tempLocalFiles = getIntent().getParcelableArrayListExtra(Key.BEAN);
        localFiles = new ArrayList<>();
    }

    @Override
    public void initViews() {
        mTitleTv.setText("音频文件");
        mRightTv.setVisibility(View.VISIBLE);
        mRightTv.setText("录音");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(layoutManager);
        adapter = new SystemAudioListAdapter(this);
        mRv.setAdapter(adapter);

        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseClick();
            }
        });
    }

    @OnClick(R.id.tv_right)
    public void recordAudio(){
        Intent intent = new Intent();
        intent.setClass(this, RecordOnlyActivity.class);
        startActivityForResult(intent, Constant.VOICE_RECORD_REQUEST_CODE);
    }

    public void chooseClick() {
        long totalSize = 0;
        ArrayList<LocalFile> tempList = new ArrayList<>();
        for (LocalFile localFile : localFiles) {
            if (localFile.isCheck == 1) {
                tempList.add(localFile);
                totalSize = totalSize + (new File(localFile.localPath)).length();
            }
        }

        if (totalSize > 4*1024*1024){
            ToastUtil.show("音频上传大小不能超过4M");
            return;
        }

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(Key.BEAN, tempList);
        setResult(Constant.CODE_RESULT_MEDIA, intent);
        finish();
    }

    @Override
    public void loadData() {

        Thread scanThread = new Thread(){
            @Override
            public void run() {

                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    //File root = Environment.getExternalStorageDirectory();
                    File root = new File(Constant.AUDIO_DIR);
                    localFiles.clear();
                    getSDFile(root);

                    if (tempLocalFiles != null) {
                        LocalFile tempLocalFile1;
                        LocalFile tempLocalFile2;

                        for (int i = 0; i < tempLocalFiles.size(); i++) {
                            tempLocalFile1 = tempLocalFiles.get(i);
                            for (int j = 0; j < localFiles.size(); j++) {
                                tempLocalFile2 = localFiles.get(j);
                                if (tempLocalFile1.fileName.equals(tempLocalFile2.fileName) && tempLocalFile1.localPath.equals(tempLocalFile2.localPath)) {
                                    tempLocalFile2.isCheck = tempLocalFile1.isCheck;
                                }
                            }
                        }
                    }

                    Collections.sort(localFiles, new SortByDate());
                    ThreadUtil.runAtMain(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setDataList(localFiles);
                        }
                    });

                }else{

                    ThreadUtil.runAtMain(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show("没有SD卡");
                        }
                    });
                }
            }
        };
        scanThread.setDaemon(true);
        scanThread.start();
    }

    private void scanSystemAudio() {

        // 扫描外部设备中的音频
        String str[] = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE};
        Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, str, null, null, null);
        System.out.println("count = " + cursor.getCount());  //获取总共有多少个条目

        localFiles = new ArrayList<>();
        LocalFile localFile = null;
        while (cursor.moveToNext()) {
            localFile = new LocalFile(cursor.getString(1), cursor.getString(3), cursor.getString(2), 0);
            localFiles.add(localFile);
        }

        if (tempLocalFiles != null) {

            LocalFile tempLocalFile1;
            LocalFile tempLocalFile2;

            for (int i = 0; i < tempLocalFiles.size(); i++) {
                tempLocalFile1 = tempLocalFiles.get(i);
                for (int j = 0; j < localFiles.size(); j++) {
                    tempLocalFile2 = localFiles.get(i);
                    if (tempLocalFile1.fileName.equals(tempLocalFile2.fileName)) {
                        tempLocalFile2.isCheck = tempLocalFile1.isCheck;
                    }
                }
            }
        }

        ThreadUtil.runAtMain(new Runnable() {
            @Override
            public void run() {
                adapter.setDataList(localFiles);
            }
        });
    }

    private void getSDFile(File root) {

        HashSet<LocalFile> hashSet = new HashSet<>();
        // 遍历接收一个文件路径，然后把文件子目录中的所有文件遍历并输出来
        File files[] = root.listFiles();
        //为空的文件夹，不做任何动作
        if (files != null && !mIsExit) {
            for (File f : files) {
                if (f.isDirectory())//判断是否是文件夹
                {
                    getSDFile(f);
                } else {

                    LocalFile localFile = null;

                    //获取小于限制大小的音频文件
                    if (f.length() <= Constant.UPLOAD_FILE_SIZE &&
                            (f.getPath().endsWith(".amr") || f.getPath().endsWith(".mp3") || f.getPath().endsWith("m4a"))) {

                        localFile = new LocalFile(f.getName(), f.length() + "", f.getPath(), f.lastModified());
                        localFiles.add(localFile);

                    }
                }
            }
        }

    }

    public void stopScanFile(){
        mIsExit = true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Constant.VOICE_RECORD_RESULT_CODE: {
                mIsExit = false;
                loadData();
                break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopScanFile();
    }

    public void finishActivity(View view) {
        finish();
    }
}

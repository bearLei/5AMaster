package com.puti.education.util;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;


/**
 * Created by xbjin on 2017/5/27 0027.
 *
 *  系统文件下载器
 *
 */

public class DownLoadManagerUtil {

    static DownLoadManagerUtil downLoadManagerUtil;
    private int downloadId;
    private DownloadManager dManager;

    public static  DownLoadManagerUtil getInstance(){

        if (downLoadManagerUtil == null){
                 downLoadManagerUtil = new DownLoadManagerUtil();
        }
        return downLoadManagerUtil;
    }

    public long downLoadFile(Context context,String downLoadPath){
         String fileName = downLoadPath.substring(downLoadPath.lastIndexOf("/")+1);
          dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
         Uri uri = Uri.parse(downLoadPath);
         DownloadManager.Request request = new DownloadManager.Request(uri);
        // 设置下载路径和文件名
        String downloadPath = Constant.STORAGE_DIR+"/download";
        LogUtil.i("down load file dir: ",downloadPath);
         LogUtil.i("down load file name: ",fileName);

         request.setDestinationInExternalPublicDir(downloadPath, fileName);
         request.setDescription("文件下载");
         request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED | DownloadManager.Request.VISIBILITY_VISIBLE);
         request.setVisibleInDownloadsUi(true);//是否显示下载界面
         // 设置为可被媒体扫描器找到
         request.allowScanningByMediaScanner();
         // 设置为可见和可管理
         request.setVisibleInDownloadsUi(true);
        context.registerReceiver(receiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
         // 获取此次下载的ID
        downloadId = (int) dManager.enqueue(request);
        return dManager.enqueue(request);

    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor c = dManager.query(query);
            if (c.moveToFirst()){
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                LogUtil.d("lei","当前状态："+status);
                switch (status){
                    case DownloadManager.STATUS_SUCCESSFUL:
                        if (downStatusImp != null){
                            downStatusImp.success();
                        }
                        break;
                    case DownloadManager.STATUS_RUNNING:
                        if (downStatusImp != null){
                            downStatusImp.downloading();
                        }
                        break;
                    case DownloadManager.STATUS_FAILED:
                        if (downStatusImp != null){
                            downStatusImp.fail();
                        }
                        break;
                }
            }
        }
    };

    public void openDownloadFile(Context context,long downloadId){

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = null;
        try {
            c = downloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                openFileToPlay(context,c);
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    //打开下载文件
    private void openFileToPlay(Context context, Cursor cursor) {

        String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        String mimetype = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
        Uri path = Uri.parse(filePath);
        if (path.getScheme() == null) {
            path = Uri.fromFile(new File(filePath));
        }
        Intent activityIntent = new Intent(Intent.ACTION_VIEW);
        activityIntent.setDataAndType(path, mimetype);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(activityIntent);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "text/plain";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }

    private DownStatusImp downStatusImp;

    public void setDownStatusImp(DownStatusImp downStatusImp) {
        this.downStatusImp = downStatusImp;
    }

    public interface DownStatusImp{
        void success();
        void fail();
        void downloading();
    }

}

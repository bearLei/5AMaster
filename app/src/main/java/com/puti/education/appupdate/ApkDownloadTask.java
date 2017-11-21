package com.puti.education.appupdate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

import com.puti.education.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


@SuppressWarnings("ConstantConditions")
public class ApkDownloadTask extends AsyncTask<String, Integer, String> {
    private static final String TAG = "ApkDownloadTask";
    private long downloadSize = 0;
    private Context m_context;
    private ProgressDialog m_Bar;
    private static final int TIMEOUT = 10 * 1000;
    private String UpdateDir = null;
    private String UpdateFile = null;
    private boolean mDownloadCanceled = false;

    private String reportDownloadUrl;
    private long reportTimeStart;
    private long reportTimeEnd;
    private long reportTimeDuring;
    private long reportFilesize;
    private String reportResult;
    private String reportHttpCode;

    private long mTime=0;
    public ApkDownloadTask(Context context) {
        this(context, "FormaxCopyMaster.apk");
    }

    public ApkDownloadTask(Context context, String apkFileName) {
        m_context = context;
        UpdateDir = FileUtils.getAppDownloadPath();
        UpdateFile = UpdateDir + apkFileName;
    }

    @Override
    protected String doInBackground(String... arg0) {

//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        reportDownloadUrl =  arg0[0];
        Log.i(TAG, "下载的URL = " + arg0[0]);

        mTime= System.currentTimeMillis();
        reportTimeStart = mTime;
        try {

            if (createDirectory(UpdateDir)) {
                Log.i(TAG, "创建目录成功" + UpdateDir);
                downloadSize = downloadUpdateFile(arg0[0], UpdateFile);
            } else {
                reportResult = "创建目录失败"+UpdateDir;
                publishProgress(-1);
                Log.i(TAG, "创建目录失败" + UpdateDir);
            }

        } catch (Exception e) {
            reportResult = "下载出现问题=" + e.getMessage();
            Log.i(TAG, "下载出现问题=" + e);
            publishProgress(-1);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        m_Bar.dismiss();
        reportTimeEnd = System.currentTimeMillis();
        if (downloadSize > 0 && !mDownloadCanceled) {
            Log.i(TAG, "APP下载完毕");
            mTime= System.currentTimeMillis()-mTime;
            if(downloadSize>=reportFilesize){
                reportResult = "APK下载成功";
            }else{
                reportResult = "APK文件下载不完整"+downloadSize;
            }
           AppUpdateUtil.update(m_context, UpdateFile);
        } else if(mDownloadCanceled){
            reportResult = "取消下载";
        } else {
            Log.i(TAG, "已经取消了下载");
            FileUtils.delFile(UpdateFile);
        }
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        m_Bar = new ProgressDialog(m_context);
//        if (AppUpdate.s_force_update) {
//            m_Bar.setCancelable(false);
//        }
        // m_Bar.setTitle("正在下载");
        // m_Bar.setMessage("请稍候...");
        m_Bar.setCanceledOnTouchOutside(false);
        m_Bar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        m_Bar.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                mDownloadCanceled = true;
//                ToastUtil.showToastLong(m_context.getResources().getString(R.string.download_canceled));
            }
        });
        m_Bar.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if (values[0] == -1 && !mDownloadCanceled) {
//            ToastUtil.showToastLong(m_context.getResources()
//                    .getString(R.string.update_failed));
        } else {
            m_Bar.setProgress(values[0]);
        }

    }

    private long downloadUpdateFile(String down_url, String file)
            throws Exception {
        int totalSize;// 文件总大小
        int downloadCount = 0;// 已经下载好的大小
        InputStream inputStream;
        OutputStream outputStream;

        URL url = new URL(down_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);

        httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
        httpURLConnection.connect();
        // 获取下载文件的size

        totalSize = httpURLConnection.getContentLength();
        // m_Bar.setMax(totalSize);
        Log.i(TAG, "总的大小=" + totalSize);
        reportFilesize = totalSize;
        reportHttpCode = httpURLConnection.getResponseCode()+"";
        if (httpURLConnection.getResponseCode() == 404) {
            throw new Exception("fail!");
        }

        inputStream = httpURLConnection.getInputStream();
        outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
        byte buffer[] = new byte[1024 * 4];
        int readsize;
        while ((readsize = inputStream.read(buffer)) != -1 && !mDownloadCanceled) {
            outputStream.write(buffer, 0, readsize);
            downloadCount += readsize;// 时时获取下载到的大小
            // LogUtil.i(NetInterface.TAG,"下载的大小="+ downloadCount );
            int size = downloadCount / (totalSize / (1024 / 10));
            // publishProgress(downloadCount);
            publishProgress(size);
//            Thread.sleep(30);
        }

        httpURLConnection.disconnect();
        inputStream.close();
        outputStream.close();

        return downloadCount;

    }

    private boolean createDirectory(String filePath) {
        if (null == filePath) {
            return false;
        }

        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }

        return file.mkdirs();

    }

}

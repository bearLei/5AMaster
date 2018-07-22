package unit.moudle.eventdeal.holder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.Constant;
import com.puti.education.util.DownLoadManagerUtil;
import com.puti.education.util.LogUtil;
import com.puti.education.util.PopupWindowFactory;
import com.puti.education.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.DealEventMain;
import unit.entity.Evidence;
import unit.moudle.eventdeal.adapter.EvidenceImageAdapter;
import unit.moudle.eventdeal.adapter.EvidenceVioceAdapter;

/**
 * Created by lei on 2018/6/22.
 * 事件确认详情-头部基础信息
 */

public class DealEventDetailHeadHolder extends BaseHolder<DealEventMain> {

    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.evidence_icon_layout)
    GridView evidenceIconLayout;
    @BindView(R.id.evidence_vioce_layout)
    GridView evidenceVioceLayout;
    @BindView(R.id.evidence_video_layout)
    LinearLayout evidenceVideoLayout;
    @BindView(R.id.evidence_tv)
    TextView evidenceTv;

    private EvidenceImageAdapter mImageAdapter;
    private EvidenceVioceAdapter mVioceAdapter;

    private PopupWindowFactory mPop;
    private String mDownloadPath;
    private ProgressDialog mProgressDialog;
    private String videoUrl;

    public DealEventDetailHeadHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_deal_event_head_holder);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mRootView.setLayoutParams(params);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, DealEventMain data) {
        if (data == null) {
            return;
        }
        time.setText(data.getTime());
        address.setText(data.getAddress());
        desc.setText(data.getDescription());
        List<Evidence> evidences = data.getEvidences();
        if (evidences == null || evidences.size() == 0){
            evidenceTv.setVisibility(View.GONE);
        }else {
            evidenceTv.setVisibility(View.VISIBLE);
        }
        List<String> imageData = new ArrayList<>();
        List<String> voiceData = new ArrayList<>();
        for (int i = 0; i < evidences.size(); i++) {
            Evidence evidence = evidences.get(i);
            switch (evidence.getFileType()) {
                case 0:
                    imageData.add(evidences.get(i).getFile());
                    break;
                case 1:
                    voiceData.add(evidence.getFile());
                    break;
                case 2:
                    videoUrl = evidence.getFile();
                    break;

            }
        }
        if (imageData.size() > 0) {
            evidenceIconLayout.setVisibility(View.VISIBLE);
            mImageAdapter = new EvidenceImageAdapter(mContext, (ArrayList<String>) imageData);
            evidenceIconLayout.setAdapter(mImageAdapter);
        } else {
            evidenceIconLayout.setVisibility(View.GONE);
        }

        if (voiceData.size() > 0) {
            evidenceVioceLayout.setVisibility(View.VISIBLE);
            mVioceAdapter = new EvidenceVioceAdapter(mContext, voiceData);
            mVioceAdapter.setChooseVioceCallBack(new EvidenceVioceAdapter.ChooseVioceCallBack() {
                @Override
                public void show() {
                    showPopDialog(mRootView);
                }

                @Override
                public void dissmiss() {
                    if (mPop != null) {
                        mPop.dismiss();
                    }
                }
            });
            evidenceVioceLayout.setAdapter(mVioceAdapter);

        } else {
            evidenceVioceLayout.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(videoUrl)) {
            evidenceVideoLayout.setVisibility(View.GONE);
        } else {
            evidenceVideoLayout.setVisibility(View.VISIBLE);
            evidenceVideoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoPlay(videoUrl);
                }
            });
        }
    }

    private void videoPlay(final String path) {

        LogUtil.i("video play path", path);

        final File file = isHasCurrentFile(path);

        if (file != null) {
            //播放
            startPlayVideo(file);
        } else {
            //下载
            Toast.makeText(mContext, "开始下载...", Toast.LENGTH_LONG).show();
            disLoading("正在下载...");
            DownLoadManagerUtil.getInstance().downLoadFile(mContext, path);
            DownLoadManagerUtil.getInstance().setDownStatusImp(new DownLoadManagerUtil.DownStatusImp() {
                @Override
                public void success() {
                    hideLoading();
                    videoPlay(videoUrl);
                }

                @Override
                public void fail() {
                    hideLoading();
                    ToastUtil.show("下载视频失败");
                }

                @Override
                public void downloading() {

                }
            });
        }
    }

    private void startPlayVideo(File videofile) {
        String absolutePath = videofile.getAbsolutePath();
        //调用系统自带的播放器
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            Uri uri;
            if (Build.VERSION.SDK_INT >= 24) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", new File(videofile.getAbsolutePath()));
            } else {
                uri = Uri.parse("file://" + videofile.getAbsolutePath());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            intent.setDataAndType(uri, "video/mp4");
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disLoading(String msg) {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private File isHasCurrentFile(String url) {
        mDownloadPath = Constant.STORAGE_ROOT + "/download/";
        String curentFileName = url.substring(url.lastIndexOf("/") + 1);
        File fileDir = new File(mDownloadPath);

        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File[] listFile = fileDir.listFiles();
        int size = listFile.length;
        for (int i = 0; i < size; i++) {
            if (listFile[i].getName().equals(curentFileName)) {
                return listFile[i];
            }
        }
        return null;
    }

    private void showPopDialog(View viewPar) {
        //PopupWindow的布局文件
        final View view = View.inflate(mContext, R.layout.layout_speaker, null);
        View time = view.findViewById(R.id.tv_recording_time);
        time.setVisibility(View.GONE);
        mPop = new PopupWindowFactory(mContext, view);
        mPop.showAtLocation(viewPar, Gravity.CENTER, 0, 0);
    }
}

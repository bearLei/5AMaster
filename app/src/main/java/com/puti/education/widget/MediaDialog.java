package com.puti.education.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.adapter.ABaseAdapter;
import com.puti.education.adapter.MediaListAdapter;
import com.puti.education.bean.AudioRecord;
import com.puti.education.bean.EventFile;
import com.puti.education.bean.Proof;
import com.puti.education.util.PopupWindowFactory;
import com.puti.education.util.TimeUtils;

import java.util.ArrayList;

/**
 * Created by xjbin on 2017/5/16 0016.
 */

public class MediaDialog extends BaseDialog {

    private String mMediaType;
    private int mDisplayType;
    private String mTime;
    private ArrayList<Proof> mMediaRecord;
    private Context mContext;

    private PopupWindowFactory mPop;
    private ImageView mImageView;
    private TextView mTextView;
    private LinearLayout mFramelayout;

    public MediaDialog(Context context,ArrayList<Proof> files, String tracetime, String type, int displayType) {
        super(context);
        this.mMediaRecord = files;
        this.mContext = context;
        this.mTime = tracetime;
        this.mMediaType = type;
        this.mDisplayType= displayType;
    }

    @Override
    public int getDialogLayoutId() {
        return R.layout.dialog_media_list;
    }

    @Override
    public void setting() {
        TextView tvType = (TextView)findViewById(R.id.tvTitleType);
        if (mMediaType.equals("2")){
            tvType.setText("视频记录");
        }else{
            tvType.setText("音频记录");
        }
        GridView gv = (GridView) findViewById(R.id.involved_people_grid);
        TextView dateTv = (TextView) findViewById(R.id.date_tv);
        Button greenBtn = (Button) findViewById(R.id.sure_btn);
        mFramelayout = (LinearLayout) findViewById(R.id.layout_file);
        dateTv.setText(TextUtils.isEmpty(mTime) ? "暂无":this.mTime);
        if (mMediaRecord != null && mMediaRecord.size() > 0){
            MediaListAdapter mediaListAdapter = new MediaListAdapter(this.mContext, mDisplayType);
            mediaListAdapter.setmList(mMediaRecord);
            gv.setAdapter(mediaListAdapter);
            mediaListAdapter.setmAudioClickListener(new MediaListAdapter.AudioClickListener() {
                @Override
                public void click(String path) {

                    if (mMediaType.equals("1") && mAudioClickListener != null){
                        mAudioClickListener.click(path);
                    }else if (mMediaType.equals("2") && mVideoClickListener != null){
                        mVideoClickListener.click(path);
                    }

                }
            });
        }
        greenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaType.equals("1")){
                    mAudioClickListener.cancel();
                }
                dismiss();
            }
        });

        //PopupWindow的布局文件
        final View view = View.inflate(mContext, R.layout.layout_speaker, null);
        mPop = new PopupWindowFactory(mContext,view);

        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);

    }

    public interface DialogMediaClickListener{

        void click(String path);
        void cancel();
    }

    DialogMediaClickListener mAudioClickListener;
    DialogMediaClickListener mVideoClickListener;

    public void setDialogAudioClickListener(DialogMediaClickListener mAudioClickListener) {
        this.mAudioClickListener = mAudioClickListener;
    }

    public void setDialogVideoClickListener(DialogMediaClickListener mVideoClickListener) {
        this.mVideoClickListener = mVideoClickListener;
    }

    public void startPlay(long length){
        if (mPop != null){
            if (length > 0){
                mTextView.setText(TimeUtils.long2String(length));
            }else{
                mTextView.setVisibility(View.GONE);
            }

            mPop.showAtLocation(mFramelayout, Gravity.CENTER, 0, 0);
        }
    }

    public void finishPlay(){
        if (mPop != null){
            mPop.dismiss();
        }
    }
}


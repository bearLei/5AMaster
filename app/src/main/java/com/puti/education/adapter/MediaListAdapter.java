package com.puti.education.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.ConsultExpert;
import com.puti.education.bean.EventFile;
import com.puti.education.bean.Proof;
import com.puti.education.util.ImgLoadUtil;

/**
 * Created by xjbin on 2017/4/25 0025
 *
 *  专家
 *
 */

public class MediaListAdapter extends ABaseAdapter<Proof>{

    private int mDisplayType;
    public MediaListAdapter(Context context, int displayType) {
        super(context);
        mDisplayType = displayType;
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_media_dialog_layout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent, ViewHolder holder) {

        final RelativeLayout relativeLayout = holder.obtainView(convertView,R.id.audio_relative);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mAudioClickListener != null){
                    if (mDisplayType == 1){
                        mAudioClickListener.click(mList.get(position).url);
                    }else if (mDisplayType == 2){
                        mAudioClickListener.click(mList.get(position).file);
                    }

                }

            }
        });

        return convertView;
    }

    public interface AudioClickListener{
        void click(String path);
    }

    AudioClickListener mAudioClickListener;

    public void setmAudioClickListener(AudioClickListener mAudioClickListener) {
        this.mAudioClickListener = mAudioClickListener;
    }
}

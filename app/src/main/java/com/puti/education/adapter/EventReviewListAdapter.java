package com.puti.education.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.EventEvaluation;
import com.puti.education.bean.EventTrackData;
import com.puti.education.util.Constant;
import com.puti.education.util.ImgLoadUtil;

/**
 * Created by xjbin on 2017/4/25 0025.
 *
 * 事件跟进
 */

public class EventReviewListAdapter extends ABaseAdapter<EventEvaluation> implements SeekBar.OnSeekBarChangeListener{

    private int mType = 1;
    private int BASE_NUMBER = 10;
    private int mTempProgress = 0;

    //type 1家长 2专家
    public EventReviewListAdapter(Context context, int type) {
        super(context);
        mType = type;
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_event_review_layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {

        TextView reviewName = holder.obtainView(convertView,R.id.tv_name);
        ImageView reviewAvatar = holder.obtainView(convertView,R.id.iv_avatar);
        SeekBar seekBar = holder.obtainView(convertView,R.id.seekBar);
        TextView scoreValue = holder.obtainView(convertView,R.id.tv_review_score);

//
//        if (mType == 1){
//            Drawable drawableThumb = context.getResources().getDrawable(R.mipmap.ic_seekbar_thumb_yellow);
//            seekBar.setThumb(drawableThumb);
//            Drawable drawableProgress = context.getResources().getDrawable(R.drawable.seekbar_layer_yellow);
//            seekBar.setProgressDrawable(drawableProgress);
//        }else{
//            Drawable drawableThumb = context.getResources().getDrawable(R.mipmap.ic_seekbar_thumb_green);
//            seekBar.setThumb(drawableThumb);
//            Drawable drawableProgress = context.getResources().getDrawable(R.drawable.seekbar_layer_green);
//            seekBar.setProgressDrawable(drawableProgress);
//        }

        EventEvaluation review = mList.get(position);
        if (review.user != null){
            reviewName.setText(!TextUtils.isEmpty(review.user.name) ? review.user.name:"暂无");
            ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_middle, review.user.avatar, reviewAvatar);
        }else{
            reviewName.setText(!TextUtils.isEmpty(review.name) ? review.name:"暂无");
        }




        int intScore = (int)review.score + BASE_NUMBER;
        Log.d("", "getView index: " + position + ", score: " + intScore);

        scoreValue.setText((int)review.score + "分");
        seekBar.setOnSeekBarChangeListener(null);
        seekBar.setProgress(intScore);
        seekBar.setTag(position);
        seekBar.setOnSeekBarChangeListener(this);


        return convertView;
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mTempProgress = i;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int pos = (int)seekBar.getTag();
        Log.d("", "onProgressChanged: " + mTempProgress);
        mList.get(pos).score = mTempProgress-BASE_NUMBER;
        this.notifyDataSetChanged();

    }

}

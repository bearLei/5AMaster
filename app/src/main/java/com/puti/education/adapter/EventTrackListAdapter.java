package com.puti.education.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.puti.education.R;
import com.puti.education.bean.EventTrackData;
import com.puti.education.bean.Track;
import com.puti.education.util.Constant;

/**
 * Created by xjbin on 2017/4/25 0025.
 *
 * 事件跟进
 */

public class EventTrackListAdapter extends ABaseAdapter<EventTrackData> implements View.OnClickListener{

    public enum CallBackType{
        PICTURE, AUDIO, VIDEO,
    }

    public EventTrackListAdapter(Context context) {
        super(context);
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_event_track_layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {

        EditText desTv = holder.obtainView(convertView,R.id.et_track_desc);
        TextView typeTv = holder.obtainView(convertView,R.id.et_track_name);

        LinearLayout layoutPicText = holder.obtainView(convertView,R.id.text_pic_record_linear);
        LinearLayout layoutAudio = holder.obtainView(convertView,R.id.media_record_linear);
        LinearLayout layoutVideo = holder.obtainView(convertView,R.id.video_record_linear);
        layoutPicText.setOnClickListener(this);
        layoutPicText.setTag(position);
        layoutAudio.setOnClickListener(this);
        layoutAudio.setTag(position);
        layoutVideo.setOnClickListener(this);
        layoutVideo.setTag(position);

        EventTrackData track = mList.get(position);
        desTv.setText(!TextUtils.isEmpty(track.content) ? track.content:"暂无");

        desTv.setFocusable(false);
        desTv.setFocusableInTouchMode(false);

        if (track.personneltype == Constant.ROLE_PARENTS){
            typeTv.setText("家长追踪反馈");
        }else if (track.personneltype == Constant.ROLE_TEACHER){
            typeTv.setText("班主任追踪反馈");
        }else{
            typeTv.setText("追踪反馈");
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.text_pic_record_linear){
            lookCallbackLinstener.look(CallBackType.PICTURE,(int)v.getTag());
        }else if(v.getId() == R.id.media_record_linear){
            lookCallbackLinstener.look(CallBackType.AUDIO,(int)v.getTag());
        }else if (v.getId() == R.id.video_record_linear){
            lookCallbackLinstener.look(CallBackType.VIDEO,(int)v.getTag());
        }

    }

    public interface LookCallbackLinstener{
         void look(CallBackType type,int position);
    }

    LookCallbackLinstener lookCallbackLinstener;

    public void setLookCallbackLinstener(LookCallbackLinstener lookCallbackLinstener) {
        this.lookCallbackLinstener = lookCallbackLinstener;
    }
}

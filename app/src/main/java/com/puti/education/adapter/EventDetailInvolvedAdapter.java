package com.puti.education.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.puti.education.R;
import com.puti.education.bean.EVENT_TYPE;
import com.puti.education.bean.EventDeailInvolvedPeople;
import com.puti.education.bean.EventDetailBean;
import com.puti.education.ui.uiTeacher.ProofListActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.widget.RadarView;



/**
 * Created by xjbin on 2017/4/25 0025.
 *
 * 事件详情涉事人 adapter
 */

public class EventDetailInvolvedAdapter extends BasicRecylerAdapter<EventDeailInvolvedPeople> implements View.OnClickListener{

    private EventDeailInvolvedPeople people= null;
    private EVENT_TYPE event_type;

    private int mRoleType;//角色选择

    public void setmRoleType(int mRoleType) {
        this.mRoleType = mRoleType;
    }

    public void setEvent_type(EVENT_TYPE event_type) {
        this.event_type = event_type;
    }

    public EventDetailInvolvedAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return com.puti.education.R.layout.event_detail_involved_people_layout;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CommonViewHolder cHolder = (CommonViewHolder) holder;
        ImageView headImg = cHolder.obtainView(R.id.detail_head_img);
        TextView nameTv = cHolder.obtainView(R.id.detail_name_tv);
        TextView classTv = cHolder.obtainView(R.id.detail_class_tv);
        TextView responsebleTypeTv = cHolder.obtainView(R.id.detail_responseble_type_tv);
        RadarView radarView = cHolder.obtainView(R.id.involved_people_ratingbar);
        LinearLayout zuozhengLienar = cHolder.obtainView(R.id.detail_zuozheng_container);

        if (this.event_type == EVENT_TYPE.NORMAL){
            zuozhengLienar.setVisibility(View.GONE);
        }else if (this.event_type == EVENT_TYPE.ABNORMAL){
            zuozhengLienar.setVisibility(View.VISIBLE);
        }

        people = mList.get(position);
//        ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default,people.avatar,headImg);
//        nameTv.setText(people.name);
//        classTv.setText(people.className);
//        if (people.dutyRank!= null && people.dutyRank.equals("主要责任人")){
//            responsebleTypeTv.setBackgroundResource(R.drawable.event_detail_red_circular_bead_bg);
//        }else{
//            responsebleTypeTv.setBackgroundResource(R.drawable.event_detail_yellow_circular_head_bg);
//        }
//        responsebleTypeTv.setText(people.dutyRank);
//        zuozhengLienar.setOnClickListener(this);
//
//        if (mRoleType == Constant.ROLE_STUDENT){
//            radarView.setVisibility(View.GONE);
//        }else{
//            radarView.setVisibility(View.VISIBLE);
//
//            if (people.actionData != null && people.actionData.size() > 0){
//
//                String[] textArray = new String[6];
//                double[] doublesArray = new double[6];
//
//                for (int i = 0;i< 6;i++){
//                    textArray[i] = people.actionData.get(i).name;
//                    doublesArray[i] = people.actionData.get(i).value;
//                }
//                radarView.setTextContent(textArray);
//                radarView.setValueContent(doublesArray);
//                radarView.setCenterValue("10");
//                //radarView.setmRadiusPercent(0.7f);
//                radarView.startDraw();
//            }
//
//        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.detail_zuozheng_container){
            Intent intent = new Intent(mContext,ProofListActivity.class);
            mContext.startActivity(intent);
        }

    }
}

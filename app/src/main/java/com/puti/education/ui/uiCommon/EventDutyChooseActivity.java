package com.puti.education.ui.uiCommon;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.bean.Duty;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.EventType;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiTeacher.AddEventZxingActivity;
import com.puti.education.ui.uiTeacher.ChoosePersonListActivity;
import com.puti.education.ui.uiTeacher.TeacherAddEventActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.Key;
import com.puti.education.util.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;

public class EventDutyChooseActivity extends BaseActivity {

    public static final int REFER_TEACHER_ADD = 0;//来自老师添加事件页面
    public static final int REFER_ZXING = 3;//来自二维码扫描

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.tv_title)
    TextView mTvEventBack;
    @BindView(R.id.recyclerview)
    RecyclerView mRecycleView;

    private int mType = 0;
    private boolean mAbnormal;
    private Context mContext;
    private ListAdapter mListAdapter;
    private List<EventType> mTopList = new ArrayList<>();
    private Stack<EventType> mSk = new Stack<EventType>();

    private ArrayList<EventAboutPeople> mInvolvePeopleList = null;

    private List<Duty.DutyDetail> mDutyList = new ArrayList<>();

    private int mEventTyPeId = -1;
    private String mEventTypeName;
    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_event_duty_choose;
    }

    @Override
    public void initVariables() {
        mContext = this;
        mType = this.getIntent().getIntExtra("type", 0);

        if (mType == REFER_TEACHER_ADD) {
            mAbnormal = this.getIntent().getBooleanExtra(Key.EVENT_ABNORMOL, false);
        }else if (mType == REFER_ZXING){
            mAbnormal = this.getIntent().getBooleanExtra("isabnormal", false);
            mEventTyPeId = this.getIntent().getIntExtra("eventtypeid", -1);
            mEventTypeName=this.getIntent().getStringExtra("eventtypename");
           if (mInvolvePeopleList == null){
               mInvolvePeopleList = new ArrayList<>();
           }
            mInvolvePeopleList.addAll((ArrayList<EventAboutPeople>)getIntent().getSerializableExtra(AddEventZxingActivity.ZXING_LIST));
        }
    }

    @Override
    public void initViews() {
        mTitleTv.setText("责任等级");

        mListAdapter = new ListAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mListAdapter);
        mListAdapter.setDataList(mTopList);

        mTvEventBack.setClickable(false);
        mTvEventBack.setText("请选择责任类型:");

    }

    @Override
    public void loadData() {
        getDutyList();
    }


    public class ListAdapter extends BasicRecylerAdapter {

        public ListAdapter(Context myContext){
            super(myContext);
        }

        @Override
        public int getLayoutId() {
            return R.layout.common_dropview_item_layout;
        }

        public void refershData(List<Duty.DutyDetail> lists){
            setDataList(lists);
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            Duty.DutyDetail eventEntity = (Duty.DutyDetail)mList.get(position);

            CommonViewHolder viewHolder = (CommonViewHolder) holder;
            TextView textView = (TextView) viewHolder.obtainView(R.id.item_name_tv);
            textView.setText(eventEntity.value);

            viewHolder.itemView.setTag(R.id.onclick_position, position);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posi = (int)v.getTag(R.id.onclick_position);
                    Duty.DutyDetail entity = (Duty.DutyDetail)mList.get(posi);
                    newEvet(entity);


                }
            });
        }
    }

    private void newEvet(Duty.DutyDetail et){
        if (et != null) {
            Intent intent = new Intent();

          if (mType == REFER_ZXING) {
                intent.putExtra("eventtypeid", mEventTyPeId);
                intent.putExtra("eventtypename", mEventTypeName);
                intent.putExtra("isabnormal", mAbnormal);
                opearteLDutyist(et.key);
                if (mInvolvePeopleList != null && mInvolvePeopleList.size() > 0) {
                    intent.putExtra(AddEventZxingActivity.ZXING_LIST, (Serializable) mInvolvePeopleList);
                }
                intent.setClass(this, TeacherAddEventActivity.class);
            }  else  {
                intent.putExtra(Key.EVENT_ABNORMOL, mAbnormal);
                intent.putExtra(Key.DUTY_TYPE, et.key);
                if (et.key.equals(Constant.EVENT_DUTY_MAJOR) || et.key.equals(Constant.EVENT_DUTY_MINOR) ||
                        et.key.equals(Constant.EVENT_DUTY_REPORT)) {
                    intent.putExtra(Key.CHOOSE_STUDENT, 1);
                } else {
                    intent.putExtra(Key.CHOOSE_BOTH, 1);
                }

                intent.putExtra(Key.NUMBER_TO_NEED, Constant.CHOOSE_PEOPLE_MAX);
                intent.putExtra(Key.NUMBER_IS_CHOOSED, 0);
                intent.putExtra(Key.BEAN, mInvolvePeopleList);
                intent.setClass(this, ChoosePersonListActivity.class);
            }
                startActivity(intent);
            this.finish();
        }
    }

    private void opearteLDutyist(String duty){
        if (mInvolvePeopleList != null && mInvolvePeopleList.size() > 0){
            for (int i = 0; i < mInvolvePeopleList.size(); i++) {
                EventAboutPeople eventAboutPeople = mInvolvePeopleList.get(i);
                eventAboutPeople.dutyType = duty;
            }
        }
    }
    //责任等级
    private void getDutyList(){

        disLoading("数据初始化中...");

        TeacherModel.getInstance().getDutyWarnList(new BaseListener(Duty.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();
                if (infoObj != null){
                    Duty duty = (Duty) infoObj;
                    if (mType == 0 || mType == REFER_ZXING){
                        mDutyList.addAll(duty.options);
                    }else{
                        if (duty.options.size() > 0){
                            for (Duty.DutyDetail one: duty.options){
                                if (one.key.equals(Constant.EVENT_DUTY_MAJOR) ||
                                        one.key.equals(Constant.EVENT_DUTY_MINOR)){
                                    mDutyList.add(one);
                                }
                            }
                        }
                    }

                    mListAdapter.setDataList(mDutyList);
                }else{
                    ToastUtil.show("责任等级获取失败！");
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(TextUtils.isEmpty(errorMessage) ? Constant.REQUEST_FAILED_STR : errorMessage);
            }
        });
    }
}

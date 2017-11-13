package com.puti.education.ui.fragment;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.puti.education.R;
import com.puti.education.adapter.ChildBehaviorPageAdapter;
import com.puti.education.bean.ChildInfo;
import com.puti.education.bean.ChildRelativeDes;
import com.puti.education.bean.NewNotice;
import com.puti.education.bean.ParentHomeData;
import com.puti.education.bean.PushData;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.PatriarchModel;
import com.puti.education.ui.BaseFragment;
import com.puti.education.ui.uiCommon.QuestionnaireDetailActivity;
import com.puti.education.ui.uiCommon.WebViewActivity;
import com.puti.education.ui.uiPatriarch.ActionEventAddActivity;
import com.puti.education.ui.uiPatriarch.GrowthTrackDetailActivity;
import com.puti.education.ui.uiPatriarch.PatriarchMainActivity;
import com.puti.education.ui.uiStudent.ActionEventActivity;
import com.puti.education.ui.uiTeacher.TeacherEventDetailActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.Key;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.AppSwipeRefreshLayout;
import com.puti.education.widget.RadarView;
import com.puti.education.widget.RelationDesBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xjbin on 2017/5/22 0022.
 *
 * 家长首页
 */

public class ParentHomeFragment extends BaseFragment{

    @BindView(R.id.tv_notice_title)
    TextView mNoticeTitleView;
    @BindView(R.id.tv_notice_time)
    TextView mNoticeTimeTv;
    @BindView(R.id.parent_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.dot_frame)
    LinearLayout mDotFrame;
    @BindView(R.id.relation_des_view)
    RelationDesBarView mRelationBarView;
    @BindView(R.id.newnotice_rel)
    RelativeLayout mLatestNotice;
    @BindView(R.id.refresh_layout)
    AppSwipeRefreshLayout mSwipeRefreshLayout;

    private ImageView emptyImg;

    private int lastSelectIndex = 0;
    private NewNotice mNewNotice;

    ViewStub emptyViewStup=null;

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_parenthome;
    }

    @Override
    public void initVariables() {

    }

    @OnClick(R.id.frame_img)
    public void newActionEvent(){
        Intent inten = new Intent();
        inten.setClass(this.getActivity(), ActionEventAddActivity.class);
        startActivity(inten);
    }

    @Override
    public void initViews(View view) {
        mLatestNotice.setVisibility(View.GONE);
        emptyViewStup = (ViewStub) view.findViewById(R.id.empty_viewstup);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipe_refresh_progress));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //getNewNotice();
                getParentHomeData();
            }
        });
    }

    @Override
    public void loadData() {
        getNewNotice();
        getParentHomeData();
    }


    private void showViewPagerChildInfo(List<ChildInfo> childInfos){

        if (childInfos == null || childInfos.size() == 0){
            emptyViewStup.inflate();
            return;
        }

        List<View> views = new ArrayList<>();
        ChildInfo childInfo;
        for (int i =0;i<childInfos.size();i++){
            childInfo = childInfos.get(i);
            views.add(showItemChilInfo(childInfo));
        }
        ChildBehaviorPageAdapter childBehaviorPageAdapter = new ChildBehaviorPageAdapter(views);
        mViewPager.setAdapter(childBehaviorPageAdapter);
        List<ImageView> dotViews = dotViews(childInfos);
        if (dotViews == null){
            mDotFrame.setVisibility(View.GONE);
        }else{
            mViewPager.setOnPageChangeListener(new ChildInfoPagerListener(dotViews));
        }
    }

    private View showItemChilInfo(ChildInfo childInfo){

        View childView = LayoutInflater.from(getActivity()).inflate(R.layout.item_parenthome_viewpager_item_layout,null);
        TextView chilNameTv = (TextView) childView.findViewById(R.id.child_name_tv);
        ImageView childRecordImg = (ImageView) childView.findViewById(R.id.child_record_img);
        RadarView radarView = (RadarView) childView.findViewById(R.id.radarview);

        TextView unRegulationTv = (TextView) childView.findViewById(R.id.tv_unregulation_count);//违规
        TextView offenceCountTv = (TextView) childView.findViewById(R.id.tv_offence_count);//违纪
        TextView honourCountTv = (TextView) childView.findViewById(R.id.tv_honour_count);//表彰次数
        TextView exceptionCountTv = (TextView) childView.findViewById(R.id.tv_exception_value);//表异常行为
        TextView unDailyTv = (TextView) childView.findViewById(R.id.tv_daily_value);//日常事件数量

        String titles[] = new String[6];
        double values[] = new double[6];

        titles[0] = "学生习惯";
        values[0] = childInfo.student.academicRecordIndex/100.0;
        titles[1] = "性格特征";
        values[1] = childInfo.student.characterIndex/100.0;
        titles[2] = "学校氛围";
        values[2] = childInfo.student.schoolAtmosphereIndex/100.0;
        titles[3] = "社交能力";
        values[3] = childInfo.student.socialSkillsIndex/100.0;
        titles[4] = "行为能力";
        values[4] = childInfo.student.behaviorAbilityIndex/100.0;
        titles[5] = "同伴影响";
        values[5] = childInfo.student.peerInfluenceIndex/100.0;

        radarView.setTextContent(titles);
        radarView.setValueContent(values);
        radarView.setCenterValue(0 + "%");
        radarView.setmRadiusPercent(0.5f);
        radarView.startDraw();

        chilNameTv.setText(childInfo.student.name);
        unRegulationTv.setText(childInfo.student.violateRegulations + "");
        offenceCountTv.setText(childInfo.student.violateDiscipline+"");
        honourCountTv.setText(childInfo.student.commend+"");
        exceptionCountTv.setText(childInfo.student.anomaly+"");
        unDailyTv.setText(childInfo.student.dailyBehavior + "");

        childRecordImg.setTag(R.id.detail_id, childInfo.student.uid);
        childRecordImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String detailid = (String)v.getTag(R.id.detail_id);
                eventRecords(detailid);
            }
        });

        return childView;
    }


    public void eventRecords(String id){
        Intent intent = new Intent();
        intent.putExtra("id", id);
        intent.setClass(this.getActivity(), ActionEventActivity.class);
        startActivity(intent);
    }

    private List<ImageView> dotViews(List<ChildInfo> list){

        if (list.size() == 1){
            return  null;
        }

        mDotFrame.removeAllViews();
        List<ImageView> dotViews = new ArrayList<>();
        for (int i =0; i< list.size();i++){
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(R.drawable.dot_unselect_style);
            if (i == 0){
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DisPlayUtil.dip2px(getActivity(),10),
                        DisPlayUtil.dip2px(getActivity(),7));
                imageView.setLayoutParams(layoutParams);
            }else{
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DisPlayUtil.dip2px(getActivity(),10),
                        DisPlayUtil.dip2px(getActivity(),7));
                layoutParams.leftMargin = DisPlayUtil.dip2px(getActivity(),20);
                imageView.setLayoutParams(layoutParams);
            }

            dotViews.add(imageView);
            mDotFrame.addView(imageView);
        }
        ImageView firstImg = dotViews.get(0);
        firstImg.setImageResource(R.drawable.dot_select_style);

        return dotViews;
    }

    private class ChildInfoPagerListener implements ViewPager.OnPageChangeListener{

        private List<ImageView> dotViews = null;

        public ChildInfoPagerListener(List<ImageView> dotViews) {
            this.dotViews = dotViews;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            ImageView mLastImg =  this.dotViews.get(position);
            ImageView mImg = this.dotViews.get(lastSelectIndex);
            mImg.setImageResource(R.drawable.dot_unselect_style);
            mLastImg.setImageResource(R.drawable.dot_unselect_style);
            lastSelectIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void getParentHomeData(){


        PatriarchModel.getInstance().getParentHomeData(new BaseListener(ParentHomeData.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);

                if (infoObj != null){

                    ParentHomeData parentHomeData = (ParentHomeData) infoObj;

                    List<ChildInfo> listChildInfo = parentHomeData.childrenList;
                    List<ChildRelativeDes> relativeDesList = parentHomeData.relativeDesc;

                    //行为
                    showViewPagerChildInfo(listChildInfo);

                    //关系
                    mRelationBarView.setInitData(relativeDesList);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                mSwipeRefreshLayout.setRefreshing(false);
                ToastUtil.show(TextUtils.isEmpty(errorMessage) ? Constant.REQUEST_FAILED_STR:errorMessage);
            }

        });

    }


    //查看系统通知消息
    @OnClick(R.id.newnotice_rel)
    public void lookNetNoticeClick(){
        if (mNewNotice == null || mNewNotice.extContent == null){
            return;
        }
        switch (mNewNotice.extContent.subType){
            case PushData.TARGET_QUESTIONNAIRE: //在线调查
                Intent intent = new Intent();
                intent.putExtra("id", mNewNotice.extContent.value);
                intent.setClass(this.getActivity(), QuestionnaireDetailActivity.class);
                startActivity(intent);
                break;
            case PushData.TARGET_MUTUAL_REVIEW: //互评
                PatriarchMainActivity tMainAy = (PatriarchMainActivity)this.getActivity();
                tMainAy.gotoReview();
                break;
            case PushData.TARGET_EVENT_UNTRACK:
            case PushData.TARGET_EVENT_PUSH_PARENT:
                openEventDetail(mNewNotice.extContent.value, mNewNotice.extContent.valueExt);
                break;
            case PushData.TARGET_SYS_MESSAGE://通知消息
                Intent intent3 = new Intent();
                intent3.putExtra("type", 1);
                intent3.putExtra("msg_id", mNewNotice.uid);
                intent3.setClass(this.getActivity(), WebViewActivity.class);
                startActivity(intent3);
                break;
            default:
                Intent intent5 = new Intent();
                intent5.putExtra("type", 2);
                intent5.putExtra("msg_title", mNewNotice.alertMsg);
                intent5.putExtra("content", mNewNotice.alertMsg);
                intent5.setClass(this.getActivity(), WebViewActivity.class);
                startActivity(intent5);
                break;
        }

    }

    private void openEventDetail(String eventId, String peopleuid)
    {
        Intent intent4 = new Intent();
        intent4.putExtra("type", 4);
        intent4.putExtra(Key.EVENT_KEY, eventId);
        intent4.putExtra(Key.KEY_PEOPLE_UID, peopleuid);
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        intent4.setClass(this.getContext(), TeacherEventDetailActivity.class);
        this.getContext().startActivity(intent4);
    }


    private void openGrowthTrack(int id){
        Intent intent4 = new Intent();
        intent4.putExtra("id", id + "");
        intent4.setClass(this.getActivity(), GrowthTrackDetailActivity.class);
        startActivity(intent4);
    }

    private void getNewNotice(){
        CommonModel.getInstance().getLatestNotice(new BaseListener(NewNotice.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                NewNotice tempObj = (NewNotice)infoObj;
                if (tempObj != null && tempObj.extContent != null){
                    mLatestNotice.setVisibility(View.VISIBLE);
                    mNewNotice = tempObj;
                    mNoticeTitleView.setText(mNewNotice.alertMsg);
                    mNoticeTimeTv.setText(mNewNotice.bizTime);
                }else{
                    mLatestNotice.setVisibility(View.GONE);
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                mLatestNotice.setVisibility(View.GONE);
                ToastUtil.show("获取最新消息出错 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }
}

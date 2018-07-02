package unit.moudle.home.holder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;
import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.ToastUtil;
import com.puti.education.util.ViewUtils;

import unit.debug.DebugActivity;
import unit.moudle.classevent.PutiClassEventActivity;
import unit.moudle.eventdeal.EventListActivity;
import unit.moudle.eventregist.PutiChooseEventActivity;
import unit.moudle.ques.PutiQuesActivity;
import unit.moudle.record.PutiChooseStuRecordAcitivity;
import unit.moudle.record.PutiTeacherRecordActivity;
import unit.moudle.reports.PutiParReportsActivity;
import unit.moudle.work.PutiWorkCheckActivity;
import unit.sp.DataStorage;

/**
 * Created by lei on 2018/6/6.
 * 首页能力栏holder
 */

public class HomePowerHolder extends BaseHolder<Object>{

    private FlexboxLayout mParentView;

    private HomeBaseItemHolder mEventSureHolder;//事件确认
    private HomeBaseItemHolder mReportHolder;//举报
    private HomeBaseItemHolder mQuesHolder;//问卷
    public HomePowerHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_home_power_holder);
        mParentView = (FlexboxLayout) mRootView.findViewById(R.id.root_view);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Object data) {
        mParentView.removeAllViews();
        //事件登记
        mParentView.addView(getItem(
                R.drawable.puti_home_add_event,
                R.string.puti_home_add_event,
                new HomeBaseItemHolder.ItemClickListener() {
            @Override
            public void itemClick() {
               jump(PutiChooseEventActivity.class);
            }
        }));
        //事件确认
        mEventSureHolder = new HomeBaseItemHolder(mContext, new HomeBaseItemHolder.ItemClickListener() {
            @Override
            public void itemClick() {
                jump(EventListActivity.class);
            }
        });
        mParentView.addView(getItem(mEventSureHolder,
                R.drawable.puti_home_event_sure,
                R.string.puti_home_event_sure));

        //班级事件
        mParentView.addView(getItem(
                R.drawable.puti_home_event_manager,
                R.string.puti_home_event_manager,
                new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                      jump(PutiClassEventActivity.class);
                    }
                }));


        //家长举报
        mReportHolder = new HomeBaseItemHolder(mContext, new HomeBaseItemHolder.ItemClickListener() {
            @Override
            public void itemClick() {
                setmReportHolderRedDog(false);
                DataStorage.putUserHasReport(false);
                jump(PutiParReportsActivity.class);
            }
        });
        setmReportHolderRedDog(DataStorage.getUserHasReport());
        mParentView.addView(getItem(mReportHolder,
                R.drawable.puti_home_parent_report,
                R.string.puti_home_parent_report));


        //学生档案
        mParentView.addView(getItem(
                R.drawable.puti_home_stu_record,
                R.string.puti_home_stu_record,
                new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                     jump(PutiChooseStuRecordAcitivity.class);
                    }
                }));
        //我的档案
        mParentView.addView(getItem(
                R.drawable.puti_home_my_record,
                R.string.puti_home_my_record,
                new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                       jump(PutiTeacherRecordActivity.class);
                    }
                }));
        //我的问卷
        mQuesHolder = new HomeBaseItemHolder(mContext, new HomeBaseItemHolder.ItemClickListener() {
            @Override
            public void itemClick() {
                setmQuesHolderRedDog(false);
                DataStorage.putUserQues(false);
                jump(PutiQuesActivity.class);
            }
        });
        mQuesHolder.showRedDog(DataStorage.getUserHasQues());
        mParentView.addView(getItem(mQuesHolder,
                R.drawable.puti_home_my_psq,
                R.string.puti_home_my_question));

        //工作检查
        mParentView.addView(getItem(
                R.drawable.puti_home_work_check,
                R.string.puti_home_work_check,
                new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                        jump(PutiWorkCheckActivity.class);
                    }
                }));
    }

    private View getItem(int iconRes, int titleRes, HomeBaseItemHolder.ItemClickListener listener){
        HomeBaseItemHolder holder = new HomeBaseItemHolder(mContext,listener);
        holder.setUI(iconRes,titleRes);
        View rootView = holder.getRootView();
        FlexboxLayout.LayoutParams params= new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = ViewUtils.getScreenWid(mContext)/4;
        params.height = ViewUtils.dip2px(mContext,100);
        rootView.setLayoutParams(params);
        return holder.getRootView();
    }

    private View getItem(HomeBaseItemHolder holder,int iconRes, int titleRes){
        holder.setUI(iconRes,titleRes);
        View rootView = holder.getRootView();
        FlexboxLayout.LayoutParams params= new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = ViewUtils.getScreenWid(mContext)/4;
        params.height = ViewUtils.dip2px(mContext,100);
        rootView.setLayoutParams(params);
        return holder.getRootView();
    }
    private void jump(Class t){
        Intent intent = new Intent(mContext,t);
        mContext.startActivity(intent);
    }

    //控制问卷红点
    public void setmQuesHolderRedDog(boolean show){
        mQuesHolder.showRedDog(show);
    }

    //控件举报红点
    public void setmReportHolderRedDog(boolean show){
        mReportHolder.showRedDog(show);
    }

}

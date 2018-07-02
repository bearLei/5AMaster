package unit.moudle.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.ViewUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import unit.entity.PushInfo;
import unit.eventbus.AvatarChangeEvent;
import unit.eventbus.PutiEventBus;
import unit.eventbus.PutiMsgNotice;
import unit.eventbus.TokenErrorEvent;
import unit.moudle.home.holder.HomeCountHolder;
import unit.moudle.home.holder.HomeFeedBackHolder;
import unit.moudle.home.holder.HomeHeadHolder;
import unit.moudle.home.holder.HomePowerHolder;
import unit.moudle.home.holder.HomeToolHolder;
import unit.sp.DataStorage;

/**
 * Created by lei on 2018/6/5.
 * 首页 Ptr
 */

public class HomePtr implements BaseMvpPtr {

    private static final int Msg_event = 1;//事件通知
    private static final int Msg_Report = 2;//家长举报
    private static final int Msg_Ques = 3;//问卷

    private Context mContext;
    private HomeView mView;

    private HomeHeadHolder mHeadHolder;//顶部个人信息
    private HomeCountHolder mCountHolder;//个人统计信息
    private HomePowerHolder mPowerHolder;//能力栏
    private HomeFeedBackHolder mFeedBackHolder;//有奖反馈
    private HomeToolHolder mToolHolder;//工具栏
    public HomePtr(Context mContext, HomeView mView) {
        this.mContext = mContext;
        this.mView = mView;
        if (!PutiEventBus.g().isRegistered(this)){
            PutiEventBus.g().register(this);
        }
    }

    @Override
    public void star() {
        initHeadHolder();
        initCountHolder();
        initPowerHolder();
        initFeedBackHolder();
        initToolHolder();
    }

    @Override
    public void stop() {

    }
    //初始化头部个人信息Holder
    private void initHeadHolder(){
        if (mHeadHolder == null){
            mHeadHolder = new HomeHeadHolder(mContext);
            mHeadHolder.setData(true);
        }
        View rootView = mHeadHolder.getRootView();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewUtils.dip2px(mContext,48));
        rootView.setLayoutParams(params);
        mView.addHeadLayout(rootView);
    }

    //初始化统计信息Holder
    private void initCountHolder(){
        if (mCountHolder == null){
            mCountHolder = new HomeCountHolder(mContext);
            mCountHolder.queryData();
        }
        mView.addCountLayout(operateSize(mCountHolder));
    }

    //初始化能力栏Holder
    private void initPowerHolder(){
        if (mPowerHolder == null){
            mPowerHolder = new HomePowerHolder(mContext);
            mPowerHolder.setData(true);
        }
        mView.addPowerLayout(operateSize(mPowerHolder));
    }

    //初始化有奖反馈Holder
    private void initFeedBackHolder(){
        if (mFeedBackHolder == null){
            mFeedBackHolder = new HomeFeedBackHolder(mContext);
            mFeedBackHolder.setData(true);
        }

        mView.addFeedBackLayout(operateSize(mFeedBackHolder));
    }

    //初始化工具栏
    private void initToolHolder(){
        if (mToolHolder == null){
            mToolHolder = new HomeToolHolder(mContext);
            mToolHolder.setData(true);
        }
        mView.addToolLayout(operateSize(mToolHolder));
    }

    private View operateSize(BaseHolder holder){
        View rootView = holder.getRootView();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(params);
        return rootView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on3EventMainThread(AvatarChangeEvent event) {
        if (mHeadHolder != null){
            mHeadHolder.updateAvatar();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on3EventMainThread(PutiMsgNotice event) {
        int category = event.getCategory();
        switch (category){
            case Msg_event:
                mHeadHolder.setRedDog(true);
                DataStorage.putUserHasNotice(true);
                break;
            case Msg_Ques:
                mPowerHolder.setmQuesHolderRedDog(true);
                DataStorage.putUserQues(true);
                break;
            case Msg_Report:
                mPowerHolder.setmReportHolderRedDog(true);
                DataStorage.putUserHasReport(true);
                break;
        }
    }

}

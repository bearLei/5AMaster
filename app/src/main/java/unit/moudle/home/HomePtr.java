package unit.moudle.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.ViewUtils;

import unit.moudle.home.holder.HomeCountHolder;
import unit.moudle.home.holder.HomeFeedBackHolder;
import unit.moudle.home.holder.HomeHeadHolder;
import unit.moudle.home.holder.HomePowerHolder;
import unit.moudle.home.holder.HomeToolHolder;

/**
 * Created by lei on 2018/6/5.
 * 首页 Ptr
 */

public class HomePtr implements BaseMvpPtr {

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
}

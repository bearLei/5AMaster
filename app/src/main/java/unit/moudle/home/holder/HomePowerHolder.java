package unit.moudle.home.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;
import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.ToastUtil;
import com.puti.education.util.ViewUtils;

/**
 * Created by lei on 2018/6/6.
 * 首页能力栏holder
 */

public class HomePowerHolder extends BaseHolder<Object>{

    private FlexboxLayout mParentView;

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
                ToastUtil.show("事件登记");
            }
        }));
        //事件确认
        mParentView.addView(getItem(
                R.drawable.puti_home_event_sure,
                R.string.puti_home_event_sure,
                new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                        ToastUtil.show("事件确认");
                    }
                }));
        //班级事件
        mParentView.addView(getItem(
                R.drawable.puti_home_event_manager,
                R.string.puti_home_event_manager,
                new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                        ToastUtil.show("班级事件");
                    }
                }));
        //家长举报
        mParentView.addView(getItem(
                R.drawable.puti_home_parent_report,
                R.string.puti_home_parent_report,
                new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                        ToastUtil.show("家长举报");
                    }
                }));
        //学生档案
        mParentView.addView(getItem(
                R.drawable.puti_home_stu_record,
                R.string.puti_home_stu_record,
                new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                        ToastUtil.show("学生档案");
                    }
                }));
        //我的档案
        mParentView.addView(getItem(
                R.drawable.puti_home_my_record,
                R.string.puti_home_my_record,
                new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                        ToastUtil.show("我的档案");
                    }
                }));
        //我的问卷
        mParentView.addView(getItem(
                R.drawable.puti_home_my_psq,
                R.string.puti_home_my_question,
                new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                        ToastUtil.show("我的问卷");
                    }
                }));
        //工作检查
        mParentView.addView(getItem(
                R.drawable.puti_home_work_check,
                R.string.puti_home_work_check,
                new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                        ToastUtil.show("工作检查");
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

}

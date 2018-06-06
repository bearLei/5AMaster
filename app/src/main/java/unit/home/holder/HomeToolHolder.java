package unit.home.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.flexbox.FlexboxLayout;
import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.ToastUtil;
import com.puti.education.util.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lei on 2018/6/6.
 * 首页工具栏holder
 */

public class HomeToolHolder extends BaseHolder<Object> {

    @BindView(R.id.feed_back)
    RelativeLayout feedBack;
    @BindView(R.id.tool_container)
    FlexboxLayout VToolContainer;

    public HomeToolHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_home_tool_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Object data) {
        VToolContainer.removeAllViews();
        //校园通讯录
        VToolContainer.addView(getItem(
                R.drawable.puti_login_account_selected,
                R.string.navi_survey_online, new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                        ToastUtil.show("校园通讯录");
                    }
                }));

        //家长通讯录
        VToolContainer.addView(getItem(
                R.drawable.puti_login_account_selected,
                R.string.navi_survey_online, new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                        ToastUtil.show("家长通讯录");
                    }
                }));

        //班级课表
        VToolContainer.addView(getItem(
                R.drawable.puti_login_account_selected,
                R.string.navi_survey_online, new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                        ToastUtil.show("班级课表");
                    }
                }));

        //我的课表
        VToolContainer.addView(getItem(
                R.drawable.puti_login_account_selected,
                R.string.navi_survey_online, new HomeBaseItemHolder.ItemClickListener() {
                    @Override
                    public void itemClick() {
                        ToastUtil.show("我的课表");
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

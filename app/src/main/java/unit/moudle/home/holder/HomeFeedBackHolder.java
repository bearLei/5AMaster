package unit.moudle.home.holder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.moudle.personal.feedback.FeedBackActivity;

/**
 * Created by lei on 2018/6/6.
 * 首页反馈有奖holder
 */

public class HomeFeedBackHolder extends BaseHolder<Object> {
    @BindView(R.id.feed_back)
    RelativeLayout feedBack;
    @BindView(R.id.cover)
    ImageView cover;

    public HomeFeedBackHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_home_feedback_holder);
        ButterKnife.bind(this, mRootView);
        feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FeedBackActivity.class);
                mContext.startActivity(intent);
            }
        });
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Object data) {
        // TODO: 2018/6/6 加载背景图
    }
}

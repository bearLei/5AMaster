package unit.moudle.ques;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.QuesInfo;

/**
 * Created by lei on 2018/6/29.
 */

public class QuesHolder extends BaseHolder<QuesInfo> {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.status)
    TextView status;

    public QuesHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_ques_holder);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewUtils.dip2px(mContext,60));
        mRootView.setLayoutParams(params);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, final QuesInfo data) {
        if (data == null){
            return;
        }
        switch (data.getStatus()){
            case 0:
                //未完成
                status.setText("待完成");
                title.setTextColor(mContext.getResources().getColor(R.color.base_39BCA1));
                status.setTextColor(mContext.getResources().getColor(R.color.base_39BCA1));
                break;
            case 1:
                //已过期
                status.setText("已过期");
                title.setTextColor(mContext.getResources().getColor(R.color.base_333333));
                status.setTextColor(mContext.getResources().getColor(R.color.base_cdcdcd));
                break;
            case 2:
                //已完成
                status.setText("已完成");
                title.setTextColor(mContext.getResources().getColor(R.color.base_333333));
                status.setTextColor(mContext.getResources().getColor(R.color.base_cdcdcd));
                break;
        }

        title.setText(data.getSurveyName());


        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getStatus() == 0) {
                    Intent intent = new Intent(mContext, PutiQuesDetailActivity.class);
                    intent.putExtra(PutiQuesDetailActivity.Parse_Intent, data);
                    mContext.startActivity(intent);
                }
            }
        });
    }
}

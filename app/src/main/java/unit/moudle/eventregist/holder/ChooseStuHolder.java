package unit.moudle.eventregist.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lei on 2018/6/11.
 * 事件登记详情-选择学生holder
 */

public class ChooseStuHolder extends BaseHolder<Object> {
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.scan)
    ImageView scan;

    public ChooseStuHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_event_detail_stu_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Object data) {

    }
}

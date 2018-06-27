package unit.moudle.eventdeal.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.entity.Event2Involved;

/**
 * Created by lei on 2018/6/25.
 * 事件确认详情-通知holder
 */

public class DealEventDetailNotifyHolder extends BaseHolder<Event2Involved> {

    @BindView(R.id.student_office)
    TextView studentOffice;
    @BindView(R.id.parent)
    TextView parent;
    @BindView(R.id.psy_teacher)
    TextView psyTeacher;

    private boolean mSelectedStudentOffice;
    private boolean mSelectedParent;
    private boolean mSelectedPsy;

    public DealEventDetailNotifyHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_deal_event_notify_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Event2Involved data) {
        if (data == null){
            return;
        }
        studentOffice.setSelected(data.isDefaultNeedVaied() ? true : false);
        parent.setSelected(data.isDefaultNeedParent() ? true : false);
        psyTeacher.setSelected(data.isDefaultPsy() ? true : false);
    }

    @OnClick({R.id.student_office, R.id.parent, R.id.psy_teacher})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.student_office:
                mSelectedStudentOffice = !mSelectedStudentOffice;
               oprateDrawble(studentOffice,mSelectedStudentOffice);
                break;
            case R.id.parent:
                mSelectedParent = !mSelectedParent;
               oprateDrawble(parent,mSelectedParent);
                break;
            case R.id.psy_teacher:
                mSelectedPsy = !mSelectedPsy;
               oprateDrawble(psyTeacher,mSelectedPsy);
                break;
        }
    }

    private void oprateDrawble(TextView view,boolean selected){
        Drawable drawable;
        if (selected){
            drawable = mContext.getResources().getDrawable(R.drawable.puti_check_39bca1);
        }else {
            drawable = mContext.getResources().getDrawable(R.drawable.puti_check_cdcdcd);
        }
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable,null,null,null);
    }

    public boolean needValid(){
        return mSelectedStudentOffice;
    }

    public boolean needParentNotice(){
        return mSelectedParent;
    }
    public boolean needPsy(){
        return mSelectedPsy;
    }
}

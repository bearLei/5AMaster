package unit.moudle.record;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.moudle.record.ptr.PutiTeacherPtr;
import unit.moudle.record.view.PutiTeacherView;
import unit.util.UserInfoUtils;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/18.
 */

public class PutiTeacherRecordActivity extends PutiActivity implements PutiTeacherView {
    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.base_info_layout)
    LinearLayout baseInfoLayout;
    @BindView(R.id.class_manager_layout)
    LinearLayout classManagerLayout;

    private PutiTeacherPtr mPtr;

    @Override
    public int getContentView() {
        return R.layout.puti_teacher_record;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null){
            mPtr = new PutiTeacherPtr(this,this);
        }
    }

    @Override
    public void ParseIntent() {

    }

    @Override
    public void AttachPtrView() {

    }

    @Override
    public void DettachPtrView() {

    }

    @Override
    public void InitView() {

        headview.setCallBack(new HeadView.HeadViewCallBack() {
            @Override
            public void backClick() {
                finish();
            }
        });

        headview.setTitle(UserInfoUtils.getUserInfo().getRealName());
    }

    @Override
    public void Star() {
        mPtr.star();
    }

    @Override
    public void addBaseInfoLayout(View view) {
        baseInfoLayout.removeAllViews();
        baseInfoLayout.addView(view);
    }

    @Override
    public void addClassManageLayout(View view) {
        classManagerLayout.removeAllViews();
        classManagerLayout.addView(view);
    }
}

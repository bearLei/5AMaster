package unit.moudle.eventregist.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.widget.GridViewForScrollView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.StudentEntity;
import unit.moudle.eventregist.adapter.StudentAdapter;
import unit.moudle.eventregist.entity.ChooseStuEntity;

/**
 * Created by lei on 2018/6/15.
 * 选择学生，涉事人列表holder
 */

public class ChooseStuDetailHolder extends BaseHolder<ChooseStuEntity> {

    @BindView(R.id.letter)
    TextView letter;
    @BindView(R.id.stu_list)
    GridViewForScrollView stuList;

    private StudentAdapter mAdapter;
    public ChooseStuDetailHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_choose_stu_list_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, ChooseStuEntity data) {
        if (data == null || data.getmStuents() == null) return;
        letter.setText(data.getLetter());
        ArrayList<StudentEntity.Student> list = data.getmStuents();
        if (mAdapter == null){
            mAdapter = new StudentAdapter(mContext,list);
        }
        stuList.setAdapter(mAdapter);
    }
}

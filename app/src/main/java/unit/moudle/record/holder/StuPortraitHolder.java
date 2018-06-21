package unit.moudle.record.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.widget.GridViewForScrollView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.StudentPortraitInfo;
import unit.moudle.record.adapter.StuRecordExpressionAdapter;

/**
 * Created by lei on 2018/6/18.
 */

public class StuPortraitHolder extends BaseHolder<StudentPortraitInfo> {

    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.class_name)
    TextView className;
    @BindView(R.id.major_name)
    TextView majorName;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.total_point)
    TextView totalPoint;
    @BindView(R.id.pencent)
    TextView pencent;
    @BindView(R.id.comparison_point)
    TextView comparisonPoint;
    @BindView(R.id.class_rank_point)
    TextView classRankPoint;
    @BindView(R.id.grade_rank_layout)
    TextView gradeRankLayout;
    @BindView(R.id.expression_list)
    GridViewForScrollView expressionList;

    private StuRecordExpressionAdapter mAdapter;
    public StuPortraitHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_record_stu_portrait);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, StudentPortraitInfo data) {

        if (data == null){
            return;
        }

        StudentPortraitInfo.StudentInfom studentInfoms = data.getStudentInfoms();
        StudentPortraitInfo.StuPortrar stuPortrar = data.getStuPortrar();
        List<StudentPortraitInfo.StudentPort> studentPort = data.getStudentPort();


        if (studentInfoms != null){
            avatar.setImageURI(studentInfoms.getPhtot());
            className.setText(studentInfoms.getClassName());
            majorName.setText(studentInfoms.getProfessionalName());
            time.setText(studentInfoms.getStatusTime());
        }

        if (stuPortrar != null){
            totalPoint.setText(String.valueOf(stuPortrar.getScore()));
            comparisonPoint.setText(String.valueOf(stuPortrar.getContrastiveScore()));
            classRankPoint.setText(String.valueOf(stuPortrar.getGraderanks()));
            gradeRankLayout.setText(String.valueOf(stuPortrar.getSchoolranks()));
        }

        if (mAdapter == null){
            mAdapter = new StuRecordExpressionAdapter(mContext,studentPort);
        }
        expressionList.setAdapter(mAdapter);
    }
}

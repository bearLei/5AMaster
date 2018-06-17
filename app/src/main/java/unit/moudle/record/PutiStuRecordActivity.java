package unit.moudle.record;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.Student;
import unit.moudle.record.ptr.PutiStuPtr;
import unit.moudle.record.view.PutiStuView;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/18.
 */

public class PutiStuRecordActivity extends PutiActivity implements PutiStuView {

    public static final String Parse_Intent = "parse_intent";

    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.stu_likeness)
    LinearLayout stuLikeness;
    @BindView(R.id.stu_info)
    LinearLayout stuInfo;

    private Student student;
    private PutiStuPtr mPtr;
    @Override
    public int getContentView() {
        return R.layout.puti_stu_record_activity;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null){
            mPtr = new PutiStuPtr(this,this);
        }
    }

    @Override
    public void ParseIntent() {
        if (getIntent() != null) {
            student = (Student) getIntent().getSerializableExtra(Parse_Intent);
        }
    }

    @Override
    public void AttachPtrView() {

    }

    @Override
    public void DettachPtrView() {

    }

    @Override
    public void InitView() {

    }

    @Override
    public void Star() {
        mPtr.star();
    }

    @Override
    public void addLikeNessView(View view) {

    }

    @Override
    public void addBaseInfoView(View view) {

    }

    @Override
    public String getStudentUid() {
        if (student != null){
            return student.getStudentUID();
        }
        return "";
    }
}

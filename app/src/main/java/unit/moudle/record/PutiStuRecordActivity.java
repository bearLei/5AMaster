package unit.moudle.record;

import android.view.View;
import android.widget.LinearLayout;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;

import butterknife.BindView;
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
    LinearLayout VStuLikeNessLayout;
    @BindView(R.id.stu_info)
    LinearLayout VStuInfoLayout;

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
        headview.setCallBack(new HeadView.HeadViewCallBack() {
            @Override
            public void backClick() {
                finish();
            }
        });
    }

    @Override
    public void Star() {
        mPtr.star();
    }

    @Override
    public void addLikeNessView(View view) {
        VStuLikeNessLayout.removeAllViews();
        VStuLikeNessLayout.addView(view);
    }

    @Override
    public void addBaseInfoView(View view) {
        VStuInfoLayout.removeAllViews();
        VStuInfoLayout.addView(view);
    }

    @Override
    public String getStudentUid() {
        if (student != null){
            return student.getStudentUID();
        }
        return "";
    }

    @Override
    public void setHeadTitle(String name) {
        if (headview != null){
            headview.setTitle(name);
        }
    }
}

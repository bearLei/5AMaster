package unit.moudle.record.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.StudentInfo;
import unit.widget.PutiRecordItem;

/**
 * Created by lei on 2018/6/18.
 * 学生档案-学生基础信息
 */

public class StuBaseInfoHolder extends BaseHolder<StudentInfo> {
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.name)
    PutiRecordItem name;
    @BindView(R.id.sex)
    PutiRecordItem sex;
    @BindView(R.id.nation)
    PutiRecordItem nation;
    @BindView(R.id.birth)
    PutiRecordItem birth;
    @BindView(R.id.category)
    PutiRecordItem category;
    @BindView(R.id.cencus)
    PutiRecordItem cencus;
    @BindView(R.id.mobile)
    PutiRecordItem mobile;
    @BindView(R.id.stu_card)
    PutiRecordItem stuCard;
    @BindView(R.id.family_address)
    PutiRecordItem familyAddress;
    @BindView(R.id.father_name)
    PutiRecordItem fatherName;
    @BindView(R.id.father_mobile)
    PutiRecordItem fatherMobile;
    @BindView(R.id.father_card)
    PutiRecordItem fatherCard;
    @BindView(R.id.mother_name)
    PutiRecordItem motherName;
    @BindView(R.id.mother_mobile)
    PutiRecordItem motherMobile;
    @BindView(R.id.mother_card)
    PutiRecordItem motherCard;
    @BindView(R.id.pull_down)
    ImageView pullDown;
    @BindView(R.id.content_layout)
    LinearLayout contentLayout;

    private boolean hide;

    public StuBaseInfoHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_stu_record_base_info_holder);
        ButterKnife.bind(this, mRootView);
        pullDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide = !hide;
                contentLayout.setVisibility(hide ? View.GONE : View.VISIBLE);
            }
        });
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, StudentInfo data) {
        if (data == null) {
            return;
        }
        StudentInfo.StuBasicInfo stuBasicInfo = data.getStuBasicInfo();
        StudentInfo.StuFatherInfo stuFatherInfo = data.getStuFatherInfo();
        StudentInfo.StuMotherInfo stuMotherInfo = data.getStuMotherInfo();
        StudentInfo.StudentInfoms studentInfoms = data.getStudentInfoms();
        StudentInfo.StuDorminfo stuDorminfo = data.getStuDorminfo();
        StudentInfo.StuHeadInfo stuHeadInfo = data.getStuHeadInfo();

        //基础信息
        if (stuBasicInfo != null) {
            name.setTDesc(stuBasicInfo.getUserName());
            if (stuBasicInfo.getSex().equals("F")){
                sex.setTDesc("男");
            }else {
                sex.setTDesc("女");
            }
            sex.setTDesc(stuBasicInfo.getSex());
            // TODO: 2018/6/18 民族
            birth.setTDesc(stuBasicInfo.getBirthday());
            cencus.setTDesc(stuBasicInfo.getCensusRegister());

            mobile.setTDesc(stuBasicInfo.getMobile());
            stuCard.setTDesc(stuBasicInfo.getIdCard());
            familyAddress.setTDesc(stuBasicInfo.getAddress());
        }
        //父亲信息
        if (stuFatherInfo != null) {
            fatherName.setTDesc(stuFatherInfo.getUserName());
            fatherMobile.setTDesc(stuFatherInfo.getMobile());
            fatherCard.setTDesc(stuFatherInfo.getIdCard());
        }
        //母亲信息
        if (stuMotherInfo != null) {
            motherName.setTDesc(stuMotherInfo.getUserName());
            motherMobile.setTDesc(stuMotherInfo.getMobile());
            motherCard.setTDesc(stuMotherInfo.getIdCard());
        }

    }
}

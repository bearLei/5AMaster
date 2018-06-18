package unit.moudle.record.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
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
    @BindView(R.id.refer_school)
    PutiRecordItem referSchool;
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

    public StuBaseInfoHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_stu_record_base_info_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, StudentInfo data) {
       if (data == null){
           return;
       }
        StudentInfo.StuBasicInfo stuBasicInfo = data.getStuBasicInfo();
        StudentInfo.StuFatherInfo stuFatherInfo = data.getStuFatherInfo();
        StudentInfo.StuMotherInfo stuMotherInfo = data.getStuMotherInfo();
        StudentInfo.StudentInfoms studentInfoms = data.getStudentInfoms();
        StudentInfo.StuDorminfo stuDorminfo = data.getStuDorminfo();
        StudentInfo.StuHeadInfo stuHeadInfo = data.getStuHeadInfo();

        //基础信息
        if (stuBasicInfo != null){
            name.setTDesc(stuBasicInfo.getUserName());
            sex.setTDesc(stuBasicInfo.getSex());
            // TODO: 2018/6/18 民族
            birth.setTDesc(stuBasicInfo.getBirthday());
            // TODO: 2018/6/18 户口性质
            cencus.setTDesc(stuBasicInfo.getCensusRegister());
            // TODO: 2018/6/18 来源学校
            mobile.setTDesc(stuBasicInfo.getMobile());
            stuCard.setTDesc(stuBasicInfo.getIdCard());
            familyAddress.setTDesc(stuBasicInfo.getAddress());
        }
        //父亲信息
        if (stuFatherInfo != null){
            fatherName.setTDesc(stuFatherInfo.getUserName());
            fatherMobile.setTDesc(stuFatherInfo.getMobile());
            fatherCard.setTDesc(stuFatherInfo.getIdCard());
        }
        //母亲信息
        if (stuMotherInfo != null){
            motherName.setTDesc(stuMotherInfo.getUserName());
            motherMobile.setTDesc(stuMotherInfo.getMobile());
            motherCard.setTDesc(stuMotherInfo.getIdCard());
        }

    }
}

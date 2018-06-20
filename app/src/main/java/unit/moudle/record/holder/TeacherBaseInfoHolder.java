package unit.moudle.record.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.TeacherBaseInfo;
import unit.widget.PutiRecordItem;

/**
 * Created by lei on 2018/6/18.
 * 教师档案-基础信息
 */

public class TeacherBaseInfoHolder extends BaseHolder<TeacherBaseInfo> {

    @BindView(R.id.pull_down)
    ImageView pullDown;
    @BindView(R.id.name)
    PutiRecordItem name;
    @BindView(R.id.sex)
    PutiRecordItem sex;
    @BindView(R.id.marital_status)
    PutiRecordItem maritalStatus;
    @BindView(R.id.birth)
    PutiRecordItem birth;
    @BindView(R.id.country)
    PutiRecordItem country;
    @BindView(R.id.nation)
    PutiRecordItem nation;
    @BindView(R.id.native_place)
    PutiRecordItem nativePlace;
    @BindView(R.id.category)
    PutiRecordItem category;
    @BindView(R.id.mobile)
    PutiRecordItem mobile;
    @BindView(R.id.id_card)
    PutiRecordItem idCard;
    @BindView(R.id.code)
    PutiRecordItem code;
    @BindView(R.id.own_major)
    PutiRecordItem ownMajor;
    @BindView(R.id.zhichen_level)
    PutiRecordItem zhichenLevel;
    @BindView(R.id.current_status)
    PutiRecordItem currentStatus;
    @BindView(R.id.height_education)
    PutiRecordItem heightEducation;
    @BindView(R.id.graduation_time)
    PutiRecordItem graduationTime;
    @BindView(R.id.content_layout)
    LinearLayout contentLayout;


    private boolean hide;

    public TeacherBaseInfoHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_teacher_record_base_info_holder);
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
    protected void updateUI(Context context, TeacherBaseInfo data) {
        if (data == null) {
            return;
        }

        name.setTDesc(data.getName());
        sex.setTDesc(data.getSex());
        maritalStatus.setTDesc(data.isMarriage() ? "已婚" : "未婚");
        birth.setTDesc(data.getBirthday());
        country.setTDesc(data.getCountry());
        nation.setTDesc(data.getNation());
        nativePlace.setTDesc(data.getCensusRegister());
        if (data.getCensusType() == 1) {
            category.setTDesc("农村");
        } else if (data.getCensusType() == 2) {
            category.setTDesc("城镇");
        }

        mobile.setTDesc(data.getMobile());
        idCard.setTDesc(data.getIdCard());
        code.setTDesc(data.getCode());

        ownMajor.setTDesc(data.getMajor());

        zhichenLevel.setTDesc(data.getJobTitle());


        // TODO: 2018/6/20 目前在职状态
        currentStatus.setTDesc("在职");
        heightEducation.setTDesc(data.getGraduate());
        graduationTime.setTDesc(data.getGraduateTime());

    }
}

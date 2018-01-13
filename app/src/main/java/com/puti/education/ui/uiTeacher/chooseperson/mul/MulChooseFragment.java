package com.puti.education.ui.uiTeacher.chooseperson.mul;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.ui.BaseFragment;
import com.puti.education.ui.uiTeacher.chooseperson.ChoosePersonView;
import com.puti.education.ui.uiTeacher.chooseperson.event.ChooseCompleteEvent;
import com.puti.education.ui.uiTeacher.chooseperson.single.SingleChooseBean;
import com.puti.education.ui.uiTeacher.chooseperson.ChoosePersonParameter;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.DividerDecoration;
import com.puti.education.util.LogUtil;
import com.puti.education.widget.CommonDropView;
import com.puti.education.widget.QuickIndexBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lenovo on 2018/1/12.
 */

public class MulChooseFragment extends BaseFragment implements ChoosePersonView {
    private static final String TAG = "SingleChooseFragment";
    @BindView(R.id.back_frame)
    FrameLayout VBackFrame;//返回按钮
    @BindView(R.id.title_textview)
    TextView TTitleTv;//标题
    @BindView(R.id.tv_right)
    TextView TCommit;//提交
    @BindView(R.id.layout_search_topbar)
    RelativeLayout VLayoutSearchTopbar;//搜索bar
    @BindView(R.id.stu_choose_btn)
    RadioButton VStuChooseBtn;//选择学生
    @BindView(R.id.teacher_choose_btn)
    RadioButton VTeacherChooseBtn;//老师
    @BindView(R.id.parent_choose_btn)
    RadioButton VParentChooseBtn;//家长
    @BindView(R.id.expert_choose_btn)
    RadioButton VExpertChooseBtn;//专家
    @BindView(R.id.volunteer_choose_btn)
    RadioButton VolunteerChooseBtn;//义工
    @BindView(R.id.radio_group)
    RadioGroup vRadioGroup;
    @BindView(R.id.class_choose_btn)
    Button BClassChooseBtn;//选择班级
    @BindView(R.id.et_input_name)
    EditText EtInputName;//输入学生输入框
    @BindView(R.id.choose_btn_linear)
    LinearLayout VChooseBtnLinear;//选择班级输入学生栏目
    @BindView(R.id.person_rv)
    RecyclerView mList;//列表
    @BindView(R.id.quick_indexbar)
    QuickIndexBar quickIndexbar;//拼音选择控件
    @BindView(R.id.empty_rel)
    RelativeLayout VEmptyRel;//空view
    private CommonDropView mCommonDropView;//选择班级的pop窗口
    private LinearLayoutManager manager;

    private MulChooseListPtr mPtr;
    private List<MulPersonBean> mData;
    private MulChooseAdapter mAdapter;
    private boolean mbAbnormal = false;  //是否异常事件，默认为普通事件
    private int refer;//页面启动来源
    private String mDutyType; //主要责任人，次要责任人，证人，知情人，举报人，
    private String mSchoolId;//学校id
    private int max = 1; //一次最多选择多少人
    private int mTempIndex = -1;//？？？
    private ArrayList<MulPersonBean> mSelectedList;//切换列表的时候数据清空，之前列表选择的人物就在这保存1个列表集合

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_choose_event_person;
    }

    @Override
    public void initVariables() {
        mSchoolId = ConfigUtil.getInstance(getActivity()).get(Constant.KEY_SCHOOL_ID, "");
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (mSelectedList == null) {
            mSelectedList = new ArrayList<>();
        }
        parseIntent();
    }

    @Override
    public void initViews(View view) {
        setPresenter();
        max = 50;
        ChoosePersonParameter.LIMIT = max;
        BClassChooseBtn.setVisibility(View.GONE);
        TTitleTv.setText("选择");
        TCommit.setVisibility(View.GONE);
        EtInputName.clearFocus();
        mList.setHasFixedSize(true);
        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(manager);
        DividerDecoration divider = new DividerDecoration.Builder(getActivity())
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.line_bg)
                .build();
        mList.addItemDecoration(divider);
        if (mAdapter == null) {
            mAdapter = new MulChooseAdapter(getActivity(), mData,new MulSeletePersonCallBack() {
                @Override
                public void seleted(boolean seleted, MulPersonBean info) {
                    if (seleted) {
                        mSelectedList.add(info);
                        oprataListWhenAdd();
                        ChoosePersonParameter.LIMIT--;
                    } else if (mSelectedList.contains(info)) {
                        oprataListWhenRemove(info);
                        ChoosePersonParameter.LIMIT++;
                    }
                    opearateCompleteNumber(mSelectedList.size());
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
        mList.setAdapter(mAdapter);
        //字母滑动回调
        quickIndexbar.setOnLetterChangeListener(new QuickIndexBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                LogUtil.d(TAG, letter);
                if (!TextUtils.isEmpty(letter)) {
                    for (int i = 0; i < mData.size(); i++) {
                        MulPersonBean bean = mData.get(i);
                        String s = String.valueOf(bean.getPinyin().charAt(0));
                        if (letter.equalsIgnoreCase(s)) {
                            manager.scrollToPositionWithOffset(i, 0);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onReset() {

            }
        });

        vRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    //老师
                    case R.id.teacher_choose_btn:
                        mPtr.getList(MulChooseListPtr.TEA);
                        break;
                    //学生
                    case R.id.stu_choose_btn:
                        mPtr.getList(MulChooseListPtr.STU);
                        break;
                    //家长
                    case R.id.parent_choose_btn:
                        mPtr.getList(MulChooseListPtr.PAR);
                        break;
                    //专家
                    case R.id.expert_choose_btn:
                        mPtr.getList(MulChooseListPtr.EXP);
                        break;
                    //义工
                    case R.id.volunteer_choose_btn:
                        mPtr.getList(MulChooseListPtr.VoL);
                        break;
                }

            }
        });
        VStuChooseBtn.setChecked(true);
        TCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete();
            }
        });
        EtInputName.setHint("请输入姓名");
        EtInputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPtr.getAllUserList(s.toString());
            }
        });
    }

    @Override
    public void loadData() {
      mPtr.getAllUserList("");
    }

    public void setPresenter() {
        mPtr = new MulChooseListPtr(getActivity());
        mPtr.attatchView(this);
    }

    @Override
    public void parseIntent() {
        if (null != getArguments()) {
            mbAbnormal = getArguments().getBoolean(ChoosePersonParameter.EVENT_ABNORMOL);
            mDutyType = getArguments().getString(ChoosePersonParameter.DUTY_TYPE);
            refer = getArguments().getInt(ChoosePersonParameter.REFER);
        }
    }

    @Override
    public void updateList(ArrayList<SingleChooseBean> data) {

    }

    @Override
    public void updateAllList(ArrayList<MulPersonBean> data) {
        if (data == null) return;

        mData.clear();
        mData.addAll(data);
        oprataListWhenAdd();
        mAdapter.notifyDataSetChanged();
    }


    //刷新前处理下上次选中的人的情况
    private void oprataListWhenAdd() {
        for (int i = 0; i < mSelectedList.size(); i++) {
            String uid = mSelectedList.get(i).getPersonnelUid();
            for (int j = 0; j < mData.size(); j++) {
                MulPersonBean personBean = mData.get(j);
                if (uid.equals(personBean.getPersonnelUid())) {
                    personBean.setSelected(true);
                }
            }
        }
    }

    //刷新前处理下移除选择的人的情况
    private void oprataListWhenRemove(MulPersonBean info) {
        if (mData.contains(info)) {
            String uid = info.getPersonnelUid();
            for (int i = 0; i < mData.size(); i++) {
                MulPersonBean personBean = mData.get(i);
                if (uid.equals(personBean.getPersonnelUid())) {
                    personBean.setSelected(false);
                    break;
                }
            }
        }
        if (mSelectedList.contains(info)) {
            mSelectedList.remove(info);
        }
    }

    @Override
    public void opearateCompleteNumber(int count) {
        if (count == 0) {
            TCommit.setVisibility(View.GONE);
        } else {
            TCommit.setVisibility(View.VISIBLE);
        }
        TCommit.setText("完成(" + count + "/" + max + ")");
    }

    @Override
    public void complete() {
        //处理完后post事件出去
        ArrayList<EventAboutPeople> list = new ArrayList<>();
        for (int i = 0; i < mSelectedList.size(); i++) {
            MulPersonBean bean = mSelectedList.get(i);
            EventAboutPeople people = new EventAboutPeople();
            people.avatar = "";
            people.uid = bean.getPersonnelUid();
            people.name = bean.getRealName();
            people.type = 2;
            list.add(people);
        }
        ChooseCompleteEvent event = new ChooseCompleteEvent();
        event.setmList(list);
        event.setDuty(mDutyType);
        EventBus.getDefault().post(event);
        getActivity().finish();
    }

    @Override
    public boolean isAbnormal() {
        return mbAbnormal;
    }

    @Override
    public void showLoading(boolean show) {
        if (show) {
            disLoading();
        } else {
            hideLoading();
        }
    }

    @Override
    public void setClassName(String name) {
        BClassChooseBtn.setText(name);
    }

    public static MulChooseFragment newInstance(boolean abNormal, String dutyType, int refer) {
        Bundle args = new Bundle();
        args.putBoolean(ChoosePersonParameter.EVENT_ABNORMOL, abNormal);
        args.putString(ChoosePersonParameter.DUTY_TYPE, dutyType);
        args.putInt(ChoosePersonParameter.REFER, refer);
        MulChooseFragment fragment = new MulChooseFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

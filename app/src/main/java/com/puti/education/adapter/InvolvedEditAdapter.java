package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.Duty;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.InvolvedSettingBean;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.LogUtil;
import com.puti.education.util.SimpleTextWatcher;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;
import com.puti.education.widget.GridViewForScrollView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by xjbin on 2017/4/25 0025.
 *
 * 编辑涉事人Adapter
 */

public class InvolvedEditAdapter extends BasicRecylerAdapter<InvolvedSettingBean> implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    private HashMap<Integer,String> mEditMap = null;
    private Integer index  = -1;

    private List<Duty.DutyDetail> mDutyList;
    private List<String> mWarnList;

    public void setmEditMap(HashMap<Integer, String> mEditMap) {
        this.mEditMap = mEditMap;
    }

    public void setmDutyList(List<Duty.DutyDetail> mDutyList) {
        this.mDutyList = mDutyList;
    }

    public void setmWarnList(List<String> mWarnList) {
        this.mWarnList = mWarnList;
    }

    public interface DelOnclickListener{
        void delClick(int position);
    }

    public interface AddClassLeaderListener{
        void addclick(int position);
    }

    public interface AddProfessorListener{
        void addclick(int position);
    }


    public interface  ChildGvDeleteClickLintener{
        void click(int parentPositon,int position);
    }

    public interface  ProDeleteClickLintener{
        void click(int parentPositon,int position);
    }

    public AddClassLeaderListener addClassLeaderListener;
    public AddProfessorListener addProfessorListener;
    public DelOnclickListener delOnclickListener;


    ChildGvDeleteClickLintener childGvDeleteClickLintener ;
    ProDeleteClickLintener proDeleteClickLintener ;

    public void setProDeleteClickLintener(ProDeleteClickLintener proDeleteClickLintener) {
        this.proDeleteClickLintener = proDeleteClickLintener;
    }

    public void setChildGvDeleteClickLintener(ChildGvDeleteClickLintener childGvDeleteClickLintener) {
        this.childGvDeleteClickLintener = childGvDeleteClickLintener;
    }

    public void setAddClassLeaderListener(AddClassLeaderListener addClassLeaderListener) {
        this.addClassLeaderListener = addClassLeaderListener;
    }

    public void setAddProfessorListener(AddProfessorListener addProfessorListener) {
        this.addProfessorListener = addProfessorListener;
    }

    public void setDelOnclickListener(DelOnclickListener delOnclickListener) {
        this.delOnclickListener = delOnclickListener;
    }

    public InvolvedEditAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_edit_involved_people_layout;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int parentPosition) {
        final InvolvedSettingBean settingBean = mList.get(parentPosition);

        CommonViewHolder cHolder = (CommonViewHolder) holder;
        ImageView headImg = cHolder.obtainView(R.id.edit_head_img);
        TextView nameTv = cHolder.obtainView(R.id.edit_involved_name_tv);
        TextView classNameTv = cHolder.obtainView(R.id.edit_involved_class_tv);
        FrameLayout delFrame = cHolder.obtainView(R.id.edit_del_frame);
        TextView dutyChooseTv =  cHolder.obtainView(R.id.responsibility_type_choose_tv);
        TextView warnsTv =  cHolder.obtainView(R.id.important_grade_tv);
        LinearLayout parentLinear =  cHolder.obtainView(R.id.parentLinear);
        ImageView parentImg =  cHolder.obtainView(R.id.parent_img);
        LinearLayout parentImgLinear =  cHolder.obtainView(R.id.parent_img_linear);

        LinearLayout stuLinear =  cHolder.obtainView(R.id.stuLinear);
        ImageView stuImg = cHolder.obtainView(R.id.stu_img);

        ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default,settingBean.avartar,headImg);

        //班主任
        LinearLayout otherClassLeaderLinear = cHolder.obtainView(R.id.otherLeaderLinear);
        ImageView otherImg =  cHolder.obtainView(R.id.other_leader_img);
        GridViewForScrollView otherLeaderGv = cHolder.obtainView(R.id.other_leader_gv);

        RadioGroup radioGroup =  cHolder.obtainView(R.id.is_follow_rg);
        radioGroup.setTag(settingBean);
        final EditText msgEdit =  cHolder.obtainView(R.id.deal_edit_msg);
        //专家
        GridViewForScrollView professorGv = cHolder.obtainView(R.id.professor_gv);

        radioGroup.setOnCheckedChangeListener(this);
        parentLinear.setOnClickListener(this);
        stuLinear.setOnClickListener(this);
        otherClassLeaderLinear.setOnClickListener(this);

        nameTv.setText(settingBean.name);
        classNameTv.setText(settingBean.className);

        //责任，警告
        dutyChooseTv.setOnClickListener(this);
        warnsTv.setOnClickListener(this);
        dutyChooseTv.setTag(parentPosition);
        warnsTv.setTag(parentPosition);
        dutyChooseTv.setText(settingBean.dutyRank);
        warnsTv.setText(settingBean.warningRank);

        //家长
        parentLinear.setTag(parentPosition);
        if (settingBean.isParentCheck){
            parentImg.setImageResource(R.mipmap.ic_item_selected);
        }else{
            parentImg.setImageResource(R.mipmap.ic_item_unselected);
        }
        if (settingBean.parentList.size() > 0){
            parentImgLinear.setVisibility(View.VISIBLE);
            int size = settingBean.parentList.size();
            EventAboutPeople simplePeople = null;
            for (int i=0; i< size;i++){
                simplePeople = settingBean.parentList.get(i);
                ImageView chilImg = (ImageView) parentImgLinear.getChildAt(i);
                chilImg.setVisibility(View.VISIBLE);
                ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default,simplePeople.avatar,chilImg);
            }
        }else{
            parentImgLinear.setVisibility(View.INVISIBLE);
            for (int i=0; i< 2;i++){
                parentImgLinear.getChildAt(i).setVisibility(View.GONE);
            }
        }

        //学生处
        stuLinear.setTag(parentPosition);
        if (settingBean.isStuDePartmentCheck){
            stuImg.setImageResource(R.mipmap.ic_item_selected);
        }else{
            stuImg.setImageResource(R.mipmap.ic_item_unselected);
        }

        //其他班主任
        otherClassLeaderLinear.setTag(parentPosition);
        if (settingBean.isOtherClassLeaderCheck){
            otherImg.setImageResource(R.mipmap.ic_item_selected);
        }else{
            otherImg.setImageResource(R.mipmap.ic_item_unselected);
        }
        EventAboutPeopleAdapter otherLeaderAdapter = new EventAboutPeopleAdapter(mContext);
        otherLeaderGv.setAdapter(otherLeaderAdapter);
        otherLeaderAdapter.setmList(settingBean.otherLeaderList);
        LogUtil.i("leader size  ",settingBean.otherLeaderList.size() + "");
        otherLeaderGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               EventAboutPeople people =  settingBean.otherLeaderList.get(position);
                if (!people.isPeople && addClassLeaderListener != null){
                    addClassLeaderListener.addclick(parentPosition);
                }else{
                    if (childGvDeleteClickLintener != null){
                        childGvDeleteClickLintener.click(parentPosition,position);
                    }
                }
            }
        });

        //专家
        EventAboutPeopleAdapter professorListAdapter = new EventAboutPeopleAdapter(mContext);
        professorGv.setAdapter(professorListAdapter);
        professorListAdapter.setmList(settingBean.professorList);
        professorGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventAboutPeople people =  settingBean.professorList.get(position);
                if (!people.isPeople && addProfessorListener != null){
                    addProfessorListener.addclick(parentPosition);
                }else{
                    if (proDeleteClickLintener != null){
                        proDeleteClickLintener.click(parentPosition,position);
                    }
                }
            }
        });


        //删除
        delFrame.setOnClickListener(this);
        delFrame.setTag(parentPosition);

        //事件描述
        msgEdit.setTag(parentPosition);
        msgEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    index = (Integer) v.getTag();
                }
                return false;
            }
        });

        msgEdit.clearFocus();
        if (index != -1 && index == parentPosition) {
            msgEdit.requestFocus();
            if (!TextUtils.isEmpty(msgEdit.getText().toString())){
                msgEdit.setSelection(msgEdit.getText().toString().length());
            }
        }

        msgEdit.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                Integer tag = (Integer) msgEdit.getTag();
                mEditMap.put(tag,s.toString());
            }
        });

        msgEdit.setText(mEditMap.get(parentPosition));

        if (settingBean.isFollow){
            radioGroup.check(R.id.yes_rbtn);
        }else{
            radioGroup.check(R.id.no_rbtn);
        }
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()){
            case R.id.edit_del_frame:
            {
                if (delOnclickListener != null){
                    delOnclickListener.delClick((int)v.getTag());
                }
            }
            break;

            //责任等级选择
            case R.id.responsibility_type_choose_tv:
            {
//                if (mDutyList != null && mDutyList.size() > 0){
//                    final CommonDropView commonDropView = new CommonDropView(mContext,v,mDutyList);
//                    commonDropView.showAsDropDown(v);
//                    commonDropView.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
//                        @Override
//                        public void onItemClick(int position) {
//                            TextView mTextView = (TextView) v;
//                            mTextView.setText(mDutyList.get(position));
//                            mList.get((Integer) mTextView.getTag()).dutyRank = mTextView.getText().toString();
//                            commonDropView.dismiss();
//                        }
//                    });
//                }else{
//                    ToastUtil.show("没有数据");
//                }
            }
                break;

            //警告等级选择
            case R.id.important_grade_tv:
            {
                if (mWarnList != null && mWarnList.size() > 0){
                    final CommonDropView commonDropView = new CommonDropView(mContext,v,mWarnList);
                    commonDropView.showAsDropDown(v);
                    commonDropView.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            TextView mTextView = (TextView) v;
                            mTextView.setText(mWarnList.get(position));
                            mList.get((Integer) mTextView.getTag()).warningRank = mTextView.getText().toString();
                            commonDropView.dismiss();
                        }
                    });
                }else{
                    ToastUtil.show("没有数据");
                }
            }
                break;

            //家长
            case R.id.parentLinear:
            {
                LinearLayout parentLinear = (LinearLayout) v;
                ImageView imageView = (ImageView)parentLinear.getChildAt(0);

                InvolvedSettingBean bean = mList.get((Integer) v.getTag());
                if (bean.isParentCheck){
                    bean.isParentCheck = false;
                    imageView.setImageResource(R.mipmap.ic_item_unselected);

                }else{
                    bean.isParentCheck = true;
                    imageView.setImageResource(R.mipmap.ic_item_selected);
                }

            }
            break;

            //学生处
            case R.id.stuLinear:
            {
                LinearLayout parentLinear = (LinearLayout) v;
                ImageView imageView = (ImageView)parentLinear.getChildAt(0);
                InvolvedSettingBean bean = mList.get((Integer) v.getTag());
                if (bean.isStuDePartmentCheck){
                    bean.isStuDePartmentCheck = false;
                    imageView.setImageResource(R.mipmap.ic_item_unselected);

                }else{
                    bean.isStuDePartmentCheck = true;
                    imageView.setImageResource(R.mipmap.ic_item_selected);
                }

            }
            break;

            //其他班主任
            case R.id.otherLeaderLinear:
            {
                LinearLayout parentLinear = (LinearLayout) v;
                ImageView imageView = (ImageView)parentLinear.getChildAt(0);
                InvolvedSettingBean bean = mList.get((Integer) v.getTag());
                if (bean.isOtherClassLeaderCheck){
                    bean.isOtherClassLeaderCheck = false;
                    imageView.setImageResource(R.mipmap.ic_item_unselected);
                }else{
                    bean.isOtherClassLeaderCheck = true;
                    imageView.setImageResource(R.mipmap.ic_item_selected);
                }
            }
            break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        InvolvedSettingBean settingBean = (InvolvedSettingBean) group.getTag();
        if (checkedId == R.id.yes_rbtn){
            settingBean.isFollow = true;
        }else{
            settingBean.isFollow = false;
        }

    }
}

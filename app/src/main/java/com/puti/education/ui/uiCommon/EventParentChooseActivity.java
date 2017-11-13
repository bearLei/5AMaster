package com.puti.education.ui.uiCommon;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.ParentFromStudent;
import com.puti.education.bean.Duty;
import com.puti.education.bean.EventType;
import com.puti.education.bean.StudentDetailBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiTeacher.ChoosePersonListActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.Key;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;

public class EventParentChooseActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.tv_title)
    TextView mTvEventBack;
    @BindView(R.id.recyclerview)
    RecyclerView mRecycleView;

    private Context mContext;
    private ListAdapter mListAdapter;
    private String mStudentUid;
    private ArrayList<ParentFromStudent> mParentList = new ArrayList<>();
    private int mTempIndex = -1;


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_event_duty_choose;
    }

    @Override
    public void initVariables() {
        mContext = this;
        mStudentUid = this.getIntent().getStringExtra(Key.KEY_PEOPLE_UID);
        if (this.getIntent().hasExtra(Key.INDEX)) {
            mTempIndex = this.getIntent().getIntExtra(Key.INDEX, -1);
        }
    }

    @Override
    public void initViews() {
        mTitleTv.setText("父母选择");
        mTvEventBack.setVisibility(View.GONE);

        mListAdapter = new ListAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mListAdapter);
        mListAdapter.setDataList(mParentList);

        mListAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.putExtra(Key.INDEX, mTempIndex);
                intent.putExtra(Key.BEAN, mParentList.get(position));
                setResult(Constant.ROLE_PARENTS, intent);
            }
        });

        mTvEventBack.setClickable(false);
        mTvEventBack.setText("请选择责任类型:");

    }

    @Override
    public void loadData() {
        if (TextUtils.isEmpty(mStudentUid)){
            ToastUtil.show("学校的ID为空");
            return;
        }
        getStudentDetail(mStudentUid);
    }

    /**
     * 学生详情
     * 目的：获得学生对应的家长信息
     */
    private void getStudentDetail(final String studentId){

        disLoading();
        CommonModel.getInstance().getStudentDetail(studentId,new BaseListener(StudentDetailBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                hideLoading();
                if (infoObj != null){
                    StudentDetailBean bean = (StudentDetailBean) infoObj;
                    if (!TextUtils.isEmpty(bean.fatheruid)) {
                        ParentFromStudent parentFromStudent1 = new ParentFromStudent(bean.fatheruid, bean.fatherName, bean.fatherAvatar, mStudentUid);
                        mParentList.add(parentFromStudent1);
                    }
                    if (!TextUtils.isEmpty(bean.motheruid)){
                        ParentFromStudent parentFromStudent2 = new ParentFromStudent(bean.motheruid,bean.motherName,bean.motherAvatar,mStudentUid);
                        mParentList.add(parentFromStudent2);
                    }
                    mListAdapter.setDataList(mParentList);
                }else{
                    ToastUtil.show("获取学生详情失败");
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });
    }


    public class ListAdapter extends BasicRecylerAdapter {

        public ListAdapter(Context myContext){
            super(myContext);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_addevent_about_people;
        }

        public void refershData(List<ParentFromStudent> lists){
            setDataList(lists);
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            ParentFromStudent eventEntity = (ParentFromStudent)mList.get(position);

            CommonViewHolder viewHolder = (CommonViewHolder) holder;

            ImageView headImg = (ImageView)viewHolder.obtainView(R.id.grid_head_img);
            TextView nameTv = (TextView) viewHolder.obtainView(R.id.grid_head_name_tv);
            TextView involeType = (TextView)viewHolder.obtainView( R.id.grid_tv_invole_type);
            ImageView delImg = (ImageView)viewHolder.obtainView(R.id.del_img);
            involeType.setVisibility(View.GONE);


            ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default,eventEntity.avatar,headImg);
            nameTv.setText(eventEntity.name);
            delImg.setVisibility(View.GONE);
            involeType.setVisibility(View.GONE);

            viewHolder.itemView.setTag(R.id.onclick_position, position);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posi = (int)v.getTag(R.id.onclick_position);
                    ParentFromStudent onestuend = (ParentFromStudent)mList.get(posi);

                    Intent intent= new Intent();
                    intent.putExtra(Key.KEY_PARENT_DATA, onestuend);
                    setResult(Constant.CODE_RESULT_PARENTS, intent);
                    finish();

                }
            });

        }
    }


}

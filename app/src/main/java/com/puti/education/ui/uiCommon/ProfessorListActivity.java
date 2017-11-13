package com.puti.education.ui.uiCommon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.ProfessorChooseListAdapter;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.Professor;
import com.puti.education.bean.Teacher;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiTeacher.CommitAuditActivity;
import com.puti.education.util.CharacterParser;
import com.puti.education.util.Constant;
import com.puti.education.util.DividerDecoration;
import com.puti.education.util.Key;
import com.puti.education.util.ProfessorPinyinComparator;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.QuickIndexBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xjbin on 2017/5/18 0018.
 *
 * 专家
 */

public class ProfessorListActivity extends BaseActivity{

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.person_rv)
    RecyclerView mRv;
    @BindView(R.id.quick_indexbar)
    QuickIndexBar mQuickIndexBar;

    private List<Professor> mProfessorList;

    private ProfessorChooseListAdapter mProfessorListAdapter;
    private LinearLayoutManager manager;

    private int mSourceWindow;//来自哪个Activity

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_choose_professor;
    }

    @Override
    public void initVariables() {

        Intent intent = getIntent();
        mSourceWindow = intent.getIntExtra(Key.FROM_SOURCE_WINDOW, -1);

        mProfessorList = new ArrayList<>();
        mProfessorListAdapter = new ProfessorChooseListAdapter(ProfessorListActivity.this);
    }

    @Override
    public void initViews() {
        mTitleTv.setText("选择");

        mRv.setHasFixedSize(true);
        manager = new LinearLayoutManager(ProfessorListActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        //条目间的间隔线
        DividerDecoration divider = new DividerDecoration.Builder(ProfessorListActivity.this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.line_bg)
                .build();
        mRv.addItemDecoration(divider);

        //字母滑动回调
        mQuickIndexBar.setOnLetterChangeListener(new QuickIndexBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                if (mProfessorList == null || mProfessorList.size() == 0){
                    return;
                }
                for (int i = 0; i < mProfessorList.size(); i++) {

                    if (letter.equalsIgnoreCase(mProfessorList.get(i).pinyin.charAt(0) + "")) {

                        int position = mProfessorListAdapter.getPositionForSection(mProfessorList.get(i).pinyin.charAt(0));
                        if (position != -1) {
                            //滑动到指定位置
                            manager.scrollToPositionWithOffset(position, 0);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onReset() {

            }
        });

        mProfessorListAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                Professor professor = mProfessorList.get(position);
                EventAboutPeople eventAboutPeople = new EventAboutPeople(professor.avatar, professor.name, professor.uid, 5);
                Intent intent = new Intent();
                intent.putExtra(Key.BEAN,eventAboutPeople);
                setResult(Constant.CODE_RESULT_PROFESSOR,intent);
                finish();
            }
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadData() {
        getProfessorList();
    }


    private void getProfessorList(){

        CommonModel.getInstance().getProfessorList(new BaseListener(Professor.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {

                mProfessorList = (List<Professor>)listObj;

                if (mProfessorList != null && mProfessorList.size() > 0){
                    CharacterParser characterParser = new CharacterParser();
                    for (Professor professor:mProfessorList) {
                        professor.pinyin = characterParser.getSelling(professor.name);
                        if (TextUtils.isEmpty(professor.pinyin)) {
                            professor.pinyin = "#";
                        } else if (Constant.ALPHABETS.indexOf(professor.pinyin.charAt(0)) == -1){
                            professor.pinyin = "#";
                        }
                    }
                    ProfessorPinyinComparator pinyinComparator = new ProfessorPinyinComparator();
                    Collections.sort(mProfessorList, pinyinComparator);
                    mRv.setAdapter(mProfessorListAdapter);
                    mProfessorListAdapter.setDataList(mProfessorList);
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });

    }



    @OnClick(R.id.back_frame)
    public void finishActivityClick(){
        finish();
    }
}

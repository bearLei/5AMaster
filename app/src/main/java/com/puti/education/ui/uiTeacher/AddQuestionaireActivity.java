package com.puti.education.ui.uiTeacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.puti.education.R;
import com.puti.education.adapter.QuestionListAdapter;
import com.puti.education.bean.AddEventResponseBean;
import com.puti.education.bean.AddNewQuestionaireResponse;
import com.puti.education.bean.Teacher;
import com.puti.education.bean.TeacherQuestion;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netApi.TeacherApi;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xbjin on 2017/5/11 0011.
 *
 * 新增问券
 */

public class AddQuestionaireActivity extends BaseActivity implements View.OnClickListener{

    private final static  String TAG = AddQuestionaireActivity.class.getName();

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.event_commit_btn)
    Button mCommitBtn;

    private EditText mTitleEdit;
    private Button addBtn;

    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private QuestionListAdapter mQuestionListAdapter;
    private List<TeacherQuestion> mQuestionList;
    private HashMap<Integer,String> dataMap;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_add_questionaire;
    }

    @Override
    public void initVariables() {
        dataMap = new HashMap<>();
        mQuestionList = new ArrayList<>(20);
    }

    @Override
    public void initViews() {
        mTitleTv.setText("新增问券");
        mQuestionListAdapter = new QuestionListAdapter(this);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(this,mQuestionListAdapter);
        mQuestionListAdapter.setDataMap(dataMap);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        View headView = LayoutInflater.from(this).inflate(R.layout.head_questionaire_title,null);
        mTitleEdit = (EditText) headView.findViewById(R.id.questionnaire_title_edit);
        View footerView = LayoutInflater.from(this).inflate(R.layout.footer_questionaire_title,null);
        addBtn = (Button) footerView.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(this);

        lRecyclerViewAdapter.addHeaderView(headView);
        lRecyclerViewAdapter.addFooterView(footerView);
        mRv.setLayoutManager(layoutManager);
        mRv.setAdapter(lRecyclerViewAdapter);
        mRv.setItemAnimator(new DefaultItemAnimator());
        mQuestionListAdapter.setDataList(mQuestionList);

        //删除
        mQuestionListAdapter.setDelLinstener(new QuestionListAdapter.DelLinstener() {
            @Override
            public void click(int position) {
                LogUtil.i("position","---index = ---"+position);
                mQuestionListAdapter.delete(position);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void loadData() {

    }

    @OnClick(R.id.event_commit_btn)
    public void commitClick(){

        String paramStr = createParamStr();
        if (paramStr != null){
            addNetQuestionaire(paramStr);
        }

    }

    //添加问题
    @Override
    public void onClick(View v) {
        dataMap.put(mQuestionListAdapter.mList.size(),"");
        mQuestionListAdapter.insert(new TeacherQuestion());
        LogUtil.i("list size",mQuestionListAdapter.mList.size()+"");
    }

    private String createParamStr(){

        JSONObject jsonObject = new JSONObject();
        if (TextUtils.isEmpty(mTitleEdit.getText().toString())){
            ToastUtil.show("请输入问券标题");
            return null;
        }

        if (mQuestionListAdapter.mList.size() == 0){
            ToastUtil.show("请添加问券问题");
            return null;
        }

        jsonObject.put("title",mTitleEdit.getText().toString());

        JSONArray jsonArray = new JSONArray();
        Iterator<Map.Entry<Integer, String>> it = dataMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, String> entry = it.next();
            jsonArray.add(entry.getValue() == null ? "":entry.getValue());
        }
        jsonObject.put("question",jsonArray);
        LogUtil.i(TAG+"  params",jsonObject.toString());
        return jsonObject.toString();

    }

    private void addNetQuestionaire(String paramStr){

        disLoading();

        TeacherModel.getInstance().addNetQuestionaire(paramStr,new BaseListener(AddNewQuestionaireResponse.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                hideLoading();
                if (infoObj != null){
                    ToastUtil.show("新增事件问券成功");
                    Intent intent = new Intent();
                    setResult(TeacherAddEventActivity.CODE_RESULT_TO_ADD_QUESTION,intent);
                    finish();
                }else{
                    ToastUtil.show("新增事件问券失败");
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });
    }

    public void finishActivity(View view){
        finish();
    }
}

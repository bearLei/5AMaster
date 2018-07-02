package unit.moudle.ques;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.api.PutiTeacherModel;
import unit.base.BaseResponseInfo;
import unit.entity.QuesDetailInfo;
import unit.entity.QuesInfo;
import unit.entity.Question;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/29.
 */

public class PutiQuesDetailActivity extends PutiActivity {

    public static final String Parse_Intent = "Parse_Intent";

    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.ques_type)
    TextView quesType;
    @BindView(R.id.ques_desc)
    TextView quesDesc;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.commit)
    TextView commit;


    private QuesInfo info;
    private QuestDetailAdapter mAdapter;
    private ArrayList<Question> mData;

    @Override
    public int getContentView() {
        return R.layout.puti_ques_detail_activity;
    }

    @Override
    public void BindPtr() {

    }

    @Override
    public void ParseIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            info = (QuesInfo) intent.getSerializableExtra(Parse_Intent);
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


        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new QuestDetailAdapter(this, mData);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(mAdapter);

    }

    @Override
    public void Star() {
        if (info != null) {
            headview.setTitle(info.getSurveyName());
            queryData(info.getUserSurveyUID());
        }
    }


    private void queryData(String suveryUid) {
        PutiTeacherModel.getInstance().getQuesDetail(suveryUid, new BaseListener(QuesDetailInfo.class) {
            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
                ArrayList<QuesDetailInfo> list = (ArrayList<QuesDetailInfo>) listObj;
                if (list != null && list.size() > 0) {
                    handleResult(list.get(0));
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show(errorMessage);
            }
        });
    }


    private void handleResult(QuesDetailInfo info) {
        if (info != null) {
            List<Question> questionList = info.getQuestions();
            quesType.setText(info.getCategoryName());
            quesDesc.setText(info.getRemark());
            mData.clear();
            mData.addAll(questionList);
            mAdapter.notifyDataSetChanged();
        }
    }


    @OnClick(R.id.commit)
    public void onClick() {
        String answers = mAdapter.getAnswers();
        if (TextUtils.isEmpty(answers)){
            return;
        }
        PutiTeacherModel.getInstance().commitQues(answers,info.getUserSurveyUID(),new BaseListener(BaseResponseInfo.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show(errorMessage);
            }
        });
    }
}

package unit.moudle.ques;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import unit.api.PutiTeacherModel;
import unit.entity.QuesInfo;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/29.
 */

public class PutiQuesActivity extends PutiActivity {
    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;


    private ArrayList<QuesInfo> mData;
    private QuesAdapter mAdapter;
    @Override
    public int getContentView() {
        return R.layout.puti_ques_activity;
    }

    @Override
    public void BindPtr() {

    }

    @Override
    public void ParseIntent() {

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
        headview.setTitle("我的问卷");

        if (mData == null){
            mData = new ArrayList<>();
        }

        if (mAdapter == null){
            mAdapter = new QuesAdapter(this,mData);
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void Star() {
        queryData();
    }

    private void queryData() {
        PutiTeacherModel.getInstance().getQuesList(new BaseListener(QuesInfo.class) {
            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                ArrayList<QuesInfo> list = (ArrayList<QuesInfo>) listObj;
                handleResult(list);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show(errorMessage);
            }
        });
    }

    private void handleResult(ArrayList<QuesInfo> data){
        mData.clear();
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
    }
}
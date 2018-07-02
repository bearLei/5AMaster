package unit.moudle.message;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.ToastUtil;
import com.puti.education.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.api.PutiCommonModel;
import unit.entity.MessageEntity;
import unit.util.UserInfoUtils;
import unit.widget.HeadView;
import unit.widget.SpaceItemDecoration;

/**
 * Created by lei on 2018/6/7.
 * 消息列表
 */

public class MessageActivity extends PutiActivity {


    @BindView(R.id.list)
    RecyclerView VList;
    @BindView(R.id.headview)
    HeadView headview;


    private MessageAdapter mAdapter;
    private List<MessageEntity> mData;

    @Override
    public int getContentView() {
        return R.layout.puti_message_activity;
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

        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new MessageAdapter(this, mData);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        VList.setLayoutManager(manager);
        VList.setAdapter(mAdapter);

    }

    @Override
    public void Star() {
        queryData();
    }

    private void queryData() {

        PutiCommonModel.getInstance().queryMessageList( new BaseListener(MessageEntity.class) {

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
               List <MessageEntity> msgList = (List<MessageEntity>) listObj;
                handleResult(msgList);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show(errorMessage);
            }
        });
    }

    private void handleResult( List <MessageEntity> msgList) {
        if (msgList == null || msgList.size() == 0) return;

        mData.clear();
        mData.addAll(msgList);
        mAdapter.notifyDataSetChanged();
    }

}

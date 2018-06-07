package unit.moudle.message;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.listener.BaseListener;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.api.PutiCommonModel;
import unit.entity.MessageEntity;
import unit.util.UserInfoUtils;

/**
 * Created by lei on 2018/6/7.
 * 消息列表
 */

public class MessageActivity extends PutiActivity {

    private static final int Default_Size = 10;//默认查询的个数


    @BindView(R.id.list)
    LRecyclerView VList;


    private MessageAdapter mAdapter;
    private List<MessageEntity.MessageInfo> mData;
    private int index;

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

        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new MessageAdapter(this, mData);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        VList.setLayoutManager(manager);
        VList.setAdapter(mAdapter);

        VList.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
                index = 0;
                queryData();
            }

            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onBottom() {
                index = mData.size() - 1;
                queryData();
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {

            }
        });
    }

    @Override
    public void Star() {
        queryData();
    }

    private void queryData() {
        String uid = "";
        if (UserInfoUtils.isInLoginStata()) {
            uid = UserInfoUtils.getUid();
        }
        PutiCommonModel.getInstance().queryMessageList(uid, index, Default_Size, new BaseListener(MessageEntity.class) {
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                MessageEntity messageEntity = (MessageEntity) infoObj;
                handleResult(messageEntity);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show(errorMessage);
            }
        });
    }

    private void handleResult(MessageEntity messageEntity) {
        if (messageEntity == null) return;
        List<MessageEntity.MessageInfo> messageList = messageEntity.getMessageList();
        mData.clear();
        mData.addAll(messageList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}

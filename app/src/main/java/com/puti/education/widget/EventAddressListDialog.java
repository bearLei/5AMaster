package com.puti.education.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.EventAddressListAdapter;
import com.puti.education.adapter.MediaListAdapter;
import com.puti.education.bean.AudioRecord;
import com.puti.education.bean.EventAddress;

import java.util.List;

/**
 * Created by xjbin on 2017/5/16 0016.
 */

public class EventAddressListDialog extends BaseDialog {

    private List<EventAddress> mEventListAddressList;
    private Context mContext;

    public EventAddressListDialog(Context context, List<EventAddress> addressList) {
        super(context);
        this.mEventListAddressList = addressList;
        this.mContext = context;
    }

    @Override
    public int getDialogLayoutId() {
        return R.layout.dialog_event_address_list;
    }

    @Override
    public void setting() {

        RecyclerView mRv = (RecyclerView) findViewById(R.id.event_list_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        EventAddressListAdapter addressListAdapter = new EventAddressListAdapter(mContext);
        mRv.setLayoutManager(layoutManager);
        mRv.setAdapter(addressListAdapter);
        addressListAdapter.setDataList(mEventListAddressList);
        addressListAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                if (myItemOnclickListener != null){
                    myItemOnclickListener.onItemClick(position);
                }
            }
        });
    }

    public interface MyItemOnclickListener{
        void onItemClick(int position);
    }

    public MyItemOnclickListener myItemOnclickListener;

    public void setMyItemOnclickListener(MyItemOnclickListener myItemOnclickListener){
        this.myItemOnclickListener = myItemOnclickListener;
    }


}


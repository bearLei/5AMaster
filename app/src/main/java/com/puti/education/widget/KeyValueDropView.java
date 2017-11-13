package com.puti.education.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.bean.KeyValue;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.LogUtil;

import java.util.List;

/**
 * Created by xjbin on 2017/5/2 0002.
 *
 * 考虑项目下拉框比较多，特定制公用自定义下拉框
 */

public class KeyValueDropView extends PopupWindow {


    PopOnItemClickListener myOnItemClickListener = null;
    private Context mContext;
    private RecyclerView mRecycleView;
    private ListStrAdapter mPopListAdapter;

    private int itemHeight;
    int anchorViewWidth;
    int anchorViewHeight;

    public KeyValueDropView(Context context, View anchorView, List<KeyValue> dataList) {
        super(context);
        mContext = context;
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dropview_layout, null);
        setContentView(rootView);
        FrameLayout containerFrame = (FrameLayout)rootView.findViewById(R.id.container);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);

        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);

        anchorViewHeight = anchorView.getMeasuredHeight();
        anchorViewWidth = anchorView.getMeasuredWidth();
        LogUtil.i("anchorViewHeight,anchorViewWidth",anchorViewHeight+"  "+ anchorViewWidth);

        mPopListAdapter = new ListStrAdapter(mContext);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.role_recycler);

        RelativeLayout.LayoutParams frameLayoutParam = (RelativeLayout.LayoutParams)containerFrame.getLayoutParams();
        frameLayoutParam.width = anchorViewWidth-5;
        if (dataList.size() > 6){
            frameLayoutParam.height = DisPlayUtil.dip2px(mContext,40 * 6);
        }else{
            frameLayoutParam.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        containerFrame.setLayoutParams(frameLayoutParam);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mPopListAdapter);
        mPopListAdapter.setDataList(dataList);


        mPopListAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                if (myOnItemClickListener != null) {
                    myOnItemClickListener.onItemClick(position);
                }
            }
        });
    }


    public class ListStrAdapter extends BasicRecylerAdapter{

        public ListStrAdapter(Context myContext){
            super(myContext);
        }


        @Override
        public int getLayoutId() {
            return R.layout.common_dropview_item_layout;
        }

        public void refershData(List<KeyValue> lists){
            setDataList(lists);
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            KeyValue keyvalue = (KeyValue)mList.get(position);

            CommonViewHolder viewHolder = (CommonViewHolder) holder;
            TextView textView = (TextView) viewHolder.obtainView(R.id.item_name_tv);
            textView.setText(keyvalue.key);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemOnclickListener != null) {
                        myItemOnclickListener.onItemClick(position);
                    }
                }
            });
        }
    }

    public interface PopOnItemClickListener {
        void onItemClick(int position);
    }

    public void setPopOnItemClickListener(PopOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }
}

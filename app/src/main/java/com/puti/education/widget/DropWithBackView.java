package com.puti.education.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import com.puti.education.bean.EventType;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by icebery on 2017/5/2 0002.
 *
 * 类似于文件目录结构的下拉框
 */

public class DropWithBackView extends PopupWindow {


    PopOnItemClickListener mEventOnItemClickListener = null;
    private Context mContext;
    private RecyclerView mRecycleView;
    private ListStrAdapter mPopListAdapter;

    private TextView mTvEventBack;
    private List<EventType> mTopList = null;
    private Stack<EventType> mSk = new Stack<EventType>();

    private int itemHeight;
    int anchorViewWidth;
    int anchorViewHeight;

    public DropWithBackView(Context context, View anchorView, List<EventType> dataList) {
        super(context);
        mContext = context;
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dropview_with_back_layout, null);
        setContentView(rootView);
        mTvEventBack = (TextView)rootView.findViewById(R.id.tv_back);
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
        }


        containerFrame.setLayoutParams(frameLayoutParam);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mPopListAdapter);
        mPopListAdapter.setDataList(dataList);

        mTvEventBack.setClickable(false);
        mTvEventBack.setText("");

        mTopList = dataList;
        mTvEventBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSk.size() > 0) {
                    EventType tempEvent = mSk.pop();
                    if (mSk.size() <= 0) {
                        mPopListAdapter.setDataList(mTopList);
                    } else {
                        EventType parentEvent = mSk.pop();
                        mPopListAdapter.setDataList(parentEvent.child);
                    }
                }else{
                    mTvEventBack.setText(".");
                    mTvEventBack.setCompoundDrawables(null, null, null,null);

                    mPopListAdapter.setDataList(mTopList);

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

        public void refershData(List<EventType> lists){
            setDataList(lists);
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            EventType eventEntity = (EventType)mList.get(position);

            CommonViewHolder viewHolder = (CommonViewHolder) holder;
            TextView textView = (TextView) viewHolder.obtainView(R.id.item_name_tv);
            textView.setText(eventEntity.name);

            viewHolder.itemView.setTag(R.id.onclick_position, position);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posi = (int)v.getTag(R.id.onclick_position);
                    EventType entity = (EventType)mList.get(posi);
                    if (entity.child == null || entity.child.size() <= 0){
                        if (mEventOnItemClickListener != null) {
                            mEventOnItemClickListener.onItemClick(entity);
                        }
                    }else{
                        mTvEventBack.setClickable(true);
                        mTvEventBack.setText("返回");
                        Drawable tempIc = mContext.getResources().getDrawable(R.mipmap.ic_black_back);
                        tempIc.setBounds(0, 0, tempIc.getMinimumWidth(), tempIc.getMinimumHeight());
                        mTvEventBack.setCompoundDrawables(tempIc, null, null,null);
                        mTvEventBack.setCompoundDrawablePadding(DisPlayUtil.dip2px(mContext, 5));

                        mSk.push(entity);
                        setDataList(entity.child);
                    }

                }
            });
        }
    }

    public interface PopOnItemClickListener {
        void onItemClick(EventType type);
    }

    public void setPopOnItemClickListener(PopOnItemClickListener myOnItemClickListener) {
        this.mEventOnItemClickListener = myOnItemClickListener;
    }
}

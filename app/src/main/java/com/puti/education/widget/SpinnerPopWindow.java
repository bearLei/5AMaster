package com.puti.education.widget;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.PopListAdapter;
import com.puti.education.bean.Role;
import com.puti.education.R;

import java.util.List;

/**
 * Created by Administrator on 2017/4/15 0015.
 */

public class SpinnerPopWindow extends PopupWindow {

    public interface MyOnItemClickListener{
        void onItemClick(int position);
    }

    MyOnItemClickListener myOnItemClickListener = null;
    public void setMyOnItemClickListener(MyOnItemClickListener myOnItemClickListener){
        this.myOnItemClickListener = myOnItemClickListener;
    }

    private Context mContext;
    private RecyclerView mRecycleView;
    private PopListAdapter mPopListAdapter;

    public SpinnerPopWindow(Context context)
    {
        super(context);
        mContext = context;
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.role_choose_layout,null);
        setContentView(rootView);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);

        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);

        mPopListAdapter = new PopListAdapter(mContext);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.role_recycler);

        ViewGroup.LayoutParams layoutParams = mRecycleView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mRecycleView.setLayoutParams(layoutParams);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mPopListAdapter);
        mPopListAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                if (myOnItemClickListener != null){
                    myOnItemClickListener.onItemClick(position);
                }
            }
        });
    }

    public void refreshData(List<Role> list){
        mPopListAdapter.refershData(list);
    }

}

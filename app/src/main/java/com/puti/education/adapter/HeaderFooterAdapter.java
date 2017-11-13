package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2017/5/20 0020.
 */

public abstract class HeaderFooterAdapter<T> extends RecyclerView.Adapter {

    private RecyclerView mRecyclerView;

    public List<T> mList = new ArrayList<>();
    public Context mContext;

    private View VIEW_FOOTER;
    private View VIEW_HEADER;

    //Type
    private int TYPE_NORMAL = 1000;
    private int TYPE_HEADER = 1001;
    private int TYPE_FOOTER = 1002;

    public HeaderFooterAdapter(List<T> data, Context mContext) {
        this.mList = data;
        this.mContext = mContext;
    }

    @Override
    public HeaderFooterAdapter.HeadFootHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new HeadFootHolder(VIEW_FOOTER);
        } else if (viewType == TYPE_HEADER) {
            return new HeadFootHolder(VIEW_HEADER);
        } else {
            return new HeadFootHolder(getLayout(getLayoutId()));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!isHeaderView(position) && !isFooterView(position)) {
            if (haveHeaderView()) position--;
            processBindData(holder, position);
            // TextView content = (TextView) holder.itemView.findViewById(R.id.item_content);
            // TextView time = (TextView) holder.itemView.findViewById(R.id.item_time);
            // content.setText(data.get(position));
            // time.setText("2016-1-1");
        }
    }

    public abstract int getLayoutId();
    public abstract void processBindData(RecyclerView.ViewHolder holder, int position);

    public void setDataList(Collection<T> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int count = (mList == null ? 0 : mList.size());
        if (VIEW_FOOTER != null) {
            count++;
        }

        if (VIEW_HEADER != null) {
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return TYPE_HEADER;
        } else if (isFooterView(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        try {
            if (mRecyclerView == null && mRecyclerView != recyclerView) {
                mRecyclerView = recyclerView;
            }
            ifGridLayoutManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View getLayout(int layoutId) {
        return LayoutInflater.from(mContext).inflate(layoutId, null);
    }

    public void addHeaderView(View headerView) {
        if (haveHeaderView()) {
            throw new IllegalStateException("hearview has already exists!");
        } else {
            //避免出现宽度自适应
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerView.setLayoutParams(params);
            VIEW_HEADER = headerView;
            ifGridLayoutManager();
            notifyItemInserted(0);
        }

    }

    public void addFooterView(View footerView) {
        if (haveFooterView()) {
            throw new IllegalStateException("footerView has already exists!");
        } else {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(params);
            VIEW_FOOTER = footerView;
            ifGridLayoutManager();
            notifyItemInserted(getItemCount() - 1);
        }
    }

    private void ifGridLayoutManager() {
        if (mRecyclerView == null) return;
        final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager.SpanSizeLookup originalSpanSizeLookup =
                    ((GridLayoutManager) layoutManager).getSpanSizeLookup();
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeaderView(position) || isFooterView(position)) ?
                            ((GridLayoutManager) layoutManager).getSpanCount() :
                            1;
                }
            });
        }
    }

    private boolean haveHeaderView() {
        return VIEW_HEADER != null;
    }

    public boolean haveFooterView() {
        return VIEW_FOOTER != null;
    }

    private boolean isHeaderView(int position) {
        return haveHeaderView() && position == 0;
    }

    private boolean isFooterView(int position) {
        return haveFooterView() && position == getItemCount() - 1;
    }

    public interface MyItemOnclickListener{
        public void onItemClick(int position);
    }

    public MyItemOnclickListener myItemOnclickListener;

    public void setMyItemOnclickListener(MyItemOnclickListener myItemOnclickListener){
        this.myItemOnclickListener = myItemOnclickListener;
    }

    public class HeadFootHolder extends RecyclerView.ViewHolder {
        public SparseArray<View> views = new SparseArray<View>();
        public View view = null;
        public HeadFootHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public <E extends View> E obtainView(int resId){
            View v = views.get(resId);
            if(null == v){
                v = view.findViewById(resId);
                views.put(resId, v);
            }
            return (E)v;
        }

        public HeadFootHolder setText(int viewId, String text) {
            TextView tv = obtainView(viewId);
            tv.setText(text);
            return this;
        }
    }
}

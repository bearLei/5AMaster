package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.SpannableString;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 这是新建的RecyclerView 的基类适配器,减少viewholder的创建
 */
public abstract class BasicRecylerAdapter<T> extends RecyclerView.Adapter{

	public char lastChar = '\u0000';
	public int DisplayIndex = 0;

	public Context mContext;
	public LayoutInflater mLayoutinInflater = null;
	public ArrayList<T> mList = new ArrayList<T>();

	public interface  MyItemLongOnclickListener{
		void onItemLongClick(int position);
	}

	public MyItemLongOnclickListener myItemLongOnclickListener = null;
	public void setMyItemLongOnclickListener(MyItemLongOnclickListener myItemLongOnclickListener){
		this.myItemLongOnclickListener = myItemLongOnclickListener;
	}

	public interface MyItemOnclickListener{
		void onItemClick(int position);
	}
	
	public MyItemOnclickListener myItemOnclickListener;
	
	public void setMyItemOnclickListener(MyItemOnclickListener myItemOnclickListener){
		this.myItemOnclickListener = myItemOnclickListener;
	}
	
	public BasicRecylerAdapter(Context context){
		this.mContext = context;
		mLayoutinInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	@Override
	public CommonViewHolder onCreateViewHolder(ViewGroup viewgroup, int position) {
		View view  = mLayoutinInflater.inflate(getLayoutId(),viewgroup,false);
		CommonViewHolder commonViewHolder = new CommonViewHolder(view);
		return commonViewHolder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

	}

	public void setDataList(Collection<T> list) {
	        this.mList.clear();
	        this.mList.addAll(list);
	        notifyDataSetChanged();
	 }
	
	public abstract int getLayoutId();

	public class CommonViewHolder extends ViewHolder {
		public SparseArray<View> views = new SparseArray<View>();
		public View view = null;
		public CommonViewHolder(View itemView) {
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

		public CommonViewHolder setText(int viewId, String text) {
			TextView tv = obtainView(viewId);
			tv.setText(text);
			return this;
		}
		public CommonViewHolder append(int viewId, SpannableString text) {
			TextView tv = obtainView(viewId);
			tv.append(text);
			return this;
		}

	}
	
	  public void delete(int position) {
	        mList.remove(position);
	        notifyItemRemoved(position);
	        notifyItemRangeChanged(position,mList.size() - position);
	  }

	public void insertList(List<T> tempList){
		if (tempList != null && tempList.size() > 0){
			mList.addAll(tempList);
			notifyItemRangeInserted(mList.size(),tempList.size());
		}
	}

	public void insert(T t){
			mList.add(t);
			notifyItemRangeInserted(mList.size(),1);
	}

	public void update(int position){
		notifyItemChanged(position);
	}

    public void clear() {
    	mList.clear();
        notifyDataSetChanged();
    }

}

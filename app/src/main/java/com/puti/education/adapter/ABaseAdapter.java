package com.puti.education.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ABaseAdapter<E> extends BaseAdapter{
	
     Context context;
     private LayoutInflater mLayoutInlater;
     public List<E> mList = new ArrayList<E>();
     
     public interface BtnClickLintener<E>{
    	 public void call(E e, int type);
     }
     
     public BtnClickLintener btnClickLintener;
     
     public void setBtnClickLintener(BtnClickLintener btnClickLintener){
    	 this.btnClickLintener = btnClickLintener;
     }
     
     protected ABaseAdapter(Context context) {
         this.context = context;
         mLayoutInlater = LayoutInflater.from(context);
     }
     
     public List<E> getmList() {
		return mList;
	}

	public void setmList(List<E> mList) {
		this.mList = mList;
	}
	
	public void clearList(){
		this.mList.clear();
	}
	
	public void addList(List<E> list){
		if (list != null) {
			this.mList.addAll(list);
		}
		
	}

	@Override
 	 public int getCount() {
 		return mList.size();
 	 }

 	@Override
 	public E getItem(int position) {
 		return mList.get(position);
 	}

 	@Override
 	public long getItemId(int position) {
 		return position;
 	}
 
     /**
      * 各个控件的缓存
      */
     public class ViewHolder{
         public SparseArray<View> views = new SparseArray<View>();
 
         /**
          * 指定resId和类型即可获取到相应的view
         * @param convertView
          * @param resId
          * @param <T>
          * @return
          */
         public <T extends View> T obtainView(View convertView, int resId){
             View v = views.get(resId);
             if(null == v){
                 v = convertView.findViewById(resId);
                 views.put(resId, v);
             }
             return (T)v;
         }
 
     }
 
     /**
      * 改方法需要子类实现，需要返回item布局的resource id
      * @return
      */
     public abstract int itemLayoutRes();
 
     @Override
     public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder holder;
         if(null == convertView){
             holder = new ViewHolder();
             convertView =mLayoutInlater.inflate(itemLayoutRes(), null);
             convertView.setTag(holder);
         }else{
             holder = (ViewHolder) convertView.getTag();
         }
         return getView(position, convertView, parent, holder);
     }
 
     /**
      * 使用该getView方法替换原来的getView方法，需要子类实现
      * @param position
      * @param convertView
      * @param parent
      * @param holder
      * @return
      */
     public abstract View getView(int position, View convertView, ViewGroup parent, ViewHolder holder);
	 
}

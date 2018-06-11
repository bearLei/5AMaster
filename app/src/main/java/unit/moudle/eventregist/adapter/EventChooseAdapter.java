package unit.moudle.eventregist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.base.holder.ViewHolder;
import com.puti.education.bean.Practice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unit.entity.EventMainTier;
import unit.entity.EventTypeEntity;

/**
 * Created by lei on 2018/6/9.
 * 选择事件的适配器
 */

public class EventChooseAdapter extends BaseRVAdapter {

    private List<EventMainTier> mList;
    private BaseHolder mHolder;

    private Map<Integer,Boolean> pullStatusMap = new HashMap<>();
    public EventChooseAdapter(Context context) {
        super(context);
    }

    public EventChooseAdapter(Context context, List<EventMainTier> list) {
        super(context);
      this.mList = list;
    }

    @Override
    protected Object getItem(int position) {
        if (mList == null || mList.size() <= position) return null;
        return mList.get(position);
    }

    @Override
    protected int getCount() {
        if (mList  == null){
            return 0;
        }
        return mList.size();
    }

    @Override
    public void onBindViewHolder(ListHolder holder, final int position) {
        View itemView = holder.getView();
        BaseHolder itemHolder = ViewHolder.getHolderTag(itemView);
        itemHolder.setSysTemHolder(holder);
        onBindItemHolder(itemHolder, position);
        if (itemHolder instanceof EventBaseHolder) {
            ((EventBaseHolder) itemHolder).setData(
                    (EventMainTier) getItem(position),
                   getValue(position),
                    new EventBaseHolder.PullCallBack() {
                        @Override
                        public void pullDown() {
                            pullStatusMap.put(position,true);
                        }

                        @Override
                        public void pullUp() {
                            pullStatusMap.remove(position);
                        }
                    });
        } else {
            itemHolder.setData(getItem(position));
        }
    }
    @Override
    protected BaseHolder getViewHolder(Context context, ViewGroup parent, int viewType) {
        mHolder = new EventBaseHolder(context);
        return mHolder;
    }

    private boolean getValue(int position){
        if (pullStatusMap.containsKey(position)){
            return pullStatusMap.get(position);
        }else {
            return false;
        }
    }

}

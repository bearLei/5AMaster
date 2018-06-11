package unit.moudle.eventregist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.base.holder.ViewHolder;
import com.puti.education.bean.Practice;

import java.util.List;

import unit.entity.EventMainTier;
import unit.entity.EventTypeEntity;

/**
 * Created by lei on 2018/6/9.
 * 选择事件的适配器
 */

public class EventChooseAdapter extends BaseRVAdapter {

    private List<EventMainTier> mList;
    private BaseHolder mHolder;
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
    protected BaseHolder getViewHolder(Context context, ViewGroup parent, int viewType) {
        mHolder = new EventBaseHolder(context);
        return mHolder;
    }

}

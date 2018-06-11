package unit.moudle.eventregist.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;

import java.util.List;

import unit.entity.EventDetail;

/**
 * Created by lei on 2018/6/9.
 */

public class EventDetailAdapter extends BaseRVAdapter {

    private List<EventDetail> mList;
    private int type;
    private BaseHolder mHolder;
    public EventDetailAdapter(Context context) {
        super(context);
    }

    public EventDetailAdapter(Context context, List<EventDetail> mList) {
        super(context);
        this.mList = mList;
    }


    @Override
    protected Object getItem(int position) {
        if (mList == null || mList.size() <= position) return null;
        return mList.get(position);
    }

    @Override
    protected int getCount() {
        if (mList == null){
            return 0;
        }
        return mList.size();
    }

    @Override
    protected BaseHolder getViewHolder(Context context, ViewGroup parent, int viewType) {
        mHolder = new EventDetailHolder(context,type);
        return mHolder;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

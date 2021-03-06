package unit.moudle.eventdeal.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;

import java.util.ArrayList;

import unit.entity.Event;
import unit.moudle.eventdeal.holder.EventSureHolder;

/**
 * Created by lei on 2018/6/22.
 */

public class EventsSureAdapter extends BaseRVAdapter {

    private Context mContext;
    private ArrayList<Event> mData;

    public EventsSureAdapter(Context context, ArrayList<Event> mData) {
        super(context);
        this.mData = mData;
    }

    public EventsSureAdapter(Context context) {
        super(context);
    }

    @Override
    protected Object getItem(int position) {
        if (mData == null ) return null;
        return mData.get(position);
    }

    @Override
    protected int getCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    @Override
    protected BaseHolder getViewHolder(Context context, ViewGroup parent, int viewType) {
        EventSureHolder holder = new EventSureHolder(context);
        return holder;
    }
}

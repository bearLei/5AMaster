package unit.moudle.eventdeal.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;

import java.util.ArrayList;

import unit.entity.PutiEvents;
import unit.moudle.eventdeal.EventSureHolder;

/**
 * Created by lei on 2018/6/22.
 */

public class EventsSureAdapter extends BaseRVAdapter {

    private Context mContext;
    private ArrayList<PutiEvents.Event> mData;

    public EventsSureAdapter(Context context, ArrayList<PutiEvents.Event> mData) {
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

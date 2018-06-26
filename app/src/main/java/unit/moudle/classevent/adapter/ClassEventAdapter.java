package unit.moudle.classevent.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;

import java.util.ArrayList;

import unit.entity.Event;
import unit.moudle.classevent.holder.ClassEventHolder;

/**
 * Created by lei on 2018/6/26.
 */

public class ClassEventAdapter extends BaseRVAdapter {

    private Context mContext;
    private ArrayList<Event> mData;

    public ClassEventAdapter(Context context) {
        super(context);
    }

    public ClassEventAdapter(Context context, ArrayList<Event> mData) {
        super(context);
        this.mData = mData;
    }

    @Override
    protected Object getItem(int position) {
        if (mData == null) return null;
        return mData.get(position);
    }

    @Override
    protected int getCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    @Override
    protected BaseHolder getViewHolder(Context context, ViewGroup parent, int viewType) {
        ClassEventHolder holder = new ClassEventHolder(context);
        return holder;
    }
}

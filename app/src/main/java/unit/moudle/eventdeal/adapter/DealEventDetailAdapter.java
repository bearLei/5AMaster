package unit.moudle.eventdeal.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;

import java.util.ArrayList;

import unit.entity.Event2Involved;

/**
 * Created by lei on 2018/6/22.
 */

public class DealEventDetailAdapter extends BaseRVAdapter {

    private Context context;
    private ArrayList<Event2Involved> mData;


    public DealEventDetailAdapter(Context context, ArrayList<Event2Involved> mData) {
        super(context);
        this.mData = mData;
    }

    public DealEventDetailAdapter(Context context) {
        super(context);
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
        return null;
    }
}

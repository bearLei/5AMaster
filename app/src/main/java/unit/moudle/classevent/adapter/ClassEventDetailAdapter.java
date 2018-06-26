package unit.moudle.classevent.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;

import java.util.ArrayList;

import unit.entity.DealEntity;
import unit.entity.Event2Involved;
import unit.moudle.classevent.holder.ClassEventDetailHolder;
import unit.moudle.eventdeal.holder.DealEventDetailPeopleHolder;

/**
 * Created by lei on 2018/6/22.
 */

public class ClassEventDetailAdapter extends BaseRVAdapter {

    private Context context;
    private ArrayList<DealEntity> mData;


    public ClassEventDetailAdapter(Context context, ArrayList<DealEntity> mData) {
        super(context);
        this.mData = mData;
    }

    public ClassEventDetailAdapter(Context context) {
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
        ClassEventDetailHolder holder = new ClassEventDetailHolder(context);
        return holder;
    }
}

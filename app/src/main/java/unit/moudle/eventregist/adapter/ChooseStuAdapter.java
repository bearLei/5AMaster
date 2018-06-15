package unit.moudle.eventregist.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;

import java.util.ArrayList;

import unit.moudle.eventregist.entity.ChooseStuEntity;
import unit.moudle.eventregist.holder.ChooseStuDetailHolder;

/**
 * Created by lei on 2018/6/15.
 */

public class ChooseStuAdapter extends BaseRVAdapter {
    private Context mContext;
    private ArrayList<ChooseStuEntity> mList;
    private BaseHolder holder;

    public ChooseStuAdapter(Context context) {
        super(context);
    }

    public ChooseStuAdapter(Context context,ArrayList<ChooseStuEntity> mData) {
        super(context);
        this.mContext = context;
        this.mList = mData;
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
        holder = new ChooseStuDetailHolder(mContext);
        return holder;
    }
}

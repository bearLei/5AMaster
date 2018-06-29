package unit.moudle.ques;

import android.content.Context;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;

import java.util.ArrayList;

import unit.entity.QuesInfo;

/**
 * Created by lei on 2018/6/29.
 */

public class QuesAdapter extends BaseRVAdapter {


    private Context mContext;
    private ArrayList<QuesInfo> mData;


    public QuesAdapter(Context context, ArrayList<QuesInfo> mData) {
        super(context);
        this.mData = mData;
    }

    public QuesAdapter(Context context) {
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
        QuesHolder holder = new QuesHolder(context);
        return holder;
    }
}

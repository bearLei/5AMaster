package unit.moudle.reports;

import android.content.Context;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;

import java.util.ArrayList;

import unit.entity.ReportInfo;

/**
 * Created by lei on 2018/7/5.
 */

public class ParReportsAdapter extends BaseRVAdapter {

    private ArrayList<ReportInfo> mData;
    private Context mContext;

    public ParReportsAdapter(Context context) {
        super(context);
    }

    public ParReportsAdapter(Context context, ArrayList<ReportInfo> mData) {
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
        return  mData.size();
    }

    @Override
    protected BaseHolder getViewHolder(Context context, ViewGroup parent, int viewType) {
        ParReportHolder holder = new ParReportHolder(context);
        return holder;
    }
}

package unit.moudle.contacts.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;

import java.util.ArrayList;

import unit.entity.ParContactInfo;
import unit.entity.ParShowContactInfo;
import unit.entity.SchoolContactInfo;
import unit.moudle.contacts.holder.ParContactsHolder;
import unit.moudle.contacts.holder.SchoolContactsHolder;

/**
 * Created by lei on 2018/6/19.
 */

public class ParContactAdapter extends BaseRVAdapter {

    private ArrayList<ParShowContactInfo> mData;
    private BaseHolder holder;

    public ParContactAdapter(Context context) {
        super(context);
    }

    public ParContactAdapter(Context context, ArrayList<ParShowContactInfo> mData) {
        super(context);
        this.mData = mData;
    }

    @Override
    protected Object getItem(int position) {
        if (mData == null || mData.size() <= position) return null;
        return mData.get(position);
    }

    @Override
    protected int getCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    @Override
    protected BaseHolder getViewHolder(Context context, ViewGroup parent, int viewType) {
        holder = new ParContactsHolder(context);
        return holder;
    }
}

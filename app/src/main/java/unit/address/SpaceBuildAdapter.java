package unit.address;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;

import java.util.ArrayList;

/**
 * Created by lei on 2018/6/13.
 */

public class SpaceBuildAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SpaceEnitity> mData;

    public SpaceBuildAdapter(Context mContext, ArrayList<SpaceEnitity> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        if (mData == null){
            return 0;
        }
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final View rootView;
        final ViewHolder holder;

        if (convertView == null){
            rootView = InflateService.g().inflate(R.layout.puti_space_adapter_item);
            holder = new ViewHolder();
            holder.title = (TextView) rootView.findViewById(R.id.title);
            rootView.setTag(holder);
        }else {
            rootView = convertView;
            holder = (ViewHolder) rootView.getTag();
        }

        holder.title.setText(mData.get(position).getBuild());

        return rootView;
    }

    public class ViewHolder{
        TextView title;

    }
}

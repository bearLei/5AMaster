package unit.moudle.contacts.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;

import java.util.ArrayList;

import unit.entity.ContactInfo;

/**
 * Created by lei on 2018/6/19.
 */

public class SchoolContactHolderAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ContactInfo> mData;

    public SchoolContactHolderAdapter(Context context, ArrayList<ContactInfo> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        if (mData == null) return 0;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = InflateService.g().inflate(R.layout.puti_school_contact_holder_adapter_item);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.job = (TextView) convertView.findViewById(R.id.job);
            viewHolder.telePhone = (TextView) convertView.findViewById(R.id.telephone);
            viewHolder.mobile = (TextView) convertView.findViewById(R.id.mobile);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ContactInfo info = mData.get(position);
        viewHolder.name.setText(info.getName());
        viewHolder.job.setText(info.getMajor());
        viewHolder.telePhone.setText(info.getTelephone());
        viewHolder.mobile.setText(info.getMobile());
        return convertView;
    }

    public class ViewHolder{
        public TextView name,job,telePhone,mobile;
    }

}

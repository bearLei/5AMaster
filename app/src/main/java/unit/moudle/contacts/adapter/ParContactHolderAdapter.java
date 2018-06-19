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
import unit.entity.ParContactInfo;

/**
 * Created by lei on 2018/6/19.
 */

public class ParContactHolderAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ParContactInfo> mData;

    public ParContactHolderAdapter(Context context, ArrayList<ParContactInfo> mData) {
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
            convertView = InflateService.g().inflate(R.layout.puti_par_contact_holder_adapter_item);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.guardian = (TextView) convertView.findViewById(R.id.guardian);
            viewHolder.guandianMoble = (TextView) convertView.findViewById(R.id.guardian_mobile);
            viewHolder.fName = (TextView) convertView.findViewById(R.id.fname);
            viewHolder.fMobile = (TextView) convertView.findViewById(R.id.fmobile);
            viewHolder.mName = (TextView) convertView.findViewById(R.id.mname);
            viewHolder.mMobile = (TextView) convertView.findViewById(R.id.mmobile);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ParContactInfo info = mData.get(position);

        viewHolder.name.setText(info.getName());
        viewHolder.guardian.setText(info.getGuardian());
        viewHolder.guandianMoble.setText(info.getGuardianMobile());
        viewHolder.fName.setText(info.getfName());
        viewHolder.fMobile.setText(info.getfMobile());
        viewHolder.mName.setText(info.getmName());
        viewHolder.mMobile.setText(info.getmMobile());
        return convertView;
    }

    public class ViewHolder{
        public TextView name,guardian,guandianMoble,fName,fMobile,mName,mMobile;
    }

}

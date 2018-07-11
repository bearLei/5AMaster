package unit.moudle.eventdeal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.puti.education.R;
import com.puti.education.base.InflateService;

import java.util.ArrayList;
import java.util.List;

import unit.preview.PreviewActivity;

/**
 * Created by lei on 2018/7/10.
 */

public class EvidenceImageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> mData;


    public EvidenceImageAdapter(Context context, ArrayList<String> mData) {
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rootView = null;
        ViewHolder holder = null;

        if (convertView == null){
            rootView = InflateService.g().inflate(R.layout.puti_evidence_icon_item);
            holder = new ViewHolder();
            holder.icon = (SimpleDraweeView) rootView.findViewById(R.id.icon);
            rootView.setTag(holder);
        }else {
            rootView = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        holder.icon.setImageURI(mData.get(position));
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PreviewActivity.class);
                intent.putExtra("location",position);
                intent.putStringArrayListExtra("list",mData);
                context.startActivity(intent);
            }
        });
        return rootView;

    }


    public class  ViewHolder{
        SimpleDraweeView icon;
    }
}

package unit.moudle.schedule;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.InflateService;
import com.puti.education.util.ViewUtils;

import java.util.ArrayList;

import unit.entity.CursorInfo;

/**
 * Created by lei on 2018/6/28.
 */

public class CursorAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<CursorInfo> mData;

    public CursorAdapter(Context mContext, ArrayList<CursorInfo> mData) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = InflateService.g().inflate(R.layout.puti_cursor_item);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewUtils.dip2px(mContext,70));
            convertView.setLayoutParams(params);
            viewHolder.cursorName = (TextView) convertView.findViewById(R.id.cursor_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CursorInfo info = mData.get(position);
        viewHolder.cursorName.setText(info.getCourseName());
        return convertView;
    }

    public class ViewHolder{
        public TextView cursorName;
    }
}

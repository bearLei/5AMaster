package unit.moudle.record.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;

import java.util.List;

import unit.entity.StudentPortraitInfo;

/**
 * Created by lei on 2018/6/21.
 */

public class StuRecordExpressionAdapter extends BaseAdapter {

    private Context context;
    private List<StudentPortraitInfo.StudentPort> mData;

    public StuRecordExpressionAdapter(Context context, List<StudentPortraitInfo.StudentPort> mData) {
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
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;

        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = InflateService.g().inflate(R.layout.puti_record_stu_expression_item);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.score = (TextView) convertView.findViewById(R.id.score);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        StudentPortraitInfo.StudentPort studentPort = mData.get(position);
        viewHolder.title.setText(studentPort.getEventName());
        viewHolder.score.setText(String.valueOf(studentPort.getScore()));
        return convertView;
    }


    public class ViewHolder{
        public TextView title,score;
    }
}

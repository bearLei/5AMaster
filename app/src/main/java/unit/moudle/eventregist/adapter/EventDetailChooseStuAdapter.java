package unit.moudle.eventregist.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.facebook.drawee.view.SimpleDraweeView;
import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.util.ViewUtils;

import java.util.ArrayList;

import unit.entity.Student;
import unit.moudle.eventregist.ChooseStuManager;
import unit.moudle.eventregist.PutiChooseStuActivity;
import unit.moudle.eventregist.callback.OprateStuCallBack;

/**
 * Created by lei on 2018/6/17.
 */

public class EventDetailChooseStuAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Student> mData;
    private OprateStuCallBack callBack;

    public EventDetailChooseStuAdapter(Context context, ArrayList<Student> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    public EventDetailChooseStuAdapter(Context mContext, ArrayList<Student> mData, OprateStuCallBack callBack) {
        this.mContext = mContext;
        this.mData = mData;
        this.callBack = callBack;
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
      ViewHolder holder ;

        if (convertView == null){
            convertView = InflateService.g().inflate(R.layout.puti_event_detail_choosed_stu_item);
            int wid = ViewUtils.getScreenWid(mContext)-ViewUtils.dip2px(mContext,100);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(wid/4,wid/4);
            convertView.setLayoutParams(params);
            holder = new ViewHolder();
            holder.avatar = (SimpleDraweeView) convertView.findViewById(R.id.avatar);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.addLayout = (RelativeLayout) convertView.findViewById(R.id.add_layout);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Student student = mData.get(position);
        if (student.isAdd()){
            holder.avatar.setVisibility(View.GONE);
            holder.name.setVisibility(View.GONE);
            holder.addLayout.setVisibility(View.VISIBLE);
        }else {
            holder.avatar.setVisibility(View.VISIBLE);
            holder.addLayout.setVisibility(View.GONE);
            holder.name.setVisibility(View.VISIBLE);
            holder.avatar.setImageURI("");
            holder.name.setText(student.getStudentName());
        }

        holder.addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PutiChooseStuActivity.class);
                mContext.startActivity(intent);
            }
        });

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ChooseStuManager.students.contains(student) && callBack != null){
                    ChooseStuManager.students.remove(student);
                    callBack.removeStu(student);
                }
            }
        });
        return convertView;
    }

    public class ViewHolder{
        public SimpleDraweeView avatar;
        public TextView name;
        public RelativeLayout addLayout;
    }
}

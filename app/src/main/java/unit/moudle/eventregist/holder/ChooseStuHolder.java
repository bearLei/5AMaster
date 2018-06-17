package unit.moudle.eventregist.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.widget.GridViewForScrollView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.Student;
import unit.entity.StudentEntity;
import unit.moudle.eventregist.adapter.EventDetailChooseStuAdapter;

/**
 * Created by lei on 2018/6/11.
 * 事件登记详情-选择学生holder
 */

public class ChooseStuHolder extends BaseHolder<Object> {
    @BindView(R.id.gridView)
    GridViewForScrollView gridView;
    @BindView(R.id.scan)
    ImageView scan;

    private ArrayList<Student> mData;
    private EventDetailChooseStuAdapter mAdapter;
    public ChooseStuHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_event_detail_stu_holder);
        ButterKnife.bind(this, mRootView);
        if (mData == null){
            mData = new ArrayList<>();
        }
        if (mAdapter == null){
            mAdapter = new EventDetailChooseStuAdapter(mContext,mData);
        }
        gridView.setAdapter(mAdapter);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Object data) {

    }

    public void setList(ArrayList<Student> data) {
        mData.clear();
        for (int i = 0; i < data.size(); i++) {
            Student student = data.get(i);
            if (mData.contains(student)){
                continue;
            }
            mData.add(student);
        }
        addAddItem();
        mAdapter.notifyDataSetChanged();
    }


    private void addAddItem(){
        Student student = new Student();
        student.setAdd(true);
        mData.add(student);
    }

}

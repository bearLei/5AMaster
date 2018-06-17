package unit.moudle.eventdeal;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.moudle.eventdeal.ptr.EventListPtr;
import unit.moudle.eventdeal.view.EventListView;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/17.
 */

public class EventListActivity extends PutiActivity implements EventListView {
    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.class_name)
    TextView className;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private EventListPtr mPtr;
    @Override
    public int getContentView() {
        return R.layout.puti_event_list;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null){
            mPtr = new EventListPtr(this,this);
        }
    }

    @Override
    public void ParseIntent() {

    }

    @Override
    public void AttachPtrView() {

    }

    @Override
    public void DettachPtrView() {

    }

    @Override
    public void InitView() {
        headview.setCallBack(new HeadView.HeadViewCallBack() {
            @Override
            public void backClick() {
                finish();
            }
        });
        headview.setTitle("事件确认");
    }

    @Override
    public void Star() {
        mPtr.star();
    }

    @Override
    public void setDesc(String desc) {

    }

    @Override
    public void setClassName(String name) {
        className.setText(name);
    }
}

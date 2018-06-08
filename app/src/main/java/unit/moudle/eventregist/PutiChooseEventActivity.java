package unit.moudle.eventregist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.puti.education.R;
import com.puti.education.base.PutiActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.moudle.eventregist.ptr.ChooseEventPtr;
import unit.moudle.eventregist.view.ChooseEventView;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/8.
 * 事件登记-事件选择页面
 */

public class PutiChooseEventActivity extends PutiActivity implements ChooseEventView {

    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.recyclerview)
    LRecyclerView recyclerview;
    @BindView(R.id.headview)
    HeadView headview;

    private ChooseEventPtr mPtr;


    @Override
    public int getContentView() {
        return R.layout.puti_choose_event_activity;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null){
            mPtr = new ChooseEventPtr(this,this);
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

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
    }

    @Override
    public void Star() {

    }

}

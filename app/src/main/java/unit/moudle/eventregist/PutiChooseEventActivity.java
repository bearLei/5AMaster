package unit.moudle.eventregist;

import android.os.Bundle;
import android.widget.EditText;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.puti.education.R;
import com.puti.education.base.PutiActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/8.
 * 事件登记-事件选择页面
 */

public class PutiChooseEventActivity extends PutiActivity {

    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.recyclerview)
    LRecyclerView recyclerview;
    @BindView(R.id.headview)
    HeadView headview;

    @Override
    public int getContentView() {
        return R.layout.puti_choose_event_activity;
    }

    @Override
    public void BindPtr() {

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
    }

    @Override
    public void Star() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

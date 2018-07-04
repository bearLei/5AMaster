package unit.moudle.work;

import android.os.Bundle;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.moudle.work.ptr.WorkCheckPtr;
import unit.moudle.work.view.WorkCheckView;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/19.
 */

public class PutiWorkCheckActivity extends PutiActivity implements WorkCheckView {
    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.fillter_class)
    TextView fillterClass;


    private WorkCheckPtr mPtr;

    @Override
    public int getContentView() {
        return R.layout.puti_work_check_activity;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null) {
            mPtr = new WorkCheckPtr(this, this);
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
        headview.setTitle("工作检查");
    }

    @Override
    public void Star() {

    }

    @Override
    public void setClassName(String name) {
        fillterClass.setText(name);
    }


    @OnClick(R.id.fillter_class)
    public void onClick() {
        mPtr.showClassDialog(fillterClass);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void showEmptyView() {

    }
}

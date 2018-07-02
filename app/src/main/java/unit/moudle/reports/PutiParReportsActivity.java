package unit.moudle.reports;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.listener.BaseListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.api.PutiTeacherModel;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/7/2.
 */

public class PutiParReportsActivity extends PutiActivity {
    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @Override
    public int getContentView() {
        return R.layout.puti_par_report_activity;
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
        headview.setTitle("家长举报");
    }

    @Override
    public void Star() {
        queryData();
    }

    private void queryData(){
        PutiTeacherModel.getInstance().getParReports(new BaseListener());
    }
}

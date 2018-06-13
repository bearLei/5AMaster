package unit.moudle.eventregist;

import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import butterknife.BindView;
import unit.moudle.eventregist.ptr.EventDetailPtr;
import unit.moudle.eventregist.view.EventDetailView;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/11.
 * 事件注册的详情页面
 */

public class PutiChooseDetailActivity extends PutiActivity implements EventDetailView {
    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.choose_stu_layout)
    LinearLayout VChooseStuLayout;
    @BindView(R.id.time_and_place_layout)
    LinearLayout VTimeAndPlaceLayout;
    @BindView(R.id.desc_layout)
    LinearLayout VDescLayout;
    @BindView(R.id.evidence_layout)
    LinearLayout VEvidenceLayout;
    @BindView(R.id.commit)
    TextView TCommit;

    private EventDetailPtr mPtr;

    @Override
    public int getContentView() {
        return R.layout.puti_event_detail_activity;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null){
            mPtr = new EventDetailPtr(this,this);
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

    }

    @Override
    public void Star() {
        mPtr.star();
    }


    @Override
    public void addChooseStuView(View view) {
        VChooseStuLayout.removeAllViews();
        VChooseStuLayout.addView(view);
    }

    @Override
    public void addTimeAndSpaceView(View view) {
        VTimeAndPlaceLayout.removeAllViews();
        VTimeAndPlaceLayout.addView(view);
    }

    @Override
    public void addDescView(View view) {
        VDescLayout.removeAllViews();
        VDescLayout.addView(view);
    }

    @Override
    public void addEvidenceView(View view) {
        VEvidenceLayout.removeAllViews();
        VEvidenceLayout.addView(view);
    }
}

package unit.moudle.eventregist;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.util.Constant;

import butterknife.BindView;
import unit.entity.EventDetail;
import unit.moudle.eventregist.ptr.EventDetailPtr;
import unit.moudle.eventregist.view.EventDetailView;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/11.
 * 事件登记的详情页面
 */

public class PutiChooseDetailActivity extends PutiActivity implements EventDetailView {

    public static final String Parse_Intent = "Parse_Intent";

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
    private EventDetail eventDetail;
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
        if (getIntent() != null){
            eventDetail = (EventDetail) getIntent().getSerializableExtra(Parse_Intent);
        }
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
        if (eventDetail != null){
            headview.setTitle(eventDetail.getTypeName());
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case Constant.CODE_RESULT_VIDEO:
            case  Constant.CODE_RESULT_IMG_TEXT:
            case Constant.CODE_RESULT_MEDIA:
                mPtr.evidenceActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}

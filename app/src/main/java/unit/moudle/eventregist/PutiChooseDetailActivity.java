package unit.moudle.eventregist;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.util.Constant;
import com.puti.education.zxing.ZxingUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import unit.entity.EventDetail;
import unit.entity.Student;
import unit.eventbus.ChooseStuEvent;
import unit.moudle.eventregist.ptr.AddEventDetailPtr;
import unit.moudle.eventregist.view.AddEventDetailView;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/11.
 * 事件登记的详情页面
 */

public class PutiChooseDetailActivity extends PutiActivity implements AddEventDetailView {

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

    private AddEventDetailPtr mPtr;
    private EventDetail eventDetail;

    @Override
    public int getContentView() {
        return R.layout.puti_event_detail_activity;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null) {
            mPtr = new AddEventDetailPtr(this, this);
        }
    }

    @Override
    public void ParseIntent() {
        if (getIntent() != null) {
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
        if (eventDetail != null) {
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
    public String getEventType() {
        if (eventDetail != null){
            return eventDetail.getTypeUID();
        }
        return "";
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode ){
            case ZxingUtil.REQUEST_CODE:
                mPtr.onActivityResult(requestCode, resultCode, data);
                break;
            default:
                switch (resultCode) {
                    case Constant.CODE_RESULT_VIDEO:
                    case Constant.CODE_RESULT_IMG_TEXT:
                    case Constant.CODE_RESULT_MEDIA:
                        mPtr.evidenceActivityResult(requestCode, resultCode, data);
                        break;
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on3EventMainThread(ChooseStuEvent event) {
        if (event != null) {
            ArrayList<Student> list = event.getList();
            mPtr.setChooseStu(list);
        }
    }

    @OnClick(R.id.commit)
    public void onClick() {
        mPtr.addEvent();
    }

}

package unit.moudle.eventdeal.ptr;

import android.content.Context;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import unit.api.PutiCommonModel;
import unit.entity.DealEventMain;
import unit.entity.Event2Involved;
import unit.moudle.eventdeal.holder.DealEventDetailHeadHolder;
import unit.moudle.eventdeal.view.EventDetailView;

/**
 * Created by lei on 2018/6/22.
 */

public class EventDetailPtr implements BaseMvpPtr {

    private Context context;
    private EventDetailView mView;
    private DealEventDetailHeadHolder dealEventDetailHeadHolder;

    public EventDetailPtr(Context context, EventDetailView mView) {
        this.context = context;
        this.mView = mView;
    }

    @Override
    public void star() {
        initDealEventDetailHeadHolder();
        queryData(mView.getEventId());
    }

    @Override
    public void stop() {

    }

    private void initDealEventDetailHeadHolder(){
        if (dealEventDetailHeadHolder == null){
            dealEventDetailHeadHolder = new DealEventDetailHeadHolder(context);
        }
        mView.getHeadHolderView(dealEventDetailHeadHolder.getRootView());
    }

    private void queryData(final String eventId){
        PutiCommonModel.getInstance().queryEventDetail(eventId,new BaseListener(DealEventMain.class){
            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
                List<DealEventMain> list = (List<DealEventMain>) listObj;
                if (list != null && list.size() > 0){
                    handleResult(list.get(0));
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show(errorMessage);
            }
        });
    }

    private void handleResult(DealEventMain eventMain){
        dealEventDetailHeadHolder.setData(eventMain);
        mView.setTitle(String.valueOf(eventMain.getEvent2Involveds().size()));
        mView.success((ArrayList<Event2Involved>) eventMain.getEvent2Involveds());
    }


}

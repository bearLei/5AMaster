package unit.moudle.eventregist.ptr;

import android.content.Context;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.bean.EventType;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import unit.api.PutiCommonModel;
import unit.entity.EventMainTier;
import unit.entity.EventTypeEntity;
import unit.moudle.eventregist.view.ChooseEventView;

/**
 * Created by lei on 2018/6/8.
 * 事件选择页面ptr
 */

public class ChooseEventPtr implements BaseMvpPtr {

    private Context mContext;
    private ChooseEventView mView;

    public ChooseEventPtr(Context mContext, ChooseEventView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void star() {
        queryData();
    }

    @Override
    public void stop() {

    }


    private void queryData(){
        PutiCommonModel.getInstance().getEventType(new BaseListener(EventTypeEntity.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                List<EventTypeEntity> list = (List<EventTypeEntity>) listObj;
            }

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                ArrayList<EventTypeEntity> list = (ArrayList<EventTypeEntity>) listObj;
              handleResult(list);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show(errorMessage);
            }
        });
    }

    private void handleResult( List<EventTypeEntity> list){
        ArrayList<EventMainTier> data = new ArrayList();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            int indexType = list.get(i).getIndexType();
            List<EventMainTier> types = list.get(i).getTypes();
            int i1 = types.size();
            for (int j = 0; j < i1; j++) {
                types.get(j).setIndexType(indexType);
            }
            data.addAll(types);
        }
        mView.handleResult(data);
    }
}

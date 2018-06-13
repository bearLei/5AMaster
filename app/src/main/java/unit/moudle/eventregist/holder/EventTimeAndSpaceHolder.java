package unit.moudle.eventregist.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.TimeChooseUtil;
import com.puti.education.util.TimeUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.address.SpaceChooseDialog;
import unit.address.SpaceEnitity;
import unit.api.PutiCommonModel;

/**
 * Created by lei on 2018/6/11.
 * 事件详情-事件和地点选择holder
 */

public class EventTimeAndSpaceHolder extends BaseHolder<Object> implements View.OnClickListener {

    @BindView(R.id.time_desc)
    TextView timeDesc;
    @BindView(R.id.time_layout)
    LinearLayout timeLayout;
    @BindView(R.id.space)
    TextView space;
    @BindView(R.id.edit_space)
    EditText editSpace;
    @BindView(R.id.choose_space)
    ImageView chooseSpace;
    @BindView(R.id.space_layout)
    RelativeLayout spaceLayout;


    private SpaceChooseDialog dialog ;
    private ArrayList<SpaceEnitity> mData;
    public EventTimeAndSpaceHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_event_detail_time_and_space_holder);
        ButterKnife.bind(this, mRootView);
        timeLayout.setOnClickListener(this);
        spaceLayout.setOnClickListener(this);
        if (dialog == null){
            dialog = new SpaceChooseDialog(mContext);
        }

        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Object data) {
        //时间默认当前时间
        String nowTime = TimeUtils.getCurrentTime();
        timeDesc.setText(nowTime);

        PutiCommonModel.getInstance().getAddress(0,new BaseListener(SpaceEnitity.class){

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
               mData = (ArrayList<SpaceEnitity>) listObj;
                handleResult(mData);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
            }
        });
    }
    private void handleResult(ArrayList<SpaceEnitity> data){
        dialog.setSpaceData(data, new SpaceChooseDialog.ChooseSpaceCallBack() {
            @Override
            public void callBack(String space) {
                editSpace.setText(space);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.time_layout:
                TimeChooseUtil chooseUtil = new TimeChooseUtil();
                chooseUtil.showTimeDialog(mContext,timeDesc,false);
                break;
            case R.id.space_layout:
                if (dialog != null && !dialog.isShowing()){
                    dialog.show();
                }
                break;
        }
    }
}

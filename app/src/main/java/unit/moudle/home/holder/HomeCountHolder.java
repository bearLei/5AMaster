package unit.moudle.home.holder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.listener.BaseListener;
import com.puti.education.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.api.PutiCommonModel;
import unit.entity.HomeCountEntity;
import unit.moudle.personal.PersonalActivity;
import unit.moudle.work.PutiWorkCheckActivity;
import unit.util.UserInfoUtils;

/**
 * Created by lei on 2018/6/6.
 * 首页用户统计信息
 */

public class HomeCountHolder extends BaseHolder<HomeCountEntity>{

    @BindView(R.id.week_new_event)
    TextView weekNewEvent;
    @BindView(R.id.my_post)
    TextView myPost;
    @BindView(R.id.forward_personal_info)
    LinearLayout VForwardPersonalInfo;

    public HomeCountHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_home_count_holder);
        ButterKnife.bind(this,mRootView);
        VForwardPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PutiWorkCheckActivity.class);
                mContext.startActivity(intent);
            }
        });
        weekNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PutiWorkCheckActivity.class);
                mContext.startActivity(intent);
            }
        });
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, HomeCountEntity data) {

       if (data == null){
           return;
       }
        weekNewEvent.setText(String.valueOf(data.getSchoolWeekCount()));
        myPost.setText(String.valueOf(data.getMyWriteCount()));

    }


    //教师端查询统计信息
    public void queryData() {
        if (UserInfoUtils.isInLoginStata()) {
            PutiCommonModel.getInstance().queryCountInfo(
                    new BaseListener(HomeCountEntity.class){
                        @Override
                        public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                            super.responseResult(infoObj, listObj, code, status);
                            HomeCountEntity entity = (HomeCountEntity) infoObj;
                            updateUI(mContext,entity);
                        }

                        @Override
                        public void requestFailed(boolean status, int code, String errorMessage) {
                            super.requestFailed(status, code, errorMessage);
                            ToastUtil.show(errorMessage);
                        }
                    });
        }
    }
}

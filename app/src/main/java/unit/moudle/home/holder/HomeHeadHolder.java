package unit.moudle.home.holder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.ImgLoadUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.entity.UserBaseInfo;
import unit.moudle.message.MessageActivity;
import unit.util.UserInfoUtils;

/**
 * Created by lei on 2018/6/6.
 * 首页顶部用户信息holder
 */

public class HomeHeadHolder extends BaseHolder<Object> {


    @BindView(R.id.head_icon)
    ImageView headIcon;
    @BindView(R.id.nick_name)
    TextView nickName;
    @BindView(R.id.school_name)
    TextView schoolName;
    @BindView(R.id.red_dog)
    ImageView redDog;
    @BindView(R.id.role_name)
    TextView roleName;
    @BindView(R.id.forward_msg_list)
    FrameLayout VForwardMsgList;


    public HomeHeadHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_home_headview);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Object data) {
        if (UserInfoUtils.isInLoginStata()) {
            UserBaseInfo userInfo = UserInfoUtils.getUserInfo();
            ImgLoadUtil.displayPic(R.mipmap.ic_avatar_default, userInfo.getAvatar(), headIcon);
            nickName.setText(userInfo.getRealName());
            roleName.setText(userInfo.getRole());
            schoolName.setText(userInfo.getSchoolName());

        } else {
            ImgLoadUtil.displayPic(R.mipmap.ic_avatar_default, "", headIcon);
            nickName.setText("");
            roleName.setText("");
            schoolName.setText("");
        }
    }

    //跳转消息列表
    @OnClick(R.id.forward_msg_list)
    public void onClick() {
        Intent intent = new Intent(mContext, MessageActivity.class);
        mContext.startActivity(intent);
    }
}

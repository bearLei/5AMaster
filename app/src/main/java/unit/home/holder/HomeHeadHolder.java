package unit.home.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.RolePower;
import unit.entity.UserBaseInfo;
import unit.entity.UserInfo;
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
            UserBaseInfo  userInfo = UserInfoUtils.getUserInfo();
                ImgLoadUtil.displayPic(R.mipmap.ic_avatar_default, userInfo.getAvatar(), headIcon);
                nickName.setText(userInfo.getRealName());
                roleName.setText(userInfo.getRole());
                schoolName.setText(userInfo.getSchoolName());

        }else {
            ImgLoadUtil.displayPic(R.mipmap.ic_avatar_default, "", headIcon);
            nickName.setText("");
            roleName.setText("");
            schoolName.setText("");
        }
    }
}

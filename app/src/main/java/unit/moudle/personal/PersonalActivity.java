package unit.moudle.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.baidu.platform.comapi.map.F;
import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.widget.EduDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.entity.UserBaseInfo;
import unit.util.UserInfoUtils;
import unit.widget.HeadView;
import unit.widget.SettingItem;

/**
 * Created by lei on 2018/6/7.
 * 个人信息页面
 */

public class PersonalActivity extends PutiActivity implements PersonView {

    @BindView(R.id.head_icon)
    ImageView headIcon;
    @BindView(R.id.trouble_help)
    SettingItem troubleHelp;
    @BindView(R.id.current_version)
    SettingItem currentVersion;
    @BindView(R.id.headview)
    HeadView headview;


    private PersonPtr mPtr;

    @Override
    public int getContentView() {
        return R.layout.puti_personal_activity;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null) {
            mPtr = new PersonPtr(this, this);
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
        if (UserInfoUtils.isInLoginStata()) {
            UserBaseInfo userInfo = UserInfoUtils.getUserInfo();
            if (userInfo != null) {
                ImgLoadUtil.displayPic(
                        R.mipmap.ic_avatar_default,
                        userInfo.getAvatar(),
                        headIcon);
                headview.setTitle(userInfo.getRealName());
            }
        }
        headview.setCallBack(new HeadView.HeadViewCallBack() {
            @Override
            public void backClick() {
                finish();
            }
        });
        currentVersion.updateBrief(mPtr.getVersionName());
        troubleHelp.updateBrief(getString(R.string.puti_person_trouble_phone));
    }


    @OnClick({ R.id.head_icon, R.id.qr_code, R.id.invite_use, R.id.feed_back, R.id.evaluate, R.id.update_psw, R.id.trouble_help, R.id.current_version, R.id.lgout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_icon:
                mPtr.updateAvatar();
                break;
            case R.id.qr_code:
                mPtr.getQrCode();
                break;
            case R.id.invite_use:
                mPtr.inviteUse();
                break;
            case R.id.feed_back:
                mPtr.feedBack();
                break;
            case R.id.evaluate:
                mPtr.evaluate();
                break;
            case R.id.update_psw:
                mPtr.updatePsw();
                break;
            case R.id.trouble_help:
                mPtr.troubleHelp();
                break;
            case R.id.current_version:
                mPtr.currentCode();
                break;
            case R.id.lgout:
                mPtr.ReqlogoutDialog();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPtr.onActivityResult(requestCode, resultCode, data, headIcon);
    }

    @Override
    public void updateAvatar(String path) {
        ImgLoadUtil.displayPic(
                R.mipmap.ic_avatar_default,
                path,
                headIcon);
        UserInfoUtils.setAvatar(path);
    }

}

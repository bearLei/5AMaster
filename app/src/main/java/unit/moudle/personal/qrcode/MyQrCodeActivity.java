package unit.moudle.personal.qrcode;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.util.ImgLoadUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.UserBaseInfo;
import unit.entity.UserInfo;
import unit.location.LocationManager;
import unit.location.MapEvent;
import unit.util.UserInfoUtils;

/**
 * Created by lei on 2018/6/8.
 * 我的二维码名片
 */

public class MyQrCodeActivity extends PutiActivity implements View.OnClickListener {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.qr_code)
    ImageView qrCode;

    @Override
    public int getContentView() {
        return R.layout.puti_my_code_activity;
    }

    @Override
    public void BindPtr() {

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
        back.setOnClickListener(this);
    }

    @Override
    public void Star() {
        if (UserInfoUtils.isInLoginStata()){
            UserBaseInfo userInfo = UserInfoUtils.getUserInfo();
            name.setText(userInfo.getRealName());
            LocationManager.g().requestGpsPermissions(this);
            ImgLoadUtil.displayPic(R.mipmap.ic_avatar_default,userInfo.getAvatar(),avatar);
            ImgLoadUtil.displayPic(0,userInfo.getQrCode(),qrCode);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * 定位信息返回
     *
     * @param mapEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on3EventMainThread(MapEvent mapEvent) {
        if (mapEvent.isSuccess){
            BDLocation bdLocation = mapEvent.bdLocation;
            String city = bdLocation.getCity();
           location.setText(city);
        }
    }

}

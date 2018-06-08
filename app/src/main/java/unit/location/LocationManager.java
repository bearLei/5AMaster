package unit.location;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;

import com.puti.education.App;
import com.puti.education.ui.uiCommon.LoginActivity;
import com.puti.education.util.ToastUtil;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import unit.permission.PermissionUtil;

/**
 * Created by lei on 2018/6/3.
 * 百度地图管理类
 */

public class LocationManager {

    private LocationHelper mLocationHelper;

    private static class Instance {
        public static LocationManager singleInstance = new LocationManager();
    }

    public LocationManager() {
    }

    public static LocationManager g() {
        return Instance.singleInstance;
    }


    /**
     * 开启GPS权限
     */
    public void requestGpsPermissions(final Activity activity) {
        PermissionUtil.g().requestPermissions(activity,
                new PermissionUtil.PermissionCallBack() {
                    @Override
                    public void success() {
                        if (null == mLocationHelper) {
                            mLocationHelper = new LocationHelper();
                        }
                        mLocationHelper.getLocation();
                    }
                    @Override
                    public void fail() {
                        ToastUtil.show("获取定位权限失败");
                    }
                },
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO);

        PermissionUtil.g().setRationaleListener(new RationaleListener() {
            @Override
            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                PermissionUtil.g().showRequestPermissionRationaleDialog(activity,
                        "你已拒绝过定位权限，沒有定位定位权限无法精确发送求助信息！",
                        rationale);
            }
        });
    }

}
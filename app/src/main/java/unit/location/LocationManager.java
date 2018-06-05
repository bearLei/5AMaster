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

    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            AlertDialog.newBuilder(App.getContext())
                    .setTitle("友好提醒")
                    .setMessage("你已拒绝过定位权限，沒有定位定位权限无法精确发送求助信息！")
                    .setPositiveButton("打开", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            rationale.resume();
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            rationale.resume();
                        }
                    }).show();
        }

    };
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 200) {
                if (null == mLocationHelper) {
                    mLocationHelper = new LocationHelper();
                }
                mLocationHelper.getLocation();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                ToastUtil.show("获取定位权限失败");
            }
        }
    };

    /**
     * 开启GPS权限
     */
    public void requestGpsPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            AndPermission.with(activity)
                    .requestCode(200)
                    .permission(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.RECORD_AUDIO)
                    .rationale(rationaleListener)
                    .callback(listener)
                    .start();
        } else {
            if (null == mLocationHelper) {
                mLocationHelper = new LocationHelper();
            }
            mLocationHelper.getLocation();
        }
    }

}
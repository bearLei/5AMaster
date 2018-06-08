package unit.permission;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;

import com.puti.education.App;
import com.puti.education.util.ToastUtil;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import java.util.List;

/**
 * Created by lei on 2018/6/7.
 */

public class PermissionUtil {

    private PermissionUtil mPermission;
    private PermissionCallBack mCallBack;
    private RationaleListener rationaleListener;
    private static class Instance {
        public static PermissionUtil singleInstance = new PermissionUtil();
    }

    public PermissionUtil() {
    }

    public static PermissionUtil g() {

        return Instance.singleInstance;
    }

    public void setRationaleListener(RationaleListener rationaleListener) {
        this.rationaleListener = rationaleListener;
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            if (requestCode == 200 && mCallBack != null) {
                mCallBack.success();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200 && mCallBack != null) {
                mCallBack.fail();
            }
        }
    };
    /**
     * 开启权限
     */
    public void requestPermissions(Activity activity
                                   ,PermissionCallBack callBack,
                                   String... permissions) {
        this.mCallBack = callBack;
        if (Build.VERSION.SDK_INT >= 23) {
            AndPermission.with(activity)
                    .requestCode(200)
                    .permission(permissions)
                    .rationale(rationaleListener)
                    .callback(listener)
                    .start();
        } else if (mCallBack != null){
            mCallBack.success();
        }
    }

    public interface PermissionCallBack{
        void success();
        void fail();
    }

    public void showRequestPermissionRationaleDialog(Activity activity,String msg,final Rationale rationale){
        AlertDialog.newBuilder(activity)
                .setTitle("友好提醒")
//                    .setMessage("你已拒绝过定位权限，沒有定位定位权限无法精确发送求助信息！")
                .setMessage(msg)
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
}

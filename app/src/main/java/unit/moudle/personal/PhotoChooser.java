package unit.moudle.personal;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.puti.education.R;
import com.puti.education.util.FileUtils;
import com.puti.education.util.ToastUtil;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import unit.permission.PermissionUtil;
import unit.widget.PutiListDialog;

/**
 * 拍照或从相册中选择图片
 */
public class PhotoChooser {

    public static final int REQUEST_CAMERA = 11001;// 拍照
    public static final int REQUEST_ALBUM = 11002; // 相册

    // 保存拍照、裁剪后产生的临时图片的目录路径
    public static String TEMP_PHOTO_DIR = FileUtils.getTempDirPath() + File.separator;
    private static String clipedPhotoPath = "";// 裁剪后的图片路径
    private static String tempPhotoPath = "";// 临时图片路径

    private Activity mActivity;
    private Fragment mFragment;

    private PutiListDialog popUpDialog;
    private int max;

    //------------------------------Constructor------------------------------
    public PhotoChooser(Activity context) {
        this(context, null);
    }

    public PhotoChooser(Activity context, Fragment fragment) {
        mActivity = context;
        mFragment = fragment;
    }

    public PhotoChooser(Activity mActivity, int max) {
        this.mActivity = mActivity;
        this.max = max;
    }
//------------------------------Public------------------------------

    /**
     * 拍照
     */
    public void startCamera() {
        PermissionUtil.g().requestPermissions(mActivity,
                new PermissionUtil.PermissionCallBack() {
                    @Override
                    public void success() {
                        startCameraActivity();
                    }

                    @Override
                    public void fail() {
                        ToastUtil.show("请先开启相册权限");
                    }
                },
                Manifest.permission.CAMERA);

        PermissionUtil.g().setRationaleListener(new RationaleListener() {
            @Override
            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                PermissionUtil.g().showRequestPermissionRationaleDialog(mActivity,
                        "你已拒绝过相机权限，沒有相机权限无法打开相机！",
                        rationale);
            }
        });
    }

    private void startCameraActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getCameraTempFilePath());
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * 相册
     */
    @TargetApi(19)
    public void startAlbum() {
        PermissionUtil.g().requestPermissions(mActivity,
                new PermissionUtil.PermissionCallBack() {
                    @Override
                    public void success() {
                        startAlbumActivity();
                    }

                    @Override
                    public void fail() {
                        ToastUtil.show("请先开启存储权限");
                    }
                },Manifest.permission.WRITE_EXTERNAL_STORAGE);
        PermissionUtil.g().setRationaleListener(new RationaleListener() {
            @Override
            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                PermissionUtil.g().showRequestPermissionRationaleDialog(mActivity,
                        "你已拒绝过存储权限，沒有存储权限无法打开相册！",
                        rationale);
            }
        });
    }

    private void startAlbumActivity() {
        PhotoPickerIntent intent = new PhotoPickerIntent(mActivity);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照
        intent.setMaxTotal(max); // 最多选择照片数量，默认为9
        startActivityForResult(intent, REQUEST_ALBUM);
    }

    /**
     * 拍照、相册 选择对话框
     */
    public void chooseDialog(ChooseCallBack chooseCallBack) {
        this.mCallBack = chooseCallBack;
        clipedPhotoPath = "";
        //---------------------------------------------------替换对话框---------------------------------------------
        PutiListDialog.Builder dialogBuilder = new PutiListDialog.Builder(mActivity);
        dialogBuilder.setItems(new String[]{
                mActivity.getString(R.string.puti_camera),
                mActivity.getString(R.string.puti_album)});
        dialogBuilder.setOnItemClickListener(new PutiListDialog.OnItemClickListener() {
            @Override
            public void onItemClick(int which) {
                if (which == 0) {
                    startCamera();
                    dismissDialog();
                } else if (which == 1) {
                    startAlbum();
                    dismissDialog();
                }
            }
        });
        popUpDialog =  dialogBuilder.create();
        popUpDialog.show();
    }
    /**关闭弹窗*/
    public void dismissDialog() {
        if ( popUpDialog!= null) {
            popUpDialog.dismiss();
            popUpDialog = null;
        }
    }

    public void onActivityResult(Activity context, int requestCode, int resultCode, Intent data) {
        onActivityResult(context, requestCode, resultCode, data, null);

    }

    public void onActivityResult(Activity context, int requestCode, int resultCode, Intent data, ImageView imageView) {
        if (resultCode != Activity.RESULT_OK)
            return;
        try {
            switch (requestCode) {
                case PhotoChooser.REQUEST_CAMERA:
                    try {
                        String srcFilePath = "";
                        srcFilePath = getPhotoPath(context, data);
                        if(TextUtils.isEmpty(srcFilePath)){
                            srcFilePath = getPhotoPathAgain(data, srcFilePath);
                        }
                        if(TextUtils.isEmpty(srcFilePath)){
                            srcFilePath = tempPhotoPath;
                        }
                        if (!TextUtils.isEmpty(srcFilePath)){
                            srcFilePath = URLDecoder.decode(srcFilePath);
                        }
                        if (mCallBack != null){
                            mCallBack.chooseCamer(srcFilePath);
                        }
                    } catch (Exception ex) {

                    } catch (OutOfMemoryError ex) {

                    }
                    break;
                case PhotoChooser.REQUEST_ALBUM:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    if (mCallBack != null){
                        mCallBack.chooseAlbum(list);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
        }
    }

    private String getPhotoPathAgain(Intent data, String srcFilePath) {
        if (data!=null){
            Uri uri = getUri(data);
            if(uri!=null){
                srcFilePath = uri2StrPath(mActivity, uri);
            }
        }
        return srcFilePath;
    }

    private String getPhotoPath(Activity context, Intent data) {
        if(data == null || data.getData() == null){
            return null;
        }
        String srcFilePath;
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            srcFilePath = cursor.getString(columnIndex);
            cursor.close();
        } else {
            srcFilePath = selectedImage.getPath();
        }
        return srcFilePath;
    }

    private Bitmap bitmap;

    //------------------------------Private------------------------------
    private void startActivityForResult(Intent intent, int resultCode) {
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            try{
                if (mFragment != null) {
                    mFragment.startActivityForResult(intent, resultCode);
                } else {
                    mActivity.startActivityForResult(intent, resultCode);
                }
            }catch (Exception e){

            }
        } else {

        }

    }

    /**
     * 拍照后保存的图片路径
     */
    private Uri getCameraTempFilePath() {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(TEMP_PHOTO_DIR);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            Toast.makeText(mActivity, "保存图像失败！", Toast.LENGTH_SHORT).show();
            return null;
        }

        // 照片命名
        tempPhotoPath = TEMP_PHOTO_DIR + String.valueOf(System.currentTimeMillis()) + ".jpg";
        // 裁剪头像的绝对路径
        return FileUtils.toUri(mActivity,tempPhotoPath);
    }

    /**
     * 获取裁剪后的图片路径
     */
    public static String getClipedPhotoPath() {
        if (TextUtils.isEmpty(clipedPhotoPath)) {
            clipedPhotoPath = TEMP_PHOTO_DIR + String.valueOf(System.currentTimeMillis()) + ".jpg";

        }
        return PhotoChooser.clipedPhotoPath;
    }

    public static void setClipedPhotoPath(String clipedPhotoPath) {
        PhotoChooser.clipedPhotoPath = clipedPhotoPath;
    }

    public static void setTempPhotoPath(String tempPhotoPath) {
        PhotoChooser.tempPhotoPath = tempPhotoPath;
    }

    public static String getTempPhotoDir() {
        if (TextUtils.isEmpty(tempPhotoPath)) {
            tempPhotoPath = TEMP_PHOTO_DIR + String.valueOf(System.currentTimeMillis()) + ".jpg";
        }
        return PhotoChooser.tempPhotoPath;
    }

    private String uri2StrPath(Activity context, Uri uri) {
        if (uri.toString().startsWith("file:///")) {
            return uri.toString().replace("file://", "");
        }

        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * 获取uri
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Uri getUri(Intent data) {
        Uri uri;
        if (!TextUtils.isEmpty(clipedPhotoPath)) {
            uri = Uri.fromFile(new File(clipedPhotoPath));
        } else {
            uri = data.getData();
            if(uri == null){
                return uri;
            }
            if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(mActivity, uri)) {    //android 4.4开始的新方法
                String wholeID = DocumentsContract.getDocumentId(uri);
                String id = wholeID.split(":")[1];
                String[] column = {MediaStore.Images.Media.DATA};
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cursor = mActivity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
                        sel, new String[]{id}, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    String filePath = cursor.getString(columnIndex);
                    uri = Uri.fromFile(new File(filePath));
                }
                cursor.close();
            }
        }
        return uri;
    }

    private ChooseCallBack mCallBack;
    public interface ChooseCallBack{
        void chooseCamer(String path);
        void chooseAlbum(ArrayList<String> list);
    }


}

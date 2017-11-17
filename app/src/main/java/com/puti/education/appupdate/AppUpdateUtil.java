package com.puti.education.appupdate;//package com.puti.education.appupdate;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.Uri;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.puti.education.R;
//
//import java.io.File;
//import java.util.Random;
//
///**
// * Created by lei on 2017/11/11.
// *检查更新的类
// */
//public class AppUpdateUtil {
//
//
//    public static PopUpDialog popUpDialog;
//    private static ImageView cancle;
//    private static View update;
//    private static boolean comfirmed = false;
//    private static RelativeLayout pop_outSideClick;
//
//
//    public static void update(Context context, String StrFile) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(Uri.fromFile(new File(StrFile)),
//                "application/vnd.android.package-archive");
//        context.startActivity(intent);
//    }
//
//
//    public void queryVersion(){
//
//    }
//
//
//
//    public static void getPopDialog(final Context mContext) {
//        if (popUpDialog == null) {
//            popUpDialog = PopUpDialog.create(mContext, new PopUpDialog.DialogBuilder() {
//                @Override
//                public int getContent() {
//                    return R.layout.popdialog_appupdate;
//                }
//
//                @Override
//                public void getView(View v) {
//                    cancle = (ImageView) v.findViewById(R.id.pop_delect);
//                    update = v.findViewById(R.id.pop_update);
//                    pop_outSideClick = (RelativeLayout) v.findViewById(R.id.pop_outside);
//                    pop_outSideClick.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (popUpDialog!=null){
//                                popUpDialog.dismiss();
//                                popUpDialog=null;
//                            }
//                        }
//                    });
//                    TextView version = (TextView) v.findViewById(R.id.pop_version);
//                    TextView content = (TextView) v.findViewById(R.id.pop_content_textview);
//                }
//            });
//        }
//        comfirmed = false;
//        if (AppUpdate.s_force_update == true) {
//            pop_outSideClick.setEnabled(false);
//            cancle.setVisibility(View.GONE);
//            popUpDialog.setCancelable(false);
//            update.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    comfirmed = true;
//                    if (AppUpdate.s_AppUpdateUrl.size()>0) {
//                        new ApkDownloadTask(
//                                mContext)
//                                .execute(AppUpdate.s_AppUpdateUrl
//                                        .get(Math
//                                                .abs(new Random()
//                                                        .nextInt())
//                                                % AppUpdate.s_AppUpdateUrl
//                                                .size()));
//                    }
//                    if (popUpDialog!=null){
//                        popUpDialog.dismiss();
//                        popUpDialog=null;
//                    }
//
//                }
//            });
//        } else {
//            pop_outSideClick.setEnabled(true);
//            cancle.setVisibility(View.VISIBLE);
//            DataStorage.setVersionUpdateTime();
//            cancle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (popUpDialog!=null){
//                        popUpDialog.dismiss();
//                        popUpDialog=null;
//                    }
//                }
//            });
//            update.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TCUtils.onEvent(IReportEventID.APP_UPDATE,mContext.getString(R.string.confirm));
//                    comfirmed = true;
//                    if (AppUpdate.s_AppUpdateUrl.size()>0) {
//                        new ApkDownloadTask(
//                                mContext)
//                                .execute(AppUpdate.s_AppUpdateUrl
//                                        .get(Math
//                                                .abs(new Random()
//                                                        .nextInt())
//                                                % AppUpdate.s_AppUpdateUrl
//                                                .size()));
//                    }
//                    if (popUpDialog!=null){
//                        popUpDialog.dismiss();
//                        popUpDialog=null;
//                    }
//                }
//            });
//        }
//        popUpDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                if(!comfirmed){
//                }
//            }
//        });
//        if (popUpDialog != null && !popUpDialog.isShowing()) {
//            popUpDialog.show();
//        }
//    }
//
//}

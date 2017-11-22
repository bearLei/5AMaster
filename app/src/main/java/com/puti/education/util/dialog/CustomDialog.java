package com.puti.education.util.dialog;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2016/10/18.
 */
public class CustomDialog {

    /**大部分还是简单的Dialog  所以提供2个接口外界实现*/
    private PositiveCallBack mPositiveCallBack;
    private NavigationCallBack mNegativeCallBack;

    private  final String TAG="CustomDialog";
    private  List<String> itemList;
    private   DialogWindow dialog;
    private Activity activity;

    public CustomDialog(Activity activity) {
        this.activity = activity;
    }

    /**单个按钮  居中的弹窗
     * @param title  标题
     * @param msg 消息正文
     * @param positive 填充的单个文本的按钮
     *  @param  callBack          按钮实现的接口
     * */
    public  void showSingleDialog(  String title, String msg, String positive , final PositiveCallBack callBack){

        if (dialog==null){
            dialog=new DialogWindow(activity);
        }
        if (itemList==null){
            itemList=new ArrayList<>();
        }
        itemList.clear();
        itemList.add(positive);
        dialog.initData(title,msg,null,itemList, DialogWindow.Style.Alert);
        dialog.setOnItemClickListener(new OnDialogItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                callBack.callBack();
            }
        });

        show(activity);
    }
    public  void showDoubleDialog(int icon, String title, String msg, final String positive, final String navigation, final PositiveCallBack positiveCallBack, final NavigationCallBack navigationCallBack){
        if (dialog==null){
            dialog=new DialogWindow(activity);
        }
        if (itemList==null){
            itemList=new ArrayList<>();
        }
        itemList.clear();
        itemList.add(positive);
        itemList.add(navigation);
        dialog.initData(title,msg,null,itemList, DialogWindow.Style.Alert);
        dialog.setMsgIconRedId(icon);
        dialog.setOnItemClickListener(new OnDialogItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                switch (position) {
                    case 0:
                        if (positiveCallBack != null)
                            positiveCallBack.callBack();
                        break;
                    case 1:
                        if (navigationCallBack != null)
                            navigationCallBack.callBack();
                        break;
                }
            }
        } );
        show(activity);
    }

    /**2个按钮  居中的弹窗
     * @param title  标题
     * @param msg 消息正文
     * @param positive 左边按钮的文本
     * @param navigation  右边按钮的文本
     *  @param  positiveCallBack          左边按钮实现的接口
     *  @param navigationCallBack 右边按钮实现的接口
     * */
    public  void showDoubleDialog(String title, String msg, final String positive, final String navigation, final PositiveCallBack positiveCallBack, final NavigationCallBack navigationCallBack){
        showDoubleDialog(0, title, msg, positive, navigation, positiveCallBack, navigationCallBack);
    }

    /**底部弹窗
     * @param title  标题
     * @param msg 消息正文
     * @param  cancle 底部的取消按钮，如果填充 如果填充则充当一项item  并且position永远置为-1
     * @param  list 填充 弹窗的item选项
     * @param onDialogItemClickListener 实现各item点击事件的接口
     * */
    public  void showBottomDialog(String title,String msg,String cancle,List<String> list,OnDialogItemClickListener onDialogItemClickListener){
        if (dialog==null){
            dialog=new DialogWindow(activity);
        }
        if (itemList==null){
            itemList=new ArrayList<>();
        }
        itemList.clear();
        itemList.addAll(list);
        dialog.initData(title,msg,cancle,itemList, DialogWindow.Style.ActionSheet);
        dialog.setOnItemClickListener(onDialogItemClickListener);
        show(activity);
    }

    /**只需要传递item列表的底部弹窗
     *不需要取消按钮
     * */
    public void showBottomDialog(List<String> list ,OnDialogItemClickListener onDialogItemClickListener){
        this.showBottomDialog(null,null,null,list,onDialogItemClickListener);
    }

    /**
     * 需要取消按钮的底部弹窗
     * 注意：如果有取消按钮，那么取消按钮的postion一直都是-1 在监听时候需要对case -1 做个处理
     * */
    public void showBottomDialog(String cancle,List<String> list ,OnDialogItemClickListener onDialogItemClickListener){
        this.showBottomDialog(null,null,cancle,list,onDialogItemClickListener);
    }
    public  void show(Activity activity) {
        if (activity == null || activity.isFinishing()){
            return;
        }
        try {
            activity.getFragmentManager().executePendingTransactions();
            if (!dialog.isAdded() && !dialog.isResumed()) {
                dialog.show(activity.getFragmentManager(), TAG);
            }
        }catch (IllegalStateException e){
        }
    }
    public void hide(){
        if (dialog != null && dialog.isResumed()){
            dialog.dismiss();
        }
    }
    /**静态方法*/
    public static void showSingleDialog(Activity activity,String content,String buttonTV){
        final CustomDialog customDialog=new CustomDialog(activity);
        customDialog.showSingleDialog("", content, buttonTV,new PositiveCallBack() {
            @Override
            public void callBack() {
                customDialog.hide();
            }
        });
    }
    public static void showSingleDialog(Activity activity,String content,String buttonTV,PositiveCallBack callBack){
        CustomDialog customDialog=new CustomDialog(activity);
        customDialog.showSingleDialog("",content,buttonTV,callBack);
    }
    public static void showSingleDialog(Activity activity,String title,String content,String buttonTV,PositiveCallBack callBack){
        CustomDialog customDialog=new CustomDialog(activity);
        customDialog.showSingleDialog(title,content,buttonTV,callBack);
    }

    public static void showSingleDialog(boolean enableCancel, Activity activity,String title,String content,String buttonTV,PositiveCallBack callBack){
        CustomDialog customDialog=new CustomDialog(activity);
        customDialog.showSingleDialog(title,content,buttonTV,callBack);
        customDialog.setOutSideClickDissMiss(enableCancel);
    }

    public static void showDoubleDialog(boolean enableCancel, Activity activity, String title, String conetnt, String lefeButtonTV, String rightButtonTV, final PositiveCallBack positiveCallBack, final NavigationCallBack navigationCallBack){
        CustomDialog customDialog=new CustomDialog(activity);
        customDialog.showDoubleDialog(title,conetnt,lefeButtonTV,rightButtonTV,positiveCallBack,navigationCallBack);
        customDialog.setOutSideClickDissMiss(enableCancel);
    }

    public static void showDoubleDialog(Activity activity, String title, String conetnt, String lefeButtonTV, String rightButtonTV, final PositiveCallBack positiveCallBack, final NavigationCallBack navigationCallBack){
        CustomDialog customDialog=new CustomDialog(activity);
        customDialog.showDoubleDialog(title,conetnt,lefeButtonTV,rightButtonTV,positiveCallBack,navigationCallBack);
    }

    public void setTitleColor(int titleColor) {
      dialog.setTitleColor(titleColor);
    }

    public void setMsgColor(int msgColor) {
        dialog.setMsgColor(msgColor);
    }

    public void setTitleSize(int titleSize) {
        dialog.setTitleSize(titleSize);
    }

    public void setMsgSize(int msgSize) {
        dialog.setMsgSize(msgSize);
    }

    public void setMsgGravity(int msgGravity) {
        dialog.setMsgGravity(msgGravity);
    }

    public void setTitleGravity(int titleGravity) {
        dialog.setTitleGravity(titleGravity);
    }

    public void setSingleButtonBackground(int singleButtonBackground) {
        dialog.setSingleButtonBackground(singleButtonBackground);
    }

    public void setSingleButtonTVColor(int singleButtonTV) {
        dialog.setSingleButtonTV(singleButtonTV);
    }

    public void setLeftButtonBackground(int leftButtonBackground) {
        dialog.setLeftButtonBackground(leftButtonBackground);
    }

    public void setLeftButtonTVColor(int leftButtonTV) {
        dialog.setLeftButtonTV(leftButtonTV);
    }

    public void setRightButtonBackground(int rightButtonBackground) {
        dialog.setRightButtonBackground(rightButtonBackground);
    }

    public void setRightButtonTVColor(int rightButtonTV) {
        dialog.setRightButtonTV(rightButtonTV);
    }




    public void setmPositiveCallBack(PositiveCallBack mPositiveCallBack) {
        this.mPositiveCallBack = mPositiveCallBack;
    }

    public void setmNegativeCallBack(NavigationCallBack mNegativeCallBack) {
        this.mNegativeCallBack = mNegativeCallBack;
    }
    public void setOutSideClickDissMiss(boolean outSideClickDissMiss) {
       dialog.setOutSideClickDissMiss(outSideClickDissMiss);
    }
    public void setMsgMarginTop(int marginTop){
        this.dialog.setMsgMarginTop(marginTop);
    }

    public void setMarginBottom(int marginBottom){
        this.dialog.setMarginBottom(marginBottom);
    }

    public void setOnStopDismiss(boolean onStopDismiss) {
        dialog.setOnStopDismiss(onStopDismiss);
    }
    /**----------------------接口部分--------------------------------*/



    public interface PositiveCallBack{
        void callBack();
    }
    public interface NavigationCallBack{
        void callBack();
    }
}

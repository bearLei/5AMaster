package com.puti.education.util.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.puti.education.R;
import com.puti.education.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


/**
 * Created by lei on 2016/10/17.
 */
public class DialogWindow extends DialogFragment implements DialogInterface.OnDismissListener {


    public static final String TAG="DIALOGWINDOW";
    private ViewGroup contentContainer;
    private ViewGroup outMostContainer;
    private ViewGroup rootView;//AlertView 的 根View
    private ViewGroup loAlertHeader;//窗口headerView
    private TextView tvAlertTitle;
    private TextView tvAlertMsg;
//    private EditText etAlertInput;
    private Context context;
    private String cancel;
    private ArrayList<String> mDatas ;
    public static final int HORIZONTAL_BUTTONS_MAXCOUNT = 2;
    public static final int CANCELPOSITION = -1;//点击取消按钮返回 －1，其他按钮从0开始算
    private String title;
    private String msg;


    /**常用的一些属性*/
    private int titleColor;
    private int msgColor;

    private int titleSize;
    private int msgSize;

    private int msgIconRedId;

    private int msgGravity;
    private int titleGravity;

    private int singleButtonBackground;
    private int singleButtonTV;
    private int leftButtonBackground;
    private int leftButtonTV;
    private int rightButtonBackground;
    private int rightButtonTV;

    private int marginTop = 30;
    private int marginBottom = 30;
    private boolean outSideClickDissMiss;
    private boolean onStopDismiss;
    public enum Style{
        ActionSheet,
        Alert
    }
    /**默认值*/
    private Style style = Style.Alert;
    private int gravity = Gravity.BOTTOM;
    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    );


    public DialogWindow() {

    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public DialogWindow(Context context) {
        this.context = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = gravity;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            if (inAnim != null) {
                rootView.startAnimation(inAnim);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (onStopDismiss) {
            dismiss();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        return rootView;
    }


    private void initAttribute(){
        titleSize= 17;
        msgSize=14;
        titleColor=context.getResources().getColor(R.color.base_333333);
        msgColor=context.getResources().getColor(R.color.base_666666);
        msgGravity=Gravity.CENTER;
        titleGravity=Gravity.CENTER;

        singleButtonBackground=R.drawable.dialog_btn_single_bottom;
        singleButtonTV=R.color.base_ffffff;

        leftButtonBackground=R.drawable.bg_corner_bottom_left_gray;
        leftButtonTV=R.color.base_4577dc;

        rightButtonBackground=R.drawable.dialog_btn_right_bottom;
        rightButtonTV=R.color.base_ffffff;
        outSideClickDissMiss=true;
        onStopDismiss = true;
    }
    public void initData(String title, String msg, String cancel, List<String> destructive, Style style) {
        this.title = title;
        this.msg = msg;
        if (mDatas==null){
            mDatas=new ArrayList<>();
        }
        mDatas.clear();
        if (destructive != null){
            this.mDatas.addAll(destructive);
        }
        if (cancel != null){
            this.cancel = cancel;
            /**弹窗出现在中间 并且按钮选项少于2个， 则取消按钮充当1项item*/
            if(style == Style.Alert && mDatas.size() < HORIZONTAL_BUTTONS_MAXCOUNT){
                this.mDatas.add(0,cancel);
            }
        }
        this.style=style;
        initAttribute();
    }

    public void setOnItemClickListener(OnDialogItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void initView(){
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        rootView= (ViewGroup) layoutInflater.inflate(R.layout.layout_normal_dialog,null);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outSideClickDissMiss) {
                    hide();
                }
            }
        });
        contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);


        int margin_alert_left_right ;
        switch (style){
            case ActionSheet:
                params.gravity = Gravity.BOTTOM;
                margin_alert_left_right = 0;
                params.setMargins(margin_alert_left_right,0,margin_alert_left_right,margin_alert_left_right);
                contentContainer.setLayoutParams(params);
                gravity = Gravity.BOTTOM;
                initActionSheetViews(layoutInflater);
                break;
            case Alert:
                params.gravity = Gravity.CENTER;
                margin_alert_left_right = context.getResources().getDimensionPixelSize(R.dimen.dp_30);
                params.setMargins(margin_alert_left_right,0,margin_alert_left_right,0);
                contentContainer.setLayoutParams(params);
                gravity = Gravity.CENTER;
                initAlertViews(layoutInflater);
                break;
        }
    }

    protected void initActionSheetViews(LayoutInflater layoutInflater) {
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.layout_dialog_actionsheet,contentContainer);
        initHeaderView(viewGroup);

        initListView();
        TextView tvAlertCancel = (TextView) viewGroup.findViewById(R.id.tvAlertCancel);
        if(cancel != null){
            tvAlertCancel.setVisibility(View.VISIBLE);
            tvAlertCancel.setText(cancel);
        }
        tvAlertCancel.setOnClickListener(new OnTextClickListener(CANCELPOSITION));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void initAlertViews(LayoutInflater layoutInflater) {
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.layout_dialog_alert, contentContainer);
        initHeaderView(viewGroup);
        int position = 0;
        /***如果总数据小于等于HORIZONTAL_BUTTONS_MAXCOUNT，则是横向button*
         *根据item的个数考虑是横向还是纵向展示
         * 超过2个 纵向展示
         *
         */

        if(mDatas.size()<=HORIZONTAL_BUTTONS_MAXCOUNT){
            ViewStub viewStub = (ViewStub) viewGroup.findViewById(R.id.viewStubHorizontal);
            viewStub.inflate();
            LinearLayout loAlertButtons = (LinearLayout) viewGroup.findViewById(R.id.loAlertButtons);
            for (int i = 0; i < mDatas.size(); i ++) {
                //如果不是第一个按钮
//                if (i != 0){
//                    //添加上按钮之间的分割线
//                    View divier = new View(context);
//                    divier.setBackgroundColor(context.getResources().getColor(R.color.base_ededed));
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)context.getResources().getDimension(R.dimen.dp_1), LinearLayout.LayoutParams.MATCH_PARENT);
//                    loAlertButtons.addView(divier,params);
//                }
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_dialogbutton, null);
                TextView tvAlert = (TextView) itemView.findViewById(R.id.tvAlert);
                tvAlert.setClickable(true);

                //设置点击效果
                if(mDatas.size() == 1){
                    tvAlert.setTextColor(context.getResources().getColor(singleButtonTV));
//                    tvAlert.setBackgroundDrawable(context.getResources().getDrawable(singleButtonBackground));
                    tvAlert.setBackgroundResource(singleButtonBackground);
                }
                else if(i == 0){//设置最左边的按钮效果
                    tvAlert.setTextColor(context.getResources().getColor(leftButtonTV));
                    tvAlert.setBackgroundResource(leftButtonBackground);
                }
                else if(i == mDatas.size() - 1){//设置最右边的按钮效果
                    tvAlert.setTextColor(context.getResources().getColor(rightButtonTV));
                    tvAlert.setBackgroundResource(rightButtonBackground);
                }
                String data = mDatas.get(i);
                tvAlert.setText(data);

                //取消按钮的样式
                if (data == cancel){
                    tvAlert.setTypeface(Typeface.DEFAULT_BOLD);
                    tvAlert.setTextColor(context.getResources().getColor(R.color.base_4577dc));
                    tvAlert.setOnClickListener(new OnTextClickListener(CANCELPOSITION));
                    position = position - 1;
                }

                tvAlert.setOnClickListener(new OnTextClickListener(position));
                position++;
                loAlertButtons.addView(itemView,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            }
        }
        else{
            ViewStub viewStub = (ViewStub) viewGroup.findViewById(R.id.viewStubVertical);
            viewStub.inflate();
            initListView();
        }
    }


    /**Dialog的标题和消息部分*/
    protected void initHeaderView(ViewGroup viewGroup){
        loAlertHeader = (ViewGroup) viewGroup.findViewById(R.id.loAlertHeader);
        //标题和消息
        tvAlertTitle = (TextView) viewGroup.findViewById(R.id.tvAlertTitle);
         tvAlertMsg = (TextView) viewGroup.findViewById(R.id.tvAlertMsg);
        tvAlertMsg.setMovementMethod(ScrollingMovementMethod.getInstance());


        if(!TextUtils.isEmpty(title)) {
            tvAlertTitle.setText(title);
            tvAlertTitle.setGravity(titleGravity);
            tvAlertTitle.setTextSize(titleSize);
            tvAlertTitle.setTextColor(titleColor);
        }else{
            tvAlertTitle.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(msg)) {
            tvAlertMsg.setText(msg);
            tvAlertMsg.setGravity(msgGravity);
            if(msgIconRedId != 0){
                Drawable icon = context.getResources().getDrawable(msgIconRedId);
                icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
                tvAlertMsg.setCompoundDrawables(null, icon, null, null);
                tvAlertMsg.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            tvAlertMsg.setTextColor(msgColor);
            tvAlertMsg.setTextSize(msgSize);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvAlertMsg.getLayoutParams();
            params.bottomMargin = ViewUtils.dip2px(context, marginBottom);
            params.topMargin = ViewUtils.dip2px(context, marginTop);
            tvAlertMsg.setLayoutParams(params);

        }else{
            tvAlertMsg.setVisibility(View.GONE);
        }
    }

    /**提供外界调用属性修改*/
    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public void setMsgColor(int msgColor) {
        this.msgColor = msgColor;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public void setMsgIconRedId(int msgIconRedId) {
        this.msgIconRedId = msgIconRedId;
    }

    public void setMsgSize(int msgSize) {
        this.msgSize = msgSize;
    }

    public void setMsgGravity(int msgGravity) {
        this.msgGravity = msgGravity;
    }

    public void setTitleGravity(int titleGravity) {
        this.titleGravity = titleGravity;
    }

    public void setSingleButtonBackground(int singleButtonBackground) {
        this.singleButtonBackground = singleButtonBackground;
    }

    public void setSingleButtonTV(int singleButtonTV) {
        this.singleButtonTV = singleButtonTV;
    }

    public void setLeftButtonBackground(int leftButtonBackground) {
        this.leftButtonBackground = leftButtonBackground;
    }

    public void setLeftButtonTV(int leftButtonTV) {
        this.leftButtonTV = leftButtonTV;
    }

    public void setRightButtonBackground(int rightButtonBackground) {
        this.rightButtonBackground = rightButtonBackground;
    }

    public void setRightButtonTV(int rightButtonTV) {
        this.rightButtonTV = rightButtonTV;
    }

    public void setOutSideClickDissMiss(boolean outSideClickDissMiss) {
        this.outSideClickDissMiss = outSideClickDissMiss;
    }

    public void setOnStopDismiss(boolean onStopDismiss) {
        this.onStopDismiss = onStopDismiss;
    }

    public void setMsgMarginTop(int marginTop){
        this.marginTop = marginTop;
    }

    public void setMarginBottom(int marginBottom){
        this.marginBottom = marginBottom;
    }

    /**dialog的可操作选项部分  --item*/
    protected void initListView(){
        ListView alertButtonListView = (ListView) contentContainer.findViewById(R.id.alertButtonListView);
        /**把cancel作为footerView*/
        if(cancel != null && style == Style.Alert){
//            View itemView = LayoutInflater.from(context).inflate(R.layout.item_dialogbutton, null);
            View itemView= LayoutInflater.from(context).inflate(R.layout.item_dialogbutton,null);
            TextView tvAlert = (TextView) itemView.findViewById(R.id.tvAlert);
            tvAlert.setText(cancel);
            tvAlert.setClickable(true);
            tvAlert.setTypeface(Typeface.DEFAULT_BOLD);
            tvAlert.setTextColor(context.getResources().getColor(R.color.base_4577dc));
            tvAlert.setBackgroundResource(R.drawable.bg_alertbutton_bottom);
            tvAlert.setOnClickListener(new OnTextClickListener(CANCELPOSITION));
            alertButtonListView.addFooterView(itemView);
        }
        DialogViewAdapter adapter = new DialogViewAdapter(mDatas);
        alertButtonListView.setAdapter(adapter);
        alertButtonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(onItemClickListener != null)onItemClickListener.onItemClick(this,position);
                hide();
            }
        });
    }

    /**---------------------------------接口部分---------------------------------------------*/
    private OnDialogItemClickListener onItemClickListener;

    class OnTextClickListener implements View.OnClickListener{

        private int position;
        public OnTextClickListener(int position){
            this.position = position;
        }
        @Override
        public void onClick(View view) {
            if(onItemClickListener != null)onItemClickListener.onItemClick(this,position);
           hide();
        }
    }
    @Override
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
    }
    /**--------------------------------- /**动画部分-----------------------------------------*/
    private Animation outAnim;
    private Animation inAnim;
    protected void init() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }
    public Animation getInAnimation() {
        int res = getAnimationResource(this.gravity, true);
        if (res !=INVALID) {
            return AnimationUtils.loadAnimation(context, res);
        }else
            return null;
    }


    public Animation getOutAnimation() {
        int res = getAnimationResource(this.gravity, false);
        if (res != INVALID) {
            return AnimationUtils.loadAnimation(context, res);
        }else
            return null;
    }

   private void hide(){
       if (outAnim!=null) {
           outAnim.setAnimationListener(new Animation.AnimationListener() {
               @Override
               public void onAnimationStart(Animation animation) {

               }

               @Override
               public void onAnimationEnd(Animation animation) {
                   dismiss();
               }

               @Override
               public void onAnimationRepeat(Animation animation) {

               }
           });
           rootView.startAnimation(outAnim);
       }else {
           dismiss();
       }
   }

    private  final int INVALID = -1;
    private int getAnimationResource(int gravity, boolean isInAnimation) {

        switch (gravity) {
            case Gravity.BOTTOM:
                return isInAnimation ? R.anim.slide_in_bottom : R.anim.slide_out_bottom;
            case Gravity.CENTER:
//                return isInAnimation ? R.anim.fade_in_center : R.anim.fade_out_center;
                return INVALID;
        }

        return INVALID;
    }
}

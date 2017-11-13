package com.puti.education.ui.uiPatriarch;

import android.content.Intent;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.NotificationMsg;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.PatriarchModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;

import butterknife.BindView;

/**
 * Created by icebery on 2017/5/18 0018.
 * 成长轨迹详情
 */

public class GrowthTrackDetailActivity extends BaseActivity {
    @BindView(R.id.webiew)
    WebView webView;
    @BindView(R.id.title_textview)
    TextView titleTv;

    private String msgId;
    private String msgTitle;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_helpercenter;
    }

    @Override
    public void initVariables() {
        Intent intent = getIntent();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.cancel(); 默认的处理方式，WebView变成空白页
                handler.proceed();//接受证书
                //handleMessage(Message msg); 其他处理
            }
        });


        msgId = intent.getStringExtra("id");
        msgTitle = intent.getStringExtra("title");
        if (TextUtils.isEmpty(msgTitle))
        {
            titleTv.setText("成长详情");
        }
        else
        {
            titleTv.setText(msgTitle);
        }

        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void initViews() {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadData() {
        refreshMsgDetail();
    }

    //
    private void refreshMsgDetail()
    {
        LogUtil.d("", "do get message detail: " + msgId);
        if (TextUtils.isEmpty(msgId))
        {
            ToastUtil.show("消息ID无效：" + msgId);
            return;
        }

        PatriarchModel.getInstance().getGrowthTrackDetail(msgId, new BaseListener(NotificationMsg.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                if (infoObj != null)
                {
                    NotificationMsg msg = (NotificationMsg)infoObj;

                    String msgContent = msg.content;

                    if (!TextUtils.isEmpty(msg.title)){
                        titleTv.setText(msg.title);
                    }else{
                        //titleTv.setText(getMessageTypeName(msgs.get(0).getType()));
                    }

                    webView.loadDataWithBaseURL(null, msgContent, "text/html", "UTF-8",  null); //显示文本
                    //webView.loadDataWithBaseURL(baseUrl, string, "text/html", "utf-8", null); //显示本地图片，baseUrl为存储照片的本地路径
                }
                else
                {
                    ToastUtil.show("获取消息详情为空 " );
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("获取消息详情出错 " );
            }
        });

    }



//    public String getMessageTypeName(int type)
//    {
//        if (type >= 0 && type < Constants.MESSAGE_TYPE_STRING.length)
//        {
//            return Constants.MESSAGE_TYPE_STRING[type];
//        }
//        else
//        {
//            return "消息详情";
//        }
//
//    }
}

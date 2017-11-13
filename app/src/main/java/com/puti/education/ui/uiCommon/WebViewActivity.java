package com.puti.education.ui.uiCommon;

import android.content.Intent;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.puti.education.R;
import com.puti.education.bean.NotificationMsg;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;

import butterknife.BindView;

/**
 * Created by icebery on 2017/5/18 0018.
 */

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.webiew)
    WebView webView;
    @BindView(R.id.title_textview)
    TextView titleTv;

    private int mType;//1.根据消息ID获取详情, 2显示传递过来的内容, 3显示web　网页
    private String mWebUrl = null;

    private String msgId;
    private String msgTitle;


    /**
     * 1.	在线调查
     2.	互评
     3.	通知消息
     4.	事件佐证（老师）
     5.	事件分析（家长）
     6.	驳回
     7.	推送
     8.	问卷消息（告诉老师问卷写完了）
     9.	警示事件

     */



    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_helpercenter;
    }

    @Override
    public void initVariables() {
        Intent intent = getIntent();

        mType   = intent.getIntExtra("type", -1);
        mWebUrl = intent.getStringExtra("weburl");


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.cancel(); 默认的处理方式，WebView变成空白页
                handler.proceed();//接受证书
                //handleMessage(Message msg); 其他处理
            }
        });

        //webView.loadUrl(NetOper.HTTP_PREFIX + "/setting/help");
        //titleTv.setText("帮助中心");

        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setJavaScriptEnabled(true);

        msgTitle = intent.getStringExtra("msg_title");
        if (TextUtils.isEmpty(msgTitle))
        {
            titleTv.setText("消息");
        }
        else
        {
            titleTv.setText(msgTitle);
        }

        if (mType == 1){
            msgId = intent.getStringExtra("msg_uid");
        }else if (mType == 2) {
            String msgContent = this.getIntent().getStringExtra("content");
            webView.loadDataWithBaseURL(null, msgContent, "text/html", "UTF-8", null); //显示文本
        }else if (mType == 3){
            if (TextUtils.isEmpty(mWebUrl)){
                ToastUtil.show("URL为空！");
                return;
            }

            if (TextUtils.isEmpty(msgTitle)){
                titleTv.setText("公告");
            }

            webView.loadUrl(mWebUrl);
        }


    }

    @Override
    public void initViews() {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadData() {
        if (mType == 1){
            refreshMsgDetail();
        }
    }

    //
    private void refreshMsgDetail()
    {
        LogUtil.d("", "do get message detail: " + msgId);
        if (TextUtils.isEmpty(msgId))
        {
            ToastUtil.show("消息ID无效");
            return;
        }

        CommonModel.getInstance().getMsgDetail(msgId, new BaseListener(NotificationMsg.class){

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
                ToastUtil.show("获取消息详情出错 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
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

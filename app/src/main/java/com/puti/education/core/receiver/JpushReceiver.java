package com.puti.education.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.puti.education.bean.PushData;
import com.puti.education.ui.uiCommon.QuestionnaireDetailActivity;
import com.puti.education.ui.uiCommon.WebViewActivity;
import com.puti.education.ui.uiPatriarch.ActionEventDetailActivity;
import com.puti.education.ui.uiPatriarch.GrowthTrackDetailActivity;
import com.puti.education.ui.uiPatriarch.PatriarchMainActivity;
import com.puti.education.ui.uiPatriarch.TrainPracticeDetailActivity;
import com.puti.education.ui.uiStudent.PracticeDetailActivity;
import com.puti.education.ui.uiStudent.StudentMainActivity;
import com.puti.education.ui.uiTeacher.DetectiveListActivity;
import com.puti.education.ui.uiTeacher.ReportListActivity;
import com.puti.education.ui.uiTeacher.TeacherEventDetailActivity;
import com.puti.education.ui.uiTeacher.TeacherMainActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by icebery on 2017/5/24 0024.
 */

public class JpushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private Context mCtx;

    @Override
    public void onReceive(Context context, Intent intent) {

        mCtx = context;
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            if (!TextUtils.isEmpty(regId)) {
                //MeApplication.mJPushRegId = regId;
            }
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            //processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            processNotify(context, bundle);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


    private void processNotify(Context ctx, Bundle bundle){
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        PushData dateInfo = null;
        if (!TextUtils.isEmpty(extras)) {
            try {
                dateInfo = JSON.parseObject(extras, PushData.class);
                if (null != dateInfo) {
                    dateInfo.parseExtContent();
                    parseExtraData(dateInfo);
                }
            } catch (Exception e) {

            }

        }

    }

    private void parseExtraData(PushData info){
        if (info.type.equals(PushData.TYPE_MSG))
        {
            //仅仅在状态栏显示
        }
        else if (info.type.equals(PushData.TYPE_WEB))
        {
            //打开网页
            processWeb(info);
        }
        else if (info.type.equals(PushData.TYPE_EVENT))
        {
            //事件处理
            processEvent(info);
        }
    }

    private void processWeb(PushData detail)
    {
        if (detail.extValue != null)
        {
            Intent i = new Intent(mCtx, WebViewActivity.class);
            i.putExtra("type", 3);
            i.putExtra("weburl", detail.extValue.webUrl);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            mCtx.startActivity(i);
        }
    }

    private void processEvent(PushData detail)
    {
        if (detail.extValue != null)
        {
            switch (detail.extValue.subType) {
                case PushData.TARGET_QUESTIONNAIRE:
                    openQuestionairDetail(detail.extValue.value);
                    break;
                case PushData.TARGET_MUTUAL_REVIEW:     //跳转互评
                    openReviewPage();
                    break;
                case PushData.TARGET_EVENT_UNCONFIRM:
                case PushData.TARGET_EVENT_UNCHECK:
                case PushData.TARGET_EVENT_CHECKED:
                case PushData.TARGET_EVENT_UNTRACK:
                case PushData.TARGET_EVENT_PUSH_OFFICE:
                case PushData.TARGET_EVENT_FINISHED:
                case PushData.TARGET_EVENT_PUSH_PARENT:
                    openEventAnalysis(detail.extValue.value, detail.extValue.valueExt);
                    break;
                case PushData.TARGET_TRAIN_PARENT:
                    openParentPractice(detail.extValue.value);
                    break;
                case PushData.TARGET_TRAIN_STUDENT:
                    openStudentPractice(detail.extValue.value);
                    break;
                case PushData.TARGET_DETECT_UNCONFIRM:
                    openUnconfirmDetect();
                    break;
                case PushData.TARGET_REPORT_UNCONFIRM:
                    openUnconfirmReport();
                    break;
                case PushData.TARGET_SYS_MESSAGE:
                    openMessageDetail(detail.extValue.value, detail.alertMsg);
                    break;
            }
        }
    }

    //互评列表
    private void openReviewPage(){
        Intent intent4 = new Intent();
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );

        int trole = ConfigUtil.getInstance(mCtx).get(Constant.KEY_ROLE_TYPE, -1);
        if (trole == Constant.ROLE_STUDENT){
            intent4.putExtra("tabindex", 1);
            intent4.setClass(mCtx, StudentMainActivity.class);
            mCtx.startActivity(intent4);
        }else if(trole == Constant.ROLE_PARENTS){
            intent4.putExtra("tabindex", 2);
            intent4.setClass(mCtx, TeacherMainActivity.class);
            mCtx.startActivity(intent4);
        }else if (trole == Constant.ROLE_TEACHER){
            intent4.putExtra("tabindex", 1);
            intent4.setClass(mCtx, PatriarchMainActivity.class);
            mCtx.startActivity(intent4);
        }
    }

    private void openActionEvent(int id){
        Intent intent4 = new Intent();
        intent4.putExtra("id", id);
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        intent4.setClass(mCtx, ActionEventDetailActivity.class);
        mCtx.startActivity(intent4);
    }

    private void openGrowthTrack(String uid){
        Intent intent4 = new Intent();
        intent4.putExtra("id", uid);
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        intent4.setClass(mCtx, GrowthTrackDetailActivity.class);
        mCtx.startActivity(intent4);
    }

    private void openStudentPractice(String uid){
        Intent intent4 = new Intent();
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );

        int trole = ConfigUtil.getInstance(mCtx).get(Constant.KEY_ROLE_TYPE, -1);
        if (trole == Constant.ROLE_TEACHER){
            intent4.putExtra("tabindex", 3);
            intent4.setClass(mCtx, PatriarchMainActivity.class);
            mCtx.startActivity(intent4);
        }
    }

    private void openUnconfirmDetect(){
        Intent intent4 = new Intent();
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        intent4.setClass(mCtx, DetectiveListActivity.class);
        mCtx.startActivity(intent4);
    }

    private void openUnconfirmReport(){
        Intent intent4 = new Intent();
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        intent4.setClass(mCtx, ReportListActivity.class);
        mCtx.startActivity(intent4);
    }

    private void openEventAnalysis(String eventId, String peopleuid)
    {
        Intent intent4 = new Intent();
        intent4.putExtra("type", 4);
        intent4.putExtra(Key.EVENT_KEY, eventId);
        intent4.putExtra(Key.KEY_PEOPLE_UID, peopleuid);
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        intent4.setClass(mCtx, TeacherEventDetailActivity.class);
        mCtx.startActivity(intent4);
    }


    private void openParentPractice(String uid){
        Intent intent4 = new Intent();
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );

        int trole = ConfigUtil.getInstance(mCtx).get(Constant.KEY_ROLE_TYPE, -1);
        if (trole == Constant.ROLE_TEACHER){
            intent4.putExtra("tabindex", 3);
            intent4.setClass(mCtx, PatriarchMainActivity.class);
            mCtx.startActivity(intent4);
        }
    }

    private void openQuestionairDetail(String qtUid)
    {
        Intent intent = new Intent();
        intent.putExtra("id", qtUid);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        intent.setClass(mCtx, QuestionnaireDetailActivity.class);
        mCtx.startActivity(intent);
    }

    private void openMessageDetail(String messageId, String title)
    {
        Intent intent = new Intent(mCtx, WebViewActivity.class);
        intent.putExtra("type", 1);
        intent.putExtra("msg_id", messageId);
        intent.putExtra("msg_title", title);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        mCtx.startActivity(intent);
    }

}

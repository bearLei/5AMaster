package com.puti.education.speech;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONException;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.puti.education.R;
import com.puti.education.util.LogUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by lei on 2017/11/13.
 * 语音操作类
 */
public class SpeechUtil {

    private static final String APPID = "5a07e75c";
    private RecognizerListener mRecognizerListener;
    private RecognizerDialog iatDialog;

    private static class SingletonHolder {
        private static final SpeechUtil INSTANCE = new SpeechUtil();
    }
    public static SpeechUtil g(Context context){
        initSpeech(context);
        return SingletonHolder.INSTANCE;
    }

    /**
     * 初始化
     * @param context
     */
    private static void initSpeech(Context context){
        SpeechUtility.createUtility(context, SpeechConstant.APPID +"="+APPID);
    }
    private SpeechDialog dialog ;
    public void createDialog(final Context context, final SpeechResultCallBack speechResultCallBack){
        if (dialog == null) {
            dialog = new SpeechDialog(context);
        }
        dialog.setOnSpeechListener(new SpeechDialog.SpeechOnClickListener() {
            @Override
            public void sure(String s) {
                dialog.dismiss();
                speechResultCallBack.result(s);
            }

            @Override
            public void cancle() {
                dialog.dismiss();
            }

            @Override
            public void speech() {
                SpeechUtil.this.speech(context, new SpeechCallBack() {
                    @Override
                    public void startSpeech() {
                    }

                    @Override
                    public void endSpeech() {

                    }

                    @Override
                    public void result(String s) {
                        dialog.setEdit(s);
                    }

                    @Override
                    public void error() {

                    }
                });
            }
        });

        dialog.show();
    }

    public void speech(Context context, final SpeechCallBack speechCallBack){
        if (speechCallBack == null) return;

        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(context,mTtsInitListener);
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
//        if (iatDialog == null){
//            iatDialog = new RecognizerDialog(context,mTtsInitListener);
//        }
//        iatDialog.show();
        if (mRecognizerListener == null){
            mRecognizerListener = new RecognizerListener() {
                @Override
                public void onVolumeChanged(int i, byte[] bytes) {
                    //音量值  0-30
                }

                @Override
                public void onBeginOfSpeech() {
                    //开始录音
                    speechCallBack.startSpeech();
                }

                @Override
                public void onEndOfSpeech() {
                    //结束录音
                    speechCallBack.endSpeech();
                }

                @Override
                public void onResult(RecognizerResult recognizerResult, boolean b) {
                    //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
                    //关于解析Json的代码可参见MscDemo中JsonParser类；
                    //isLast等于true时会话结束。
                    if (recognizerResult == null ) return;
                        if (b){
                            // TODO: 2017/11/15 录音完毕需要处理什么
                            return;
                        }
                        HashMap<String, String> speechRecognizerResults = new LinkedHashMap<String, String>();
                        StringBuilder builder = new StringBuilder();
                        String s = JsonParser.parseIatResult(recognizerResult.getResultString());
                        String sn = null;
                        // 读取json结果中的sn字段
                        try {
                            JSONObject jsonObject = new JSONObject(recognizerResult.getResultString());
                            sn = jsonObject.optString("sn");
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                        speechRecognizerResults.put(sn, s);
                        for (String key : speechRecognizerResults.keySet()) {
                            builder.append(speechRecognizerResults.get(key));
                        }

                        speechCallBack.result(builder.toString());

                }

                @Override
                public void onError(SpeechError speechError) {
                    //会话发生错误回调接口
                    speechCallBack.error();
                }

                @Override
                public void onEvent(int i, int i1, int i2, Bundle bundle) {
                    //扩展用接口
                }
            };
        }
        Log.d("lei","开启监听");
        mIat.startListening(mRecognizerListener);
    }

    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                // showTip("初始化失败,错误码：" + code);

            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    public interface SpeechCallBack{
        void startSpeech();
        void endSpeech();
        void result(String s);
        void error();
    }

    public interface SpeechResultCallBack{
        void result(String s);
    }

}

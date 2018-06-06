package unit.api;

import android.os.Build;

import com.puti.education.App;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.CommonSubscriber;
import com.puti.education.netFrame.RetrofitUtil;
import com.puti.education.netFrame.netApi.CommonApi;
import com.puti.education.netFrame.netModel.BaseModel;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.response.ResponseInfo;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import unit.base.BaseResponseInfo;
import unit.base.PutiBaseModel;
import unit.base.PutiCommonSubscriber;
import unit.entity.VerifyPostInfo;

/**
 * Created by lei on 2018/6/4.
 */

public class PutiCommonModel extends PutiBaseModel{

    private PutiCommonApi mCommonApi = null;
    private static PutiCommonModel gCommonModel = null;
    private MediaType mMediaType;

    public PutiCommonModel(){
        mCommonApi = RetrofitUtil.getRetrofit().create(PutiCommonApi.class);
        mMediaType = okhttp3.MediaType.parse("application/json; charset=utf-8");
    }

    public static PutiCommonModel getInstance(){
        if (gCommonModel == null){
            gCommonModel = new PutiCommonModel();
            return gCommonModel;
        }
        return  gCommonModel;
    }

    public static void clearInstance(){
        gCommonModel = null;
    }

        /******************api调用开始***********************************/

    /**
     *
     * @param userName 登录账号
     * @param psd 登录密码
      * @param verifyInfo 验证码信息
     * @param deviceInfo 设备信息
     * @param baseListener 回调
     */
    public void login(String userName, String psd, VerifyPostInfo verifyInfo, String deviceInfo, final BaseListener baseListener){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"loginName\":\"").append(userName).append("\"");
        sb.append(",\"password\":\"").append(psd).append("\"");
        sb.append("}");
//        sb.append(",\"verifyCode\":{").append("\"uuidKey\":\"").append(verifyInfo.getUuidKey());
//        sb.append(",\"vericode\":\""+verifyInfo.getVericode()+"}");
//        sb.append(",\"deviceInfo\":{");
//        sb.append("\"type\":10,");
//        sb.append("\"deviceDesc\":\"" + Build.MODEL+ "\",");
//        sb.append("\"pushId\":\"" + App.mJPushRegId + "\",");
//        sb.append("\"deviceToken\":\" \"}}");

        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),sb.toString());
        mCommonApi.login(body).subscribeOn(Schedulers.io())//请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribe(new PutiCommonSubscriber(baseListener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJsonStr(responseInfo, baseListener);
                    }
                });
    }

    /**
     *
     * @param refer 请求来源，1 登录
     * @param listener
     */
    public void queryVerify(int refer,final BaseListener listener){
        mCommonApi.queryVerify(refer).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJsonStr(responseInfo,listener);
                    }
                });
    }

}

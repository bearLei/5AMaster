package unit.base;


import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.ResponseInfo;

import java.util.List;

public class PutiBaseModel {

    /**
     * 针对json字符串处理，可根据需要重载，或者创建其他方法处理
     * @param responseInfo
     * @param baseListener
     */
    public void dealJsonStr(BaseResponseInfo responseInfo, BaseListener baseListener){

        if (responseInfo.isSuccess()){
            if (responseInfo.getData()!= null){
                Object infoObj = JSON.parseObject( responseInfo.getData(),baseListener.getSubClass());
//                List<Object> listObj = responseInfo.getData().parseList(baseListener.getSubClass());
                baseListener.responseResult(infoObj,null, responseInfo.getCode(),responseInfo.isSuccess());
            }else{
                baseListener.responseResult(null, null,responseInfo.getCode(), responseInfo.isSuccess());
            }
        }else{
            baseListener.requestFailed(responseInfo.isSuccess(),responseInfo.getCode(), responseInfo.getMsg());
        }

    }

    public void dealJsonArrayStr(BaseResponseInfo responseInfo, BaseListener baseListener){

        if (responseInfo.isSuccess()){
            if (responseInfo.getData() != null){
                List<Object> listObj = responseInfo.parseList(baseListener.getSubClass());
                baseListener.responseListResult(null,listObj, null,responseInfo.getCode(),responseInfo.isSuccess());
            }else{
                baseListener.responseListResult(null, null,null, responseInfo.getCode(), responseInfo.isSuccess());
            }
        }else{
            baseListener.requestFailed(responseInfo.isSuccess(),responseInfo.getCode(), responseInfo.getMsg());
        }
    }
    public void dealJson(BaseResponseInfo responseInfo, BaseListener baseListener){
        if (responseInfo.isSuccess()){
            if (responseInfo.getData() != null){
                if (responseInfo.getData().startsWith("{")){
                    dealJsonStr(responseInfo, baseListener);
                }else if (responseInfo.getData().startsWith("[")){
                    dealJsonArrayStr(responseInfo,baseListener);
                }
            }else {
                baseListener.responseResult(null, null,responseInfo.getCode(), responseInfo.isSuccess());
            }
        }else {
            baseListener.requestFailed(responseInfo.isSuccess(),responseInfo.getCode(), responseInfo.getMsg());
        }
    }
    /**
     * 处理比较简单的响应数据，例如 注销，信息修改保存等接口放回的数据.避免创建类
     *
     */
    public void dealSimpleStr(ResponseInfo responseInfo, BaseListener baseListener){

        if (responseInfo.status){
            if (responseInfo.data != null && responseInfo.data.info != null){
                baseListener.responseResult(responseInfo.data.info,null, responseInfo.code,responseInfo.status);
            }else{
                baseListener.responseResult(null,null, responseInfo.code,responseInfo.status);
            }
        }else{
            baseListener.requestFailed(responseInfo.status,responseInfo.code, responseInfo.error);
        }

    }

    /**
     * 针对纯字符串处理，可根据需要重载，或者创建其他方法处理
     * @param  str
     * @param baseListener
     */
    public void dealStr(String str, BaseListener baseListener){

        if (!TextUtils.isEmpty(str)){

        }else{

        }

    }
}

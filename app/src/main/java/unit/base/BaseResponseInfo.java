package unit.base;

import com.alibaba.fastjson.JSON;
import com.puti.education.netFrame.response.DataInfo;

import java.util.ArrayList;

/**
 * Created by lei on 2018/6/4.
 */

public class BaseResponseInfo {
    private boolean Success;
    private String Msg;
    private String Data;
    private int code;
    public BaseResponseInfo() {
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public <T>Object parseInfo(Class<T> clazz)
    {
        Object objs = null;
        if (Data != null && !Data.equals("") && !Data.equals("{}"))
        {
            objs = JSON.parseObject(Data, clazz);
        }
        return objs;
    }

    public <T>ArrayList parseList(Class<T> clazz)
    {
        ArrayList<T> objs = null;
        if (Data != null && !Data.equals("") && !Data.equals("{}"))
        {
            try {
                objs = (ArrayList<T>) JSON.parseArray(Data, clazz);
            }catch(Exception e){
                String erro = e.getMessage();
            }
        }
        return objs;
    }
}

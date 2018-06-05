package unit.util;

import unit.entity.UserInfo;
import unit.sp.DataStorage;

/**
 * Created by lei on 2018/6/4.
 * 用户基础信息操作类 缓存下用户的基础信息
 */

public class UserInfoUtils {

    public static UserInfo getUserInfo(){
        return (UserInfo) DataStorage.getComObject(DataStorage.User_info);
    }

    public static void setUserInfo(UserInfo userInfo){
        DataStorage.saveComObject(DataStorage.User_info,userInfo);
    }

    public static String getToken(){
        if (getUserInfo() != null){
            return getUserInfo().getToken();
        }else
            return "";
    }

    public static boolean isInLoginStata(){
        if (getUserInfo() == null){
            return false;
        }else {
            return true;
        }
    }
}

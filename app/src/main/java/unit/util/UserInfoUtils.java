package unit.util;

import unit.entity.UserBaseInfo;
import unit.entity.UserInfo;
import unit.sp.DataStorage;

/**
 * Created by lei on 2018/6/4.
 * 用户基础信息操作类 缓存下用户的基础信息
 */

public class UserInfoUtils {

    public static UserBaseInfo getUserInfo(){
        return (UserBaseInfo) DataStorage.getComObject(DataStorage.User_info);
    }

    public static void setUserInfo(UserBaseInfo userInfo){
        DataStorage.saveComObject(DataStorage.User_info,userInfo);
    }

    public static String getToken(){
        if (getUserInfo() != null){
            return "Basic "+getUserInfo().getToken();
        }else
            return "";
    }


    public static boolean isInLoginStata(){
        return getUserInfo() != null;
    }
    public static void setAvatar(String url){
        UserBaseInfo userInfo = getUserInfo();
        userInfo.setAvatar(url);
        setUserInfo(userInfo);
    }

    public static String getUid(){
        String userId = DataStorage.getUserId();
        return userId;
    }
    public static void setUid(String uid){
        DataStorage.putUserId(uid);
    }

    public static int getUserType(){
        return DataStorage.getUserType();
    }
    public static void setUserType(int type){
        DataStorage.putUserType(type);
    }

    public static String getAreaUid(){
        if (getUserInfo() == null){
            return "";
        }
        return getUserInfo().getAreaUID();
    }


    public static boolean isMaster(){
        if (getUserInfo() == null){
            return false;
        }
        return getUserInfo().getPersonType() == 3;
    }

}

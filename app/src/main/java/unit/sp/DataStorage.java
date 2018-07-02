package unit.sp;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/4/26.
 * 具体的sp操作
 */

public class DataStorage {

    public static final String User_info = "UserInfo";
    public static final String User_id = "UserId";//用户id
    public static final String User_Type = "UserType";//用户身份类型
    public static final String User_Has_Notice = "HasMsgNotice";//是否有消息通知
    public static final String User_Has_Ques = "HasQues";//是否有事件待确认
    public static final String User_Has_Report = "HasReport";//是否有举报事件

    /**
     * 将一个Serializable对象保存到sp中. <br/>
     * 如果参数key为空, 则直接返回, 不进行任何操作.<br/>
     * 如果参数obj为null, 则会直接删掉sp中对应的key的条目.
     * <p/>
     */
    public static <T extends Serializable> void saveComObject(final String key, final T obj) {

            if (TextUtils.isEmpty(key)) {
                return;
            }

            if (null == obj) {
                DataStorageBase.remove(key);
                return;
            }
            try {
                String value = ObjectSerializer.serialize(obj);
                DataStorageBase.putString(key, value);
            } catch (Exception e) {

        }
    }


    /**
     * 从sp中取出key所对应的Serializable对象.
     */
    public static Serializable getComObject(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        String value = DataStorageBase.getString(key, "");
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        try {
            return (Serializable) ObjectSerializer.deserialize(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUserId(){
        return DataStorageBase.getString(User_id,"");
    }

    public static void putUserId(String user_id){
        DataStorageBase.putString(User_id,user_id);
    }
    public static int getUserType(){
        return DataStorageBase.getInt(User_Type,0);
    }

    public static void putUserType(int userType){
        DataStorageBase.putInt(User_Type,userType);
    }


    public static void putUserHasNotice(boolean has){
        DataStorageBase.putBool(User_Has_Notice,has);
    }

    public static boolean getUserHasNotice(){
        return DataStorageBase.getBool(User_Has_Notice,false);
    }
    public static void putUserQues(boolean has){
        DataStorageBase.putBool(User_Has_Ques,has);
    }

    public static boolean getUserHasQues(){
        return DataStorageBase.getBool(User_Has_Ques,false);
    }
    public static void putUserHasReport(boolean has){
        DataStorageBase.putBool(User_Has_Report,has);
    }

    public static boolean getUserHasReport(){
        return DataStorageBase.getBool(User_Has_Report,false);
    }
}

package com.puti.education.util;

import android.os.Environment;

import com.puti.education.App;

/**
 * 全局常量
 */

public class Constant {

    public static final int ROLE_TEACHER    = 1;
    public static final int ROLE_STUDENT    = 2;
    public static final int ROLE_VOLUNTEER  = 3;
    public static final int ROLE_PARENTS    = 4;
    public static final int ROLE_EXPERT     = 5;

    public static final String KEY_ROLE_TYPE    = "role_type";
    public static final String KEY_SCHOOL_ID    = "school_id";
    public static final String KEY_SCHOOL_NAME  = "school_name";
    public static final String KEY_SCHOOL_URL   = "school_url";
    public static final String KEY_LOGIN_NAME   = "login_name";
    public static final String KEY_LOGIN_PWD    = "login_pwd";
    public static final String KEY_TOKEN        = "token";
    public static final String KEY_TEACHER      = "teacher";
    public static final String KEY_USER_ID      = "user_id";
    public static final String KEY_USER_NAME    = "user_name";
    public static final String KEY_USER_AVATAR  = "user_avatar";
    public static final String KEY_VT_NUMBER    = "volunter_number";
    public static final String KEY_IS_STUDENT_AFFAIRS   = "is_student_affairs";

    public static final int REQUEST_CAMERA_CODE     = 10;
    public static final int REQUEST_PREVIEW_CODE    = 20;
    public static final int REQUEST_CODE_HEADTEACHER    = 50;
    public static final int REQUEST_CODE_BOTH           = 51;  //选择学生和教师
    public static final int REQUEST_CODE_STUDENT        = 52;  //选择学生
    public static final int REQUEST_CODE_BOTH_INVOLOVER = 53;  //选择学生和教师


    public final static String ALPHABETS = "abcdefghijklmnopqrstuvwxyz";
    public final static int ADDVENT_ACTIVITY        = 111;//TeacherAddEventActivity
    public final static int REPORT_EVENT_ACTIVITY   = 112;//StudentReportAddActivity

    public static final int    IMAGE_MAX_NUMBER   = 9;
    public static final int    IMAGE_MAX_NUMBER_6 = 6;
    public static final String IMAGE_ADD_FLAG     = "000000";
    public final static String REQUEST_FAILED_STR = "系统异常";

    //事件状态
    public static final int EVENT_STATUS_REFUSE     = 0; //已拒绝
    public static final int EVENT_STATUS_UNCONFIRM  = 1; //待确认
    public static final int EVENT_STATUS_CONFIRMED  = 2; //已确认
    public static final int EVENT_STATUS_UNCHECK    = 3; //待审核
    public static final int EVENT_STATUS_UNTRACK    = 4; //待追踪
    public static final int EVENT_STATUS_FINISHED   = 5; //已结案
    public static String EVENT_HAS_AFFIRM = "已确认";
    public static String EVENT_AUDITING= "审核中";
    public static String EVENT_PUBLISH = "已发布";
    public static String EVENT_HAS_FINISH = "已结案";
    public static String EVENT_OTHER_LEADER_RESULT_AFTER_HANDLE = "推送";

    public static final String EVENT_DUTY_MAJOR   = "1";//主要事件人
    public static final String EVENT_DUTY_MINOR   = "2";//次要事件人
    public static final String EVENT_DUTY_WITNESS = "3";//证人
    public static final String EVENT_DUTY_REPORT  = "4";//举报者
    public static final String EVENT_DUTY_KNOWN   = "5";//知情者

    //选择图文，音频，视频记录
    public final static int CODE_RESULT_PARENTS = 997;
    public final static int CODE_REQUEST_PARENTS = 998;

    public final static int CODE_RESULT_TEXT = 999;
    public final static int CODE_REQUEST_TEXT = 1000;

    public final static int CODE_RESULT_IMG_TEXT = 1001;
    public final static int CODE_REQUEST_IMG_TEXT = 1002;

    public final static int CODE_RESULT_MEDIA = 1003;
    public final static int CODE_REQUEST_MEDIA = 1004;

    public final static int CODE_RESULT_VIDEO= 1005;
    public final static int CODE_REQUEST_VIDEO = 1006;

    public final static int CODE_RESULT_SAMPLE= 1007;
    public final static int CODE_REQUEST_SAMPLE = 1008;

    public final static int CODE_RESULT_PROFESSOR = 1009;
    public final static int CODE_REQUEST_PROFESSOR = 1010;

    public static final int VOICE_REQUEST_CODE = 66;
    public static final int VOICE_RECORD_REQUEST_CODE = 67;
    public static final int VOICE_RECORD_RESULT_CODE = 68;



    public static final int  TYPE_RADIO = 1;       //单选
    public static  final int TYPE_MULTI = 2;       //多选
    public static  final int TYPE_JUDGE = 3;       //判断
    public static  final int TYPE_CLOZE = 4;       //填空
    public static  final int TYPE_TEXTAREA    = 5;    //简答
    public static  final int TYPE_NUMBERTAREA = 6;    //数字选择
    public static  final int TYPE_TEACHER_UID = 7;    //选择教师
    public static  final int TYPE_STUDENT_UID = 8;    //选择学生
    public static  final int TYPE_PARENT_UID  = 9;    //选择家长
    public static  final int TYPE_COURSE_UID  = 10;   //选择课程

    public static final int CHOOSE_PEOPLE_MAX = 50;   //涉事人最大数量


    //记录数
    public static String COUNT = "count";
    public static String UID   = "uid";
    public static String BASE_IP = "http://54.223.26.249:8090";
    //public static final String BLUETOOTH_SHORT_CLICK_CMD = "6C6F6E67"; //旧的蓝牙设备指令
    public static final String BLUETOOTH_SHORT_CLICK_CMD = "01";         //新的蓝牙设备指令
    public static final String BLUETOOTH_LONG_CLICK_CMD  = "73686F7274";

    public static final int UPLOAD_FILE_SIZE = 4*1024*1024;

    public static final String BROADCAST_ADD_INVOLVER   = "broadcst_add_involver";
    public static final String BROADCAST_REFRESH_REPORT = "broadcst_refresh_report";
    public static final String BROADCAST_REFRESH_EVENT = "broadcst_refresh_event";
    public static final String BROADCAST_REFRESH_EVALUATION = "broadcst_refresh_evaluation";
    public static final String BROADCAST_REFRESH_AUDIO_FILE = "broadcst_refresh_audio_file";

    public static final String STORAGE_DIR = "/ms_record";
    public static final String STORAGE_ROOT = Environment.getExternalStorageDirectory() + STORAGE_DIR;

    public static final String AUDIO_DIR = STORAGE_ROOT + "/";

}

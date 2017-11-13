package com.puti.education.bean;

/**
 * Created by yzh on 2017/9/7 0007.
 */

public class VideoObject {
    /** 视频最大时长，默认10秒 */
    private int mMaxDuration;
    /** 视频目录 */
    private String mOutputDirectory;
    /** 对象文件 */
    private String mOutputObjectPath;
    /** 视频码率 */
    private int mVideoBitrate;
    /** 最终视频输出路径 */
    private String mOutputVideoPath;
    /** 最终视频截图输出路径 */
    private String mOutputVideoThumbPath;
    /** 文件夹及文件名 */
    private String mKey;
    /** 开始时间 */
    private long mStartTime;
    /** 结束时间 */
    private long mEndTime;
    /** 视频移除标志位 */
    private boolean mRemove;


}

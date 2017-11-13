package com.puti.education.core.video;

import com.puti.education.bean.VideoObject;

/**
 * Created by yzh on 2017/9/7 0007.
 */

public interface IMediaRecorder {
    /**
     * 开始录制
     * @return 录制失败返回null
     */
    public VideoObject startRecord();
    /**
     * 停止录制
     */
    public void stopRecord();
}

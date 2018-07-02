package unit.api;

import com.puti.education.netFrame.response.ResponseInfo;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;
import unit.base.BaseResponseInfo;

/**
 * Created by lei on 2018/6/14.
 */

public interface PutiUploadApi {

    /**
     * 上传多张图片
     * @param imgs1
     * @return
     */
    @Multipart
    @POST("Common/Upload")
    Observable<BaseResponseInfo> uploadImage(@PartMap
                                                 Map<String, RequestBody> imgs1);
    @Multipart
    @POST("Common/ChangePhoto")
    Observable<BaseResponseInfo> changeAvatar(@PartMap
                                                     Map<String, RequestBody> imgs1);

    /**
     * 上传多个音频文件
     * @param imgs1
     * @return
     */
    @Multipart
    @POST("Common/Upload")
    Observable<BaseResponseInfo> uploadAudio(@PartMap
                                                 Map<String, RequestBody> imgs1);

    /**
     * 上传多个视频文件
     * @param imgs1
     * @return
     */
    @Multipart
    @POST("Common/Upload")
    Observable<BaseResponseInfo> uploadVideo(@PartMap
                                                 Map<String, RequestBody> imgs1);
}

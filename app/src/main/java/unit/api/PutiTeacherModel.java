package unit.api;

import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.RetrofitUtil;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import unit.base.BaseResponseInfo;
import unit.base.PutiBaseModel;
import unit.base.PutiCommonSubscriber;

/**
 * Created by lei on 2018/6/14.
 */

public class PutiTeacherModel extends PutiBaseModel{

    private PutiTeacherApi mTeacherApi = null;
    private static PutiTeacherModel gCommonModel = null;
    private MediaType mMediaType;

    public PutiTeacherModel(){
        mTeacherApi = RetrofitUtil.getRetrofit().create(PutiTeacherApi.class);
        mMediaType = okhttp3.MediaType.parse("application/json; charset=utf-8");
    }

    public static PutiTeacherModel getInstance(){
        if (gCommonModel == null){
            gCommonModel = new PutiTeacherModel();
            return gCommonModel;
        }
        return  gCommonModel;
    }

    public static void clearInstance(){
        gCommonModel = null;
    }


    /**
     * 获取教师所带班级
     * @param termUID 学期主键，为空时默认当前学期
     * @param listener
     */
    public void getClass(String termUID, final BaseListener listener){
        mTeacherApi.getClass(termUID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }

    /**
     * 获取学生列表
     * @param classUID 班级id 为空时代表查询所有所带班级
     * @param termUID 学期，默认当前学期
     * @param status -1不限 0正常 1转班 2退学 3休学 4毕业
     * @param pageIndex 查询起始下标
     * @param pageSize 查询个数
     * @param listener
     */
    public void getStudent( String classUID,String termUID,int status,int pageIndex,int pageSize,final BaseListener listener){
        mTeacherApi.getStudent(classUID, termUID, status, pageIndex, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {

                        dealJson(responseInfo,listener);
                    }
                });
    }

    //获取自己的id
    public void getUid(final BaseListener listener){
        mTeacherApi.getUid()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                       dealJson(responseInfo,listener);
                    }
                });
    }

    /**
     * 获取教师的任课情况
     * @param teacherUID 教师id
     * @param listener
     */
    public void queryTeacherRecords(String teacherUID,final BaseListener listener){
        mTeacherApi.getRecords(teacherUID,"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }

    /**
     * 学生事件处理
     * @param str
     * @param listener
     */
    public void dealEvent(String str,final BaseListener listener){
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str);
        mTeacherApi.eventDeal(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }

    /**
     * 批量处理事件
     * @param str
     * @param listener
     */
    public void dealsEvent(String str,final BaseListener listener){
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str);
        mTeacherApi.eventDeals(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PutiCommonSubscriber(listener){
                    @Override
                    public void onNext(BaseResponseInfo responseInfo) {
                        dealJson(responseInfo,listener);
                    }
                });
    }
}

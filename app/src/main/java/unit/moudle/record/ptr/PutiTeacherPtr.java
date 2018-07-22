package unit.moudle.record.ptr;

import android.content.Context;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.bean.Teacher;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import unit.api.PutiCommonModel;
import unit.api.PutiTeacherModel;
import unit.entity.ClaRecordEntity;
import unit.entity.TeacherBaseInfo;
import unit.moudle.record.holder.ClaRecordInfo;
import unit.moudle.record.holder.TeacherBaseInfoHolder;
import unit.moudle.record.holder.TeacherClassManagerHolder;
import unit.moudle.record.view.PutiTeacherView;
import unit.util.UserInfoUtils;

/**
 * Created by lei on 2018/6/18.
 */

public class PutiTeacherPtr implements BaseMvpPtr {

    private Context mContext;
    private PutiTeacherView mView;

    private TeacherBaseInfoHolder teacherBaseInfoHolder;
    private TeacherClassManagerHolder teacherClassManagerHolder;

    public PutiTeacherPtr(Context mContext, PutiTeacherView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void star() {
        initTeacherInfoHolder();
        initTeacherClassMangerHolder();
        queryTeaBaseInfo();
        queryTeaClassRecord();
    }

    @Override
    public void stop() {

    }

    private void initTeacherInfoHolder(){
        if (teacherBaseInfoHolder == null){
            teacherBaseInfoHolder = new TeacherBaseInfoHolder(mContext);
        }
        mView.addBaseInfoLayout(teacherBaseInfoHolder.getRootView());
    }

    private void initTeacherClassMangerHolder(){
        if (teacherClassManagerHolder == null){
            teacherClassManagerHolder = new TeacherClassManagerHolder(mContext);
        }
        mView.addClassManageLayout(teacherClassManagerHolder.getRootView());
    }

    //获取教师的基础信息
    private void queryTeaBaseInfo(){
        PutiCommonModel.getInstance().queryTeaInfo(UserInfoUtils.getUid(),new BaseListener(TeacherBaseInfo.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                TeacherBaseInfo info = (TeacherBaseInfo) infoObj;
                if (teacherBaseInfoHolder != null){
                    teacherBaseInfoHolder.setData(info);
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show(errorMessage);
            }
        });
    }
    //教师档案：获取教师所有的任课/班主任记录
    private void queryTeaClassRecord(){
        PutiTeacherModel.getInstance().queryTeacherRecords(UserInfoUtils.getUid(),new BaseListener(ClaRecordEntity.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                ClaRecordEntity entity = (ClaRecordEntity) infoObj;
                buildRecordList(entity);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show(errorMessage);
            }
        });
    }


    private void buildRecordList( ClaRecordEntity entity){
        ArrayList<ClaRecordInfo> list = new ArrayList<>();

        List<ClaRecordEntity.ClaHeadRecord> claHeadRecords = entity.getClaHeadRecords();
        for (int i = 0; i < claHeadRecords.size(); i++) {
            ClaRecordInfo info = new ClaRecordInfo();
            if (null != claHeadRecords.get(i)) {
                info.setClassName(claHeadRecords.get(i).getClassName());
            }
            info.setMajor("班主任");
            info.setNum(i+1);
            list.add(info);
        }
        List<ClaRecordEntity.ClaTeaRecord> claTeaRecords = entity.getClaTeaRecords();
        for (int i = 0; i < claTeaRecords.size(); i++) {
            ClaRecordInfo info = new ClaRecordInfo();
            if (null != claTeaRecords.get(i)) {
                info.setClassName(claTeaRecords.get(i).getClassName());
            }
            info.setMajor("任课老师");
            info.setNum(claHeadRecords.size()+i+1);
            list.add(info);
        }

        if (teacherClassManagerHolder != null){
            teacherClassManagerHolder.setData(list);
        }
    }

}

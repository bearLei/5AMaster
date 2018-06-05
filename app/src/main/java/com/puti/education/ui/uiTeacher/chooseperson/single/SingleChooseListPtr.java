package com.puti.education.ui.uiTeacher.chooseperson.single;


import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;

import com.puti.education.App;
import com.puti.education.base.BaseMvpPtr;
import com.puti.education.bean.ClassInfo;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.ui.uiTeacher.chooseperson.ChoosePersonView;
import com.puti.education.util.CharacterParser;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * created by lei at 2017/12/30
 */

public class SingleChooseListPtr implements BaseMvpPtr {

    private static final String TAG = "SingleChooseListPtr";
    private Activity context;
    private ChoosePersonView mView;
    private String mClassId;//存个上次加载学生列表时候的id
    private ArrayList<SingleChooseBean> mData;
    private ArrayList<ClassInfo> mClassList;//班级集合
    private ArrayList<String> mClassNameList;//班级名字集合

    private boolean initClassFlag;//设置个标签位来区分是否拉取过班级


    public SingleChooseListPtr(Activity context) {
        this.context = context;
        mData = new ArrayList<>();
        mClassList = new ArrayList<>();
    }


    public void attatchView(ChoosePersonView view) {
        mView = view;
    }

    private void updataList(){
        spellList();
        mView.updateList(mData);
    }
    /**
     * 教师列表
     * @param schoolId
     */
    public void getTeacherList(String schoolId,String keyword){
        mView.showLoading(true);
        CommonModel.getInstance().getTeacherList(schoolId, keyword, new BaseListener(SingleChooseBean.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                mView.showLoading(false);
                mData.clear();
                mData = (ArrayList<SingleChooseBean>)listObj;
                updataList();
            }
            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                mView.showLoading(false);
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });
    }

    /**
     * 班级
     * @param schoolId
     */
    public void getClassList(String schoolId){
        CommonModel.getInstance().getClassList(schoolId,new BaseListener(ClassInfo.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                List<ClassInfo> allClasses = (List<ClassInfo>) listObj;
                initClassFlag = true;
                mClassList.clear();
                //如果是异常事件，则教师去选择学生,则只能选择所任课的学生; 如果是学生处教师，则可以选择所有班级
                //如果是普通事件，则都可以选择
                boolean isAffairs = ConfigUtil.getInstance(context).get(Constant.KEY_IS_STUDENT_AFFAIRS, false);
                if (!mView.isAbnormal()){
                   if (isAffairs){
                       mClassList.addAll(allClasses);
                   }else {
                       mClassList.addAll(setLeadClasses(allClasses));
                   }
                }else {
                    mClassList.addAll(allClasses);
                }{
                    /**这里一段代码的逻辑是  查找本地是否有存储上次选择的班级名字
                     * 如果有的话直接加载上次选择的班级学生列表，如果没有，就默认第一个
                     但是特别坑的是把班级列表的数据缓存到application那层，mmp.待优化
                     */

                    String lastClassUid = ConfigUtil.getInstance(context).get("classid", "");
                    String lastClassName= ConfigUtil.getInstance(context).get("classname", "");
                    boolean haveLastClass = false;
                    for (int i = 0 ;i< mClassList.size();i++){
                        if (!TextUtils.isEmpty(lastClassUid) && mClassList.get(i).uid.equals(lastClassUid)){
                            haveLastClass = true;
                        }
                    }
                    if (haveLastClass){
                        mClassId = lastClassUid;
                        mView.setClassName(lastClassName);
                    }else{
                        mClassId = mClassList.get(0).uid;
                    }
                    getStudentList(mClassId,"");
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }

        });
    }

    /**
     * 学生列表
     * @param keyWord  搜索关键字
     */
    public void getStudentList(String classId,String keyWord){
        if (!initClassFlag) return;
        LogUtil.d(TAG,"getStudent");
        mView.showLoading(true);
        CommonModel.getInstance().getStudentList(classId,keyWord,new BaseListener(SingleChooseBean.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                mView.showLoading(false);
                mData.clear();
                mData = (ArrayList<SingleChooseBean>) listObj;
                updataList();
            }
            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                mView.showLoading(false);
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });
    }

    //修改下本地保存的班级信息
    public void chooseClass(int position){
        ClassInfo classInfo = mClassList.get(position);
        mClassId = classInfo.uid;
        ConfigUtil.getInstance(context).put("classid", classInfo.uid);
        ConfigUtil.getInstance(context).put("classname", classInfo.name);
        ConfigUtil.getInstance(context).commit();

        mView.setClassName(classInfo.name);
    }

    public String getmClassId() {
        return mClassId;
    }

    public ArrayList<ClassInfo> getmClassList() {
        return mClassList;
    }

    public ArrayList<String> getmClassNameList() {
       if (mClassNameList == null){
           mClassNameList = new ArrayList<>();
           mClassNameList.clear();
           for (int i = 0; i < mClassList.size(); i++) {
               ClassInfo classInfo = mClassList.get(i);
               mClassNameList.add(classInfo.name);
           }
       }
       return mClassNameList;
    }

    //为列表设置拼音 并且按照拼音排序
    private void spellList(){
        CharacterParser characterParser = new CharacterParser();
        for (SingleChooseBean info:mData) {
            info.setPinyin(characterParser.getSelling(info.getName()));
            if (TextUtils.isEmpty(info.getPinyin())
                    || Constant.ALPHABETS.indexOf(info.getPinyin().charAt(0)) == -1) {
                info.setPinyin("#");
            }
        }
        SingleChoosePinyinComparator comparator = new SingleChoosePinyinComparator();
        Collections.sort(mData,comparator);
    }

    //如果是教师,则只显示所任课的班级
    private List<ClassInfo> setLeadClasses(List<ClassInfo> allClass){
        ArrayList<ClassInfo> mClassInfoList = new ArrayList<ClassInfo>();
        if (allClass == null || allClass.size() <= 0){
            return mClassInfoList;
        }
        List<ClassInfo> tempClass = null;
        App mApp = (App)context.getApplication();
        if (mApp != null){
            tempClass = mApp.getClassList();
            if (tempClass == null || tempClass.size() <= 0){
                return mClassInfoList;
            }
            for (ClassInfo tempOne: tempClass){
                for (ClassInfo allOne: allClass){
                    if (tempOne.uid.equals(allOne.uid)){
                        mClassInfoList.add(allOne);
                        break;
                    }
                }
            }
        }
        return mClassInfoList;
    }

    @Override
    public void star() {

    }

    @Override
    public void stop() {

    }
}

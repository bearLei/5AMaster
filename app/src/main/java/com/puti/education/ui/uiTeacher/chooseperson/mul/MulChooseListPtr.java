package com.puti.education.ui.uiTeacher.chooseperson.mul;


import android.app.Activity;
import android.text.TextUtils;

import com.puti.education.App;
import com.puti.education.base.BaseMvpPtr;
import com.puti.education.bean.ClassInfo;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.ui.uiTeacher.chooseperson.ChoosePersonView;
import com.puti.education.util.CharacterParser;
import com.puti.education.util.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * created by lei at 2017/12/30
 */

public class MulChooseListPtr implements BaseMvpPtr {

    public static final int TEA = 1;//教师
    public static final int STU = 2;//学生
    public static final int VoL = 3;//义工
    public static final int PAR = 4;//家长
    public static final int EXP = 5;//专家

    private Activity context;
    private ChoosePersonView mView;
    private ArrayList<MulPersonBean> mData;

    private ArrayList<MulChooseBean> mOriginData;
    public MulChooseListPtr(Activity context) {
        this.context = context;
        mData = new ArrayList<>();
        mOriginData = new ArrayList<>();
    }


    public void attatchView(ChoosePersonView view) {
        mView = view;
    }

    private void updataList(){
        spellList();
        mView.updateAllList(mData);
    }

    //为列表设置拼音 并且按照拼音排序
    private void spellList(){
        CharacterParser characterParser = new CharacterParser();
        for (MulPersonBean info:mData) {
            info.setPinyin(characterParser.getSelling(info.getRealName()));
            if (TextUtils.isEmpty(info.getPinyin())
                    || Constant.ALPHABETS.indexOf(info.getPinyin().charAt(0)) == -1) {
                info.setPinyin("#");
            }
        }
        MulChoosePinyinComparator comparator = new MulChoosePinyinComparator();
        Collections.sort(mData,comparator);
    }

    /**
     *
     * @param keyWord 搜索关键字
     */
    public void getAllUserList(String keyWord){
        mView.showLoading(true);
        CommonModel.getInstance().getAllUserList(keyWord,new BaseListener(MulChooseBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                mView.showLoading(false);
                mOriginData = (ArrayList<MulChooseBean>) listObj;
                getList(STU);
                super.responseResult(infoObj, listObj, code, status);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                mView.showLoading(false);
                super.requestFailed(status, code, errorMessage);
            }
        });

    }


   public void getList(int type){
       if (mOriginData.size() > 0){
           for (int i = 0; i < mOriginData.size(); i++) {
               MulChooseBean bean = mOriginData.get(i);
               if (type == bean.getPersonnelType()){
                   mData.clear();
                   mData.addAll(bean.getList());
                   for (MulPersonBean info:mData) {
                       info.setType(type);
                   }
                   break;
               }
           }
       }

       updataList();
   }
}

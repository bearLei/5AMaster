package com.puti.education.ui.uiTeacher.chooseperson;

import com.puti.education.base.BaseMvpView;
import com.puti.education.ui.uiTeacher.chooseperson.mul.MulPersonBean;
import com.puti.education.ui.uiTeacher.chooseperson.single.SingleChooseBean;

import java.util.ArrayList;

/**
 * created by lei at 2017/12/30
 */

public interface ChoosePersonView extends BaseMvpView {
    void parseIntent();
    void updateList(ArrayList<SingleChooseBean> data);
    void updateAllList(ArrayList<MulPersonBean> data);
    void opearateCompleteNumber(int count);
    void complete();
    boolean isAbnormal();
    void showLoading(boolean show);
    void setClassName(String name);
}

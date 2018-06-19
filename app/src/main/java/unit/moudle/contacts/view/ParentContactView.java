package unit.moudle.contacts.view;

import com.puti.education.base.BaseMvpView;

import java.util.ArrayList;

import unit.entity.ParShowContactInfo;

/**
 * Created by lei on 2018/6/19.
 */

public interface ParentContactView extends BaseMvpView {

    void success(ArrayList<ParShowContactInfo> data);
    void setClassName(String name);

}

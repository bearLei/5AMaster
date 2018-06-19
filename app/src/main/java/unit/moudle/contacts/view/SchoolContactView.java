package unit.moudle.contacts.view;

import com.puti.education.base.BaseMvpView;

import java.util.ArrayList;

import unit.entity.ContactInfo;
import unit.entity.SchoolContactInfo;

/**
 * Created by lei on 2018/6/19.
 */

public interface SchoolContactView extends BaseMvpView {

    void success(ArrayList<SchoolContactInfo> data);
}

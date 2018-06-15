package unit.moudle.eventregist.view;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.base.BaseMvpView;

import java.util.ArrayList;

import unit.moudle.eventregist.entity.ChooseStuEntity;

/**
 * Created by lei on 2018/6/15.
 */

public interface ChooseStuView extends BaseMvpView{

    void success(ArrayList<ChooseStuEntity> list);

}

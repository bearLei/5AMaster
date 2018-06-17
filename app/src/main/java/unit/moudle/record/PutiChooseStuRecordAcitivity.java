package unit.moudle.record;

import unit.moudle.eventregist.ChooseStuManager;
import unit.moudle.eventregist.PutiChooseStuActivity;

/**
 * Created by lei on 2018/6/18.
 */

public class PutiChooseStuRecordAcitivity extends PutiChooseStuActivity {

    @Override
    public int getRefer() {
        return ChooseStuManager.Record_Choose;
    }
}

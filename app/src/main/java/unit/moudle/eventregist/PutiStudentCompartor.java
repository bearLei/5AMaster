package unit.moudle.eventregist;

import java.util.Comparator;

import unit.moudle.eventregist.entity.ChooseStuEntity;

/**
 * Created by lei on 2018/7/5.
 */

public class PutiStudentCompartor implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        ChooseStuEntity chooseStuEntityFirst = (ChooseStuEntity) o1;
        ChooseStuEntity chooseStuEntitySecond = (ChooseStuEntity) o2;

        int mFirstLetter = chooseStuEntityFirst.getLetter().charAt(0);
        int mSecondLetter = chooseStuEntitySecond.getLetter().charAt(0);

        if (mSecondLetter > mFirstLetter){
         return -1;
        }else if (mSecondLetter == mFirstLetter ){
            return 0;
        }else {
            return 1;
        }
    }
}

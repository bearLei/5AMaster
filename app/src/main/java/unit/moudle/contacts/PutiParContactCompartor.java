package unit.moudle.contacts;

import java.util.Comparator;

import unit.entity.ParShowContactInfo;
import unit.moudle.eventregist.entity.ChooseStuEntity;

/**
 * Created by lei on 2018/7/5.
 */

public class PutiParContactCompartor implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        ParShowContactInfo showContactEntityFirst = (ParShowContactInfo) o1;
        ParShowContactInfo showContactSecond = (ParShowContactInfo) o2;

        int mFirstLetter = showContactEntityFirst.getLetter().charAt(0);
        int mSecondLetter = showContactSecond.getLetter().charAt(0);

        if (mSecondLetter > mFirstLetter){
         return -1;
        }else if (mSecondLetter == mFirstLetter ){
            return 0;
        }else {
            return 1;
        }
    }
}

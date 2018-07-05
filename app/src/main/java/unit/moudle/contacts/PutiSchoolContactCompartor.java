package unit.moudle.contacts;

import java.util.Comparator;

import unit.entity.ParShowContactInfo;
import unit.entity.SchoolContactInfo;

/**
 * Created by lei on 2018/7/5.
 */

public class PutiSchoolContactCompartor implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        SchoolContactInfo showContactEntityFirst = (SchoolContactInfo) o1;
        SchoolContactInfo showContactSecond = (SchoolContactInfo) o2;

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

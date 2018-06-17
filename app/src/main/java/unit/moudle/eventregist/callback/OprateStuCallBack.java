package unit.moudle.eventregist.callback;

import unit.entity.Student;

/**
 * Created by lei on 2018/6/17.
 */

public interface OprateStuCallBack {
    void chooseStu(Student student);
    void removeStu(Student student);
}

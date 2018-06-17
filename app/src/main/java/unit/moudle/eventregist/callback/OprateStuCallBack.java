package unit.moudle.eventregist.callback;

import unit.entity.StudentEntity;

/**
 * Created by lei on 2018/6/17.
 */

public interface OprateStuCallBack {
    void chooseStu(StudentEntity.Student student);
    void removeStu(StudentEntity.Student student);
}

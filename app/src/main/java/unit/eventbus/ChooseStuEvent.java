package unit.eventbus;

import java.util.ArrayList;

import unit.entity.StudentEntity;

/**
 * Created by lei on 2018/6/17.
 */

public class ChooseStuEvent {
    private ArrayList<StudentEntity.Student> list;

    public ArrayList<StudentEntity.Student> getList() {
        return list;
    }

    public void setList(ArrayList<StudentEntity.Student> list) {
        this.list = list;
    }

    public ChooseStuEvent() {
    }
}

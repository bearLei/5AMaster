package unit.eventbus;

import java.util.ArrayList;

import unit.entity.Student;

/**
 * Created by lei on 2018/6/17.
 */

public class ChooseStuEvent {
    private ArrayList<Student> list;

    public ArrayList<Student> getList() {
        return list;
    }

    public void setList(ArrayList<Student> list) {
        this.list = list;
    }

    public ChooseStuEvent() {
    }
}

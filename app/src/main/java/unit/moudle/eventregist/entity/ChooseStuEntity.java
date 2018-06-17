package unit.moudle.eventregist.entity;

import java.util.ArrayList;

import unit.entity.Student;

/**
 * Created by lei on 2018/6/15.
 * 涉事人列表展示的数据
 */

public class ChooseStuEntity {
    private String letter;
    private ArrayList<Student> mStuents;

    public ChooseStuEntity() {
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public ArrayList<Student> getmStuents() {
        return mStuents;
    }

    public void setmStuents(ArrayList<Student> mStuents) {
        this.mStuents = mStuents;
    }
}

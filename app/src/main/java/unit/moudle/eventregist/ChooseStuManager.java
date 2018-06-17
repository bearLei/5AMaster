package unit.moudle.eventregist;

import java.util.ArrayList;

import unit.entity.Student;

/**
 * Created by lei on 2018/6/15.
 * 涉事人列表 操作类
 */

public class ChooseStuManager {
    public static int Event_Choose = 1;//来自事件添加涉事人

    public static int Record_Choose = 2;//来自我的档案
    //选中的学生
    public static  ArrayList<Student> students = new ArrayList<>();

    public boolean container(Student student){
        for (int i = 0; i < students.size(); i++) {
            Student tempStudent = students.get(i);
            if (tempStudent.getStudentUID().equals(student.getStudentUID())){
                return true;
            }
        }
        return false;
    }

}

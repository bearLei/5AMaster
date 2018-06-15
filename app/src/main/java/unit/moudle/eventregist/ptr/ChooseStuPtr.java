package unit.moudle.eventregist.ptr;

import android.content.Context;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.CharacterParser;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import unit.api.PutiTeacherModel;
import unit.entity.ClassSimple;
import unit.entity.StudentEntity;
import unit.moudle.eventregist.entity.ChooseStuEntity;
import unit.moudle.eventregist.view.ChooseStuView;

/**
 * Created by lei on 2018/6/14.
 */

public class ChooseStuPtr implements BaseMvpPtr {

    private Context mContext;
    private ChooseStuView mView;

    private ArrayList<ClassSimple> mClassList;
    private CharacterParser characterParser;
    private ArrayList<ChooseStuEntity> mStudentList;
    private Map<String,ArrayList<StudentEntity.Student>> studentMap;
    private  StudentEntity entity;
    public ChooseStuPtr(Context mContext, ChooseStuView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void star() {
        if (characterParser == null){
            characterParser = new CharacterParser();
        }
        if (mStudentList == null){
            mStudentList = new ArrayList<>();
        }
        if (studentMap == null){
            studentMap = new HashMap<>();
        }
        queryClass();
    }

    @Override
    public void stop() {

    }

    public void queryClass(){
        PutiTeacherModel.getInstance().getClass("",new BaseListener(ClassSimple.class){
            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
                mClassList = (ArrayList<ClassSimple>) listObj;
                queryStudent(mClassList.get(0).getUID());
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("拉取班级列表失败");
            }
        });
    }

    public void queryStudent(String classUid){
        PutiTeacherModel.getInstance().getStudent(classUid,"",0,1,Integer.MAX_VALUE,new BaseListener(StudentEntity.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                entity = (StudentEntity) infoObj;
                handleResult();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("拉取学生列表失败");
            }
        });
    }

    public void handleResult() {
        final List<StudentEntity.Student> students = entity.getStudents();
        final int size = students.size();
        for (int i = 0; i < size; i++) {
            StudentEntity.Student student = students.get(i);
            String s = getSelling(student.getStudentName());
            if (studentMap.containsKey(s)) {
                studentMap.get(s).add(student);
            } else {
                ArrayList<StudentEntity.Student> list = new ArrayList<StudentEntity.Student>();
                list.add(student);
                studentMap.put(s, list);
            }
        }
        Iterator<Map.Entry<String, ArrayList<StudentEntity.Student>>> iterator = studentMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<StudentEntity.Student>> entry = iterator.next();
            ChooseStuEntity student = new ChooseStuEntity();
            student.setLetter(entry.getKey());
            student.setmStuents(entry.getValue());
            mStudentList.add(student);
        }
        mView.success(mStudentList);

    }

    private String getSelling(String name){
        if (characterParser == null){
            characterParser = new CharacterParser();
        }
        String s = characterParser.getSelling(name);
        if (s.length() > 0){
            return String.valueOf(s.charAt(0));
        }
        return "";
    }

}

package unit.moudle.contacts.ptr;

import android.content.Context;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.CharacterParser;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import unit.api.PutiCommonModel;
import unit.entity.ContactInfo;
import unit.entity.SchoolContactInfo;
import unit.entity.Student;
import unit.entity.TeacherContactInfo;
import unit.moudle.contacts.view.SchoolContactView;
import unit.moudle.eventregist.entity.ChooseStuEntity;

/**
 * Created by lei on 2018/6/19.
 */

public class SchoolContactPtr implements BaseMvpPtr {

    private Context mContext;
    private SchoolContactView mView;
    private CharacterParser characterParser;
    private Map<String,ArrayList<TeacherContactInfo>> contactMap;
    private ArrayList<SchoolContactInfo> contactList;
    public SchoolContactPtr(Context mContext, SchoolContactView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void star() {
        if (contactMap == null){
            contactMap = new HashMap<>();
        }
        if (contactList == null){
            contactList = new ArrayList<>();
        }
        if (characterParser == null){
            characterParser = new CharacterParser();
        }
        queryData();
    }

    @Override
    public void stop() {

    }

    private void queryData(){
        PutiCommonModel.getInstance().getTeacherBook(new BaseListener(ContactInfo.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                ContactInfo contactInfo = (ContactInfo) infoObj;
                handleResult((ArrayList<TeacherContactInfo>) contactInfo.getTeachers());
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show(errorMessage);
            }
        });
    }
    public void handleResult(ArrayList<TeacherContactInfo> contactInfos) {

        final int size = contactInfos.size();
        contactMap.clear();
        contactList.clear();
        for (int i = 0; i < size; i++) {
            TeacherContactInfo info = contactInfos.get(i);
            String s = getSelling(info.getRealName());
            if (contactMap.containsKey(s)) {
                contactMap.get(s).add(info);
            } else {
                ArrayList<TeacherContactInfo> list = new ArrayList<>();
                list.add(info);
                contactMap.put(s, list);
            }
        }
        Iterator<Map.Entry<String, ArrayList<TeacherContactInfo>>> iterator = contactMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<TeacherContactInfo>> entry = iterator.next();
            SchoolContactInfo schoolContactInfo = new SchoolContactInfo();
            schoolContactInfo.setLetter(entry.getKey());
            schoolContactInfo.setContactInfos(entry.getValue());
            contactList.add(schoolContactInfo);
        }
        mView.success(contactList);

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

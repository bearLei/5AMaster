package unit.moudle.contacts.ptr;

import android.content.Context;
import android.view.View;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.CharacterParser;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import unit.api.PutiCommonModel;
import unit.api.PutiTeacherModel;
import unit.entity.ClassSimple;
import unit.entity.ContactInfo;
import unit.entity.ParContactInfo;
import unit.entity.ParShowContactInfo;
import unit.entity.SchoolContactInfo;
import unit.moudle.contacts.view.ParentContactView;

/**
 * Created by lei on 2018/6/19.
 */

public class ParentContactPtr implements BaseMvpPtr {

    private Context mContext;
    private ParentContactView mView;

    private Map<String,ArrayList<ParContactInfo.ParContactDetailInfo>> contactMap;
    private ArrayList<ParShowContactInfo> contactList;
    private CharacterParser characterParser;
    private ArrayList<ClassSimple> mClassList;

    public ParentContactPtr(Context mContext, ParentContactView mView) {
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
                //默认拉取第一个班级的家长
                queryData(mClassList.get(0).getUID());
                mView.setClassName(mClassList.get(0).getName());
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("拉取班级列表失败");
            }
        });
    }
    private void queryData(String classId){
        PutiCommonModel.getInstance().getParentBook(classId,new BaseListener(ParContactInfo.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                ParContactInfo info = (ParContactInfo) infoObj;
                if (info != null) {
                    handleResult((ArrayList<ParContactInfo.ParContactDetailInfo>) info.getParents());
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show(errorMessage);
                mView.hideLoading();
                mView.showErrorView();

            }
        });
    }

    public void handleResult(ArrayList<ParContactInfo.ParContactDetailInfo> contactInfos) {

        final int size = contactInfos.size();
        contactMap.clear();
        contactList.clear();
        for (int i = 0; i < size; i++) {
            ParContactInfo.ParContactDetailInfo info = contactInfos.get(i);
            String s = getSelling(info.getStudentName());
            if (contactMap.containsKey(s)) {
                contactMap.get(s).add(info);
            } else {
                ArrayList<ParContactInfo.ParContactDetailInfo> list = new ArrayList<>();
                list.add(info);
                contactMap.put(s, list);
            }
        }
        Iterator<Map.Entry<String, ArrayList<ParContactInfo.ParContactDetailInfo>>> iterator = contactMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<ParContactInfo.ParContactDetailInfo>> entry = iterator.next();
            ParShowContactInfo parShowContactInfo = new ParShowContactInfo();
            parShowContactInfo.setLetter(entry.getKey());
            parShowContactInfo.setContactInfos(entry.getValue());
            contactList.add(parShowContactInfo);
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
    //班级筛选列表
    public void showClassDialog(View view){
        ArrayList<String> list = new ArrayList<>();
        int size = mClassList.size();
        for (int i = 0; i < size; i++) {
            list.add(mClassList.get(i).getName());
        }
        final CommonDropView dropView = new CommonDropView(mContext,view,list);
        dropView.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String uid = mClassList.get(position).getUID();
                String name = mClassList.get(position).getName();
                mView.setClassName(name);
                queryData(uid);
                dropView.dismiss();
            }
        });
        dropView.showAsDropDown(view);
    }
}

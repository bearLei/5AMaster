package unit.moudle.contacts.ptr;

import android.content.Context;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.util.CharacterParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    private Map<String,ArrayList<ParContactInfo>> contactMap;
    private ArrayList<ParShowContactInfo> contactList;
    private CharacterParser characterParser;

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
    }

    @Override
    public void stop() {

    }

    private void queryData(){

    }

    public void handleResult(ArrayList<ParContactInfo> contactInfos) {

        final int size = contactInfos.size();
        contactMap.clear();
        contactList.clear();
        for (int i = 0; i < size; i++) {
            ParContactInfo info = contactInfos.get(i);
            String s = getSelling(info.getName());
            if (contactMap.containsKey(s)) {
                contactMap.get(s).add(info);
            } else {
                ArrayList<ParContactInfo> list = new ArrayList<ParContactInfo>();
                list.add(info);
                contactMap.put(s, list);
            }
        }
        Iterator<Map.Entry<String, ArrayList<ParContactInfo>>> iterator = contactMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<ParContactInfo>> entry = iterator.next();
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

}

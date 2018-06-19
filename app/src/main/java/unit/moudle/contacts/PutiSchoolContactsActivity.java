package unit.moudle.contacts;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.widget.QuickIndexBar;

import java.util.ArrayList;

import butterknife.BindView;
import unit.entity.ContactInfo;
import unit.entity.SchoolContactInfo;
import unit.moudle.contacts.adapter.SchoolContactAdapter;
import unit.moudle.contacts.ptr.SchoolContactPtr;
import unit.moudle.contacts.view.SchoolContactView;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/19.
 * 校园通讯录
 */

public class PutiSchoolContactsActivity extends PutiActivity implements SchoolContactView {
    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.quick_indexbar)
    QuickIndexBar quickIndexbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private SchoolContactPtr mPtr;
    private ArrayList<SchoolContactInfo> mData;
    private SchoolContactAdapter mAdapter;
    @Override
    public int getContentView() {
        return R.layout.puti_school_contacts_activity;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null){
            mPtr = new SchoolContactPtr(this,this);
        }
    }

    @Override
    public void ParseIntent() {

    }

    @Override
    public void AttachPtrView() {

    }

    @Override
    public void DettachPtrView() {

    }

    @Override
    public void InitView() {

        headview.setCallBack(new HeadView.HeadViewCallBack() {
            @Override
            public void backClick() {
                finish();
            }
        });
        headview.setTitle("校园通讯录");

        if (mData == null){
            mData = new ArrayList<>();
        }
        if (mAdapter == null){
            mAdapter = new SchoolContactAdapter(this,mData);
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(mAdapter);

        //字母滑动回调
        quickIndexbar.setOnLetterChangeListener(new QuickIndexBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int size = mData.size();
                for (int i = 0; i < size; i++) {
                    SchoolContactInfo info = mData.get(i);
                    if (letter.equalsIgnoreCase(info.getLetter())){
                        recyclerview.scrollToPosition(i);
                        break;
                    }
                }
            }

            @Override
            public void onReset() {

            }
        });

    }

    @Override
    public void Star() {
        mPtr.star();
    }

    @Override
    public void success(ArrayList<SchoolContactInfo> data) {
        mData.clear();
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
    }
}

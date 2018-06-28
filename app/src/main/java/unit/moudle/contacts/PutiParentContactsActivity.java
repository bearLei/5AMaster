package unit.moudle.contacts;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.widget.QuickIndexBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.entity.ParShowContactInfo;
import unit.moudle.contacts.adapter.ParContactAdapter;
import unit.moudle.contacts.ptr.ParentContactPtr;
import unit.moudle.contacts.view.ParentContactView;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/19.
 */

public class PutiParentContactsActivity extends PutiActivity implements ParentContactView {
    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.class_name)
    TextView className;
    @BindView(R.id.filter_class)
    LinearLayout filterClass;
    @BindView(R.id.quick_indexbar)
    QuickIndexBar quickIndexbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;


    private ParentContactPtr mPtr;
    private ArrayList<ParShowContactInfo> mData;
    private ParContactAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.puti_parent_contacts_activity;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null) {
            mPtr = new ParentContactPtr(this, this);
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
        headview.setTitle("家长通讯录");

        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new ParContactAdapter(this, mData);
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
                    ParShowContactInfo info = mData.get(i);
                    if (letter.equalsIgnoreCase(info.getLetter())) {
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
    public void success(ArrayList<ParShowContactInfo> data) {
        mData.clear();
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setClassName(String name) {
        className.setText(name);
    }


    @OnClick(R.id.filter_class)
    public void onClick() {
        mPtr.showClassDialog(filterClass);
    }
}

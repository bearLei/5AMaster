package unit.moudle.contacts;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.widget.QuickIndexBar;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.entity.ParShowContactInfo;
import unit.moudle.contacts.adapter.ParContactAdapter;
import unit.moudle.contacts.ptr.ParentContactPtr;
import unit.moudle.contacts.view.ParentContactView;
import unit.widget.EmptyView;
import unit.widget.HeadView;
import unit.widget.LoadingView;

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
    @BindView(R.id.empty_view)
    EmptyView emptyView;
    @BindView(R.id.loading_view)
    LoadingView loadingView;


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

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String insert = s.toString();
                mPtr.queryData(insert);
            }
        });
    }

    @Override
    public void Star() {
        mPtr.star();
    }


    @Override
    public void success(ArrayList<ParShowContactInfo> data) {
        hideLoading();
        if (data == null || data.size() == 0){
            showEmptyView();
        }
        showSuccessView();
        mData.clear();
        mData.addAll(data);
        Collections.sort(mData,new PutiParContactCompartor());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setClassName(String name) {
        className.setText(name);
    }

    @Override
    public String getEditName() {
        return search.getText().toString();
    }


    @OnClick(R.id.filter_class)
    public void onClick() {
        mPtr.showClassDialog(filterClass);
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.showErrorDataView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.showNoDataView("暂无数据");
    }
    private void showSuccessView(){
        emptyView.setVisibility(View.GONE);
        hideLoading();
        recyclerview.setVisibility(View.VISIBLE);
    }
}

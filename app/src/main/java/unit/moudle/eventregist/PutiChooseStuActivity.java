package unit.moudle.eventregist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import unit.entity.Student;
import unit.eventbus.ChooseStuEvent;
import unit.eventbus.PutiEventBus;
import unit.moudle.eventregist.adapter.ChooseStuAdapter;
import unit.moudle.eventregist.callback.OprateStuCallBack;
import unit.moudle.eventregist.entity.ChooseStuEntity;
import unit.moudle.eventregist.ptr.ChooseStuPtr;
import unit.moudle.eventregist.view.ChooseStuView;
import unit.widget.EmptyView;
import unit.widget.HeadView;
import unit.widget.LoadingView;

/**
 * Created by lei on 2018/6/14.
 * 涉事人页面
 */

public class PutiChooseStuActivity extends PutiActivity implements ChooseStuView, OprateStuCallBack {

    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.quick_indexbar)
    QuickIndexBar quickIndexbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.class_name)
    TextView className;
    @BindView(R.id.filter_class)
    LinearLayout VFilterClassLayout;
    @BindView(R.id.empty_view)
    EmptyView emptyView;
    @BindView(R.id.loading_view)
    LoadingView loadingView;

    private ChooseStuPtr mPtr;
    private ArrayList<ChooseStuEntity> mData;
    private ChooseStuAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.puti_choose_stu_activity;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void BindPtr() {
        if (mPtr == null) {
            mPtr = new ChooseStuPtr(this, this);
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
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new ChooseStuAdapter(this, mData);
            mAdapter.setOprateStuCallBack(this);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(mAdapter);

        headview.setCallBack(new HeadView.HeadViewCallBack() {
            @Override
            public void backClick() {
                finish();

            }
        });
        if (getRefer() == ChooseStuManager.Event_Choose) {
            headview.showRightTV(true);
            setChooseTitle(ChooseStuManager.students.size());
            headview.setRightCallBack(new HeadView.HeadViewRightCallBack() {
                @Override
                public void click() {
                    ChooseStuEvent event = new ChooseStuEvent();
                    event.setList(ChooseStuManager.students);
                    PutiEventBus.post(event);
                    finish();
                }
            });
        } else {
            headview.showRightTV(false);
            headview.setTitle("学生档案");
        }
        //字母滑动回调
        quickIndexbar.setOnLetterChangeListener(new QuickIndexBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int size = mData.size();
                for (int i = 0; i < size; i++) {
                    ChooseStuEntity entity = mData.get(i);
                    if (letter.equalsIgnoreCase(entity.getLetter())) {
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
                mPtr.queryStudent(s.toString());
            }
        });
    }

    @Override
    public void Star() {
        mPtr.star();
    }

    @Override
    public void success(ArrayList<ChooseStuEntity> list) {
        if (list == null || list.size() == 0) {
            showEmptyView();
            return;
        }
        showSuccessView();
        mData.clear();
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setClassName(String name) {
        className.setText(name);
    }

    @Override
    public void setChooseTitle(int size) {
        if (headview != null) {
            if (size > 0) {
                headview.setTitle("选择 (" + size + ")人");
            } else {
                headview.setTitle("选择");
            }
        }
    }

    @Override
    public String getEditSearch() {
        return search.getText().toString();
    }


    @OnClick(R.id.filter_class)
    public void onClick() {
        mPtr.showClassDialog(VFilterClassLayout);
    }

    @Override
    public void chooseStu(Student student) {
        ChooseStuManager.students.add(student);
        setChooseTitle(ChooseStuManager.students.size());
    }

    @Override
    public void removeStu(Student student) {
        ChooseStuManager.students.remove(student);
        setChooseTitle(ChooseStuManager.students.size());
    }

    public int getRefer() {
        return ChooseStuManager.Event_Choose;
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
        recyclerview.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        hideLoading();
    }

}

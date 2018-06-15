package unit.moudle.eventregist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.widget.QuickIndexBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.StudentEntity;
import unit.moudle.eventregist.adapter.ChooseStuAdapter;
import unit.moudle.eventregist.entity.ChooseStuEntity;
import unit.moudle.eventregist.ptr.ChooseStuPtr;
import unit.moudle.eventregist.view.ChooseStuView;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/14.
 */

public class PutiChooseStuActivity extends PutiActivity implements ChooseStuView {

    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.quick_indexbar)
    QuickIndexBar quickIndexbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private ChooseStuPtr mPtr;
    private ArrayList<ChooseStuEntity> mData;
    private ChooseStuAdapter mAdapter;
    @Override
    public int getContentView() {
        return R.layout.puti_choose_stu_activity;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null) {
            mPtr = new ChooseStuPtr(this,this);
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
        if (mData == null){
            mData = new ArrayList<>();
        }
        if (mAdapter == null){
            mAdapter = new ChooseStuAdapter(this,mData);
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
        //字母滑动回调
        quickIndexbar.setOnLetterChangeListener(new QuickIndexBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {

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
    public void success(ArrayList<ChooseStuEntity> list) {
        if (list == null){
            return;
        }
        mData.clear();
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();
    }
}

package unit.moudle.eventregist.ptr;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.puti.education.base.BaseMvpPtr;

import java.util.ArrayList;

import unit.entity.Student;
import unit.moudle.eventregist.holder.ChooseStuHolder;
import unit.moudle.eventregist.holder.EventDescHolder;
import unit.moudle.eventregist.holder.EventEvidenceHolder;
import unit.moudle.eventregist.holder.EventTimeAndSpaceHolder;
import unit.moudle.eventregist.view.EventDetailView;

/**
 * Created by lei on 2018/6/11.
 */

public class EventDetailPtr implements BaseMvpPtr {

    private Context mContext;
    private EventDetailView mView;

    private ChooseStuHolder mChooseStuHolder;//选择学生holder
    private EventTimeAndSpaceHolder mTimeAndSpaceHolder;//时间和地点Holder
    private EventDescHolder mDescHolder;//描述holder
    private EventEvidenceHolder mEvidenceHolder;//佐证holder

    public EventDetailPtr(Context mContext, EventDetailView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }



    @Override
    public void star() {
        initChooseStuHolder();
        initTimeAndSpaceHolder();
        initDescHolder();
        initEvidenceHolder();
    }

    @Override
    public void stop() {

    }

    /**
     * 初始化选择学生holder
     */
    private void initChooseStuHolder(){
        if (mChooseStuHolder == null){
            mChooseStuHolder = new ChooseStuHolder(mContext);
            mChooseStuHolder.setList(new ArrayList<Student>());
        }
        View view = mChooseStuHolder.getRootView();
        oprateSize(view);
        mView.addChooseStuView(view);
    }

    /**
     * 初始化时间地点holder
     */
    private void initTimeAndSpaceHolder(){
        if (mTimeAndSpaceHolder == null){
            mTimeAndSpaceHolder = new EventTimeAndSpaceHolder(mContext);
            mTimeAndSpaceHolder.setData(true);
        }
        View view = mTimeAndSpaceHolder.getRootView();
        oprateSize(view);
        mView.addTimeAndSpaceView(view);
    }

    /**
     * 初始化描述holder
     */
    private void initDescHolder(){
        if (mDescHolder == null){
            mDescHolder = new EventDescHolder(mContext);
        }
        View view = mDescHolder.getRootView();
        oprateSize(view);
        mView.addDescView(view);
    }

    /**
     * 初始化佐证holder
     */
    private void initEvidenceHolder(){
        if (mEvidenceHolder == null){
            mEvidenceHolder = new EventEvidenceHolder(mContext);
        }
        View view = mEvidenceHolder.getRootView();
        oprateSize(view);
        mView.addEvidenceView(view);
    }

    private void oprateSize(View view){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
    }

    public void evidenceActivityResult(int requestCode, int resultCode, Intent data){
        if (mEvidenceHolder != null){
            mEvidenceHolder.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setChooseStu(ArrayList<Student> list){
        mChooseStuHolder.setList(list);
    }

}

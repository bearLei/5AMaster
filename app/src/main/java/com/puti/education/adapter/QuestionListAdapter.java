package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.puti.education.R;
import com.puti.education.bean.Teacher;
import com.puti.education.bean.TeacherQuestion;
import com.puti.education.util.SimpleTextWatcher;

import java.util.HashMap;

/**
 * Created by xjbin on 2017/5/11 0011.
 *
 * 新增问券
 */

public class QuestionListAdapter extends BasicRecylerAdapter<TeacherQuestion>{

    private HashMap<Integer,String> dataMap;

    private Integer index = -1;

    public QuestionListAdapter(Context context) {
        super(context);
    }

    public void setDataMap(HashMap<Integer, String> dataMap) {
        this.dataMap = dataMap;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_question_layout;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        CommonViewHolder cHolder = (CommonViewHolder) holder;
        FrameLayout delFrame = cHolder.obtainView(R.id.del_frame);
        final EditText edit = cHolder.obtainView(R.id.question_edit);
        edit.setTag(position);

        final TeacherQuestion teacherQuestion = mList.get(position);

        edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    index = (Integer) v.getTag();
                }

                return false;
            }
        });

        delFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (delLinstener != null){
                    delLinstener.click(position);
                }
            }
        });

        edit.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {

                Integer tag = (Integer) edit.getTag();
                dataMap.put(tag,s.toString());
            }
        });

        edit.setText(dataMap.get(position));

        edit.clearFocus();
        if (index != -1 && index == position) {
            edit.requestFocus();
            if (!TextUtils.isEmpty(edit.getText().toString())){
                edit.setSelection(edit.getText().toString().length());
            }
        }
    }

    public interface  DelLinstener{
        public void click(int position);
    }

    public DelLinstener delLinstener;

    public void setDelLinstener(DelLinstener delLinstener) {
        this.delLinstener = delLinstener;
    }
}

package com.puti.education.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.bean.Question;
import com.puti.education.R;
import com.puti.education.bean.Questionnaire;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.LogUtil;

import java.util.List;


/**
 * 问卷详情
 */
public class QuestionnaireDetailAdapter extends BasicRecylerAdapter<Question>{


    private final int TYPE_RADIO = 1;       //单选
    private final int TYPE_JUDGE = 3;       //判断
    private final int TYPE_CLOZE = 4;       //填空
    private final int TYPE_MULTI = 2;       //多选
    private final int TYPE_NORMAL= 99;

    public QuestionnaireDetailAdapter(Context myContext){
        super(myContext);
    }

    class MyTextWatcher implements TextWatcher {
        public MyTextWatcher(CommonViewHolder holder) {
            mHolder = holder;
        }

        private CommonViewHolder mHolder;

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d("", "afterTextChanged: " + s.toString());
            if (s != null && !"".equals(s.toString())) {
                Log.d("", "afterTextChanged111: " + s.toString());
                Integer cartPosi =  (Integer) ((EditText)mHolder.obtainView(R.id.et_content)).getTag();
                if (cartPosi != null)
                {
                    int position = cartPosi.intValue();
                    mList.get(position).answerd = (s.toString());// 当EditText数据发生改变的时候存到data变量中
                }
            }
        }

    }



    @Override
    public int getLayoutId() {
        return 0;
    }

    public void refershData(List<Question> lists){
        setDataList(lists);
    }

    @Override
    public int getItemViewType(int position) {
        Question question = mList.get(position);
        if (question == null){
            return TYPE_NORMAL;
        }
        int tempType = TYPE_NORMAL;
        switch (question.type){
            case TYPE_RADIO:
                tempType =  TYPE_RADIO;
                break;
            case TYPE_JUDGE:
                tempType =  TYPE_JUDGE;
                break;
            case TYPE_CLOZE:
                tempType =  TYPE_CLOZE;
                break;
            case TYPE_MULTI:
                tempType =  TYPE_MULTI;
                break;
            default:
                break;
        }
        return tempType;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup viewgroup, int type) {
        View view = null;
        if (type == TYPE_RADIO || type == TYPE_MULTI){
            view  = mLayoutinInflater.inflate(R.layout.qt_detail_radio_layout, viewgroup, false);
        }else if (type == TYPE_CLOZE){
            view  = mLayoutinInflater.inflate(R.layout.qt_detail_cloze_layout, viewgroup, false);
        }else {
            view  = mLayoutinInflater.inflate(R.layout.qt_detail_radio_layout, viewgroup, false);
        }

        CommonViewHolder commonViewHolder = new CommonViewHolder(view);
        return commonViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CommonViewHolder viewHolder = (CommonViewHolder) holder;
        Question qt = mList.get(position);
        viewHolder.setText(R.id.tv_question, (position + 1) + ". "+qt.question);
        if (qt.type == TYPE_RADIO){
            //单选
            LinearLayout answerlayout = viewHolder.obtainView(R.id.answer_layout);
            answerlayout.removeAllViews();
            processSingle(qt, position, answerlayout);
        }else if (qt.type == TYPE_CLOZE){
            //填空
            processCloze(qt,position, viewHolder);
        }else if (qt.type == TYPE_JUDGE) {
            //判断
            processJudge(qt,position, viewHolder);
        }else if (qt.type == TYPE_MULTI){
            LinearLayout answerlayout = viewHolder.obtainView(R.id.answer_layout);
            answerlayout.removeAllViews();
            processMulti(qt, position, answerlayout);
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myItemOnclickListener != null) {
                    myItemOnclickListener.onItemClick(position);
                }
            }
        });

    }

    //处理单选
    public void processSingle(Question qt, int position, LinearLayout container){

        View subview = null;
        int index = 0;
        if (!TextUtils.isEmpty(qt.optionAnswer)){
            String subAnswer[] = qt.optionAnswer.split(";");
            if (subAnswer != null){
                int subsize = subAnswer.length;
                boolean isChoosed = false;
                for (int j = 0; j<subsize;j++){
                    isChoosed = false;
                    if (qt.isAnswerd && subAnswer[j].equals(qt.answerd)){
                        isChoosed = true;
                    }
                    subview = setOneQuestionView(position, subAnswer[j], index,  isChoosed);
                    if (subview != null){
                        container.addView(subview);
                    }
                }

            }

        }
    }

    //处理多选
    public void processMulti(Question qt, int position, LinearLayout container){

        View subview = null;
        int index = 0;
        if (!TextUtils.isEmpty(qt.optionAnswer)){
            String subAnswer[] = qt.optionAnswer.split(";");
            if (subAnswer != null){
                int subsize = subAnswer.length;
                boolean isChoosed = false;
                for (int j = 0; j<subsize;j++){
                    isChoosed = false;
                    if (qt.isAnswerd && containAnswer(subAnswer[j],qt.answerd)){
                        isChoosed = true;
                    }
                    subview = setOneQuestionView(position, subAnswer[j], index,  isChoosed);
                    if (subview != null){
                        container.addView(subview);
                    }
                }

            }

        }
    }

    public boolean containAnswer(String option, String answers){
        boolean ret = false;
        if (!TextUtils.isEmpty(answers)){
            String subAnswer[] = answers.split(";");
            if (subAnswer != null){
                int subsize = subAnswer.length;
                for (int j = 0; j<subsize;j++){
                    if (option.equals(subAnswer[j])){
                        ret = true;
                        break;
                    }
                }
            }
        }
        return ret;
    }


    //处理填空题
    public void processCloze(Question qt, int position, CommonViewHolder vholder){
        EditText answerEt = vholder.obtainView(R.id.et_content);
        answerEt.setText(null);
        String value = qt.answerd;
        Log.d("", "user msg: " + value);
        if (qt.isAnswerd && !TextUtils.isEmpty(value)) {
            answerEt.setText(value.toString());
            Editable ea= answerEt.getText();
            Selection.setSelection(ea, ea.length());
        }
        answerEt.clearFocus();
        answerEt.setTag(position);
        answerEt.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent event) {
                // 在TOUCH的UP事件中，要保存当前的行下标，因为弹出软键盘后，整个画面会被重画
                // 在getView方法的最后，要根据index和当前的行下标手动为EditText设置焦点

                //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
                if ((view.getId() == R.id.et_content && canVerticalScroll((EditText)view))) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        //mIndex= storeIndex;
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }

                return false;

            }
        });

        answerEt.addTextChangedListener(new MyTextWatcher(vholder));
    }

    //处理判断
    public void processJudge(Question qt, int position, CommonViewHolder vholder){
        LinearLayout answerlayout = vholder.obtainView(R.id.answer_layout);
        answerlayout.removeAllViews();
        View subview = null;
        int index = 0;

        String subAnswer[] = {"是", "不是"};
        if (subAnswer != null) {
            int subsize = subAnswer.length;
            boolean isChoosed = false;
            String tempResult = "不是";

            for (int j = 0; j < subsize; j++) {
                isChoosed = false;

                if (qt.isAnswerd) {
                    if (qt.answerd.equals("1")){
                        tempResult = "是";
                    }

                    if (subAnswer[j].equals(tempResult)){
                        isChoosed = true;
                    }

                }
                subview = setOneQuestionView(position, subAnswer[j], index, isChoosed);
                if (subview != null) {
                    answerlayout.addView(subview);
                }
            }

        }
    }


    /**
     * EditText竖直方向是否可以滚动
     * @param editText 需要判断的EditText
     * @return true：可以滚动 false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }


    /*
	 * 显示具体问题的一条选项
	 */
    private View setOneQuestionView(int position, String answerItem, int index, boolean isAnswer)
    {
        LinearLayout.LayoutParams layoutparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DisPlayUtil.dip2px(mContext,44));
        TextView mtv = new TextView(mContext);
        mtv.setLayoutParams(layoutparam);
        int padding = DisPlayUtil.dip2px(mContext,13);
        mtv.setPadding(padding, 0, padding, 0);
        mtv.setTextSize(12);
        mtv.setGravity(Gravity.CENTER_VERTICAL);
        mtv.setCompoundDrawables(null, null, null, null);
        mtv.setText(answerItem);

        if (isAnswer){
            mtv.setBackgroundResource(R.color.green_select);
            mtv.setTextColor(Color.parseColor("#4C8029"));

            Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), R.mipmap.ic_item_selected, null);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示

            mtv.setCompoundDrawables(null, null, drawable, null);
        }else{
            mtv.setBackgroundResource(R.color.white);
            mtv.setTextColor(Color.parseColor("#6B6B6B"));
        }

        mtv.setTag(R.id.position_key, position);
        mtv.setTag(R.id.answer_index_key, answerItem);

        mtv.setOnClickListener(mRadioClickListener);
        return mtv;
    }

    private View.OnClickListener mRadioClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            int pos = (int)view.getTag(R.id.position_key);
            String answerStr = (String)view.getTag(R.id.answer_index_key);
            Question qt = mList.get(pos);

            if (qt.type == TYPE_JUDGE){
                if (answerStr.equals("是")){
                    qt.answerd = "1";
                }else{
                    qt.answerd = "0";
                }
            }else if (qt.type == TYPE_MULTI) {
                processMultiItem(qt, answerStr);
            }else {
                qt.answerd = answerStr;
            }
            qt.isAnswerd = true;

            notifyDataSetChanged();
        }
    };

    private void processMultiItem(Question qt, String currentAnswer){
       if (qt.isAnswerd){
           if (containAnswer(currentAnswer, qt.answerd)){
               //反选，取消此项的选择
               qt.answerd = qt.answerd.replace(currentAnswer, "");
               LogUtil.d("", "replace answer: " + qt.answerd);
//               int index = qt.answerd.indexOf(currentAnswer);
//               if (index == 0){
//                   //去掉
//               }
//               if (index > 0 && (index + currentAnswer.length() <= qt.answerd.length())){
//                   //去掉前分号
//
//               }
           }else{
               if (!TextUtils.isEmpty(qt.answerd)){
                   qt.answerd = qt.answerd + ";" + currentAnswer;
               }else{
                   qt.answerd = currentAnswer;
               }
           }
       }else{
           qt.answerd = currentAnswer;
       }
    }

    private String statusConvert(String status){
        String result = "未填写";
        if (status.equals("1")){
            result = "正在分析中";
        }else if (status.equals("2")){
            result = "已出结果";
        }
        return result;
    }


}

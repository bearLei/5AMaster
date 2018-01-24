package com.puti.education.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.Question;
import com.puti.education.bean.Questionnaire;
import com.puti.education.util.Constant;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.LogUtil;
import com.puti.education.widget.RegionNumberEditText;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 问卷详情
 */
public class QuestionnaireEtDetailAdapter extends BasicRecylerAdapter<Question>{



    private final int TYPE_HEADER = 10;
    private final int TYPE_FOOTER = 11;
    private final int TYPE_NORMAL= 99;

    private View VIEW_FOOTER;
    private View VIEW_HEADER;

    private RecyclerView mRecyclerView;
    private View.OnClickListener mDrowDownClickListener;

    public QuestionnaireEtDetailAdapter(Context myContext){
        super(myContext);
    }

    @Override
    public int getItemCount() {
        int count = (mList == null ? 0 : mList.size());
        if (VIEW_FOOTER != null) {
            count++;
        }

        if (VIEW_HEADER != null) {
            count++;
        }
        return count;
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
                    mList.get(position).isAnswerd = true;
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
        if (isHeaderView(position)) {
            return TYPE_HEADER;
        } else if (isFooterView(position)) {
            return TYPE_FOOTER;
        }

        Question question = mList.get(position);
        if (question == null){
            return TYPE_NORMAL;
        }
        int tempType = question.type;
        if (tempType < Constant.TYPE_RADIO || tempType > Constant.TYPE_COURSE_UID){
            tempType = TYPE_NORMAL;
        }
        return tempType;
    }


    public void addHeaderView(View headerView) {
        if (haveHeaderView()) {
            throw new IllegalStateException("hearview has already exists!");
        } else {
            //避免出现宽度自适应
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerView.setLayoutParams(params);
            VIEW_HEADER = headerView;
            ifGridLayoutManager();
            notifyItemInserted(0);
        }

    }

    public void addFooterView(View footerView) {
        if (haveFooterView()) {
            throw new IllegalStateException("footerView has already exists!");
        } else {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(params);
            VIEW_FOOTER = footerView;
            ifGridLayoutManager();
            notifyItemInserted(getItemCount() - 1);
        }
    }

    public void removeFooterView(View footerView) {
        if (haveFooterView()) {
            VIEW_FOOTER = null;
            notifyItemRemoved(getItemCount() - 1);
        }
    }

    private void ifGridLayoutManager() {
        if (mRecyclerView == null) return;
        final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager.SpanSizeLookup originalSpanSizeLookup =
                    ((GridLayoutManager) layoutManager).getSpanSizeLookup();
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeaderView(position) || isFooterView(position)) ?
                            ((GridLayoutManager) layoutManager).getSpanCount() :
                            1;
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        try {
            if (mRecyclerView == null && mRecyclerView != recyclerView) {
                mRecyclerView = recyclerView;
            }
            ifGridLayoutManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup viewgroup, int type) {
        View view = null;
        if (type == TYPE_FOOTER){
            view = VIEW_FOOTER;
        }else if (type == Constant.TYPE_RADIO || type == Constant.TYPE_MULTI){
            view  = mLayoutinInflater.inflate(R.layout.qt_detail_radio_layout, viewgroup, false);
        }else if (type == Constant.TYPE_CLOZE || type == Constant.TYPE_TEXTAREA || type == Constant.TYPE_NUMBERTAREA ) { //填空,简答,数字选择
            view = mLayoutinInflater.inflate(R.layout.qt_detail_cloze_layout, viewgroup, false);
        }else if (type == Constant.TYPE_TEACHER_UID || type == Constant.TYPE_STUDENT_UID ||  //下拉选择框,选择学生,老师,家长,课程
                type == Constant.TYPE_PARENT_UID || type == Constant.TYPE_COURSE_UID) {
            view = mLayoutinInflater.inflate(R.layout.qt_detail_dropdown_layout, viewgroup, false);
        }else {
            view  = mLayoutinInflater.inflate(R.layout.qt_detail_radio_layout, viewgroup, false);

        }

        CommonViewHolder commonViewHolder = new CommonViewHolder(view);
        return commonViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommonViewHolder viewHolder = (CommonViewHolder) holder;
        if (!isHeaderView(position) && !isFooterView(position)) {
            if (haveHeaderView()) {
                position--;
            }

            Question qt = mList.get(position);

            String question = operateQuestionType(position,qt.question,qt.type);

            TextView tv_question = viewHolder.obtainView(R.id.tv_question);
            tv_question.setText(question);
            if (qt.isRequired){
                tv_question.append(operateQuestion());
            }
//            TextView tv_questionRequired = viewHolder.obtainView(R.id.tv_question_required);
//            tv_questionRequired.setVisibility(qt.isRequired ? View.VISIBLE: View.GONE);
            if (qt.type == Constant.TYPE_RADIO) {
                //单选
                LinearLayout answerlayout = viewHolder.obtainView(R.id.answer_layout);
                answerlayout.removeAllViews();
                processSingle(qt, position, answerlayout);
            } else if (qt.type == Constant.TYPE_CLOZE || qt.type == Constant.TYPE_TEXTAREA || qt.type == Constant.TYPE_NUMBERTAREA) {
                //填空
                processCloze(qt, position, viewHolder);
            } else if (qt.type == Constant.TYPE_JUDGE) {
                //判断
                processJudge(qt, position, viewHolder);
            } else if (qt.type == Constant.TYPE_MULTI) {
                LinearLayout answerlayout = viewHolder.obtainView(R.id.answer_layout);
                answerlayout.removeAllViews();
                processMulti(qt, position, answerlayout);
            } else if (qt.type == Constant.TYPE_TEACHER_UID || qt.type == Constant.TYPE_STUDENT_UID ||  //下拉选择框,选择学生,老师,家长,课程
                    qt.type == Constant.TYPE_PARENT_UID || qt.type == Constant.TYPE_COURSE_UID){
                TextView tvValue = viewHolder.obtainView(R.id.et_content);
                processDrowdown(qt, position, tvValue);
            }

            final int newPosition = position;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemOnclickListener != null) {
                        myItemOnclickListener.onItemClick(newPosition);
                    }
                }
            });
        }

    }

    private SpannableString operateQuestion(){
        String brief = "*";
        SpannableString s = new SpannableString(brief);
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, brief.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
       return s;
    }

    private String operateQuestionType(int position,String question,int type){
        StringBuilder builder = new StringBuilder();
        builder.append(position+1).append(".").append(question);
        switch (type){
            case Constant.TYPE_RADIO:
                //单选
                builder.append("(单选)");
                break;
            case Constant.TYPE_MULTI:
                //多选
                builder.append("(多选)");
                break;
            case Constant.TYPE_CLOZE:
            case Constant.TYPE_TEXTAREA:
            case Constant.TYPE_NUMBERTAREA:
                //填空
                builder.append("(填空)");
                break;
            case Constant.TYPE_JUDGE:
                //判断
                builder.append("(判断)");
                break;
        }
        return builder.toString();
    }
    private boolean haveHeaderView() {
        return VIEW_HEADER != null;
    }

    public boolean haveFooterView() {
        return VIEW_FOOTER != null;
    }

    private boolean isHeaderView(int position) {
        return haveHeaderView() && position == 0;
    }

    private boolean isFooterView(int position) {
        return haveFooterView() && position == getItemCount() - 1;
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

    //处理下拉选择
    public void processDrowdown(Question qt, int position, TextView tv){

        if (!TextUtils.isEmpty(qt.answerd2)){
            tv.setText(qt.answerd2);
        }else{
            tv.setText(chooseTarget(qt.type));
        }

        tv.setTag(R.id.position_key, position);
        tv.setOnClickListener(null);
        tv.setOnClickListener(mDrowDownClickListener);
    }

    private String chooseTarget(int type){
        String result = "没有其它可选择";
        switch (type){
            case Constant.TYPE_TEACHER_UID:
                result = "请选择教师";
                break;
            case Constant.TYPE_STUDENT_UID:
                result = "请选择学生";
                break;
            case Constant.TYPE_PARENT_UID:
                result = "请选择家长";
                break;
            case Constant.TYPE_COURSE_UID:
                result = "请选择课程";
                break;
        }
        return result;
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
        RegionNumberEditText answerEt = vholder.obtainView(R.id.et_content);

        if (qt.type == Constant.TYPE_NUMBERTAREA){
            answerEt.setRegion(0,100);
            answerEt.setTextWatcher();
        }else {
            answerEt.setInputNormal();
        }

        answerEt.setOnTouchListener(null);
        //answerEt.addTextChangedListener(null);
        answerEt.setText("");

        String value = qt.answerd;
        Log.d("", "user msg: " + value);
        if (qt.isAnswerd && !TextUtils.isEmpty(value)) {
            answerEt.setText(value.toString());
            Editable ea= answerEt.getText();
            Selection.setSelection(ea, ea.length());
        }
        //answerEt.clearFocus();
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

            if (qt.type == Constant.TYPE_JUDGE){
                if (answerStr.equals("是")){
                    qt.answerd = "1";
                }else{
                    qt.answerd = "0";
                }
            }else if (qt.type == Constant.TYPE_MULTI) {
                processMultiItem(qt, answerStr);
            }else {
                qt.answerd = answerStr;
            }
            qt.isAnswerd = true;

            notifyDataSetChanged();
        }
    };

    public void setDrowChooseListener(View.OnClickListener listener){
        mDrowDownClickListener = listener;
    }


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

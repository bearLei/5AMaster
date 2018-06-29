package unit.moudle.ques;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.Choice;
import unit.entity.QuesDetailInfo;
import unit.entity.Question;
import unit.widget.FNRadioGroup;

/**
 * Created by lei on 2018/6/29.
 */

public class QuesDetailHolder extends BaseHolder<Question> {
    @BindView(R.id.position)
    TextView TPosition;
    @BindView(R.id.ques_name)
    TextView TQuesName;
    @BindView(R.id.answer_layout)
    FNRadioGroup VAnswerLayout;

    private WriteAnswerRpc answerRpc;
    public QuesDetailHolder(Context context) {
        super(context);
    }

    public QuesDetailHolder(Context context, WriteAnswerRpc answerRpc) {
        super(context);
        this.answerRpc = answerRpc;
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_ques_detail_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    public void setData(Question data,int position){
        mData = data;
        updateUI(mContext, data,position);
    }
    @Override
    protected void updateUI(Context context, Question data) {

    }
    protected void updateUI(Context context, final Question data, final int position) {
        TPosition.setText(position+1 +"·");
        TQuesName.setText(data.getQuestion());
        final List<Choice> choices = data.getChoices();
        VAnswerLayout.removeAllViews();
        for (int i = 0; i < choices.size(); i++) {
            VAnswerLayout.addView(getAnswerItem(choices.get(i).getChoice()));
        }

        VAnswerLayout.setOnCheckedChangeListener(new FNRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(FNRadioGroup group, int checkedId) {
                if (answerRpc != null){
                    RadioButton button = (RadioButton) VAnswerLayout.findViewById(checkedId);
                    if (button != null) {
                        answerRpc.choose(data.getQuestionUID(), button.getText().toString(), position);
                    }
                }
            }
        });
    }
    private RadioButton getAnswerItem(String answer){
        RadioButton radioButton = new RadioButton(mContext);
        radioButton.setText(answer);
        return radioButton;
    }

}

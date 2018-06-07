package unit.moudle.message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.MessageEntity;

/**
 * Created by lei on 2018/6/7.
 * 消息Holder
 */

public class MessageHolder extends BaseHolder<MessageEntity.MessageInfo> {

    @BindView(R.id.title)
    TextView title;

    public MessageHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_msg_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, MessageEntity.MessageInfo data) {
        if (data == null) {
            return;
        }

        title.setText(data.getMsgTitle());
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/6/7 需要处理消息点击的时候直接处理Schmeme就可以
            }
        });

    }
}

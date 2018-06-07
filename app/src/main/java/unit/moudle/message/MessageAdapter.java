package unit.moudle.message;

import android.content.Context;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;

import java.util.List;

import unit.entity.MessageEntity;

/**
 * Created by lei on 2018/6/7.
 */

public class MessageAdapter extends BaseRVAdapter {

    private List<MessageEntity.MessageInfo> mList;

    private BaseHolder mHolder;
    public MessageAdapter(Context context) {
        super(context);
    }

    public MessageAdapter(Context context, List<MessageEntity.MessageInfo> mList) {
        super(context);
        this.mList = mList;
    }

    @Override
    protected Object getItem(int position) {
        return position;
    }

    @Override
    protected int getCount() {
        if (mList == null || mList.size() == 0){
            return 0;
        }
        return mList.size();
    }

    @Override
    protected BaseHolder getViewHolder(Context context, ViewGroup parent, int viewType) {
        mHolder = new MessageHolder(context);
        return mHolder;
    }
}

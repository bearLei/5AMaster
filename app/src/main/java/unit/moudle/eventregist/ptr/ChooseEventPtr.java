package unit.moudle.eventregist.ptr;

import android.content.Context;

import com.puti.education.base.BaseMvpPtr;

import unit.moudle.eventregist.view.ChooseEventView;

/**
 * Created by lei on 2018/6/8.
 * 事件选择页面ptr
 */

public class ChooseEventPtr implements BaseMvpPtr {

    private Context mContext;
    private ChooseEventView mView;

    public ChooseEventPtr(Context mContext, ChooseEventView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void star() {

    }

    @Override
    public void stop() {

    }
}

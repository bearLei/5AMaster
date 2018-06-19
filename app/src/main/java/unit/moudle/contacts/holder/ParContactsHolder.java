package unit.moudle.contacts.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.widget.GridViewForScrollView;
import com.puti.education.widget.ListViewForScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.ParShowContactInfo;
import unit.entity.SchoolContactInfo;
import unit.moudle.contacts.adapter.ParContactHolderAdapter;
import unit.moudle.contacts.adapter.SchoolContactHolderAdapter;

/**
 * Created by lei on 2018/6/19.
 * 家长通讯录 holder
 */

public class ParContactsHolder extends BaseHolder<ParShowContactInfo> {
    @BindView(R.id.letter)
    TextView letter;
    @BindView(R.id.contact_list)
    ListViewForScrollView contactList;

    private ParContactHolderAdapter mAdapter;

    public ParContactsHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_school_contact_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, ParShowContactInfo data) {

        if (data == null || data.getContactInfos().size() == 0){
            return;
        }

        letter.setText(data.getLetter());
        if (mAdapter == null){
            mAdapter = new ParContactHolderAdapter(mContext,data.getContactInfos());
        }
        contactList.setAdapter(mAdapter);
    }
}

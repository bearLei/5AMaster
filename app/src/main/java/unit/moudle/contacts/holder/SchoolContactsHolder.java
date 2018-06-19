package unit.moudle.contacts.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.widget.GridViewForScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.SchoolContactInfo;
import unit.moudle.contacts.adapter.SchoolContactHolderAdapter;

/**
 * Created by lei on 2018/6/19.
 */

public class SchoolContactsHolder extends BaseHolder<SchoolContactInfo> {
    @BindView(R.id.letter)
    TextView letter;
    @BindView(R.id.contact_list)
    GridViewForScrollView contactList;

    private SchoolContactHolderAdapter mAdapter;

    public SchoolContactsHolder(Context context) {
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
    protected void updateUI(Context context, SchoolContactInfo data) {

        if (data == null || data.getContactInfos().size() == 0){
            return;
        }

        letter.setText(data.getLetter());
        if (mAdapter == null){
            mAdapter = new SchoolContactHolderAdapter(mContext,data.getContactInfos());
        }
        contactList.setAdapter(mAdapter);
    }
}

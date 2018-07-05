package unit.moudle.contacts.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.widget.GridViewForScrollView;
import com.puti.education.widget.ListViewForScrollView;

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
    ListViewForScrollView contactList;

    private SchoolContactHolderAdapter mAdapter;

    public SchoolContactsHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_school_contact_holder);
        ButterKnife.bind(this, mRootView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        contactList.setLayoutParams(params);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, SchoolContactInfo data) {

        if (data == null || data.getContactInfos().size() == 0){
            return;
        }

        letter.setText(data.getLetter());
        mAdapter = new SchoolContactHolderAdapter(mContext,data.getContactInfos());
        contactList.setAdapter(mAdapter);
    }
}

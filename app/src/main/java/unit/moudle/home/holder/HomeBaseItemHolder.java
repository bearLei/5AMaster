package unit.moudle.home.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.HomeHolderInfo;

/**
 * Created by lei on 2018/6/6.
 */

public class HomeBaseItemHolder extends BaseHolder<HomeHolderInfo> {

    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.red_dog)
    ImageView redDog;
    @BindView(R.id.title)
    TextView title;

    private ItemClickListener listener;

    public HomeBaseItemHolder(Context context) {
        super(context);
    }

    public HomeBaseItemHolder(Context context, ItemClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_home_holder);
        ButterKnife.bind(this,mRootView);
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.itemClick();
                }
            }
        });
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, HomeHolderInfo data) {

    }

    //设置ui
    public void setUI(int iconRes,int titleRes){
        icon.setImageResource(iconRes);
        title.setText(mContext.getString(titleRes));
    }

    //设置ui
    public void setUI(int iconRes,String titleRes){
        icon.setImageResource(iconRes);
        title.setText(titleRes);
    }

    public void showRedDog(boolean show){
        redDog.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public interface ItemClickListener{
        void itemClick();
    }

}

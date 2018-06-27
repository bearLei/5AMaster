package unit.moudle.eventdeal;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.util.ViewUtils;

import unit.entity.Event2Involved;
import unit.moudle.eventdeal.holder.DealEventDetailPeopleHolder;

import static com.puti.education.R.style.FilterDialogTheme;
import static com.puti.education.R.style.personal_info_right_text_style;

/**
 * Created by lei on 2018/6/27.
 */

public class DealsDialog extends Dialog {
    private Context mContext;
    private ViewGroup contentView;

    private DealEventDetailPeopleHolder mHolder;

    public DealsDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DealsDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        contentView = (ViewGroup) LayoutInflater.from(context).inflate(
                R.layout.puti_deal_dialog, null);
        setContentView(contentView);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ViewUtils.getScreenWid(context);
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.CENTER);

        if (mHolder == null){
            mHolder = new DealEventDetailPeopleHolder(mContext,true);
        }
        contentView.removeAllViews();
        contentView.addView(mHolder.getRootView());
    }


    public void setData(Event2Involved data){
        mHolder.setData(data);
    }
}

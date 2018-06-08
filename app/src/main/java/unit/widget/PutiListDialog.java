package unit.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.holder.ViewHolder;
import com.puti.education.util.ViewUtils;


/**
 * 包含ListView的对话框
 *
 * @author lei
 */
public class PutiListDialog extends Dialog {

    private Context mContext;

    private TextView mTitleText;// 标题
    private View mUnderlineView;// 标题的下划线
    private ListView mContentList;// ListView
    private TextView mCancelBtn;

    private View contentView;

    // ---------------------------------构造函数
    private PutiListDialog(Context context) {
        super(context, R.style.FilterDialogTheme);
        mContext = context;
        contentView = LayoutInflater.from(context).inflate(
                R.layout.puti_list_dialog, null);
        setContentView(contentView);

        mTitleText = (TextView) contentView.findViewById(R.id.title_textview);
        mUnderlineView = contentView.findViewById(R.id.title_underline);
        mContentList = (ListView) contentView
                .findViewById(R.id.content_listview);

        mCancelBtn = (TextView) findViewById(R.id.text_cancel);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ViewUtils.getScreenWid(context);
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);

    }

    // ----------------------------------内部方法

    /**
     * 设置标题
     */
    private void setTitle(String title) {
        if (title != null && !title.isEmpty()) {
            mTitleText.setVisibility(View.VISIBLE);
            mUnderlineView.setVisibility(View.VISIBLE);
            mTitleText.setText(title);
        } else {
            mTitleText.setVisibility(View.GONE);
            mUnderlineView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置ListView中的items
     */
    private void setItems(String[] items) {
        if (items != null && items.length > 0) {
            mContentList.setAdapter(new ListAdapter(items));
        }
    }

    /**
     * 设置ListView中的item的颜色
     */
    private void setItemsColor(String[] items, int[] itemsColor) {
        if (items != null && items.length > 0 && itemsColor != null
                && itemsColor.length > 0) {
            mContentList.setAdapter(new ListAdapter(items, itemsColor));
        }
    }

    /**
     * 设置ListView的item的监听器
     */
    private void setOnItemClickListener(
            final OnItemClickListener onItemClickListener) {
        if (onItemClickListener != null) {
            mContentList
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            onItemClickListener.onItemClick(position);
                            PutiListDialog.this.dismiss();
                        }
                    });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int which);
    }

    /**
     * ListView的适配器
     */
    private class ListAdapter extends BaseAdapter {
        String[] items;
        int[] itemsColor;

        ListAdapter(String[] items) {
            this.items = items;
        }

        ListAdapter(String[] items, int[] itemsColor) {
            this.items = items;
            this.itemsColor = itemsColor;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.puti_list_dialog_item, null);
            }

            TextView itemText = ViewHolder.get(convertView, R.id.item_textview);
            itemText.setText(items[position]);
            if (itemsColor != null && itemsColor.length > 0) {
                itemText.setTextColor(itemsColor[position]);
            }
            return convertView;
        }

    }

    // ------------------------------------------外部接口

    /**
     * FormaxListDialog构造类
     */
    public static class Builder {
        final Params mParams;

        public Builder(Context context) {
            mParams = new Params();
            mParams.mContext = context;
        }

        public Builder setTitle(int titleResId) {
            return setTitle(mParams.mContext.getString(titleResId));
        }

        public Builder setTitle(String title) {
            mParams.mTitle = title;
            return this;
        }

        public Builder setItems(int[] itemsResId) {
            if (itemsResId != null && itemsResId.length > 0) {
                String[] items = new String[itemsResId.length];
                for (int i = 0; i < itemsResId.length; i++) {
                    items[i] = mParams.mContext.getString(itemsResId[i]);
                }
                setItems(items);
            }

            return this;
        }

        public Builder setItems(String[] items) {
            mParams.mItems = items;
            return this;
        }

        public Builder setItemsColor(int[] itemsColor) {
            mParams.mItemsColor = itemsColor;
            return this;
        }

        public Builder setOnItemClickListener(
                OnItemClickListener onItemClickListener) {
            mParams.mOnItemClickListener = onItemClickListener;
            return this;
        }

        public PutiListDialog create() {
            final PutiListDialog dialog = new PutiListDialog(
                    mParams.mContext);
            if (mParams.mOnItemClickListener != null) {
                dialog.setOnItemClickListener(mParams.mOnItemClickListener);
            }

            dialog.setTitle(mParams.mTitle);
            dialog.setItems(mParams.mItems);
            dialog.setItemsColor(mParams.mItems, mParams.mItemsColor);

            return dialog;
        }

        private static class Params {
            Context mContext;
            String mTitle;// 标题
            String[] mItems;// 列表items
            int[] mItemsColor;// item的字体颜色
            OnItemClickListener mOnItemClickListener;
        }

    }

}

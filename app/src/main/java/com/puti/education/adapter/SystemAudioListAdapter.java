package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.LocalFile;

import java.util.List;

/**
 * Created by xjbin on 2017/5/23 0023.
 */

public class SystemAudioListAdapter extends BasicRecylerAdapter<LocalFile>{

    public SystemAudioListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_audio_list;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        final LocalFile localFile = mList.get(position);
        CommonViewHolder cHolder = (CommonViewHolder) holder;
        TextView mFileNameTv = cHolder.obtainView(R.id.file_name_tv);
        TextView mSizeTv = cHolder.obtainView(R.id.file_size_tv);
        final ImageView mFileCheckBox = cHolder.obtainView(R.id.file_checkbox);

        mFileNameTv.setText(localFile.fileName);
        mSizeTv.setText(localFile.size +"Byte");

        if (localFile.isCheck == 1){
            mFileCheckBox.setImageResource(R.mipmap.ic_item_selected);
        }else{
            mFileCheckBox.setImageResource(R.mipmap.ic_item_unselected);
        }

        cHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localFile.isCheck == 1){
                    localFile.isCheck = 0;
                    mFileCheckBox.setImageResource(R.mipmap.ic_item_unselected);
                }else{
                    localFile.isCheck = 1;
                    mFileCheckBox.setImageResource(R.mipmap.ic_item_selected);
                }
            }
        });
    }

}

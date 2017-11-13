package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.puti.education.R;
import com.puti.education.util.ImgLoadUtil;

import java.io.File;

/**
 * Created by xjbin on 2017/5/23 0023.
 *
 */

public class UploadFileListAdapter extends BasicRecylerAdapter<String>{

    public UploadFileListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_upload_file_layout;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        String path = mList.get(position);
        CommonViewHolder cholder = (CommonViewHolder) holder;
        ImageView img = cholder.obtainView(R.id.img);
        ImgLoadUtil.displayLocalPictrue(mContext,R.drawable.default_rect_img_bg,new File(path),img);

        cholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myItemOnclickListener != null){
                    myItemOnclickListener.onItemClick(position);
                }
            }
        });

    }
}

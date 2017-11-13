package com.puti.education.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.puti.education.R;
import com.puti.education.bean.Proof;
import com.puti.education.util.ImgLoadUtil;

/**
 * Created by xjbin on 2017/5/27 0027.
 */

public class ProofGridPicsAdapter extends ABaseAdapter<Proof>{

    private int mType = -1; //1 佐证记录，　2. 追踪记录  3巡检或举报记录
    public ProofGridPicsAdapter(Context context, int type) {
        super(context);
        mType = type;
    }

    public void updateDisplayType(int type){
        mType = type;
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_proof_pics_layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {

        ImageView imageView = holder.obtainView(convertView,R.id.proof_img);

        if (mType == 1){
            ImgLoadUtil.displayPic(R.drawable.default_rect_img_bg,mList.get(position).url,imageView);
        }else if (mType == 2){
            ImgLoadUtil.displayPic(R.drawable.default_rect_img_bg,mList.get(position).file,imageView);
        }else if (mType == 3){
            ImgLoadUtil.displayPic(R.drawable.default_rect_img_bg,mList.get(position).fileuid,imageView);
        }

        return convertView;
    }
}

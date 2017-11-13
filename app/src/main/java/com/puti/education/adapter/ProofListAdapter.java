package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import com.puti.education.R;
import com.puti.education.bean.PersonProof;

/**
 * Created by xjbin on 2017/5/8 0008.
 * 佐证记录
 */

public class ProofListAdapter extends BasicRecylerAdapter<PersonProof>{

    public ProofListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_proof_list_layout;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CommonViewHolder cHolder = (CommonViewHolder) holder;
        TextView proofTypeTv = (TextView) cHolder.obtainView(R.id.proof_title_tv);
        TextView proofContentTv = (TextView) cHolder.obtainView(R.id.proof_content_tv);
        TextView proofAddressTv = (TextView) cHolder.obtainView(R.id.proof_address_tv);
        //GridViewForScrollView gridView = (GridViewForScrollView) cHolder.obtainView(R.id.involved_people_grid);
        ImageView headImg = (ImageView) cHolder.obtainView(R.id.involved_head_img);
        TextView textView = (TextView) cHolder.obtainView(R.id.involved_name_tv);
        TextView dateTv = (TextView) cHolder.obtainView(R.id.date_tv);


    }
}

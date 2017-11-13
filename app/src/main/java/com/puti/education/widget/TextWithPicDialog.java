package com.puti.education.widget;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.adapter.ProofGridPicsAdapter;
import com.puti.education.bean.EventFile;
import com.puti.education.bean.ImageRecord;
import com.puti.education.bean.Proof;
import com.puti.education.ui.uiCommon.PhotoReviewActivity;
import com.puti.education.ui.uiPatriarch.TrainPracticeDetailActivity;
import com.puti.education.util.ImgLoadUtil;

import java.util.ArrayList;

/**
 * Created by xjbin on 2017/5/16 0016.
 *
 * 图片，文字 记录
 */

public class TextWithPicDialog extends BaseDialog {

    int mDisplayType = -1;
    String mTime;
    ArrayList<Proof> mRecord;
    private Context mContext;
    private TextView mTvDate;
    private ProofGridPicsAdapter mProofGridPicsAdapter;

    public TextWithPicDialog(Context context, ArrayList<Proof> files, String tracetime, int displaytype) {
        super(context);
        this.mRecord = files;
        this.mContext = context;
        this.mTime = tracetime;
        this.mDisplayType = displaytype;
    }

    @Override
    public int getDialogLayoutId() {
        return R.layout.dialog_text_pic_layout;
    }

    @Override
    public void setting() {

        TextView textView = (TextView) findViewById(R.id.record_text_tv);
        mTvDate = (TextView) findViewById(R.id.date_tv);
        Button greenBtn = (Button) findViewById(R.id.sure_btn);
        GridView gv = (GridView) findViewById(R.id.proof_pic_grid);

        if (mRecord != null && mRecord.size() > 0){
            textView.setText(mRecord.get(0).description);
            mProofGridPicsAdapter = new ProofGridPicsAdapter(this.mContext, mDisplayType);
            mProofGridPicsAdapter.setmList(mRecord);
            gv.setAdapter(mProofGridPicsAdapter);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (mRecord != null && mRecord.size() > 0){
                        String path;
                        if (mDisplayType == 1){
                            path = mRecord.get(i).url;
                        }else{
                            path = mRecord.get(i).file;
                        }

                        Intent intent = new Intent();
                        intent.putExtra("type", 1);
                        intent.putExtra("url", path);
                        intent.setClass(mContext, PhotoReviewActivity.class);
                        mContext.startActivity(intent);
                    }
                }
            });
        }

        if (mRecord != null){
            mTvDate.setText(mTime  == null ? "暂无":mTime);
        }

        greenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public void updateDate(ArrayList<Proof> files, String tracetime, int displaytype){
        this.mRecord = files;
        this.mTime = tracetime;
        this.mDisplayType = displaytype;
        mProofGridPicsAdapter.setmList(mRecord);
        mProofGridPicsAdapter.updateDisplayType(mDisplayType);
        mProofGridPicsAdapter.notifyDataSetChanged();
        if (mRecord != null){
            mTvDate.setText(mTime  == null ? "暂无":mTime);
        }
    }
}



package com.puti.education.ui.uiTeacher;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.puti.education.R;
import com.puti.education.adapter.ProofGridPicsAdapter;
import com.puti.education.bean.AddEventResponseBean;
import com.puti.education.bean.DetectiveBean;
import com.puti.education.bean.Proof;
import com.puti.education.bean.ReportBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiCommon.PhotoReviewActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.Key;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.GridViewForScrollView;

import butterknife.BindView;
import butterknife.OnClick;

public class ReportConfirmActivity extends BaseActivity {

    @BindView(R.id.tv_desc)
    TextView mTvDesc;
    @BindView(R.id.iv_avatar)
    ImageView mImgAvatar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    @BindView(R.id.btn_commit_refuse)
    Button mBtnRefuse;
    @BindView(R.id.gv_photo)
    GridViewForScrollView mGvPhotos;

    private ReportBean mEventBean;
    private String mReportUID;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_report_confirm;
    }

    @Override
    public void initVariables() {
        mEventBean = (ReportBean)this.getIntent().getSerializableExtra(Key.BEAN);
        if (mEventBean == null){
            ToastUtil.show("巡检事件数据为空");
            return;
        }
        mReportUID = mEventBean.uid;
    }

    @Override
    public void initViews() {

        mTvDesc.setText(mEventBean.desc);
        if (mEventBean.user != null){
            mTvName.setText(mEventBean.user.name);
            ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_middle, mEventBean.user.avatar, mImgAvatar);
        }


        if (mEventBean.records != null && mEventBean.records.size() > 0){
            ProofGridPicsAdapter proofGridPicsAdapter = new ProofGridPicsAdapter(this, 1);
            proofGridPicsAdapter.setmList(mEventBean.records);
            mGvPhotos.setAdapter(proofGridPicsAdapter);
            mGvPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Proof proof = mEventBean.records.get(position);
                    if (proof != null){
                        viewPhoto(proof.url);
                    }

                }
            });
        }

        if (mEventBean.confirm == 0){
            mBtnCommit.setVisibility(View.VISIBLE);
            mBtnRefuse.setVisibility(View.VISIBLE);
        }else{
            mBtnCommit.setVisibility(View.GONE);
            mBtnRefuse.setVisibility(View.GONE);
        }
    }

    private void viewPhoto(String photopath){
        Intent intent = new Intent();
        intent.putExtra("type", 1);
        intent.putExtra("url", photopath);
        intent.setClass(this, PhotoReviewActivity.class);
        startActivity(intent);
    }

    @Override
    public void loadData() {

    }

    @OnClick(R.id.btn_commit)
    public void confirmEvent(){
        confirmEventRequest(true);
    }

    @OnClick(R.id.btn_commit_refuse)
    public void refuseEvent(){
        confirmEventRequest(false);
    }

    /**
     * 确认举报为事件
     */
    private void confirmEventRequest(final boolean isAccept){

        String bodyStr  = createBodyStr(isAccept);
        if (bodyStr == null){
            return;
        }

        disLoading();

        TeacherModel.getInstance().confirmReport(bodyStr, new BaseListener(AddEventResponseBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                hideLoading();
                if (infoObj != null){
                    if (isAccept){
                        ToastUtil.show("确认事件成功");
                    }else{
                        ToastUtil.show("拒绝事件成功");
                    }
                    setResult(2);
                    finish();
                }else{
                    if (isAccept){
                        ToastUtil.show("确认事件失败");
                    }else{
                        ToastUtil.show("拒绝事件失败");
                    }
                }

            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });

    }

    private String createBodyStr(boolean isAccept){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reportUID", mReportUID);
        jsonObject.put("bConfirm", isAccept);
        return jsonObject.toString();
    }



}

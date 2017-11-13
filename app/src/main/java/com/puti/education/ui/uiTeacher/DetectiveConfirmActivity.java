package com.puti.education.ui.uiTeacher;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.puti.education.R;
import com.puti.education.adapter.ProofGridPicsAdapter;
import com.puti.education.bean.AddEventResponseBean;
import com.puti.education.bean.DetectiveBean;
import com.puti.education.bean.Proof;
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

public class DetectiveConfirmActivity extends BaseActivity {

    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_result)
    TextView mTvResult;
    @BindView(R.id.iv_type)
    ImageView mImgType;
    @BindView(R.id.iv_avatar)
    ImageView mImgAvatar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_starttime)
    TextView mTvStartTime;
    @BindView(R.id.tv_endtime)
    TextView mTvEndTime;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    @BindView(R.id.btn_commit_refuse)
    Button mBtnRefuse;
    @BindView(R.id.gv_photo)
    GridViewForScrollView mGvPhotos;
    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.layout_task)
    LinearLayout mLayoutTask;

    private DetectiveBean mEventBean;
    private String mDetectiveUID;
    private BaiduMap mBaiduMap;

    @OnClick(R.id.back_frame)
    public void exitActivity(){
        finish();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_detective_confirm;
    }

    @Override
    public void initVariables() {
        mEventBean = (DetectiveBean)this.getIntent().getSerializableExtra(Key.BEAN);
        if (mEventBean == null){
            ToastUtil.show("巡检事件数据为空");
            return;
        }
        mDetectiveUID = mEventBean.patrolUID;
        mBaiduMap = mMapView.getMap();
        displayAddr();
    }

    @Override
    public void initViews() {
        mTvTime.setText(mEventBean.time);
        mTvResult.setText(mEventBean.result);
        if (mEventBean.task != null){
            if (mEventBean.task.type == 1){
                mImgType.setImageResource(R.mipmap.ic_detective_help);
                if (mEventBean.task.publisher != null){
                    mTvName.setText("学生: " + mEventBean.task.publisher.name + " 求救");
                    ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_middle, mEventBean.task.publisher.avatar, mImgAvatar);
                }
            }else{
                mImgType.setImageResource(R.mipmap.ic_detective_task);
                mTvName.setText(mEventBean.task.title);
                if (TextUtils.isEmpty(mEventBean.task.taskUID) && mEventBean.submiter != null){
                    mTvName.setText("" + mEventBean.submiter.name + " 上报");
                    ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_middle, mEventBean.submiter.avatar, mImgAvatar);
                }
            }
            mTvStartTime.setText("开始时间:" + (TextUtils.isEmpty(mEventBean.task.startTime)?"":mEventBean.task.startTime));
            mTvEndTime.setText("结束时间:" + (TextUtils.isEmpty(mEventBean.task.endTime)?"":mEventBean.task.endTime));

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
     * 确认巡检事件
     */
    private void confirmEventRequest(final boolean isAccept){

        String bodyStr  = createBodyStr(isAccept);
        if (bodyStr == null){
            return;
        }

        disLoading();

        TeacherModel.getInstance().confirmDetective(bodyStr, new BaseListener(AddEventResponseBean.class){
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
        jsonObject.put("patrolUID", mDetectiveUID);
        jsonObject.put("bConfirm", isAccept);
        return jsonObject.toString();
    }


    public void displayAddr(){
        if (TextUtils.isEmpty(mEventBean.lat) || TextUtils.isEmpty(mEventBean.lng)){
            return;
        }

        double tempLat = Double.parseDouble(mEventBean.lat);
        double tempLng = Double.parseDouble(mEventBean.lng);

        //定义Maker坐标点
        LatLng point = new LatLng(tempLat, tempLng);
        //构建Marker图标

        View view = LayoutInflater.from(this).inflate(R.layout.baidu_marker_view, null);

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromView(view);

        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .title("这里？")
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        float f = mBaiduMap.getMaxZoomLevel();//获取最小比例级别，然后再
        float zoomLevel = f-5;  //

        MapStatusUpdate status = MapStatusUpdateFactory.newLatLngZoom(point, zoomLevel);
        mBaiduMap.animateMapStatus(status);

    }



}

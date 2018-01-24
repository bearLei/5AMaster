package com.puti.education.ui.uiTeacher;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puti.education.R;
import com.puti.education.adapter.EventAboutPeopleAdapter;
import com.puti.education.adapter.EventDetailExampleAdapter;
import com.puti.education.adapter.EventDetailParentAdapter;
import com.puti.education.adapter.EventInvolverPeopleAdapter;
import com.puti.education.adapter.EventReviewListAdapter;
import com.puti.education.adapter.EventTrackListAdapter;
import com.puti.education.adapter.ParentFromStudent;
import com.puti.education.adapter.ProfessorListAdapter;
import com.puti.education.adapter.PunishListAdapter;
import com.puti.education.bean.EVENT_TYPE;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.EventCourse;
import com.puti.education.bean.EventDetailBean;
import com.puti.education.bean.EventDetailData;
import com.puti.education.bean.EventEvaluation;
import com.puti.education.bean.KeyValue;
import com.puti.education.bean.LocalFile;
import com.puti.education.bean.ParentData;
import com.puti.education.bean.Professor;
import com.puti.education.bean.Proof;
import com.puti.education.bean.PunishData;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.bean.UserDetail;
import com.puti.education.bean.Warn;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.netFrame.netModel.UploadModel;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.fragment.TeacherEventListFragment;
import com.puti.education.ui.uiCommon.EventParentChooseActivity;
import com.puti.education.ui.uiCommon.ProfessorListActivity;
import com.puti.education.ui.uiCommon.VideoRecordActivity;
import com.puti.education.ui.uiCommon.WebViewActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.DownLoadManagerUtil;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.Key;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.AttentionDialog;
import com.puti.education.widget.CommonDropView;
import com.puti.education.widget.CommonNestedScrollView;
import com.puti.education.widget.ExpertDialog;
import com.puti.education.widget.GridViewForScrollView;
import com.puti.education.widget.KeyValueDropView;
import com.puti.education.widget.ListViewForScrollView;
import com.puti.education.widget.MediaDialog;
import com.puti.education.widget.RatingSmallBarView;
import com.puti.education.widget.SeekbarLayout;
import com.puti.education.widget.TextWithPicDialog;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 事件详情类型：异常事件详情，普通详情
 * 事件状态：0:已拒绝，1:待确认，2:已确认, 3:待审核, 4:待追踪, 5:已结案
 * 需要处理的状态有：待确认，已确认（待处理），待审核，待追踪,
 *                班主任可以确认/处理／追踪／结束事件
 *                家长可以追踪事件
 *                学生处老师可以审核事件
 *                其它老师只能查看事件
 */

public class TeacherEventDetailActivity extends BaseActivity {

    @BindView(R.id.scrollview)
    CommonNestedScrollView mScrollView;

    @BindView(R.id.title_textview)
    TextView mTitleTv;

    //基本信息
    @BindView(R.id.tv_event_time)
    TextView mEventTimeTv;
    @BindView(R.id.event_address_tv)
    TextView mEventAddressTv;
    @BindView(R.id.event_type_tv)
    TextView mEventTypeTv;
    @BindView(R.id.event_des_tv)
    TextView mEventDesTv;
    @BindView(R.id.rbv_event_level)
    RatingSmallBarView mRateBar;

    //涉事人
    @BindView(R.id.involved_people_grid)
    GridViewForScrollView mInvolvedPeopleGv;

    //佐证记录
    @BindView(R.id.confirm_record_linear)
    LinearLayout mConfirmRecordLinear;
    @BindView(R.id.video_proof_linear)
    LinearLayout mVideoLinear;
    @BindView(R.id.text_pic_proof_linear)
    LinearLayout mTextPicLinear;
    @BindView(R.id.media_proof_linear)
    LinearLayout mMediaLinear;

    //事件确认
    @BindView(R.id.layout_confirm)
    LinearLayout mLayoutConfirm;
    @BindView(R.id.img_main_avatar)
    ImageView mIvMainAvatar;
    @BindView(R.id.tv_name)
    TextView mTvMainName;
    @BindView(R.id.tv_duty_type)
    TextView mTvDutyName;
    @BindView(R.id.rg_event_confirm)
    RadioGroup mIsAcceptRaidoGroup;
    @BindView(R.id.layout_relate_parents)
    LinearLayout mLayoutParents;
    @BindView(R.id.et_refuse_input)
    EditText mEtRefuseReason;
    @BindView(R.id.gv_linked_parents)
    GridViewForScrollView mGvParents;

    //事件处理
    @BindView(R.id.layout_event_deal)
    LinearLayout mLayoutEventDeal;
    @BindView(R.id.img_main_avatar_deal)
    ImageView mImgDealAvatar;
    @BindView(R.id.tv_name_deal)
    TextView mTvDealName;
    @BindView(R.id.tv_duty_type_deal)
    TextView mTvDealDuty;
    @BindView(R.id.layout_punish_name)
    LinearLayout mLayoutPunishType;
    @BindView(R.id.tv_punish_name)
    TextView mTvWarnName;
    @BindView(R.id.et_punish_detail)
    EditText mEtPunishDetail;
    @BindView(R.id.et_sample_detail)
    EditText mEtSampleDetail;
    @BindView(R.id.et_deal_strategy)
    EditText mEtDealStrategy;
    @BindView(R.id.iv_sample_detail)
    ImageView mIvSampleDetail;
    @BindView(R.id.iv_deal_strategy)
    ImageView mIvDealStrategy;
    @BindView(R.id.tv_recommand_course)
    TextView mTvCourse;
    @BindView(R.id.gv_linked_expert)
    GridViewForScrollView mGvExpert;
    @BindView(R.id.tv_seekbar_layout)
    SeekbarLayout mLayoutSeekbar;
    @BindView(R.id.gb_score)
    SeekBar mSbPunishScore;
    @BindView(R.id.tv_score_up)
    TextView mTvScoreUp;
    @BindView(R.id.tv_score_down)
    TextView mTvScoreDown;
    @BindView(R.id.follow_track_radio)
    CheckBox mCbFollowTrack;
    @BindView(R.id.office_check_radio)
    CheckBox mCbOfficeCheck;

    //学生处事件审核
    @BindView(R.id.layout_check)
    LinearLayout mLayoutCheck;
    @BindView(R.id.rg_event_check)
    RadioGroup mRgCheck;
    @BindView(R.id.et_office_opinion)
    EditText mEtCheckOpinion;

    //事件追踪记录
    @BindView(R.id.layout_track_list)
    LinearLayout mLayoutTrackList;
    @BindView(R.id.event_follow_lv)
    ListViewForScrollView mTracksLv;

    //事件追踪输入
    @BindView(R.id.layout_track_input)
    LinearLayout mLayoutTrackInput;
    @BindView(R.id.et_track_name)
    TextView mTvTrackName;
    @BindView(R.id.et_track_desc)
    EditText mEtTrackDesc;
    @BindView(R.id.text_pic_record_linear)
    LinearLayout mLayoutPicture;
    @BindView(R.id.media_record_linear)
    LinearLayout mLayoutAudio;
    @BindView(R.id.video_record_linear)
    LinearLayout mLayoutVedio;

    //事件结束后显示最后评价（评分及评语）
    @BindView(R.id.layout_last_review)
    LinearLayout mLayoutLastReview;
    @BindView(R.id.layout_parent_score)
    LinearLayout mLayoutScoreParents;
    @BindView(R.id.layout_expert_score)
    LinearLayout mLayoutScoreExperts;
    @BindView(R.id.lv_parents)
    ListViewForScrollView mLvParents;
    @BindView(R.id.lv_expert)
    ListViewForScrollView mLvExperts;
    @BindView(R.id.tv_review_name)
    TextView mTvReviewName;
    @BindView(R.id.et_review_input)
    EditText mEtReviewInput;

    //结束，继续跟进
    @BindView(R.id.event_two_btn_linear)
    LinearLayout mEndAndGoOnLinear;
    @BindView(R.id.event_finish_btn)
    Button mFinishBtn;
    @BindView(R.id.event_continu_follow_btn)
    Button mFollowBtn;


    //事件操作单个按钮
    @BindView(R.id.layout_one_btn)
    FrameLayout mLayoutOneBtn;//处理事件按钮
    @BindView(R.id.btn_event_btn)
    Button mBtnEvent;

    private Handler mHandler = new Handler();

    private EventInvolverPeopleAdapter mInvolvePeopleAdapter = null;//涉事人
    private EventDetailParentAdapter mInvolveParentAdapter = null;

    //private EventDetailInvolvedAdapter mInvolvedAdapter;
    private EventDetailExampleAdapter mExampleAdapter;
    private EventTrackListAdapter mTrackListAdapter;
    private PunishListAdapter mPunishListAdapter;
    private EventAboutPeopleAdapter mProfessorListAdapter;

    private String mEventUid; //事件id
    private String mPeopleUid;//学生id

    private ArrayList<ParentFromStudent> mParentList;
    private ArrayList<EventAboutPeople> mExpertList = new ArrayList<>();  //专家

    //处分类型
    private Warn mScoreRange;
    private List<KeyValue> mWarnList;
    private String mCurrentPunish;
    private int mTempScore;
    private double mPunishScore = 0;
    private ArrayList<EventCourse> mCourseList;
    private String mCourseUid;
    private boolean mIsNeedValid;
    private boolean mIsNeedTrack;
    private TextView mTvSeekbarValue;
    private float mMoveStep = 0;//拖动条的移动步调
    private int mValueWidth = 0, mLayoutHight = 0;//

    private EventDetailData mEventBean = null;
    private EventDetailBean mEventDetail;
    private EVENT_TYPE mCurentType;

    /*
           *-1 都不可以操作（任课老师）
           * 1 各个状态都可以操作（班主任又是学生处老师）　
           * 2 各个状态都可以操作,除了审核（班主任）
           * 3　只可操作审核（学生处）　
           * 4　只可操作追踪（家长）
           */
    private int mOperateStatus;

    private int mEventStatus;
    private int mEventType;

    private boolean mIsAcceptEvent;

    //事件审核相关
    private boolean mIsOfficeAccept;

    //上传追踪记录相关
    private String mImageTextStr;
    private ArrayList<String> mImagePaths;
    private ArrayList<LocalFile> mAudioLocalFileList;
    private String mVideoPath;

    private ArrayList<UploadFileBean> mUploadedImages;
    private ArrayList<UploadFileBean> mUploadAudios;
    private ArrayList<UploadFileBean> mUploadVideos;

    //结束事件后总评价
    private ArrayList<EventEvaluation> mReviewParentList;
    private ArrayList<EventEvaluation> mReviewExpertList;

    private String mDownloadPath;
    private MediaPlayer player = null;
    FileComepeleteReceiver fileComepeleteReceiver = null;


    //文件现在完成广播
    private class FileComepeleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ToastUtil.show("下载完成");
            hideLoading();
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            try {
                String serviceString = Context.DOWNLOAD_SERVICE;
                DownloadManager dManager = (DownloadManager) context.getSystemService(serviceString);
                Intent install = new Intent(Intent.ACTION_VIEW);
                Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadId);
                LogUtil.i("receiver uri", "" + downloadFileUri);
                String mineType = dManager.getMimeTypeForDownloadedFile(downloadId);

                //install.setDataAndType(downloadFileUri, mineType);
                install.setDataAndType(downloadFileUri, "video/mp4");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_event_detail;
    }

    @Override
    public void initVariables() {
        Intent intent = getIntent();
        mEventUid = intent.getStringExtra(Key.EVENT_KEY);
        mPeopleUid= intent.getStringExtra(Key.KEY_PEOPLE_UID);

        //mDownloadPath = Environment.getExternalStorageDirectory() + "/" + this.getPackageName() + "/download/";
        mDownloadPath = Constant.STORAGE_ROOT +  "/download/";

        fileComepeleteReceiver = new FileComepeleteReceiver();
        registerReceiver(fileComepeleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        mUploadedImages = new ArrayList<>();
        mImagePaths = new ArrayList<>();

        mUploadAudios = new ArrayList<>();
        mAudioLocalFileList = new ArrayList<>();

        mUploadVideos = new ArrayList<>();
    }

    @Override
    public void initViews() {
        mTitleTv.setText("事件详情");

        mTvSeekbarValue = new TextView(this);
        mTvSeekbarValue.setBackgroundColor(Color.TRANSPARENT);
        mTvSeekbarValue.setTextColor(Color.rgb(153, 153, 153));
        mTvSeekbarValue.setTextSize(10);
        mValueWidth = mSbPunishScore.getWidth();
        mLayoutHight = mLayoutSeekbar.getHeight();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(mValueWidth, mLayoutHight);
        mLayoutSeekbar.addView(mTvSeekbarValue, layoutParams);
//        LinearLayout.LayoutParams paramSelf = (LinearLayout.LayoutParams) mLayoutSeekbar.getLayoutParams();
//        paramSelf.leftMargin = mTvScoreDown.getWidth();
//        mLayoutSeekbar.setLayoutParams(paramSelf);
        mTvSeekbarValue.layout(0, 10, mValueWidth, mLayoutHight-10);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (player != null) {
            player.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        if (fileComepeleteReceiver != null) {
            unregisterReceiver(fileComepeleteReceiver);
        }
    }

    @Override
    public void loadData() {

        getEventDetail(mEventUid, mPeopleUid);
        //getRecommandCourse();
    }



    //基本信息
    private void showTopInfo() {
        if (mEventBean != null) {
            mEventTimeTv.setText(mEventBean.eventDetail.time);
            mEventAddressTv.setText(mEventBean.eventDetail.address);
            mEventTypeTv.setText(mEventBean.eventDetail.typeName);
            mEventDesTv.setText(mEventBean.eventDetail.description);
            mEventDesTv.setMovementMethod(ScrollingMovementMethod.getInstance());
            mEventDesTv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                    return false;
                }}
            );

            mRateBar.setStar(mEventBean.eventDetail.level, true);
            mRateBar.setClickable(false);

            //涉事人
            mInvolvePeopleAdapter = new EventInvolverPeopleAdapter(this);
            mInvolvedPeopleGv.setAdapter(mInvolvePeopleAdapter);
            mInvolvePeopleAdapter.setmList(mEventBean.involvedPeople);
            mInvolvePeopleAdapter.notifyDataSetChanged();
        }
    }

    //显示处分信息
    private void showPunishInfo(){
        if (mEventBean != null && mEventBean.deal != null){
            mLayoutEventDeal.setVisibility(View.VISIBLE);
            UserDetail eventstudent  = mEventDetail.student;
            if (eventstudent != null) {
                mTvDealName.setText(eventstudent.name);
                mTvDealDuty.setText(mEventDetail.involvedTypeName);
                ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_middle,eventstudent.avatar,mImgDealAvatar);
            }
            mTvWarnName.setText(mEventBean.deal.punishment);
            mEtPunishDetail.setText(mEventBean.deal.punishmentInfo);
            mEtPunishDetail.setFocusable(false);
            mEtPunishDetail.setFocusableInTouchMode(false);
            mEtPunishDetail.setMovementMethod(ScrollingMovementMethod.getInstance());
            mEtPunishDetail.setOnTouchListener(new View.OnTouchListener() {
                                               @Override
                                               public boolean onTouch(View view, MotionEvent motionEvent) {
                                                   mScrollView.requestDisallowInterceptTouchEvent(true);
                                                   return false;
                                               }});

            mEtSampleDetail.setText(mEventBean.deal.analysis);
            mEtDealStrategy.setText(mEventBean.deal.countermeasure);
            mEtSampleDetail.setFocusable(false);
            mEtSampleDetail.setFocusableInTouchMode(false);
            mEtDealStrategy.setFocusable(false);
            mEtDealStrategy.setFocusableInTouchMode(false);
            mEtSampleDetail.setMovementMethod(ScrollingMovementMethod.getInstance());
            mEtSampleDetail.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                    return false;
                }});
            mEtDealStrategy.setMovementMethod(ScrollingMovementMethod.getInstance());
            mEtDealStrategy.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                    return false;
                }});


            mTvCourse.setText(mEventBean.deal.tutoringName);
            mTvCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(mEventBean.deal.tutoringUrl)) {
                        Intent intent5 = new Intent();
                        intent5.putExtra("type", 3);
                        intent5.putExtra("msg_title", "推荐课程");
                        intent5.putExtra("weburl", mEventBean.deal.tutoringUrl);
                        intent5.setClass(TeacherEventDetailActivity.this, WebViewActivity.class);
                        startActivity(intent5);
                    }
                }
            });

            convertExpertList();
            mProfessorListAdapter = new EventAboutPeopleAdapter(this);
            mProfessorListAdapter.setDataChanged(false);
            mProfessorListAdapter.setmList(mExpertList);
            mGvExpert.setAdapter(mProfessorListAdapter);
            mGvExpert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ExpertDialog attentionDialog = new ExpertDialog(TeacherEventDetailActivity.this, mExpertList.get(position));
                    attentionDialog.show();
                }
            });

            mSbPunishScore.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            mCbFollowTrack.setChecked(mEventBean.deal.needTrack);
            mCbOfficeCheck.setChecked(mEventBean.deal.needOfficeValid);
            mCbFollowTrack.setEnabled(false);
            mCbOfficeCheck.setEnabled(false);
        }
    }

    //专家字段转换
    private void convertExpertList(){
        if (mEventBean.parents == null || mEventBean.parents.size() <= 0){
            return;
        }

        mExpertList.clear();
        EventAboutPeople expertdata;
        ArrayList<Professor> expertlist = mEventBean.parents.get(0).expert;
        if (expertlist != null && expertlist.size() > 0){
            for (int i = 0; i< expertlist.size(); i++){
                expertdata = new EventAboutPeople(true);
                expertdata.name = expertlist.get(i).name;
                expertdata.avatar = expertlist.get(i).photo;
                expertdata.qrcode = expertlist.get(i).qrcode;
                mExpertList.add(expertdata);
            }
        }



    }


    //事件跟进
    private void showTrackList() {

        if (mEventBean.eventTracking != null && mEventBean.eventTracking.size() > 0) {
            mLayoutTrackList.setVisibility(View.VISIBLE);
            mTrackListAdapter = new EventTrackListAdapter(this);
            mTrackListAdapter.setmList(mEventBean.eventTracking);
            mTracksLv.setAdapter(mTrackListAdapter);
            mTrackListAdapter.setLookCallbackLinstener(new EventTrackListAdapter.LookCallbackLinstener() {
                @Override
                public void look(EventTrackListAdapter.CallBackType type, int position) {
                    if (mEventBean.eventTracking.get(position).files == null) {
                        ToastUtil.show("没有数据");
                        return;
                    }
                    if (type == EventTrackListAdapter.CallBackType.PICTURE) {
                        ArrayList<Proof> photoProof = getOneTypeProof(mEventBean.eventTracking.get(position).files, "0");
                        if (photoProof == null || photoProof.size() <= 0) {
                            ToastUtil.show("没有数据");
                            return;
                        }

                        displayPictureDislog(photoProof);
                    } else if (type == EventTrackListAdapter.CallBackType.AUDIO) {
                        ArrayList<Proof> audioProof = getOneTypeProof(mEventBean.eventTracking.get(position).files, "1");
                        if (audioProof == null || audioProof.size() <= 0) {
                            ToastUtil.show("没有数据");
                            return;
                        }

                        displayAudioDialog(audioProof);

                    }else if (type == EventTrackListAdapter.CallBackType.VIDEO){
                        ArrayList<Proof> videoProof = getOneTypeProof(mEventBean.eventTracking.get(position).files, "2");
                        if (videoProof == null || videoProof.size() <= 0) {
                            ToastUtil.show("没有数据");
                            return;
                        }
                        displayVideoDialog(videoProof);
                    }
                }
            });
        }

    }
    private void displayVideoDialog(ArrayList<Proof> files){
        mVideoDialog = new MediaDialog(TeacherEventDetailActivity.this, files,"", "1", 2);
        mVideoDialog.setDialogAudioClickListener(new MediaDialog.DialogMediaClickListener() {
            @Override
            public void click(String path) {
                videoPlay(path);
            }

            @Override
            public void cancel() {

            }
        });

        mVideoDialog.show();
    }

    private void displayAudioDialog(ArrayList<Proof> files){
        mMediaDialog = new MediaDialog(TeacherEventDetailActivity.this, files,"", "1", 2);
        mMediaDialog.setDialogAudioClickListener(new MediaDialog.DialogMediaClickListener() {
            @Override
            public void click(String path) {
                startNetAudio(path);
            }

            @Override
            public void cancel() {
                stopAudioPlay();
            }
        });

        mMediaDialog.show();
    }

    private void displayPictureDislog(final ArrayList<Proof> proof) {
        if (mTextWithPicDialog == null) {
            mTextWithPicDialog = new TextWithPicDialog(TeacherEventDetailActivity.this, proof, "", 2);
        }else{
            mTextWithPicDialog.updateDate(proof, "", 2);
        }
        mTextWithPicDialog.show();
    }

    private TextWithPicDialog mTextWithPicDialog;
    private MediaDialog mMediaDialog;
    private MediaDialog mVideoDialog;

    //文字，文字图片，音频记录click
    private void showProofInfo() {
        final ArrayList<Proof> mProof = mEventBean.eventEvidence;
        mVideoLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Proof> videoProof = getOneTypeProof(mProof, "2");
                if (videoProof != null && videoProof.size() > 0) {
                    mVideoDialog = new MediaDialog(TeacherEventDetailActivity.this, videoProof, "", "2", 1);
                    mVideoDialog.setDialogVideoClickListener(new MediaDialog.DialogMediaClickListener() {
                        @Override
                        public void click(String path) {
                            videoPlay(path);
                        }

                        @Override
                        public void cancel() {

                        }


                    });

                    mVideoDialog.show();
                } else {
                    ToastUtil.show("没有记录");
                }
            }
        });

        mTextPicLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Proof> photoProof = getOneTypeProof(mProof, "0");
                if (photoProof == null || photoProof.size() <= 0){
                    ToastUtil.show("没有记录");
                    return;
                }
                if (mTextWithPicDialog == null) {
                    mTextWithPicDialog = new TextWithPicDialog(TeacherEventDetailActivity.this, photoProof, "", 1);
                }else{
                    mTextWithPicDialog.updateDate(photoProof, "", 1);
                }
                mTextWithPicDialog.show();
            }
        });

        mMediaLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Proof> photoProof = getOneTypeProof(mProof, "1");
                if (photoProof != null && photoProof.size() > 0) {
                    mMediaDialog = new MediaDialog(TeacherEventDetailActivity.this, photoProof, "", "1", 1);
                    mMediaDialog.setDialogAudioClickListener(new MediaDialog.DialogMediaClickListener() {
                        @Override
                        public void click(String path) {
                            startNetAudio(path);
                        }

                        @Override
                        public void cancel() {
                            stopAudioPlay();
                        }
                    });

                    mMediaDialog.show();
                } else {
                    ToastUtil.show("没有记录");
                }
            }
        });
    }

    private ArrayList<Proof> getOneTypeProof(ArrayList<Proof> allProof, String type){
        ArrayList<Proof> tempProof = null;
        if (allProof == null || allProof.size() <= 0){
            return null;
        }

        for (Proof one: allProof){
            if (one.type.equals(type)){
                if (tempProof == null){
                    tempProof = new ArrayList<Proof>();
                }
                tempProof.add(one);
            }
        }
        return tempProof;
    };


    private void videoPlay(String path) {

        LogUtil.i("video play path", path);

        File file = isHasCurrentFile(path);

        if (file != null) {
            //播放
            startPlayVideo(file);
        } else {
            //下载
            Toast.makeText(this, "开始下载...", Toast.LENGTH_LONG).show();
            disLoading("正在下载...");
            DownLoadManagerUtil.getInstance().downLoadFile(this, path);
        }
    }

    private void startNetAudio(String path) {
        if (player == null) {
            player = new MediaPlayer();
        }

        if (player.isPlaying()){
            player.stop();
        }

        if (mMediaDialog != null){
            mMediaDialog.startPlay(0);
        }


        String relativeUrl = "";
        if (!TextUtils.isEmpty(path)){
            relativeUrl = path.replace("\\", "/");
        }else{
            ToastUtil.show("路径为空");
            return;
        }

        try {
            player.reset();

            //以下主要针对路径中含有中文或空格的情况
            //对路径进行编码 然后替换路径中所有空格 编码之后空格变成“+”而空格的编码表示是“%20” 所以将所有的“+”替换成“%20”就可以了
            String tempPath = URLEncoder.encode(relativeUrl,"utf-8").replaceAll("\\+", "%20");
            //编码之后的路径中的“/”也变成编码的东西了 所有还有将其替换回来 这样才是完整的路径
            tempPath = tempPath.replaceAll("%3A", ":").replaceAll("%2F", "/");

            player.setDataSource(tempPath);

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (mMediaDialog != null){
                        mMediaDialog.finishPlay();
                    }
                }
            });
            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    ToastUtil.show("播放出错:"+ i + "," + i1);
                    return false;
                }
            });
            player.prepareAsync();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            if (mMediaDialog != null){
                mMediaDialog.finishPlay();
            }
        }



    }

    private void stopAudioPlay(){
        if (player == null) {
            return;
        }

        if (player.isPlaying()){
            player.stop();
        }

        if (mMediaDialog != null){
            mMediaDialog.finishPlay();
        }
    }

    private File isHasCurrentFile(String url) {

        String curentFileName = url.substring(url.lastIndexOf("/") + 1);
        File fileDir = new File(mDownloadPath);

        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File[] listFile = fileDir.listFiles();
        int size = listFile.length;
        for (int i = 0; i < size; i++) {
            if (listFile[i].getName().equals(curentFileName)) {
                return listFile[i];
            }
        }
        return null;
    }

    public void startToplayAudio(File file) {
        try {
            String typeStr = DownLoadManagerUtil.getMimeType(file.getPath());
            Intent mIntent = new Intent();
            mIntent.setAction(android.content.Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            LogUtil.i("file uri", uri + "");
            mIntent.setDataAndType(uri, typeStr);
            TeacherEventDetailActivity.this.startActivity(mIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startPlayVideo(File videofile){
        String absolutePath = videofile.getAbsolutePath();
        //调用系统自带的播放器
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            Uri uri;
            if (Build.VERSION.SDK_INT >= 24) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                uri= FileProvider.getUriForFile(this, this.getPackageName()+".fileprovider", new File(videofile.getAbsolutePath()));
            }else {
                uri = Uri.parse("file://" + videofile.getAbsolutePath());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            intent.setDataAndType(uri, "video/mp4");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    //未确认事件
    private void statusUnconfirm(){
        showTopInfo();
        showProofInfo();

        if (mOperateStatus != 1 && mOperateStatus != 2){
            return;
        }
        mLayoutConfirm.setVisibility(View.VISIBLE);
        mEtRefuseReason.setFocusable(true);
        mEtRefuseReason.setEnabled(true);
        mEtRefuseReason.setMovementMethod(ScrollingMovementMethod.getInstance());
        mEtRefuseReason.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }}
        );

        mBtnEvent.setText("确认事件");
        UserDetail eventstudent  = mEventDetail.student;
        if (eventstudent != null){
            mTvMainName.setText(eventstudent.name);
            mTvDutyName.setText(mEventDetail.involvedTypeName);
            mIsAcceptRaidoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.confirm_yes_radio){
                        mIsAcceptEvent = true;
                        mLayoutParents.setVisibility(View.VISIBLE);
                        //去选择相关联的家长
                    }else{
                        mIsAcceptEvent = false;
                        mLayoutParents.setVisibility(View.GONE);
                    }
                }
            });
            mIsAcceptRaidoGroup.check(R.id.confirm_yes_radio);
            ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default,eventstudent.avatar,mIvMainAvatar);

            //涉事人
            ParentFromStudent parentone = new ParentFromStudent(false);
            mParentList = new ArrayList<>();
            mParentList.add(parentone);

            mInvolveParentAdapter = new EventDetailParentAdapter(this);
            mGvParents.setAdapter(mInvolveParentAdapter);
            mInvolveParentAdapter.setmList(mParentList);
            mInvolveParentAdapter.notifyDataSetChanged();
            mGvParents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ParentFromStudent oneparent = mParentList.get(position);
                    if (!oneparent.isPeople){
                        Intent intent = new Intent(TeacherEventDetailActivity.this,EventParentChooseActivity.class);
                        intent.putExtra(Key.KEY_PEOPLE_UID, mPeopleUid);
                        startActivityForResult(intent, Constant.CODE_REQUEST_PARENTS);
                    }else{
                        mParentList.remove(position);
                        mInvolveParentAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        setOneBtnListener();

    }

    //刷新列表
    private void sendBrodcastRefresh(){
        Intent intent = new Intent();
        intent.setAction(Constant.BROADCAST_REFRESH_EVENT);
        this.sendBroadcast(intent);
    }

    // 已确认事件
    private void statusConfirm() {
        showTopInfo();
        showProofInfo();

        if (mOperateStatus != 1 && mOperateStatus != 2){
            return;
        }

        mLayoutConfirm.setVisibility(View.GONE);
        mLayoutEventDeal.setVisibility(View.VISIBLE);

        mEtPunishDetail.setMovementMethod(ScrollingMovementMethod.getInstance());
        mEtPunishDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }});

        mEtSampleDetail.setMovementMethod(ScrollingMovementMethod.getInstance());
        mEtSampleDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }});
        mEtDealStrategy.setMovementMethod(ScrollingMovementMethod.getInstance());
        mEtDealStrategy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }});

        mBtnEvent.setText("处理事件");
        mWarnList = new ArrayList<>();

        UserDetail eventstudent  = mEventDetail.student;
        if (eventstudent != null) {
            mTvDealName.setText(eventstudent.name);
            mTvDealDuty.setText(mEventDetail.involvedTypeName);
            ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default, eventstudent.avatar, mImgDealAvatar);
        }

        PunishData punishdata  = mEventBean.deal;

            mLayoutPunishType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDealstandard();
                }
            });


        mIvSampleDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(Key.EVENT_TITLE, 1);
                intent.setClass(TeacherEventDetailActivity.this, SampleListsActivity.class);
                startActivityForResult(intent, Constant.CODE_REQUEST_SAMPLE);
            }
        });
        mIvDealStrategy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(Key.EVENT_TITLE, 2);
                intent.setClass(TeacherEventDetailActivity.this, SampleListsActivity.class);
                startActivityForResult(intent, Constant.CODE_REQUEST_SAMPLE);
            }
        });

        mTvCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRecommandCourse();
            }
        });

        //是否跟踪
        mCbFollowTrack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mIsNeedTrack = b;
            }
        });

        mCbOfficeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mIsNeedValid = b;
            }
        });

        //专家

        EventAboutPeople addSign = new EventAboutPeople(false);
        mExpertList.add(addSign);

        mProfessorListAdapter = new EventAboutPeopleAdapter(this);
        mGvExpert.setAdapter(mProfessorListAdapter);
        mProfessorListAdapter.setmList(mExpertList);
        mGvExpert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventAboutPeople people =  mExpertList.get(position);
                if (!people.isPeople ){
                    Intent intent = new Intent(TeacherEventDetailActivity.this,ProfessorListActivity.class);
                    startActivityForResult(intent,10003);
                }else{
                    mExpertList.remove(position);
                    mProfessorListAdapter.notifyDataSetChanged();
                }
            }
        });

        setOneBtnListener();
    }

    //用于处理时的显示
    private void setSeekbar(){
        if (mScoreRange == null){
            mSbPunishScore.setEnabled(false);
            mTvScoreUp.setText("0");
            mTvScoreDown.setText("0");

        }else{
            mTvScoreUp.setText(""+mScoreRange.upScore);
            mTvScoreDown.setText(""+mScoreRange.downScore);
            int tempMax = (int)(mScoreRange.upScore-mScoreRange.downScore);
            mValueWidth = mSbPunishScore.getWidth();
            mLayoutHight = mLayoutSeekbar.getHeight();

            mMoveStep = (float) (((float) mValueWidth / tempMax) * 0.8);
            if (tempMax <= 0){
                mSbPunishScore.setEnabled(false);
                if (tempMax == 0){
                    mPunishScore = mScoreRange.upScore;
                    mTvSeekbarValue.layout((int) (mValueWidth/2), 10, mValueWidth, mLayoutHight - 10);
                    mTvSeekbarValue.setText("" + mPunishScore);
                }
            }else{
                mSbPunishScore.setEnabled(true);
                mSbPunishScore.setMax(tempMax);
                mSbPunishScore.setProgress(tempMax/2);

                mSbPunishScore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        mTempScore = i;
                        double tempScore = mScoreRange.downScore + mTempScore;
                        mTvSeekbarValue.layout((int) (mTempScore * mMoveStep), 10, mValueWidth, mLayoutHight - 10);
                        mTvSeekbarValue.setText(tempScore + "");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mPunishScore = mScoreRange.downScore + mTempScore;
                        Log.d("", "current Value: " + mTempScore + " ,mPunishScore：" + mPunishScore);
                    }
                });
            }

        }
    }

    //仅仅显示用
    private void setSeekbarForDisplay(){
        if (mScoreRange == null){
            mSbPunishScore.setEnabled(false);
            mTvScoreUp.setText("0");
            mTvScoreDown.setText("0");
        }else{
            mTvScoreUp.setText(""+mScoreRange.upScore);
            mTvScoreDown.setText(""+mScoreRange.downScore);
            mValueWidth = mSbPunishScore.getWidth();
            mLayoutHight = mLayoutSeekbar.getHeight();

            int tempMax = (int)(mScoreRange.upScore-mScoreRange.downScore);
            mMoveStep = (float) (((float) mValueWidth / tempMax) * 0.8);
            if (tempMax <= 0){
                mSbPunishScore.setEnabled(false);
                mTvSeekbarValue.layout((int) (mLayoutHight/2), 10, mValueWidth, mLayoutHight - 10);
                mTvSeekbarValue.setText("" + (int)mScoreRange.upScore);
            }else{
                mSbPunishScore.setEnabled(true);
                mSbPunishScore.setMax(tempMax);
                if (mEventBean != null && mEventBean.deal != null){
                    int current = (int)(mEventBean.deal.score - mScoreRange.downScore);
                    mSbPunishScore.setProgress(current);
                    mTvSeekbarValue.layout((int) (current * mMoveStep), 10, mValueWidth, mLayoutHight - 10);
                    mTvSeekbarValue.setText("" + mEventBean.deal.score);
                }

            }

        }
    }



    //待审核
    private void statusUncheck() {
        showTopInfo();
        showProofInfo();
        showPunishInfo();
        getDealstandardForDisplay();

        if (mOperateStatus != 1 && mOperateStatus != 3){
            return;
        }

        mLayoutEventDeal.setVisibility(View.VISIBLE);
        mLayoutCheck.setVisibility(View.VISIBLE);

        mRgCheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.check_yes_radio){
                    mIsOfficeAccept = true;
                }else{
                    mIsOfficeAccept = false;
                }
            }
        });
        mRgCheck.check(R.id.check_yes_radio);

        mEtCheckOpinion.setMovementMethod(ScrollingMovementMethod.getInstance());
        mEtCheckOpinion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }});

        mBtnEvent.setText("审核事件");
        setOneBtnListener();

    }

    //待追踪
    private void statusUnTrack(){
        showTopInfo();
        showProofInfo();
        showPunishInfo();
        getDealstandardForDisplay();
        showTrackList();

        if (mOperateStatus != 1 && mOperateStatus != 2 &&
                mOperateStatus != 4){
            return;
        }

        mLayoutTrackInput.setVisibility(View.VISIBLE);
        int role = ConfigUtil.getInstance(this).get(Constant.KEY_ROLE_TYPE, -1);
        if (role == Constant.ROLE_PARENTS){
            mTvTrackName.setText("家长追踪反馈");
        }else if(role == Constant.ROLE_TEACHER){
            mTvTrackName.setText("班主任追踪反馈");
        }
        mLayoutPicture.setTag(1);
        mLayoutAudio.setTag(2);
        mLayoutVedio.setTag(3);
        mLayoutPicture.setOnClickListener(mUnTrackAttachment);
        mLayoutAudio.setOnClickListener(mUnTrackAttachment);
        mLayoutVedio.setOnClickListener(mUnTrackAttachment);

        //可以提交跟踪或结束事件
        mEndAndGoOnLinear.setVisibility(View.VISIBLE);

        if (mOperateStatus == 4){
            mFinishBtn.setVisibility(View.GONE);
        }else {
            mFinishBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(TeacherEventDetailActivity.this, FinishEventActivity.class);
                    intent.putExtra(Key.KEY_EVENT_UID, mEventUid);
                    intent.putExtra(Key.KEY_PEOPLE_UID, mPeopleUid);
                    ArrayList<EventEvaluation> parents = getEvaluation(1);
                    intent.putExtra(Key.KEY_PARENT_DATA, parents);
                    ArrayList<EventEvaluation> experts = getEvaluation(2);
                    intent.putExtra(Key.KEY_EXPERT_DATA, experts);
                    startActivity(intent);
                    finish();

                }
            });
        }

        mFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitTrackFile();
            }
        });


    }

    //type 1: 家长，　2: 专家（专家是关联上家长的，没有家长则没有专家）
    private ArrayList<EventEvaluation> getEvaluation(int type){
        ArrayList<EventEvaluation> arraylist = null;
        ArrayList<ParentData> parents = mEventBean.parents;
        ArrayList<Professor> experts = null;
        if (parents == null || parents.size() <= 0){
            //ToastUtil.show("没有相关联的家长");
            return null;
        }

        UserDetail user = null;
        if (type == 1){
            arraylist = new ArrayList<EventEvaluation>();

            for(ParentData one: parents){
                EventEvaluation oneEval = new EventEvaluation();
                oneEval.personneluid = one.uid;
                oneEval.personneltype= Constant.ROLE_PARENTS;
                user = new UserDetail();
                user.name = one.name;
                user.avatar= one.photo;
                oneEval.user = user;
                arraylist.add(oneEval);
            }
        }else {
            experts = parents.get(0).expert;
            if (experts == null || experts.size() <= 0){
                //ToastUtil.show("家长没有关联的专家");
            }else{
                arraylist = new ArrayList<EventEvaluation>();
                int expertlen = experts.size();
                Professor one = null;
                for (int i= 0; i<expertlen; i++){
                    one = experts.get(i);
                    if (one != null){
                        EventEvaluation oneEval = new EventEvaluation();
                        oneEval.personneluid = one.uid;
                        oneEval.personneltype= Constant.ROLE_EXPERT;
                        user = new UserDetail();
                        user.name = one.name;
                        user.avatar= one.photo;
                        oneEval.user = user;
                        arraylist.add(oneEval);
                    }
                }
            }

        }
        return arraylist;
    }

    private View.OnClickListener mUnTrackAttachment = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            int type = (int)view.getTag();
            switch (type){
                case 1:
                    addTextPicRecordLinear();
                    break;
                case 2:
                    addMediaRecordLinear();
                    break;
                case 3:
                    addVideoRecordClick();
                    break;
            }
        }
    };

    //添加图片
    public void addTextPicRecordLinear(){
        Intent intent = new Intent(this,AddEventWithPictrueTextActivity.class);
        intent.putStringArrayListExtra(Key.BEAN,mImagePaths);
        intent.putExtra(Key.RECORD_IMG_TEXT,TextUtils.isEmpty(mImageTextStr)?"":mImageTextStr);
        startActivityForResult(intent,Constant.CODE_REQUEST_IMG_TEXT);
    }

    //添加音频
    public void addMediaRecordLinear(){
        Intent intent = new Intent(this,SystemAudioListChooseActivity.class);
        intent.putParcelableArrayListExtra(Key.BEAN,mAudioLocalFileList);
        startActivityForResult(intent,Constant.CODE_REQUEST_MEDIA);
    }

    //添加视频
    public void addVideoRecordClick(){
        Intent intent = new Intent(this,VideoRecordActivity.class);
        startActivityForResult(intent,Constant.CODE_REQUEST_VIDEO);
    }

    //事件已结束
    private void statusFinish(){
        this.showTopInfo();
        this.showProofInfo();
        this.showPunishInfo();
        this.showTrackList();

        mLayoutLastReview.setVisibility(View.VISIBLE);
        mLayoutScoreParents.setVisibility(View.GONE);
        mLayoutScoreExperts.setVisibility(View.GONE);

        setLastReviewPeople();
        if (mReviewParentList != null && mReviewParentList.size() > 0) {
            mLayoutScoreParents.setVisibility(View.VISIBLE);
            EventReviewListAdapter mReviewParentsAdapter = new EventReviewListAdapter(this, 1);
            mReviewParentsAdapter.setmList(mReviewParentList);
            mLvParents.setAdapter(mReviewParentsAdapter);
        }
        if (mReviewExpertList != null && mReviewExpertList.size() > 0) {
            mLayoutScoreExperts.setVisibility(View.GONE);
            EventReviewListAdapter mReviewExpertsAdapter = new EventReviewListAdapter(this, 2);
            mReviewExpertsAdapter.setmList(mReviewExpertList);
            mLvExperts.setAdapter(mReviewExpertsAdapter);
        }

        if (mEventStatus == Constant.EVENT_STATUS_REFUSE){
            mTvReviewName.setText("拒绝理由");
        }else {
            mTvReviewName.setText("结案陈词");
        }
        mEtReviewInput.setText(mEventDetail.overReason);
        mEtReviewInput.setFocusable(false);
        mEtReviewInput.setFocusableInTouchMode(false);
        mEtReviewInput.setMovementMethod(ScrollingMovementMethod.getInstance());
        mEtReviewInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }});

        getDealstandardForDisplay();

    }

    //区分是给家长评价还是专家评价
    private void setLastReviewPeople(){
        ArrayList<EventEvaluation> templist = mEventBean.eventEvaluation;
        if (templist == null || templist.size() <= 0){
            return;
        }
        for (EventEvaluation one: templist){
            if (one.personneltype == Constant.ROLE_PARENTS){
                if (mReviewParentList == null){
                    mReviewParentList = new ArrayList<EventEvaluation>();
                }
                mReviewParentList.add(one);
            }else if(one.personneltype == Constant.ROLE_EXPERT){
                if (mReviewExpertList == null){
                    mReviewExpertList = new ArrayList<EventEvaluation>();
                }
                mReviewExpertList.add(one);
            }
        }
    }

    /*
    * 获取事件详情,并确定可操作状态
    *-1 都不可以操作（任课老师）
    * 1 各个状态都可以操作（班主任又是学生处老师）　
    * 2 各个状态都可以操作,除了审核（班主任）
    * 3　只可操作审核（学生处）　
    * 4　只可操作追踪（家长）
    */
    private void getEventDetail(String eventid, String studentuid) {

        disLoading();

        TeacherModel.getInstance().eventDetail(eventid, studentuid, new BaseListener(EventDetailData.class) {
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);

                hideLoading();
                if (infoObj != null) {
                    mEventBean = (EventDetailData) infoObj;
                    mEventDetail= mEventBean.eventDetail;

                    int role = ConfigUtil.getInstance(TeacherEventDetailActivity.this).get(Constant.KEY_ROLE_TYPE, -1);
                    String userUid = ConfigUtil.getInstance(TeacherEventDetailActivity.this).get(Constant.KEY_USER_ID, "");
                    if (role == Constant.ROLE_TEACHER){
                        boolean  isOffice = ConfigUtil.getInstance(TeacherEventDetailActivity.this).get(Constant.KEY_IS_STUDENT_AFFAIRS, false);
                        if (isOffice && mEventDetail.headTeacherUID.equals(userUid)){
                            mOperateStatus = 1;
                        }else if (isOffice){
                            mOperateStatus = 3;
                        }else if (mEventDetail.headTeacherUID.equals(userUid)){
                            mOperateStatus = 2;
                        }
                    }else if (role == Constant.ROLE_PARENTS){
                        mOperateStatus = 4;
                    }

                    mEventDetail.uid = mEventUid;
                    mCurentType = EVENT_TYPE.NORMAL;
                    mEventStatus= mEventDetail.status;
                    mEventType  = mEventDetail.type;

                        if ("1".equals(mEventDetail.categoriesName)){
                            mTvReviewName.setText("处理意见");
                        }else {
                            mTvReviewName.setText("结案陈词");
                    }
                    switch (mEventDetail.status){
                        case Constant.EVENT_STATUS_REFUSE:
                            statusFinish();
                            break;
                        case Constant.EVENT_STATUS_UNCONFIRM:
                            statusUnconfirm();
                            break;
                        case Constant.EVENT_STATUS_CONFIRMED:
                            statusConfirm();
                            break;
                        case Constant.EVENT_STATUS_UNCHECK:
                            statusUncheck();
                            break;
                        case Constant.EVENT_STATUS_UNTRACK:
                            statusUnTrack();
                            break;
                        case Constant.EVENT_STATUS_FINISHED:
                            statusFinish();
                            break;
                        default:
                            ToastUtil.show("不能识别的状态" + mEventDetail.status);
                    }

                } else {
                    ToastUtil.show(Constant.REQUEST_FAILED_STR);
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR : errorMessage);
            }
        });
    }

    //上传追踪数据
    private void commitTrackFile(){
        if (mImagePaths.size() == 0){
            if (mAudioLocalFileList.size() == 0){
                if (TextUtils.isEmpty(mVideoPath)){
                    commitTrack();
                }else{
                    uploadVideo();
                }
            }else{
                uploadAudio();
            }

        }else{
            uploadImages();
        }
    }

    //事件跟进
    private void commitTrack() {

        String str = createEventTrackParamStr();
        if (str == null) {
            return;
        }
        disLoading("加载中", false);
        TeacherModel.getInstance().eventTrack(str, new BaseListener() {

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();

                JSONObject jsonObject = JSONObject.parseObject(infoObj.toString());
                ToastUtil.show("事件跟进成功");
                sendBrodcastRefresh();
//                if (jsonObject.containsKey(Constant.UID)) {
//                    ToastUtil.show("事件跟进成功");
//                } else {
//                    ToastUtil.show("事件跟进失败");
//                }
                finish();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(TextUtils.isEmpty(errorMessage) ? Constant.REQUEST_FAILED_STR : errorMessage);
            }
        });

    }

    //创建事件确认json参数
    private String createConfirmParamStr() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventUid", mEventUid);
        jsonObject.put("studentUid", mPeopleUid);
        jsonObject.put("isConfirm", mIsAcceptEvent);

        //相关联的家长
        JSONArray subParent = new JSONArray();
        for (ParentFromStudent people:mParentList){
            if (people.isPeople){
                subParent.add(people.uid);
            }
        }
        jsonObject.put("parents", subParent);
        jsonObject.put("reason", mEtRefuseReason.getText().toString());
        LogUtil.i("params", jsonObject.toString());

        return jsonObject.toString();
    }

    //处理事件确认json参数
    private String createDealParamStr() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventUid", mEventUid);
        jsonObject.put("studentUid", mPeopleUid);
        if (TextUtils.isEmpty(mCurrentPunish)){
            ToastUtil.show("请选择处分类型");
            return null;
        }
        jsonObject.put("punishment", mCurrentPunish);
        jsonObject.put("punishmentInfo", mEtPunishDetail.getText().toString());
        jsonObject.put("analysis", mEtSampleDetail.getText().toString());
        jsonObject.put("countermeasure", mEtDealStrategy.getText().toString());
        jsonObject.put("tutoringUid", mCourseUid);
        jsonObject.put("score", mPunishScore);
        jsonObject.put("needOfficeValid", mIsNeedValid);
        jsonObject.put("needTrack", mIsNeedTrack);

        JSONArray professorArray = new JSONArray();
        if (mExpertList != null && mExpertList.size() > 1){

            for (int j = 0;j < mExpertList.size()-1; j++){
                if (mExpertList.get(j).isPeople){
                    professorArray.add(mExpertList.get(j).uid);
                }
            }
        }

        jsonObject.put("Expert", professorArray);
        jsonObject.put("evidence", new JSONArray());

        LogUtil.i("params", jsonObject.toString());

        return jsonObject.toString();
    }

    private String createEventTrackParamStr() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventUID", mEventUid);
        jsonObject.put("studentUID", mPeopleUid);

        String trackDes = mEtTrackDesc.getText().toString();
        if (TextUtils.isEmpty(trackDes)
                && mUploadAudios.size() == 0
                && mUploadedImages.size() == 0
                && mUploadVideos.size() == 0){
            ToastUtil.show("追踪内容不能为空,请重新输入");
            return null;
        }
        jsonObject.put("content", trackDes);

        JSONArray filsArr = new JSONArray();
        JSONObject fileobj = null;
        if (mUploadedImages != null && mUploadedImages.size() > 0){
            for (UploadFileBean one: mUploadedImages){
                fileobj = new JSONObject();
                fileobj.put("type", "0");
                fileobj.put("file", one.fileuid);
                filsArr.add(fileobj);
            }
        }
        if (mUploadAudios != null && mUploadAudios.size() > 0){
            for (UploadFileBean one: mUploadAudios){
                fileobj = new JSONObject();
                fileobj.put("type", "1");
                fileobj.put("file", one.fileuid);
                filsArr.add(fileobj);
            }
        }
        if (mUploadVideos != null&& mUploadVideos.size() > 0){
            fileobj = new JSONObject();
            fileobj.put("type", "2");
            fileobj.put("file", mUploadVideos.get(0).fileuid);
            filsArr.add(fileobj);
        }
        jsonObject.put("files", filsArr);

        LogUtil.i("params", jsonObject.toString());
        return jsonObject.toString();
    }

    //学生处老师审核
    private void commondCheck() {
        String str = createCheckParamStr();
        if (str == null){
            return;
        }
        disLoading();
        TeacherModel.getInstance().commitEventAudit(str,new BaseListener(){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();

                JSONObject jsonObject = JSONObject.parseObject(infoObj.toString());
                if (jsonObject.containsKey(Constant.UID)){
                    ToastUtil.show("提交审核成功");
                    sendBrodcastRefresh();
                }else{
                    ToastUtil.show("提交审核失败");
                }
                finish();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(TextUtils.isEmpty(errorMessage) ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });
    }

    private String createCheckParamStr(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventUID", mEventUid);
        jsonObject.put("studentUID", mPeopleUid);
        jsonObject.put("OfficeValid", mIsOfficeAccept);
        jsonObject.put("remark", mEtCheckOpinion.getText().toString());

        LogUtil.i("params", jsonObject.toString());
        return jsonObject.toString();
    }


    @OnClick(R.id.back_frame)
    public void finishActivityClick() {
        finish();
    }

    private void setOneBtnListener(){
        if (mEventStatus == Constant.EVENT_STATUS_REFUSE || mEventStatus == Constant.EVENT_STATUS_FINISHED
                || mEventStatus == Constant.EVENT_STATUS_UNTRACK){
            mLayoutOneBtn.setVisibility(View.GONE);
        }else{
            mLayoutOneBtn.setVisibility(View.VISIBLE);
            mBtnEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (mEventStatus){
                        case Constant.EVENT_STATUS_UNCONFIRM:
                            commitConfirm();
                            break;
                        case Constant.EVENT_STATUS_CONFIRMED:
                            commitDeal();
                            break;
                        case Constant.EVENT_STATUS_UNCHECK:
                            commondCheck();
                            break;
                    }
                }
            });
        }

    }

    //事件确认提交
    private void commitConfirm(){
        String str = createConfirmParamStr();
        if (str == null){
            return;
        }
        disLoading();
        TeacherModel.getInstance().commitEventConfirm(str,new BaseListener(){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();

                JSONObject jsonObject = JSONObject.parseObject(infoObj.toString());
                if (jsonObject.containsKey(Constant.UID)){
                    ToastUtil.show("事件确认成功");
                    sendBrodcastRefresh();
                }else{
                    ToastUtil.show("事件确认失败");
                }
                finish();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(TextUtils.isEmpty(errorMessage) ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });
    }

    //事件处理提交
    private void commitDeal(){
        String str = createDealParamStr();
        if (str == null){
            return;
        }
        disLoading();
        TeacherModel.getInstance().commitEventDeal(str,new BaseListener(){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();

                JSONObject jsonObject = JSONObject.parseObject(infoObj.toString());
                if (jsonObject.containsKey(Constant.UID)){
                    ToastUtil.show("提交处理成功");
                    sendBrodcastRefresh();
                }else{
                    ToastUtil.show("提交处理失败");
                }
                finish();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(TextUtils.isEmpty(errorMessage) ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });

    }


    //处分类型
    private void getDealstandard(){

        if (mScoreRange != null){
            choosePunishType(mTvWarnName, mScoreRange.punishment);
            setSeekbar();
            return;
        }
        TeacherModel.getInstance().getDealstandard(mEventType, new BaseListener(Warn.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);

                if (infoObj != null){
                    mScoreRange = (Warn) infoObj;
                    mWarnList.clear();
                    mWarnList.addAll(mScoreRange.punishments);
                    choosePunishType(mTvWarnName, mScoreRange.punishment);
                    setSeekbar();
                }else{
                    ToastUtil.show("警告等级获取失败！");
                }
                hideLoading();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(TextUtils.isEmpty(errorMessage) ? Constant.REQUEST_FAILED_STR : errorMessage);
            }
        });
    }

    //仅仅为显示不同事件类型的扣分上下限
    private void getDealstandardForDisplay(){

        TeacherModel.getInstance().getDealstandard(mEventType, new BaseListener(Warn.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);

                if (infoObj != null){
                    mScoreRange = (Warn) infoObj;
                    setSeekbarForDisplay();
                }else{
                    ToastUtil.show("警告等级获取失败！");
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show(TextUtils.isEmpty(errorMessage) ? Constant.REQUEST_FAILED_STR : errorMessage);
            }
        });
    }

    private void choosePunishType(final TextView dropView, KeyValue defaultKY){
        dropView.setText(defaultKY.value);
        if (mWarnList != null && mWarnList.size() > 0){
            final KeyValueDropView keyvalueDropView = new KeyValueDropView(this,dropView,mWarnList);
            keyvalueDropView.showAsDropDown(dropView);
            keyvalueDropView.setPopOnItemClickListener(new KeyValueDropView.PopOnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    TextView mTextView = (TextView) dropView;
                    mTextView.setText(mWarnList.get(position).value);
                    mCurrentPunish = mTextView.getText().toString();
                    keyvalueDropView.dismiss();
                }
            });
        }else{
            ToastUtil.show("没有数据");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (intent == null){
            return;
        }

        switch (resultCode){
            case Constant.CODE_RESULT_PARENTS: {
                ParentFromStudent people = (ParentFromStudent)intent.getSerializableExtra(Key.KEY_PARENT_DATA);
                if (isHasSameEelement(mParentList,people)){
                    return;
                }
                mParentList.add(mParentList.size()-1,people);
                mInvolveParentAdapter.notifyDataSetChanged();
                break;
            }
            case Constant.CODE_RESULT_VIDEO: {
                String tempFile = intent.getStringExtra(Key.RECORD_VIDEO);
                mVideoPath = tempFile;
                break;
            }
            case Constant.CODE_RESULT_IMG_TEXT: {
                mImagePaths.clear();
                mImageTextStr = intent.getStringExtra(Key.RECORD_VIDEO);
                List<String> tempImgList = intent.getStringArrayListExtra(Key.BEAN);
                mImagePaths.addAll(tempImgList);
                LogUtil.i("imgepath","" + mImagePaths.toString());
                break;
            }
            case Constant.CODE_RESULT_MEDIA: {
                mAudioLocalFileList.clear();
                List<LocalFile> temImgList =intent.getParcelableArrayListExtra(Key.BEAN);
                mAudioLocalFileList.addAll(temImgList);
                break;
            }
            case Constant.CODE_RESULT_SAMPLE:{
                int type = intent.getIntExtra("type", -1);
                String value = intent.getStringExtra("value");
                if (type == 1){
                    mEtSampleDetail.setText(value);
                }else{
                    mEtDealStrategy.setText(value);
                }
                break;
            }
            case Constant.CODE_RESULT_PROFESSOR:{
                EventAboutPeople people = (EventAboutPeople) intent.getSerializableExtra(Key.BEAN);
                EventAboutPeople one = null;
                for (int i = 0;i<mExpertList.size();i++){
                    one = mExpertList.get(i);
                    if (one.isPeople && one.uid.equals(people.uid)){
                        ToastUtil.show("不能添加相同的人");
                        return;
                    }
                }
                mExpertList.add(mExpertList.size()-1,people);
                mProfessorListAdapter.notifyDataSetChanged();
                break;
            }
        }


        super.onActivityResult(requestCode, resultCode, intent);
    }

    private boolean isHasSameEelement(List<ParentFromStudent> peopleList,ParentFromStudent selectPeople){
        for (ParentFromStudent p: peopleList){
            if (p.isPeople && p.uid.equals(selectPeople.uid)){
                ToastUtil.show("已选择"+selectPeople.name);
                return true;
            }
        }
        return false;
    }


    private void uploadImages(){
        disLoading("正在上传图片", false);
        UploadModel.getInstance().uploadManyWithLuBan(this, mHandler, mImagePaths, 1, new BaseListener(UploadFileBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();
                if (listObj != null){
                    mUploadedImages = (ArrayList<UploadFileBean>)listObj;
                    ToastUtil.show("图片上传成功");

                    if (mAudioLocalFileList.size() == 0){
                        commitTrack();
                    }else{
                        uploadAudio();
                    }

                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show("上传图片失败"+ (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    private void uploadAudio(){

        ArrayList<String> tempAudioList = new ArrayList<>();
        for (LocalFile localFile :mAudioLocalFileList){
            tempAudioList.add(localFile.localPath);
        }
        disLoading("正在上传音频", false);
        UploadModel.getInstance().uploadMany(tempAudioList, 1, new BaseListener(UploadFileBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();
                if (listObj != null){

                    mUploadAudios = (ArrayList<UploadFileBean>)listObj;
                    ToastUtil.show("音频上传成功");

                    if (TextUtils.isEmpty(mVideoPath)){
                        //成功后添加事件
                        commitTrack();
                    }else{
                        uploadVideo();
                    }


                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show("上传音频失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    private void uploadVideo(){

        ArrayList<String> tempVideoList = new ArrayList<>();
        if (!TextUtils.isEmpty(mVideoPath)){
            tempVideoList.add(mVideoPath);
        }

        disLoading("正在上传视频", false);
        UploadModel.getInstance().uploadMany(tempVideoList, 2, new BaseListener(UploadFileBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();
                if (listObj != null){
                    ArrayList<UploadFileBean> tempFile = (ArrayList<UploadFileBean>)listObj;
                    mUploadVideos = tempFile;
                    ToastUtil.show("视频上传成功");

                    //成功后添加事件
                    commitTrack();

                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show("视频上传失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    //获取推荐课程
    private void getRecommandCourse(){
        //disLoading("获取推荐课程");
        TeacherModel.getInstance().recommandCourse(new BaseListener(EventCourse.class){
            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
                if (listObj != null){
                    mCourseList = (ArrayList<EventCourse>)listObj;
                    //ToastUtil.show("获取推荐课程成功");
                    ArrayList<String> courseArr = new ArrayList<String>();
                    for (EventCourse one: mCourseList){
                        courseArr.add(one.name);
                    }
                    chooseCourseType(mTvCourse, courseArr);
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                //hideLoading();
                ToastUtil.show("获取推荐课程失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    private void chooseCourseType(final TextView dropView, ArrayList<String> values){

        if (values != null && values.size() > 0){
            final CommonDropView keyvalueDropView = new CommonDropView(this,dropView,values);
            keyvalueDropView.showAsDropDown(dropView);
            keyvalueDropView.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    TextView mTextView = (TextView) dropView;
                    mTextView.setText(mCourseList.get(position).name);
                    mCourseUid = mCourseList.get(position).uid;
                    keyvalueDropView.dismiss();
                }
            });
        }else{
            ToastUtil.show("没有数据");
        }
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText  需要判断的EditText
     * @return  true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }


}

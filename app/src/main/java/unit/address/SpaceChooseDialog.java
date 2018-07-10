package unit.address;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.util.ListViewMeasureUtil;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import unit.moudle.personal.PhotoChooser;

/**
 * Created by lei on 2018/6/13.
 */

public class SpaceChooseDialog extends Dialog{
    private Context mContext;

    private ListView buildLayout;
    private ListView floorLayout;
    private ListView roomLayout;
    private TextView cancle,chooseTV,sure;

    private SpaceBuildAdapter mBuildAdapter;
    private SpaceFloorAdapter mFloorAdapter;
    private SpaceRoomAdapter mRoomAdapter;

    private ArrayList<SpaceEnitity> mBuildData;
    private ArrayList<Floor> mFloorData;
    private ArrayList<Room> mRoomData;

    private String mChooseBuild;
    private String mChooseFloor;
    private String mChooseRoom;
    private String mChooseId;
    private ChooseSpaceCallBack chooseCallBack;
    public SpaceChooseDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SpaceChooseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public void init(Context context){
        this.mContext = context;
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        getWindow().setWindowAnimations(R.style.anim_bottom_style);
        View rootView = getLayoutInflater().inflate(R.layout.puti_space_dialog,null);
        int screenWidth = ViewUtils.getScreenWid(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenWidth, ViewUtils.dip2px(mContext,150));
        super.setContentView(rootView,params);
        initView();
    }


    private void initView(){
        buildLayout = (ListView) findViewById(R.id.build_list);
        floorLayout = (ListView) findViewById(R.id.floor_list);
        roomLayout = (ListView) findViewById(R.id.rooms_list);
        cancle = (TextView) findViewById(R.id.cancel);
        sure = (TextView) findViewById(R.id.sure);
        chooseTV = (TextView) findViewById(R.id.choose_tv);

       if (mBuildData == null){
           mBuildData = new ArrayList<>();
       }
       if (mFloorData == null){
           mFloorData = new ArrayList<>();
       }
       if (mRoomData == null){
           mRoomData = new ArrayList<>();
       }

       if (mBuildAdapter == null){
           mBuildAdapter = new SpaceBuildAdapter(mContext,mBuildData);
       }
       if (mFloorAdapter == null){
           mFloorAdapter = new SpaceFloorAdapter(mContext,mFloorData);
       }
       if (mRoomAdapter == null){
           mRoomAdapter = new SpaceRoomAdapter(mContext,mRoomData);
       }

        buildLayout.setAdapter(mBuildAdapter);
        floorLayout.setAdapter(mFloorAdapter);
        roomLayout.setAdapter(mRoomAdapter);



        buildLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpaceEnitity spaceEnitity = mBuildData.get(position);
                mFloorData.clear();
                mFloorData.addAll(spaceEnitity.getFloor());
                mFloorAdapter.notifyDataSetChanged();
                mRoomData.clear();
                if (mFloorData.size() > 0){
                    mRoomData.addAll(mFloorData.get(0).getRoom());
                }
                mRoomAdapter.notifyDataSetChanged();

                mChooseBuild = spaceEnitity.getBuild();
                mChooseFloor = "";
                if (mFloorData.size() > 0){
                    if (mRoomData.size() > 0){
                        mChooseRoom = mRoomData.get(0).getRoom();
                    }
                }
                setChooseText();

            }
        });
        floorLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Floor floor = mFloorData.get(position);
                mRoomData.clear();
                mRoomData.addAll(floor.getRoom());
                mRoomAdapter.notifyDataSetChanged();
                mChooseFloor = floor.getFloor();
                mChooseRoom = "";
//                if (mRoomData.size() > 0){
//                    mChooseRoom = mRoomData.get(0).getRoom();
//                }
                setChooseText();
            }
        });
        roomLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Room room = mRoomData.get(position);
                mChooseRoom = room.getRoom();
                mChooseId = room.getUID();
                setChooseText();
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseCallBack != null){
                    chooseCallBack.callBack(chooseTV.getText().toString(),mChooseId);
                }
                dismiss();
            }
        });
    }

    private void setChooseText(){
        StringBuilder builder = new StringBuilder();

        builder.append(mChooseBuild);
        if (!TextUtils.isEmpty(mChooseBuild) && !TextUtils.isEmpty(mChooseFloor)){
            builder.append(mChooseFloor);
        }
        if (!TextUtils.isEmpty(mChooseFloor) && !TextUtils.isEmpty(mChooseRoom)){
            builder.append(mChooseRoom);
        }
        chooseTV.setText(builder.toString());
    }


    public void setSpaceData(ArrayList<SpaceEnitity> data, ChooseSpaceCallBack chooseCallBack){
        this.chooseCallBack = chooseCallBack;
        mBuildData.clear();
        mBuildData.addAll(data);
        if (mBuildData.size() > 0){
            mFloorData.clear();
            mFloorData.addAll(mBuildData.get(0).getFloor());
            if (mFloorData.size() > 0){
                mRoomData.clear();
                mRoomData.addAll(mFloorData.get(0).getRoom());
            }
        }
        mBuildAdapter.notifyDataSetChanged();
    }

    public interface  ChooseSpaceCallBack{
        void callBack(String space,String id);
    }
}

package unit.address;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.puti.education.R;
import com.puti.education.util.ViewUtils;

import java.util.ArrayList;

import unit.moudle.personal.PhotoChooser;

/**
 * Created by lei on 2018/6/13.
 */

public class SpaceChooseDialog extends Dialog{
    private Context mContext;

    private Spinner buildLayout;
    private Spinner floorLayout;
    private Spinner roomLayout;

    private SpaceBuildAdapter mBuildAdapter;
    private SpaceFloorAdapter mFloorAdapter;
    private SpaceRoomAdapter mRoomAdapter;

    private ArrayList<SpaceEnitity> mBuildData;
    private ArrayList<Floor> mFloorData;
    private ArrayList<Room> mRoomData;

    private String mChooseBuild;
    private String mChooseFloor;
    private String mChooseRoom;
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
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        getWindow().setWindowAnimations(R.style.anim_bottom_style);
        View rootView = getLayoutInflater().inflate(R.layout.puti_space_dialog,null);
        int screenWidth = ViewUtils.getScreenWid(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenWidth-ViewUtils.dip2px(mContext,30), ViewGroup.LayoutParams.MATCH_PARENT);
        super.setContentView(rootView,params);
        initView();
    }


    private void initView(){
        buildLayout = (Spinner) findViewById(R.id.build_layout);
        floorLayout = (Spinner) findViewById(R.id.floor_layout);
        roomLayout = (Spinner) findViewById(R.id.room_layout);


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


        buildLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpaceEnitity spaceEnitity = mBuildData.get(position);
                mFloorData.clear();
                mFloorData.addAll(spaceEnitity.getFloor());
                mFloorAdapter.notifyDataSetChanged();
                mChooseBuild = spaceEnitity.getTypeName();
                if (mFloorData.size() == 0){
                    StringBuilder builder = new StringBuilder();
                    builder.append(mChooseBuild);
                    if (chooseCallBack != null){
                        chooseCallBack.callBack(builder.toString(),spaceEnitity.getBuild());
                    }
                    dismiss();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        floorLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Floor floor = mFloorData.get(position);
                mRoomData.clear();
                mRoomData.addAll(floor.getRoom());
                mRoomAdapter.notifyDataSetChanged();
                mChooseFloor = floor.getFloor();
                if (mRoomData.size() == 0){
                    StringBuilder builder = new StringBuilder();
                    builder.append(mChooseBuild).append(mChooseFloor);
                    if (chooseCallBack != null){
                        chooseCallBack.callBack(builder.toString(),"");
                    }
                    dismiss();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        roomLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Room room = mRoomData.get(position);
                mChooseRoom = room.getRoom();
                StringBuilder builder = new StringBuilder();
                builder.append(mChooseBuild).append(mChooseFloor).append(mChooseRoom);
                if (chooseCallBack != null){
                    chooseCallBack.callBack(builder.toString(),room.getUID());
                }
                dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    public void setSpaceData(ArrayList<SpaceEnitity> data, ChooseSpaceCallBack chooseCallBack){
        this.chooseCallBack = chooseCallBack;
        mBuildData.clear();
        mBuildData.addAll(data);
        mBuildAdapter.notifyDataSetChanged();
    }

    public interface  ChooseSpaceCallBack{
        void callBack(String space,String id);
    }
}

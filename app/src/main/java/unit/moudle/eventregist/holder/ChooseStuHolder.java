package unit.moudle.eventregist.holder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.ui.uiTeacher.AddEventZxingActivity;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.GridViewForScrollView;
import com.puti.education.zxing.ZxingUtil;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.Student;
import unit.entity.StudentEntity;
import unit.location.LocationHelper;
import unit.moudle.eventregist.ChooseStuManager;
import unit.moudle.eventregist.adapter.EventDetailChooseStuAdapter;
import unit.moudle.eventregist.callback.OprateStuCallBack;
import unit.permission.PermissionUtil;

/**
 * Created by lei on 2018/6/11.
 * 事件登记详情-选择学生holder
 */

public class ChooseStuHolder extends BaseHolder<Object> {
    @BindView(R.id.gridView)
    GridViewForScrollView gridView;
    @BindView(R.id.scan)
    RelativeLayout scan;

    private ArrayList<Student> mData;
    private EventDetailChooseStuAdapter mAdapter;
    public ChooseStuHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_event_detail_stu_holder);
        ButterKnife.bind(this, mRootView);
        if (mData == null){
            mData = new ArrayList<>();
        }
        if (mAdapter == null){
            mAdapter = new EventDetailChooseStuAdapter(mContext, mData, new OprateStuCallBack() {
                @Override
                public void chooseStu(Student student) {

                }

                @Override
                public void removeStu(Student student) {
                    mData.remove(student);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
        gridView.setAdapter(mAdapter);
       scan.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               requestPermissions((Activity) mContext);
           }
       });
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Object data) {
    }

    public void requestPermissions(final Activity activity) {
        PermissionUtil.g().requestPermissions(activity,
                new PermissionUtil.PermissionCallBack() {
                    @Override
                    public void success() {
                        ZxingUtil.g().startZxing(activity);
                    }
                    @Override
                    public void fail() {
                        ToastUtil.show("获取相机权限失败");
                    }
                },
                Manifest.permission.CAMERA);

        PermissionUtil.g().setRationaleListener(new RationaleListener() {
            @Override
            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                PermissionUtil.g().showRequestPermissionRationaleDialog(activity,
                        "你已拒绝过相机权限，沒有相机权限无法使用该功能！",
                        rationale);
            }
        });
    }

    public void setList(ArrayList<Student> data) {
        mData.clear();
        for (int i = 0; i < data.size(); i++) {
            Student student = data.get(i);
            if (mData.contains(student)){
                continue;
            }
            mData.add(student);
        }
        addAddItem();
        mAdapter.notifyDataSetChanged();
    }


    private void addAddItem(){
        Student student = new Student();
        student.setAdd(true);
        mData.add(student);
    }

}

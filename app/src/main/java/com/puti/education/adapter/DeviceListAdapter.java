package com.puti.education.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.BtDeviceBean;
import com.puti.education.bean.EventBean;
import com.puti.education.ui.uiTeacher.TeacherEventDetailActivity;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.widget.ItemContainer;


/**
 * Created by icebery on 2017/5/10 0019.
 *
 * 蓝牙设备 适配器
 */

public class DeviceListAdapter extends BasicRecylerAdapter<BtDeviceBean>{

    public DeviceListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_bt_devicelist;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CommonViewHolder cHolder = (CommonViewHolder) holder;
        TextView tvDeviceName = cHolder.obtainView(R.id.tv_bt_name);
        TextView tvDeviceStatus = cHolder.obtainView(R.id.tv_connect_status);
        TextView tvDeviceMac = cHolder.obtainView(R.id.tv_bt_mac);

        BtDeviceBean info = mList.get(position);
        tvDeviceName.setText(info.deviceName);
        tvDeviceMac.setText(info.deviceMac);
        if (info.deviceStatus == 1){
            tvDeviceStatus.setText("已连接");
        }else{
            tvDeviceStatus.setText("");
        }


        cHolder.itemView.setTag(position);
        cHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myItemOnclickListener != null){
                    int pos = (int)v.getTag();
                    myItemOnclickListener.onItemClick(pos);
                }
            }
        });
    }



}

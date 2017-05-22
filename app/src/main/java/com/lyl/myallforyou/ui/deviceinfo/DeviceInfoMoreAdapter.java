package com.lyl.myallforyou.ui.deviceinfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.data.DeviceInfo;
import com.lyl.myallforyou.utils.OpenLocalMapUtil;

import java.util.List;

/**
 * Created by lyl on 2017/5/22.
 */

public class DeviceInfoMoreAdapter extends RecyclerView.Adapter<DeviceInfoMoreAdapter.DeviceInfoHolder> {

    private List<DeviceInfo> mData;
    private Context mContext;


    public DeviceInfoMoreAdapter(List<DeviceInfo> data, Context context) {
        mData = data;
        mContext = context;
    }

    public void setData(List<DeviceInfo> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData != null && mData.size() > 0 ? mData.size() : 0;
    }

    @Override
    public DeviceInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_device_more, parent, false);
        return new DeviceInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceInfoHolder holder, int position) {
        final DeviceInfo info = mData.get(position);

        String system_date = info.getSystem_date();
        if (position != 0 && system_date.equals(mData.get(position - 1).getSystem_date())) {
            holder.date.setVisibility(View.GONE);
        } else {
            holder.date.setText(system_date);
            holder.date.setVisibility(View.VISIBLE);
        }

        if ("true".equals(info.getScreen_status())) {
            holder.srceen.setText(R.string.ON);
        } else {
            holder.srceen.setText(R.string.OFF);
        }

        holder.battery.setText(info.getSystem_battery());

        holder.time.setText(info.getSystem_time());

        holder.address.setText(info.getMy_address());

        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenLocalMapUtil.openMap(mContext.getApplicationContext(), info.getAddress_latitude(), info.getAddress_longitude());
            }
        });
    }

    class DeviceInfoHolder extends RecyclerView.ViewHolder {

        TextView date;
        LinearLayout layout;
        TextView srceen;
        TextView battery;
        TextView time;
        TextView address;

        public DeviceInfoHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.item_device_date);
            layout = (LinearLayout) view.findViewById(R.id.item_device_layout);
            srceen = (TextView) view.findViewById(R.id.item_device_srceen_status);
            battery = (TextView) view.findViewById(R.id.item_device_quantity);
            time = (TextView) view.findViewById(R.id.item_device_time);
            address = (TextView) view.findViewById(R.id.item_device_adress);
        }
    }
}

package com.lyl.myallforyou.ui.essay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.data.NhEassay;
import com.lyl.myallforyou.utils.ImgUtils;

import java.util.List;

/**
 * Created by lyl on 2017/5/24.
 */

public class ImageGridAdapter extends BaseAdapter {

    private Context mContext;
    List<NhEassay.DataBeanX.DataBean.GroupBean.ThumbImageListBean> mList;

    public ImageGridAdapter(Context context, List<NhEassay.DataBeanX.DataBean.GroupBean.ThumbImageListBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            if (mList.size() >= 0 && mList.size() <= 9) {
                return mList.size();
            } else if (mList.size() > 9) {
                return 9;
            }
        }
        return 0;
    }

    @Override
    public NhEassay.DataBeanX.DataBean.GroupBean.ThumbImageListBean getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        GridViewHolder holder;
        if (view == null) {
            holder = new GridViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_grid_square, viewGroup, false);
            holder.imgageView = (ImageView) view.findViewById(R.id.item_square);
            view.setTag(holder);
        } else {
            holder = (GridViewHolder) view.getTag();
        }

        ImgUtils.load(mContext, getItem(i).getUrl_list().get(0).getUrl(), holder.imgageView);

        return view;
    }

    class GridViewHolder {
        ImageView imgageView;
    }
}

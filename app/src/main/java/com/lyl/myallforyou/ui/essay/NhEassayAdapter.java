package com.lyl.myallforyou.ui.essay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.data.NhEssay;
import com.lyl.myallforyou.utils.ImgUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lyl on 2017/5/10.
 */

public class NhEassayAdapter extends RecyclerView.Adapter<NhEassayAdapter.BaseViewHolder> {

    public static final int CONTENT_TYPE_ESSAY = 1;
    public static final int CONTENT_TYPE_IMAGE = 2;

    private Context mContext;
    private ArrayList<NhEssay.DataBeanX.DataBean> mList;
    private String mContentType;
    private int mScreenWidth;

    public NhEassayAdapter(Context context, ArrayList<NhEssay.DataBeanX.DataBean> list, String contentType, int screenWidth) {
        mContext = context;
        mList = list;
        mContentType = contentType;
        mScreenWidth = screenWidth;
    }

    public void addData(ArrayList<NhEssay.DataBeanX.DataBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
    }

    @Override
    public int getItemViewType(int position) {
        if (NhEassayActivity.CONTENT_TYPE_ESSAY.equals(mContentType)) {
            return CONTENT_TYPE_ESSAY;
        } else if (NhEassayActivity.CONTENT_TYPE_IMAGE.equals(mContentType)) {
            return CONTENT_TYPE_IMAGE;
        }

        return super.getItemViewType(position);
    }

    @Override
    public NhEassayAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        NhEassayAdapter.BaseViewHolder holder = null;
        switch (viewType) {
            case CONTENT_TYPE_ESSAY:// 段子
                view = LayoutInflater.from(mContext).inflate(R.layout.item_nh_eassay, parent, false);
                holder = new MyEassayViewHolder(view);
                break;
            case CONTENT_TYPE_IMAGE:// 图片
                view = LayoutInflater.from(mContext).inflate(R.layout.item_nh_image, parent, false);
                holder = new MyImageViewHolder(view);
                break;
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_nh_eassay, parent, false);
                holder = new MyEassayViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final NhEassayAdapter.BaseViewHolder holder, int position) {
        NhEssay.DataBeanX.DataBean dataBean = mList.get(position);

        if ("5".equals(dataBean.getType())) { // 广告
            return;
        }
        NhEssay.DataBeanX.DataBean.GroupBean group = dataBean.getGroup();
        if (group == null) return;
        NhEssay.DataBeanX.DataBean.GroupBean.UserBean user = group.getUser();
        if (user != null) {
            // 头像
            ImgUtils.loadCircle(mContext, user.getAvatar_url(), holder.icon);

            // 名字
            holder.name.setText(user.getName());
        }
        // 时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dataBean.getGroup().getCreate_time());
        holder.time.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));

        // 内容
        holder.content.setText(dataBean.getGroup().getContent());

        switch (getItemViewType(position)) {
            case CONTENT_TYPE_ESSAY:// 段子
                MyEassayViewHolder eassayHolder = (MyEassayViewHolder) holder;
                break;
            case CONTENT_TYPE_IMAGE:// 图片
                MyImageViewHolder imageHolder = (MyImageViewHolder) holder;
                if (group.getLarge_image_list() == null) {// 一张图片
                    imageHolder.imageContent.setVisibility(View.VISIBLE);
                    imageHolder.imageList.setVisibility(View.GONE);

                    // 设置图片的大小
                    int width = Integer.parseInt(group.getMiddle_image().getWidth());
                    int height = Integer.parseInt(group.getMiddle_image().getHeight());
                    int tHeight = mScreenWidth * height / width;

                    ViewGroup.LayoutParams layoutParams = imageHolder.imageContent.getLayoutParams();
                    layoutParams.width = mScreenWidth;
                    if (tHeight > 1920) {
                        layoutParams.height = 1920;
                    } else {
                        layoutParams.height = tHeight;
                    }
                    imageHolder.imageContent.setLayoutParams(layoutParams);

                    // 加载图片
                    List<NhEssay.DataBeanX.DataBean.GroupBean.LargeImageBean.UrlListBean> url_list = group.getLarge_image().getUrl_list();
                    String imgUrl = url_list.get(url_list.size() - 1).getUrl();
                    if (1 == group.getIs_gif()) {// 是gif
                        ImgUtils.loadGif(mContext, imgUrl, imageHolder.imageContent);
                    } else {
                        ImgUtils.loadF(mContext, imgUrl, imageHolder.imageContent);
                    }
                } else {
                    imageHolder.imageContent.setVisibility(View.GONE);
                    imageHolder.imageList.setVisibility(View.VISIBLE);
                    List<NhEssay.DataBeanX.DataBean.GroupBean.ThumbImageListBean> thumb_image_list = group.getThumb_image_list();


                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList != null && mList.size() >= 0 ? mList.size() : 0;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        TextView time;
        TextView content;

        public BaseViewHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.item_eassay_icon);
            name = (TextView) view.findViewById(R.id.item_eassay_name);
            time = (TextView) view.findViewById(R.id.item_eassay_time);
            content = (TextView) view.findViewById(R.id.item_eassay_content);
        }
    }

    class MyEassayViewHolder extends BaseViewHolder {

        public MyEassayViewHolder(View view) {
            super(view);
        }
    }

    class MyImageViewHolder extends BaseViewHolder {
        ImageView imageContent;
        GridView imageList;

        public MyImageViewHolder(View view) {
            super(view);
            imageContent = (ImageView) view.findViewById(R.id.item_image_content);
            imageList = (GridView) view.findViewById(R.id.item_image_list);
        }
    }
}

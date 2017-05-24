package com.lyl.myallforyou.ui.essay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.data.NhEassay;
import com.lyl.myallforyou.ui.image.ImageActivity;
import com.lyl.myallforyou.ui.image.SpecialImageActivity;
import com.lyl.myallforyou.utils.ImgUtils;
import com.lyl.myallforyou.utils.LogUtils;
import com.lyl.myallforyou.utils.MyUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyl on 2017/5/10.
 */

public class NhEassayAdapter extends RecyclerView.Adapter<NhEassayAdapter.BaseViewHolder> {

    public static final int CONTENT_TYPE_ESSAY = 1;
    public static final int CONTENT_TYPE_IMAGE = 2;

    private Context mContext;
    private ArrayList<NhEassay.DataBeanX.DataBean> mList;
    private String mContentType;
    private int mScreenWidth;

    public NhEassayAdapter(Context context, ArrayList<NhEassay.DataBeanX.DataBean> list, String contentType, int screenWidth) {
        mContext = context;
        mList = list;
        mContentType = contentType;
        mScreenWidth = screenWidth;
    }

    public void addData(ArrayList<NhEassay.DataBeanX.DataBean> list) {
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
        final NhEassay.DataBeanX.DataBean dataBean = mList.get(position);

        if ("5".equals(dataBean.getType())) { // 广告
            return;
        }
        final NhEassay.DataBeanX.DataBean.GroupBean group = dataBean.getGroup();
        if (group == null) return;
        NhEassay.DataBeanX.DataBean.GroupBean.UserBean user = group.getUser();
        if (user != null) {
            // 头像
            ImgUtils.loadCircle(mContext, user.getAvatar_url(), holder.icon);

            // 名字
            holder.name.setText(user.getName());
        }
        // 时间
        holder.time.setText(MyUtils.getDate(dataBean.getGroup().getCreate_time()));

        // 内容
        holder.content.setText(dataBean.getGroup().getContent());

        switch (getItemViewType(position)) {
            case CONTENT_TYPE_ESSAY:// 段子
                MyEassayViewHolder eassayHolder = (MyEassayViewHolder) holder;
                eassayHolder.eassayLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goDetails(group, CONTENT_TYPE_ESSAY);
                    }
                });
                break;
            case CONTENT_TYPE_IMAGE:// 图片
                MyImageViewHolder imageHolder = (MyImageViewHolder) holder;
                imageHolder.imageContent.setImageBitmap(null);
                if (group.getLarge_image_list() == null) {// 一张图片
                    imageHolder.singleImg.setVisibility(View.VISIBLE);
                    imageHolder.imageGrid.setVisibility(View.GONE);

                    // 设置图片的大小
                    int width = Integer.parseInt(group.getMiddle_image().getWidth());
                    int height = Integer.parseInt(group.getMiddle_image().getHeight());
                    int tHeight = mScreenWidth * height / width;

                    boolean isLongImage = false;
                    imageHolder.longImageTxt.setVisibility(View.GONE);
                    ViewGroup.LayoutParams layoutParams = imageHolder.imageContent.getLayoutParams();
                    layoutParams.width = mScreenWidth;
                    if (tHeight > 1920) {
                        layoutParams.height = mScreenWidth / 16 * 9;
                        isLongImage = true;
                    } else {
                        layoutParams.height = tHeight;
                    }
                    imageHolder.imageContent.setLayoutParams(layoutParams);

                    // 加载图片
                    final List<NhEassay.DataBeanX.DataBean.GroupBean.LargeImageBean.UrlListBean> url_list = group.getLarge_image().getUrl_list();
                    final String imgUrl = url_list.get(0).getUrl();
                    if (1 == group.getIs_gif()) {// 是gif
                        imageHolder.longImageTxt.setText(R.string.show_gif_image);
                        imageHolder.longImageTxt.setVisibility(View.VISIBLE);

                        String tmUrl = group.getMiddle_image().getUrl_list().get(0).getUrl();
                        ImgUtils.loadC(mContext, tmUrl, imageHolder.imageContent);
                        LogUtils.d("GIF显示的Url:", tmUrl);
                        // 跳转到 GIF 页面
                        imageHolder.singleImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(mContext, SpecialImageActivity.class);
                                intent.putExtra(ConstantIntent.SPECIAL_IMAGE_URL, imgUrl);
                                intent.putExtra(ConstantIntent.SPECIAL_IMAGE_TYPE, ConstantIntent.SPECIAL_IMAGE_GIF);
                                mContext.startActivity(intent);
                            }
                        });
                    } else {
                        if (isLongImage) {// 是长图
                            imageHolder.longImageTxt.setText(R.string.show_long_image);
                            imageHolder.longImageTxt.setVisibility(View.VISIBLE);

                            // 跳转到长图页面
                            imageHolder.singleImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(mContext, SpecialImageActivity.class);
                                    intent.putExtra(ConstantIntent.SPECIAL_IMAGE_URL, imgUrl);
                                    intent.putExtra(ConstantIntent.SPECIAL_IMAGE_TYPE, ConstantIntent.SPECIAL_IMAGE_LONG);
                                    mContext.startActivity(intent);
                                }
                            });
                        } else {// 普通图片
                            ImgUtils.loadF(mContext, imgUrl, imageHolder.imageContent);
                            // 跳转到可放大缩小页面
                            imageHolder.singleImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(mContext, SpecialImageActivity.class);
                                    intent.putExtra(ConstantIntent.SPECIAL_IMAGE_URL, imgUrl);
                                    intent.putExtra(ConstantIntent.SPECIAL_IMAGE_TYPE, ConstantIntent.SPECIAL_IMAGE_NORMAL);
                                    mContext.startActivity(intent);
                                }
                            });
                        }
                    }
                } else { // 多张图片
                    imageHolder.singleImg.setVisibility(View.GONE);
                    imageHolder.imageGrid.setVisibility(View.VISIBLE);
                    final List<NhEassay.DataBeanX.DataBean.GroupBean.ThumbImageListBean> thumb_image_list = group.getThumb_image_list();

                    ViewGroup.LayoutParams layoutParams = imageHolder.imageGrid.getLayoutParams();
                    int imgSize = thumb_image_list.size();
                    if (imgSize == 2 || imgSize == 4) {
                        imageHolder.imageGrid.setNumColumns(2);
                        if (imgSize <= 2) {
                            layoutParams.height = mScreenWidth / 2 - 4;
                        } else {
                            layoutParams.height = mScreenWidth - 4;
                        }
                    } else {
                        imageHolder.imageGrid.setNumColumns(3);
                        if (imgSize <= 3) {
                            layoutParams.height = mScreenWidth / 3 - 4;
                        } else if (imgSize <= 6) {
                            layoutParams.height = mScreenWidth / 3 * 2 - 4;
                        } else {
                            layoutParams.height = mScreenWidth - 4;
                        }
                    }

                    imageHolder.imageGrid.setLayoutParams(layoutParams);

                    ImageGridAdapter imageGridAdapter = new ImageGridAdapter(mContext, thumb_image_list);
                    imageHolder.imageGrid.setAdapter(imageGridAdapter);

                    imageHolder.imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Intent intent = new Intent(mContext, ImageActivity.class);
                            intent.putExtra(ConstantIntent.IMAGE_LIST, (Serializable) group.getLarge_image_list());
                            intent.putExtra(ConstantIntent.IMAGE_LIST_POSTION, i);
                            mContext.startActivity(intent);
                        }
                    });
                }
                imageHolder.imageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goDetails(group, CONTENT_TYPE_IMAGE);
                    }
                });
                break;
        }
    }

    private void goDetails(NhEassay.DataBeanX.DataBean.GroupBean group, int conetntType) {

        Intent intent = new Intent(mContext, EassayDetailActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString(ConstantIntent.EASSAY_DETAIL_MGROUP_ID, String.valueOf(group.getGroup_id()));
        bundle.putString(ConstantIntent.EASSAY_DETAIL_NAME, String.valueOf(group.getUser().getName()));
        bundle.putString(ConstantIntent.EASSAY_DETAIL_ICON, String.valueOf(group.getUser().getAvatar_url()));
        bundle.putString(ConstantIntent.EASSAY_DETAIL_TIME, String.valueOf(group.getCreate_time()));
        bundle.putString(ConstantIntent.EASSAY_DETAIL_CONTENT, String.valueOf(group.getContent()));
        bundle.putString(ConstantIntent.EASSAY_DETAIL_COMMENT_ALL_NUM, String.valueOf(group.getComment_count()));

        switch (conetntType) {
            case CONTENT_TYPE_ESSAY:// 段子
                bundle.putInt(ConstantIntent.EASSAY_DETAIL_CONTENT_TYPE, ConstantIntent.EASSAY_DETAIL_CONTENT_TEXT);
                break;
            case CONTENT_TYPE_IMAGE:// 图片
                if (group.getLarge_image_list() == null) {// 一张图片
                    int width = Integer.parseInt(group.getMiddle_image().getWidth());
                    int height = Integer.parseInt(group.getMiddle_image().getHeight());
                    int tHeight = mScreenWidth * height / width;

                    boolean isLongImage = false;
                    if (tHeight > 1920) {
                        isLongImage = true;
                    }

                    final List<NhEassay.DataBeanX.DataBean.GroupBean.LargeImageBean.UrlListBean> url_list = group.getLarge_image().getUrl_list();
                    final String imgUrl = url_list.get(0).getUrl();

                    if (1 == group.getIs_gif()) {// 是gif
                        bundle.putString(ConstantIntent.SPECIAL_IMAGE_URL, group.getMiddle_image().getUrl_list().get(0).getUrl());
                        bundle.putInt(ConstantIntent.EASSAY_DETAIL_CONTENT_TYPE, ConstantIntent.EASSAY_DETAIL_CONTENT_GIF);
                    } else {
                        if (isLongImage) {// 是长图
                            bundle.putString(ConstantIntent.SPECIAL_IMAGE_URL, imgUrl);
                            bundle.putInt(ConstantIntent.EASSAY_DETAIL_CONTENT_TYPE, ConstantIntent.EASSAY_DETAIL_CONTENT_LONG);
                        } else {// 普通图
                            bundle.putString(ConstantIntent.SPECIAL_IMAGE_URL, imgUrl);
                            bundle.putInt(ConstantIntent.EASSAY_DETAIL_CONTENT_TYPE, ConstantIntent.EASSAY_DETAIL_CONTENT_NORMAL);
                        }
                    }
                } else { // 多张图
                    bundle.putSerializable(ConstantIntent.IMAGE_THUMB_LIST, (Serializable) group.getThumb_image_list());
                    bundle.putSerializable(ConstantIntent.IMAGE_LIST, (Serializable) group.getLarge_image_list());
                    bundle.putInt(ConstantIntent.EASSAY_DETAIL_CONTENT_TYPE, ConstantIntent.EASSAY_DETAIL_CONTENT_LIST);
                }

                break;
        }

        intent.putExtras(bundle);
        mContext.startActivity(intent);
        Toast.makeText(mContext, "进入详情", Toast.LENGTH_SHORT).show();
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
        LinearLayout eassayLayout;

        public MyEassayViewHolder(View view) {
            super(view);
            eassayLayout = (LinearLayout) view.findViewById(R.id.item_eassay_layout);
        }
    }

    class MyImageViewHolder extends BaseViewHolder {
        LinearLayout imageLayout;
        ImageView imageContent;
        TextView longImageTxt;
        RelativeLayout singleImg;
        GridView imageGrid;

        public MyImageViewHolder(View view) {
            super(view);
            imageLayout = (LinearLayout) view.findViewById(R.id.item_eassay_image_layout);
            imageContent = (ImageView) view.findViewById(R.id.item_image_content);
            longImageTxt = (TextView) view.findViewById(R.id.item_long_image_text);
            singleImg = (RelativeLayout) view.findViewById(R.id.item_single_image_layout);
            imageGrid = (GridView) view.findViewById(R.id.item_image_list);
        }
    }
}

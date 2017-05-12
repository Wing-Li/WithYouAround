package com.lyl.myallforyou.ui.essay;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.data.NhEssay;
import com.lyl.myallforyou.ui.image.ImageActivity;
import com.lyl.myallforyou.ui.image.LongImageActivity;
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
                    final List<NhEssay.DataBeanX.DataBean.GroupBean.LargeImageBean.UrlListBean> url_list = group.getLarge_image().getUrl_list();
                    final String imgUrl = url_list.get(url_list.size() - 1).getUrl();
                    if (1 == group.getIs_gif()) {// 是gif
                        ImgUtils.loadGif(mContext, imgUrl, imageHolder.imageContent);
                    } else {
                        if (isLongImage) {
                            imageHolder.longImageTxt.setVisibility(View.VISIBLE);
                            // 跳转到长图页面
                            imageHolder.singleImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(mContext, LongImageActivity.class);
                                    intent.putExtra(ConstantIntent.LONG_IMAGE_URL, imgUrl);
                                    mContext.startActivity(intent);
                                }
                            });
                        } else {
                            ImgUtils.loadF(mContext, imgUrl, imageHolder.imageContent);
                            // 跳转到可放大缩小页面
                            imageHolder.singleImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ArrayList<String> urlList = new ArrayList<String>();
                                    urlList.add(imgUrl);
                                    Intent intent = new Intent(mContext, ImageActivity.class);
                                    intent.putStringArrayListExtra(ConstantIntent.IMAGE_LIST_URL, urlList);
                                    mContext.startActivity(intent);
                                }
                            });
                        }
                    }
                } else { // 多张图片
                    imageHolder.singleImg.setVisibility(View.GONE);
                    imageHolder.imageGrid.setVisibility(View.VISIBLE);
                    final List<NhEssay.DataBeanX.DataBean.GroupBean.ThumbImageListBean> thumb_image_list = group.getThumb_image_list();

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

                    ImageListAdapter imageListAdapter = new ImageListAdapter(thumb_image_list);
                    imageHolder.imageGrid.setAdapter(imageListAdapter);

                    imageHolder.imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            ArrayList<String> urlList = new ArrayList<String>();
                            for (NhEssay.DataBeanX.DataBean.GroupBean.ThumbImageListBean bean : thumb_image_list) {
                                urlList.add(bean.getUrl_list().get(0).getUrl());
                            }

                            Intent intent = new Intent(mContext, ImageActivity.class);
                            intent.putStringArrayListExtra(ConstantIntent.IMAGE_LIST_URL, urlList);
                            intent.putExtra(ConstantIntent.IMAGE_LIST_POSTION, i);
                            mContext.startActivity(intent);
                        }
                    });
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
        TextView longImageTxt;
        RelativeLayout singleImg;
        GridView imageGrid;

        public MyImageViewHolder(View view) {
            super(view);
            imageContent = (ImageView) view.findViewById(R.id.item_image_content);
            longImageTxt = (TextView) view.findViewById(R.id.item_long_image_text);
            singleImg = (RelativeLayout) view.findViewById(R.id.item_single_image_layout);
            imageGrid = (GridView) view.findViewById(R.id.item_image_list);
        }
    }

    class ImageListAdapter extends BaseAdapter {

        List<NhEssay.DataBeanX.DataBean.GroupBean.ThumbImageListBean> mList;

        public ImageListAdapter(List<NhEssay.DataBeanX.DataBean.GroupBean.ThumbImageListBean> list) {
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
        public NhEssay.DataBeanX.DataBean.GroupBean.ThumbImageListBean getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setBackgroundResource(R.color.bg_gary);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            ImgUtils.loadC(mContext, getItem(i).getUrl_list().get(0).getUrl(), imageView);

            return imageView;
        }
    }
}

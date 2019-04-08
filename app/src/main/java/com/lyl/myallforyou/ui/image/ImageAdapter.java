package com.lyl.myallforyou.ui.image;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.lyl.myallforyou.data.NhEassay;
import com.lyl.myallforyou.utils.ImgUtils;
import com.lyl.myallforyou.utils.LogUtils;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by lyl on 2017/5/12.
 */

public class ImageAdapter extends PagerAdapter {

    private Context mContext;
    private List<NhEassay.DataBeanX.DataBean.GroupBean.LargeImageListBean> mData;

    public ImageAdapter(Context context, List<NhEassay.DataBeanX.DataBean.GroupBean.LargeImageListBean> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData != null && mData.size() >= 0 ? mData.size() : 0;
    }

    /**
     * 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        NhEassay.DataBeanX.DataBean.GroupBean.LargeImageListBean bean = mData.get(position);
        PhotoView photoView = new PhotoView(mContext);
        photoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        photoView.setAdjustViewBounds(true);

        if (bean.isIs_gif()) {
            ImgUtils.loadGif(mContext, bean.getUrl(), photoView);
        } else {
            ImgUtils.load(mContext, bean.getUrl(), photoView);
        }

        LogUtils.d("多图显示", "pos:" + position + " gif:" + bean.isIs_gif() + " ; " + bean.getUrl());

        container.addView(photoView);
        return photoView;
    }
}

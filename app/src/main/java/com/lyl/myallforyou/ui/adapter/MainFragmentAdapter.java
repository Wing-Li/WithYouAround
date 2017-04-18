package com.lyl.myallforyou.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.data.UserInfo;
import com.lyl.myallforyou.ui.MainFragment.OnListFragmentInteractionListener;

import java.util.List;

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.ViewHolder> {

    private List<UserInfo> mValues;
    private final OnListFragmentInteractionListener mListener;


    public MainFragmentAdapter(List<UserInfo> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }


    public void setData(List<UserInfo> data) {
        mValues.clear();
        mValues = data;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_main, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final UserInfo userInfo = mValues.get(position);

        // TODO 加载图片
        holder.icon.setImageResource(R.mipmap.icon);
        holder.name.setText(userInfo.getName());
        holder.content.setText(userInfo.getSign());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(userInfo);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView icon;
        public final TextView name;
        public final TextView content;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            icon = (ImageView) view.findViewById(R.id.item_icon);
            name = (TextView) view.findViewById(R.id.item_name);
            content = (TextView) view.findViewById(R.id.item_content);
        }
    }
}

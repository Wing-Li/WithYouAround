package com.lyl.myallforyou.ui.help;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.ui.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HelpActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.help_list)
    ListView helpList;

    private String[] mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.help);
        setSupportActionBar(toolbar);
        setBackUI(toolbar);

        mData = getResources().getStringArray(R.array.help_list);
        helpList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mData.length;
            }

            @Override
            public String getItem(int position) {
                return mData[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null){
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_help,null);

                    holder.title = (TextView) convertView.findViewById(R.id.item_help_q);
                    holder.content = (TextView) convertView.findViewById(R.id.item_help_a);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }

                String str = getItem(position);
                if (str.contains("##")){
                    String[] split = str.split("##");

                    holder.title.setText(split[0]);
                    holder.content.setText(split[1]);
                }else {
                    holder.content.setVisibility(View.GONE);
                    holder.title.setText(str);
                }

                return convertView;
            }

            class ViewHolder{
                TextView title;
                TextView content;
            }
        });
    }
}

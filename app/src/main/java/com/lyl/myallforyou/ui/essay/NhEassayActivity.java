package com.lyl.myallforyou.ui.essay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.data.NhEassay;
import com.lyl.myallforyou.network.imp.NeihanImp;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.utils.ImgUtils;
import com.lyl.myallforyou.utils.LogUtils;
import com.lyl.myallforyou.view.listener.OnRecycleViewScrollListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NhEassayActivity extends BaseActivity {

    public static final String CONTENT_TYPE = "content_type";
    public static final String CONTENT_TYPE_ESSAY = "-102";
    public static final String CONTENT_TYPE_IMAGE = "-103";


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private NeihanImp mNeihanImp;
    private Call<NhEassay> essayCall;

    private NhEassayAdapter mAdapter;
    private boolean mHasMore = true;
    private String mTip;
    private String mContentType;
    private int mScreenWidth;

    private ArrayList<NhEassay.DataBeanX.DataBean> mDataBeen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nh_eassay);
        ButterKnife.bind(this);

        getParameter();
        initData();
        getData();
        initView();
    }

    private void initData() {
        mDataBeen = new ArrayList<>();
        mNeihanImp = new NeihanImp(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
    }


    private void getData() {
        swipeRefresh.setRefreshing(true);
        essayCall = mNeihanImp.getNhEssayDetails(mContentType);
        Call<NhEassay> clone = essayCall.clone();
        clone.enqueue(new Callback<NhEassay>() {
            @Override
            public void onResponse(Call<NhEassay> call, Response<NhEassay> response) {
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    NhEassay body = response.body();
                    if ("success".equals(body.getMessage())) {
                        mHasMore = body.getData().isHas_more();
                        mTip = body.getData().getTip();

                        ArrayList<NhEassay.DataBeanX.DataBean> data = (ArrayList<NhEassay.DataBeanX.DataBean>) body.getData().getData();

                        mAdapter.addData(data);
                    }
                } else {
                    LogUtils.e(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<NhEassay> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(mContext, R.string.net_error, Toast.LENGTH_SHORT).show();
                LogUtils.e("Error : ", t.getLocalizedMessage());
            }
        });
    }

    private void initView() {
        if (mContentType.equals(CONTENT_TYPE_ESSAY) ){
            toolbar.setTitle(R.string.nheassay);
        }else if (mContentType.equals(CONTENT_TYPE_IMAGE)){
            toolbar.setTitle(R.string.nheassay_image);
        }
        setSupportActionBar(toolbar);
        setBackUI(toolbar);

        mAdapter = new NhEassayAdapter(mContext, mDataBeen, mContentType, mScreenWidth);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new OnRecycleViewScrollListener() {
            @Override
            public void onLoadMore() {
                if (mHasMore) {
                    getData();
                } else {
                    showT(getString(R.string.not_more));
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                getData();
            }
        });
    }

    public void getParameter() {
        Intent intent = getIntent();
        mContentType = intent.getStringExtra(CONTENT_TYPE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (essayCall != null && !essayCall.isCanceled()) {
            essayCall.cancel();
        }
        ImgUtils.cancelAllTasks(getApplicationContext());
        ImgUtils.clearMemory(getApplicationContext());
    }
}

package com.lyl.myallforyou.ui.essay;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.data.NhEassay;
import com.lyl.myallforyou.network.imp.NeihanImp;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.utils.ImgUtils;
import com.lyl.myallforyou.view.LinearLayoutManagerWrapper;
import com.lyl.myallforyou.view.listener.OnRecycleViewScrollListener;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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

        mContentType = getIntent().getStringExtra(CONTENT_TYPE);

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
                        if (data != null && data.size() > 0){
                            mAdapter.addData(data);
                        }
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<NhEassay> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(mContext, R.string.net_error, Toast.LENGTH_SHORT).show();
                if (t != null) {
                    CrashReport.postCatchedException(t);
                }
            }
        });
    }

    private void initView() {
        if (CONTENT_TYPE_ESSAY.equals(mContentType) ){
            toolbar.setTitle(R.string.nheassay);
        }else if (CONTENT_TYPE_IMAGE.equals(mContentType)){
            toolbar.setTitle(R.string.nheassay_image);
        }
        setSupportActionBar(toolbar);
        setBackUI(toolbar);

        mAdapter = new NhEassayAdapter(mContext, mDataBeen, mContentType, mScreenWidth);
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(mContext));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new OnRecycleViewScrollListener() {
            @Override
            public void onLoadMore() {
//                if (mHasMore) {
                    getData();
//                } else {
//                    showT(getString(R.string.not_more));
//                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                mHasMore = true;
                getData();
            }
        });
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

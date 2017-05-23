package com.lyl.myallforyou.ui.essay;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.data.NhComments;
import com.lyl.myallforyou.network.imp.NeihanImp;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.utils.ImgUtils;
import com.lyl.myallforyou.utils.LogUtils;
import com.lyl.myallforyou.view.listener.OnRecycleViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EassayDetailActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.eassay_detail_icon)
    ImageView eassayDetailIcon;
    @Bind(R.id.eassay_detail_name)
    TextView eassayDetailName;
    @Bind(R.id.eassay_detail_time)
    TextView eassayDetailTime;
    @Bind(R.id.eassay_detail_content)
    TextView eassayDetailContent;
    @Bind(R.id.eassay_detail_hot_count)
    TextView eassayDetailHotCount;
    @Bind(R.id.eassay_detail_recycler_hot)
    RecyclerView eassayDetailRecyclerHot;
    @Bind(R.id.eassay_detail_all_count)
    TextView eassayDetailAllCount;
    @Bind(R.id.eassay_detail_recycler_all)
    RecyclerView eassayDetailRecyclerAll;

    private String mGroupId;
    private String mUserName;
    private String mUserIcon;
    private String mTime;
    private String mContent;
    private String mCommentAllNum;
    private String mCommentHotNum;

    private EassayDetailCommentAdapter mHotCommentAdapter;
    private EassayDetailCommentAdapter mAllCommentAdapter;

    private List<NhComments.DataBean.CommentsBean> mHotCommentsList;
    private List<NhComments.DataBean.CommentsBean> mAllCommentsList;
    private NeihanImp mNeihanImp;

    private int page = 0;
    private boolean hasMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eassay_detail);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.details);
        setSupportActionBar(toolbar);
        setBackUI(toolbar);

        getParameter();
        initView();
        initData();
        getData();
    }

    private void initData() {
        mNeihanImp = new NeihanImp(this);
        mHotCommentsList = new ArrayList<>();
        mAllCommentsList = new ArrayList<>();

        hasMore = true;
        page = 0;
        getData();
    }

    private void getData() {
        if (!hasMore) {
            showT(R.string.not_more);
            return;
        }

        Call<NhComments> nhComments = mNeihanImp.getNhComments(mGroupId, page);
        Call<NhComments> clone = nhComments.clone();
        clone.enqueue(new Callback<NhComments>() {
            @Override
            public void onResponse(Call<NhComments> call, Response<NhComments> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NhComments body = response.body();
                    if ("success".equals(body.getMessage())) {
                        page++;
                        hasMore = body.isHas_more();

                        List<NhComments.DataBean.CommentsBean> top_comments = body.getData().getTop_comments();
                        if (top_comments != null && top_comments.size() > 0) {
                            mHotCommentsList.addAll(top_comments);
                            mHotCommentAdapter.setData(mHotCommentsList);

                            mCommentHotNum = String.valueOf(top_comments.size());
                            eassayDetailHotCount.setText(mCommentHotNum);
                        }

                        List<NhComments.DataBean.CommentsBean> recent_comments = body.getData().getRecent_comments();
                        if (recent_comments != null && recent_comments.size() > 0) {
                            mAllCommentsList.addAll(body.getData().getRecent_comments());
                            mAllCommentAdapter.setData(recent_comments);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<NhComments> call, Throwable t) {
                showT(R.string.net_error);
                LogUtils.e("Error : ", t.getLocalizedMessage());
            }
        });
    }

    private void getParameter() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            showT(getString(R.string.parameter_error));
            finish();
            return;
        }

        mGroupId = bundle.getString(ConstantIntent.EASSAY_DETAIL_MGROUP_ID);
        mUserName = bundle.getString(ConstantIntent.EASSAY_DETAIL_NAME);
        mUserIcon = bundle.getString(ConstantIntent.EASSAY_DETAIL_ICON);
        mTime = bundle.getString(ConstantIntent.EASSAY_DETAIL_TIME);
        mContent = bundle.getString(ConstantIntent.EASSAY_DETAIL_CONTENT);
        mCommentAllNum = bundle.getString(ConstantIntent.EASSAY_DETAIL_COMMENT_ALL_NUM);
    }

    private void initView() {
        ImgUtils.loadCircle(mContext, mUserIcon, eassayDetailIcon);

        eassayDetailName.setText(FS(mUserName));

        eassayDetailTime.setText(FS(mTime));

        eassayDetailContent.setText(FS(mContent));

        eassayDetailHotCount.setText(FS(mCommentHotNum));

        eassayDetailAllCount.setText(FS(mCommentAllNum));

        // 设置热门评论列表
        eassayDetailRecyclerHot.setLayoutManager(new LinearLayoutManager(mContext));
        mHotCommentAdapter = new EassayDetailCommentAdapter(mContext, mHotCommentsList, EassayDetailCommentAdapter.COMMENT_TYPE_HOT);
        eassayDetailRecyclerHot.setAdapter(mHotCommentAdapter);

        // 设置全部评论列表
        eassayDetailRecyclerAll.setLayoutManager(new LinearLayoutManager(mContext));
        mAllCommentAdapter = new EassayDetailCommentAdapter(mContext, mAllCommentsList, EassayDetailCommentAdapter.COMMENT_TYPE_ALL);
        eassayDetailRecyclerAll.setAdapter(mAllCommentAdapter);
        eassayDetailRecyclerAll.addOnScrollListener(new OnRecycleViewScrollListener() {
            @Override
            public void onLoadMore() {
                getData();
            }
        });
    }

}

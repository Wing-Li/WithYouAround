package com.lyl.myallforyou.ui.essay;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.data.NhCommentReply;
import com.lyl.myallforyou.data.NhComments;
import com.lyl.myallforyou.network.imp.NeihanImp;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.utils.ImgUtils;
import com.lyl.myallforyou.utils.LogUtils;
import com.lyl.myallforyou.utils.MyUtils;
import com.lyl.myallforyou.view.listener.OnRecycleViewScrollListener;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCommentReplyActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.comment_reply_icon)
    ImageView commentReplyIcon;
    @Bind(R.id.comment_reply_name)
    TextView commentReplyName;
    @Bind(R.id.comment_reply_time)
    TextView commentReplyTime;
    @Bind(R.id.comment_reply_content)
    TextView commentReplyContent;
    @Bind(R.id.comment_reply_recycler)
    RecyclerView commentReplyRecycler;
    @Bind(R.id.comment_reply_all_count)
    TextView commentReplyAllCount;

    private String mGroupId;
    private String mUserName;
    private String mUserIcon;
    private String mTime;
    private String mContent;
    private String mCommentDiggNum;
    private String mCommentHotNum;

    private EassayDetailCommentAdapter mAdapter;
    private List<NhComments.DataBean.CommentsBean> mCommentsBeenList;
    private NeihanImp mNeihanImp;
    private int page = 0;
    private boolean hasMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_comment_reply);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.comment_details);
        setSupportActionBar(toolbar);
        setBackUI(toolbar);

        getParameter();
        initView();
        initData();
    }

    private void initData() {
        mNeihanImp = new NeihanImp(this);
        mCommentsBeenList = new ArrayList<>();

        page = 0;
        hasMore = true;
        getData();
    }

    private void getData() {
        if (!hasMore) {
            showT(R.string.not_more);
            return;
        }

        Call<NhCommentReply> nhCommentReply = mNeihanImp.getNhCommentReply(mGroupId, page);
        Call<NhCommentReply> clone = nhCommentReply.clone();
        clone.enqueue(new Callback<NhCommentReply>() {
            @Override
            public void onResponse(Call<NhCommentReply> call, Response<NhCommentReply> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NhCommentReply body = response.body();
                    if ("success".equals(body.getMessage())) {
                        page++;
                        hasMore = body.getData().isHas_more();

                        commentReplyAllCount.setText("(" + body.getData().getTotal_count() + ")");
                        List<NhCommentReply.DataBeanX.DataBean> data = body.getData().getData();

                        NhComments.DataBean.CommentsBean commentsBean;
                        for (NhCommentReply.DataBeanX.DataBean d : data) {
                            commentsBean = new NhComments.DataBean.CommentsBean();
                            commentsBean.setAvatar_url(d.getUser().getAvatar_url());
                            commentsBean.setUser_name(d.getUser().getName());
                            commentsBean.setCreate_time(d.getCreate_time());
                            commentsBean.setDigg_count(d.getDigg_count());
                            commentsBean.setText(d.getText());
                            mCommentsBeenList.add(commentsBean);
                        }
                        mAdapter.setData(mCommentsBeenList);
                    }
                }
            }

            @Override
            public void onFailure(Call<NhCommentReply> call, Throwable t) {
                showT(R.string.net_error);
                if (t != null) {
                    LogUtils.e("Error : ", t.getLocalizedMessage());
                    CrashReport.postCatchedException(t);
                }
            }
        });

    }

    private void initView() {
        ImgUtils.loadCircle(mContext, mUserIcon, commentReplyIcon);

        commentReplyName.setText(mUserName);

        commentReplyTime.setText(MyUtils.getDate(Long.parseLong(mTime)));

        commentReplyContent.setText(mContent);

        commentReplyRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new EassayDetailCommentAdapter(mContext, mCommentsBeenList, EassayDetailCommentAdapter.COMMENT_TYPE_ALL);
        commentReplyRecycler.setAdapter(mAdapter);

        commentReplyRecycler.addOnScrollListener(new OnRecycleViewScrollListener() {
            @Override
            public void onLoadMore() {
                if (hasMore) {
                    getData();
                }
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
        mCommentDiggNum = bundle.getString(ConstantIntent.EASSAY_DETAIL_COMMENT_ALL_NUM);
    }
}

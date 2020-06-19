package com.boo.app.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.boo.app.R;
import com.boo.app.api.ApiRequest;
import com.boo.app.api.Response;
import com.boo.app.api.request.CommentData;
import com.boo.app.api.request.FeedData;
import com.boo.app.api.request.GetPostData;
import com.boo.app.api.request.PostManage;
import com.boo.app.api.response.FeedResponse;
import com.boo.app.api.response.PostManageResponse;
import com.boo.app.model.Comment;
import com.boo.app.model.FeedItem;
import com.boo.app.model.User;
import com.boo.app.ui.adapter.CommentRVAdapter;
import com.boo.app.ui.adapter.FeedRVAdapter;
import com.boo.app.ui.base.BaseActivity;
import com.boo.app.ui.utils.DialogUtils;
import com.boo.app.ui.utils.KeyboardUtils;
import com.boo.app.ui.utils.PageUtils;
import com.j256.ormlite.stmt.query.In;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CommentActivity extends BaseActivity {



    @Bind(R.id.comment_field)
    EditText edComment;
    @Bind(R.id.swipe)
    SwipeRefreshLayout swipe;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.comments_recycler_view)
    RecyclerView recyclerView;
    CommentRVAdapter adapter;
    List<Comment> data = new ArrayList<>();
    FeedItem post;
    User user;

    public static Intent getStartIntent(Context context, FeedItem post) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("post", post);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        post = (FeedItem) getIntent().getSerializableExtra("post");
        user = User.getCurrent();
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.comments);

        adapter = new CommentRVAdapter(this, data);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPost();
            }
        });
        getPost();
    }

    protected void getPost() {
        GetPostData param = new GetPostData();
        param.setUserID(user.getId());
        param.setPostID(post.getPostID());
        Observable<PostManageResponse> observable = ApiRequest.getInstance().getApi().getPost(param);
        Observable.Transformer<PostManageResponse, PostManageResponse> transformer = bindToLifecycle();
        observable.compose(transformer).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostManageResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipe.setRefreshing(false);
                        e.printStackTrace();
                        String msg = Response.getResponse(e);
                        DialogUtils.showAlertDialog(CommentActivity.this, msg);
                    }

                    @Override
                    public void onNext(PostManageResponse response) {
                        swipe.setRefreshing(false);
                        if (response.isSuccess()) {
                            data.clear();
                            data.addAll(response.getPost().getComments());
                            adapter.notifyDataSetChanged();
                        } else {
                            DialogUtils.showAlertDialog(CommentActivity.this, Response.resolveCode(response));
                        }
                    }
                });
    }

    protected void sendComment(String content) {
        final ProgressDialog pd=DialogUtils.showProgressDialog(this,R.string.please_wait);
        CommentData param = new CommentData();
        param.setUserID(user.getId());
        param.setRefID(post.getPostID());
        param.setContent(content);
        param.setMode(CommentData.MODE_ADD);
        Observable<PostManageResponse> observable = ApiRequest.getInstance().getApi().postComment(param);
        Observable.Transformer<PostManageResponse, PostManageResponse> transformer = bindToLifecycle();
        observable.compose(transformer).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostManageResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        e.printStackTrace();
                        String msg = Response.getResponse(e);
                        DialogUtils.showAlertDialog(CommentActivity.this, msg);
                    }

                    @Override
                    public void onNext(PostManageResponse response) {
                        pd.dismiss();
                        if (response.isSuccess()) {
                            edComment.setText("");
                            data.clear();
                            data.addAll(response.getPost().getComments());
                            adapter.notifyDataSetChanged();
                        } else {
                            DialogUtils.showAlertDialog(CommentActivity.this, Response.resolveCode(response));
                        }

                    }
                });
    }


    @OnClick(R.id.send_comment_button)
    public void onSendCommentClick() {
        String content = edComment.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            sendComment(content);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtils.hideSoftKeyboard(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

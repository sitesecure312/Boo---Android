package com.boo.app.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.boo.app.R;
import com.boo.app.api.ApiRequest;
import com.boo.app.api.Response;
import com.boo.app.api.request.GetFollowingData;
import com.boo.app.api.response.FollowingResponse;
import com.boo.app.model.FollowItem;
import com.boo.app.model.User;
import com.boo.app.ui.adapter.FollowItemRVAdapter;
import com.boo.app.ui.base.BaseActivity;
import com.boo.app.ui.utils.DialogUtils;
import com.boo.app.ui.utils.EndlessRecyclerOnScrollListener;
import com.boo.app.ui.utils.KeyboardUtils;
import com.boo.app.ui.utils.PageUtils;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class FollowingActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.follow_recycler_view)
    RecyclerView recyclerView;

    User user;
    List<FollowItem> data = new ArrayList<>();
    FollowItemRVAdapter adapter;
    EndlessRecyclerOnScrollListener mScrollListener;
    int page;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.following);

        user = User.getCurrent();

//        mScrollListener = new EndlessRecyclerOnScrollListener(llm) {
//            @Override
//            public void loadMore() {
//                Log.d("Loading", "loadMore");
//                loadPage(page + 1);
//            }
//        };

        loadPage(0);
    }


    private void loadPage(int page) {
        getFollowing(page);
    }

    protected void getFollowing(final int loadingPage) {
        final GetFollowingData param = new GetFollowingData();
        param.setUserID(user.getId());
        PageUtils.setPageInfo(param, loadingPage);
        Observable<FollowingResponse> observable = ApiRequest.getInstance().getApi().getFollowing(param);
        Observable.Transformer<FollowingResponse, FollowingResponse> transformer = bindToLifecycle();
        observable.compose(transformer).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FollowingResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        String msg = Response.getResponse(e);
                        DialogUtils.showAlertDialog(FollowingActivity.this, msg);
                    }

                    @Override
                    public void onNext(FollowingResponse response) {

                        if (response.isSuccess()) {
//                            page = loadingPage;
//                            mScrollListener.setLoaded();
//                            if (page == 0) {
//                                data.clear();
//                            }
                            data.addAll(response.getData());
                            adapter = new FollowItemRVAdapter(getBaseContext(), data);
                            LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
                            recyclerView.setNestedScrollingEnabled(false);
                            recyclerView.setLayoutManager(llm);
                            recyclerView.setAdapter(adapter);
                            recyclerView.addItemDecoration(
                                    new HorizontalDividerItemDecoration.Builder(getBaseContext())
                                            .color(ContextCompat.getColor(getBaseContext(), R.color.colorGrayDivider))
                                            .sizeResId(R.dimen.divider_feed)
                                            .build());
                        } else {
                            DialogUtils.showAlertDialog(FollowingActivity.this, Response.resolveCode(response));
                        }

                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtils.hideSoftKeyboard(this);
    }
}

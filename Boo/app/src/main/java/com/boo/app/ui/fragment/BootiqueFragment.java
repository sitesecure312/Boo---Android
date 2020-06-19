package com.boo.app.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boo.app.R;
import com.boo.app.api.ApiRequest;
import com.boo.app.api.Response;
import com.boo.app.api.request.FeedData;
import com.boo.app.api.request.PostData;
import com.boo.app.api.request.RefData;
import com.boo.app.api.request.UserPostsData;
import com.boo.app.api.response.FeedResponse;
import com.boo.app.api.response.PostManageResponse;
import com.boo.app.api.response.UserRefResponse;
import com.boo.app.model.FeedItem;
import com.boo.app.model.User;
import com.boo.app.ui.activity.CommentActivity;
import com.boo.app.ui.activity.EditProfileActivity;
import com.boo.app.ui.activity.FollowersActivity;
import com.boo.app.ui.activity.FollowingActivity;
import com.boo.app.ui.activity.WalkThroughActivity;
import com.boo.app.ui.adapter.FeedRVAdapter;
import com.boo.app.ui.base.BaseFragment;
import com.boo.app.ui.utils.DialogUtils;
import com.boo.app.ui.utils.EndlessRecyclerOnScrollListener;
import com.boo.app.ui.utils.FragmentUtils;
import com.boo.app.ui.utils.PageUtils;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BootiqueFragment extends BaseFragment implements FeedRVAdapter.ActionListener {
    private static final int POST_BOO = 4;
    @Bind(R.id.posts_amount)
    TextView txtPostsAmount;
    @Bind(R.id.followers_amount)
    TextView txtFollowersAmount;
    @Bind(R.id.following_amount)
    TextView txtFollowingAmount;
    @Bind(R.id.bootique_recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.edit_profile)
    View btnEditProfile;
    @Bind(R.id.user_name)
    TextView txtUserName;
    @Bind(R.id.user_location)
    TextView txtUserLocation;
    @Bind(R.id.profile_image)
    ImageView profileImage;
    User currentUser;
    User user;
    boolean self;
    FeedRVAdapter adapter;
    int page=0;
    List<FeedItem> data=new ArrayList<>();
    EndlessRecyclerOnScrollListener mScrollListener;
    Transformation circle = new RoundedTransformationBuilder()
            .oval(true)
            .build();
    public static BootiqueFragment newInstance(User user) {
        BootiqueFragment fragment = new BootiqueFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bootique, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentUser = User.getCurrent();
        user = (User) getArguments().getSerializable("user");
        if (user.getId().equals(currentUser.getId())) {
            self = true;
        }
        if(!self){
           btnEditProfile.setVisibility(View.GONE);
        }
        adapter = new FeedRVAdapter(getActivity(), data, this);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .color(ContextCompat.getColor(getContext(), R.color.colorGrayDivider))
                        .sizeResId(R.dimen.divider_feed)
                        .build());
        mScrollListener = new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void loadMore() {
                Log.d("Loading", "loadMore");
                loadPage(page + 1);
            }
        };
        mScrollListener.setLoading();
        recyclerView.addOnScrollListener(mScrollListener);
        updateProfileInfo();
        loadPage(0);
        getUser();
    }

    protected void updateProfileInfo() {
        Picasso.with(getContext()).load(ApiRequest.getMediaUrl(user.getUserPhoto())).placeholder(R.drawable.ic_no_avatar)
                .fit().centerCrop().transform(circle).into(profileImage);

        txtFollowersAmount.setText(String.valueOf(user.getFollowersCount()));
        txtFollowingAmount.setText(String.valueOf(user.getFollowingCount()));
        txtPostsAmount.setText(String.valueOf(user.getPostsCount()));
        txtUserLocation.setText(user.getLocationAddress());
        txtUserName.setText(user.getFullName());
    }

    private void loadPage(int page) {
        getFeed(page);
    }

    protected void getUser() {
        RefData param=new RefData();
        param.setRefID(user.getId());
        param.setUserID(currentUser.getId());
        Observable<UserRefResponse> observable = ApiRequest.getInstance().getApi().getProfile(param);
        Observable.Transformer<UserRefResponse, UserRefResponse> transformer = bindToLifecycle();
        observable.compose(transformer).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserRefResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        String msg = Response.getResponse(e);
                        DialogUtils.showAlertDialog(getContext(), msg);
                    }

                    @Override
                    public void onNext(UserRefResponse response) {
                        if (response.isSuccess()) {
                            user=response.getUser();
                            updateProfileInfo();
                        } else {
                            DialogUtils.showAlertDialog(getContext(), Response.resolveCode(response));
                        }

                    }
                });
    }


    protected void getFeed(final int loadingPage) {
        final UserPostsData param = new UserPostsData();
        param.setUserID(currentUser.getId());
        param.setRefID(user.getId());
        PageUtils.setPageInfo(param, loadingPage);
        Observable<FeedResponse> observable = ApiRequest.getInstance().getApi().getUserPosts(param);
        Observable.Transformer<FeedResponse, FeedResponse> transformer = bindToLifecycle();
        observable.compose(transformer).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FeedResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        String msg = Response.getResponse(e);
                        DialogUtils.showAlertDialog(getContext(), msg);
                    }

                    @Override
                    public void onNext(FeedResponse response) {
                        if (response.isSuccess()) {
                            page = loadingPage;
                            mScrollListener.setLoaded();
                            if (page == 0) {
                                data.clear();
                            }
                            data.addAll(response.getData());
                            adapter.notifyDataSetChanged();
                            mScrollListener.setLoaded();
                        } else {
                            DialogUtils.showAlertDialog(getContext(), Response.resolveCode(response));
                        }

                    }
                });
    }


    protected void booPost(final FeedItem post) {
        final ProgressDialog pd = DialogUtils.showProgressDialog(getContext(), R.string.please_wait);
        final PostData param = new PostData();
        param.setUserID(user.getId());
        param.setPostID(post.getPostID());
        Observable<PostManageResponse> observable = ApiRequest.getInstance().getApi().booPost(param);
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
                        DialogUtils.showAlertDialog(getContext(), msg);
                    }

                    @Override
                    public void onNext(PostManageResponse response) {
                        pd.dismiss();
                        if (response.isSuccess()) {
                            post.setIsBooedByMe(response.getPost().getIsBooedByMe());
                            post.setBooedCount(response.getPost().getBooedCount());
                            int index = data.indexOf(post);
                            if (index >= 0) {
                                adapter.notifyItemChanged(index);
                            }
                        } else {
                            DialogUtils.showAlertDialog(getContext(), Response.resolveCode(response));
                        }

                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == POST_BOO) {
            //loadPage(0);
        }
    }

    @OnClick(R.id.edit_profile)
    public void onEditProfileClick() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.followers_btn)
    public void onFollowersClick() {
        Intent followersIntent = new Intent(getContext(), FollowersActivity.class);
        startActivity(followersIntent);
    }

    @OnClick(R.id.following_btn)
    public void onFollowingClick() {
        Intent followersIntent = new Intent(getContext(), FollowingActivity.class);
        startActivity(followersIntent);
    }

    @OnClick(R.id.posts_button)
    public void onPostsClick() {
        loadPage(0);
    }

    @Override
    public void onComment(FeedItem post) {
        Intent intent = CommentActivity.getStartIntent(getContext(), post);
        startActivityForResult(intent, POST_BOO);
    }

    @Override
    public void onBoo(FeedItem post) {
        booPost(post);
    }

    @Override
    public void onUser(FeedItem post) {
        User user = new User(post);
        FragmentUtils.replaceFragment(getActivity(), R.id.home_screen_container, BootiqueFragment.newInstance(user), true);
    }

    @Override
    public void onShare(FeedItem post) {

    }
}

package com.boo.app.ui.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boo.app.R;
import com.boo.app.api.ApiRequest;
import com.boo.app.api.Response;
import com.boo.app.api.request.FeedData;
import com.boo.app.api.request.PostData;
import com.boo.app.api.request.SignUpData;
import com.boo.app.api.response.FeedResponse;
import com.boo.app.api.response.PostManageResponse;
import com.boo.app.api.response.UserResponse;
import com.boo.app.model.FeedItem;
import com.boo.app.model.User;
import com.boo.app.ui.activity.CommentActivity;
import com.boo.app.ui.activity.MainActivity;
import com.boo.app.ui.activity.PlaceActivity;
import com.boo.app.ui.activity.PostToBooActivity;
import com.boo.app.ui.activity.SearchActivity;
import com.boo.app.ui.adapter.FeedRVAdapter;
import com.boo.app.ui.base.BaseFragment;
import com.boo.app.ui.utils.DialogUtils;
import com.boo.app.ui.utils.EndlessRecyclerOnScrollListener;
import com.boo.app.ui.utils.FragmentUtils;
import com.boo.app.ui.utils.ImageUtils;
import com.boo.app.ui.utils.PageUtils;
import com.boo.app.ui.utils.TintUtils;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeScreenFragment extends BaseFragment implements FeedRVAdapter.ActionListener {

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 3;
    private static final int POST_BOO = 4;
    private static final int READ_STORAGE_REQUEST_CODE = 100;

    @Bind(R.id.home_screen_recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ic_text)
    ImageView icText;
    @Bind(R.id.ic_photo)
    ImageView icPhoto;
    @Bind(R.id.ic_place)
    ImageView icPlace;
    @Bind(R.id.profile_image)
    ImageView profileImage;
    @Bind(R.id.swipe)
    SwipeRefreshLayout mSwipe;
    EndlessRecyclerOnScrollListener mScrollListener;
    List<FeedItem> data = new ArrayList<>();
    int page;
    Uri mImageCaptureUri;
    User user;
    FeedRVAdapter adapter;

    Transformation circle = new RoundedTransformationBuilder()
            .oval(true)
            .build();

    public static HomeScreenFragment newInstance() {
        HomeScreenFragment fragment = new HomeScreenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            String imageUri = savedInstanceState.getString("imageUri");
            if (imageUri != null) {
                mImageCaptureUri = Uri.parse(imageUri);
            }
        }

        user = User.getCurrent();
        Picasso.with(getContext()).load(ApiRequest.getMediaUrl(user.getUserPhoto())).placeholder(R.drawable.ic_no_avatar).fit().centerCrop().transform(circle)
                .into(profileImage);
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
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPage(0);
            }
        });
        mScrollListener.setLoading();
        recyclerView.addOnScrollListener(mScrollListener);
        TintUtils.setPressedTint(getContext(), icText, R.color.colorBasicText, R.color.colorPrimary);
        TintUtils.setPressedTint(getContext(), icPhoto, R.color.colorBasicText, R.color.colorPrimary);
        TintUtils.setPressedTint(getContext(), icPlace, R.color.colorBasicText, R.color.colorPrimary);
        loadPage(0);

    }

    private void loadPage(int page) {
        getFeed(page);
    }

    protected void getFeed(final int loadingPage) {
        final FeedData param = new FeedData();
        param.setUserID(user.getId());
        PageUtils.setPageInfo(param, loadingPage);
        Observable<FeedResponse> observable = ApiRequest.getInstance().getApi().getFeed(param);
        Observable.Transformer<FeedResponse, FeedResponse> transformer = bindToLifecycle();
        observable.compose(transformer).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FeedResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipe.setRefreshing(false);
                        e.printStackTrace();
                        String msg = Response.getResponse(e);
                        DialogUtils.showAlertDialog(getContext(), msg);
                    }

                    @Override
                    public void onNext(FeedResponse response) {
                        mSwipe.setRefreshing(false);
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

    @OnClick({R.id.text_button, R.id.post_field})
    public void textClick() {
        Intent txtPostIntent = new Intent(getContext(), PostToBooActivity.class);
        startActivityForResult(txtPostIntent, POST_BOO);

    }

    @OnClick(R.id.photo_button)
    public void onPhotoClick() {
        showImageDialog();
    }

    @OnClick(R.id.place_button)
    public void onPlaceClick() {
        Intent placeIntent = new Intent(getContext(), PlaceActivity.class);
        startActivity(placeIntent);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                startActivity(searchIntent);
                break;
        }

        return true;
    }

    private void showImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final String[] items = new String[]{getString(R.string.take_photo), getString(R.string.from_gallery)};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        takePicture();
                        break;
                    case 1:
                        selectPicture();
                        break;
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_STORAGE_REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            selectPicture();
        }
    }

    private void selectPicture() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_REQUEST_CODE);
            return;
        }

        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_FROM_FILE);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_FROM_FILE);
        }
    }

    private void takePicture() {
        mImageCaptureUri = Uri.fromFile(new File(
                getContext().getExternalCacheDir(), String.valueOf(System.currentTimeMillis()
                + ".jpg")));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        try {
            startActivityForResult(intent, PICK_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == POST_BOO) {
            loadPage(0);
        }
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                startActivityForResult(PostToBooActivity.getStartIntent(getContext(), mImageCaptureUri.toString()), POST_BOO);
                break;

            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();
                String filePath = ImageUtils.getPath(getContext(), mImageCaptureUri);
                startActivityForResult(PostToBooActivity.getStartIntent(getContext(), "file://" + filePath), POST_BOO);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mImageCaptureUri != null) {
            outState.putString("imageUri", mImageCaptureUri.toString());
        }
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

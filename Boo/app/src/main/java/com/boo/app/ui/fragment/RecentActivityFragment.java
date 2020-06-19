package com.boo.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.boo.app.R;
import com.boo.app.api.ApiRequest;
import com.boo.app.api.Response;
import com.boo.app.api.request.ActivityData;
import com.boo.app.api.response.ActivityResponse;
import com.boo.app.model.RecentActivityItem;
import com.boo.app.model.User;
import com.boo.app.ui.adapter.RecentActivityRVAdapter;
import com.boo.app.ui.base.BaseFragment;
import com.boo.app.ui.utils.DialogUtils;
import com.boo.app.ui.utils.EndlessRecyclerOnScrollListener;
import com.boo.app.ui.utils.PageUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RecentActivityFragment extends BaseFragment {

    @Bind(R.id.activity_recycler_view)
    RecyclerView recyclerView;
    RecentActivityRVAdapter adapter;
    List<RecentActivityItem> recentActivity = new ArrayList<>();

    User user;

    public static RecentActivityFragment newInstance() {
        RecentActivityFragment fragment = new RecentActivityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recent_activity, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        user = User.getCurrent();

        loadPage(0);

    }

    private void loadPage(int page) {
        recentActivities(page);
    }

    protected void recentActivities(final int loadingPage) {
        final ActivityData param = new ActivityData();
        param.setUserID(user.getId());
        PageUtils.setPageInfo(param, loadingPage);
        Observable<ActivityResponse> observable = ApiRequest.getInstance().getApi().recentActivities(param);
        Observable.Transformer<ActivityResponse, ActivityResponse> transformer = bindToLifecycle();
        observable.compose(transformer).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActivityResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        String msg = Response.getResponse(e);
                        DialogUtils.showAlertDialog(getActivity(), msg);
                    }

                    @Override
                    public void onNext(ActivityResponse response) {

                        if (response.isSuccess()) {
                            recentActivity.addAll(response.getData());
                            adapter = new RecentActivityRVAdapter(getActivity(), recentActivity);
                            recyclerView.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(llm);
                            recyclerView.setAdapter(adapter);
                            recyclerView.addItemDecoration(
                                    new HorizontalDividerItemDecoration.Builder(getContext())
                                            .color(ContextCompat.getColor(getContext(), R.color.colorGrayDivider))
                                            .sizeResId(R.dimen.divider_recent_activity)
                                            .marginResId(R.dimen.recent_activity_divider_margin, R.dimen.recent_activity_divider_margin)
                                            .build());

                        } else {
                            DialogUtils.showAlertDialog(getActivity(), Response.resolveCode(response));
                        }

                    }
                });
    }

}

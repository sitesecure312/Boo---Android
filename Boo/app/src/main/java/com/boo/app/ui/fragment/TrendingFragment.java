package com.boo.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.boo.app.R;
import com.boo.app.model.FeedItem;
import com.boo.app.ui.activity.SearchActivity;
import com.boo.app.ui.adapter.FeedRVAdapter;
import com.boo.app.ui.base.BaseFragment;
import com.boo.app.ui.utils.TintUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrendingFragment extends BaseFragment implements FeedRVAdapter.ActionListener {

    @Bind(R.id.ic_near_you)
    ImageView icNearYou;
    @Bind(R.id.ic_global)
    ImageView icGlobal;
    @Bind(R.id.near_you_underline)
    View nearYouUnderline;
    @Bind(R.id.global_underline)
    View globalUnderline;
    @Bind(R.id.near_you_btn)
    LinearLayout nearYouButton;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    View lastSelected = null;
    FeedRVAdapter adapter;

    public static TrendingFragment newInstance() {
        TrendingFragment fragment = new TrendingFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_trending, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TintUtils.setSelectedTint(getContext(), icNearYou, R.color.colorBottomMenuItem, R.color.colorPrimary);
        TintUtils.setSelectedTint(getContext(), icGlobal, R.color.colorBottomMenuItem, R.color.colorPrimary);

        if (lastSelected == null) {
            lastSelected = nearYouButton;
            lastSelected.setSelected(true);
            nearYouUnderline.setVisibility(View.VISIBLE);
        }

        List<FeedItem> items = new ArrayList<>();

        adapter = new FeedRVAdapter(getActivity(), items,this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .color(ContextCompat.getColor(getContext(), R.color.colorGrayDivider))
                        .sizeResId(R.dimen.divider_feed)
                        .build());


    }

    @OnClick({R.id.near_you_btn, R.id.global_btn})
    protected void changeTab(View view) {
        if (lastSelected != null) {
            lastSelected.setSelected(false);
        }
        lastSelected = view;
        view.setSelected(true);
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.near_you_btn:
                globalUnderline.setVisibility(View.GONE);
                nearYouUnderline.setVisibility(View.VISIBLE);

                List<FeedItem> itemsTest = new ArrayList<>();

                adapter = new FeedRVAdapter(getActivity(), itemsTest,this);
                recyclerView.setAdapter(adapter);

                break;
            case R.id.global_btn:
                globalUnderline.setVisibility(View.VISIBLE);
                nearYouUnderline.setVisibility(View.GONE);

                List<FeedItem> items = new ArrayList<>();

                adapter = new FeedRVAdapter(getActivity(), items,this);
                recyclerView.setAdapter(adapter);

                break;
        }

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

    @Override
    public void onComment(FeedItem post) {

    }

    @Override
    public void onBoo(FeedItem post) {

    }

    @Override
    public void onUser(FeedItem post) {

    }

    @Override
    public void onShare(FeedItem post) {

    }
}

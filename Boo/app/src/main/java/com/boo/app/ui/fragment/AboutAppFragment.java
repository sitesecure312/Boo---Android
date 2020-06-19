package com.boo.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boo.app.R;
import com.boo.app.ui.activity.MainActivity;
import com.boo.app.ui.utils.PicassoUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutAppFragment extends Fragment {

    @Bind(R.id.page_bg_view)
    ImageView pageBgView;

    public static AboutAppFragment newInstance() {
        AboutAppFragment fragment = new AboutAppFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_app, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).changeTab(null);
        PicassoUtils.loadBackgroundImg(getContext(), pageBgView, R.drawable.ic_bg_page_1);
    }
}

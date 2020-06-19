package com.boo.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boo.app.R;
import com.boo.app.ui.utils.PicassoUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WalkTroughPageFragment extends Fragment {

    private static final String BG_IMAGE_ID = "bgImageId";
    private static final String PAGE_CONTENT_ID = "pageContentId";
    @Bind(R.id.page_bg_view)
    ImageView pageBgView;
    @Bind(R.id.page_content)
    TextView pageContent;

    public static WalkTroughPageFragment newInstance(int bgImageId, int pageContentId) {
        WalkTroughPageFragment fragment = new WalkTroughPageFragment();
        Bundle args = new Bundle();
        args.putInt(BG_IMAGE_ID, bgImageId);
        args.putInt(PAGE_CONTENT_ID, pageContentId);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_walk_through_page, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        int bgImageId = args.getInt(BG_IMAGE_ID);
        int contentId = args.getInt(PAGE_CONTENT_ID);

        PicassoUtils.loadBackgroundImg(getContext(), pageBgView, bgImageId);
        pageContent.setText(contentId);
    }
}

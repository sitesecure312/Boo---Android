package com.boo.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.boo.app.R;
import com.boo.app.ui.fragment.WalkTroughPageFragment;
import com.boo.app.ui.utils.TranslucenStatusBarUtils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalkThroughActivity extends MontserratFontActivity {

    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.indicator)
    CirclePageIndicator indicator;
    private List<Fragment> data = new ArrayList<>();

    public static Intent getStartIntent(Context context){
        Intent intent=new Intent(context,WalkThroughActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);
        ButterKnife.bind(this);

        TranslucenStatusBarUtils.setTranslucentStatusBar(getWindow());

        fillList();
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        indicator.setViewPager(viewPager);
    }

    private void fillList() {
        data.clear();
        data.add(WalkTroughPageFragment.newInstance(R.drawable.ic_bg_page_1, R.string.page_content));
        data.add(WalkTroughPageFragment.newInstance(R.drawable.ic_bg_page_2, R.string.page_content));
    }


    @OnClick(R.id.sign_up_button)
    public void onSignUpClick() {
        Intent intent = new Intent(WalkThroughActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_button)
    public void onLoginClick() {
        Intent intent = new Intent(WalkThroughActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }
}

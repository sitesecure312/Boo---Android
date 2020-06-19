package com.boo.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.boo.app.R;
import com.boo.app.model.User;
import com.boo.app.ui.fragment.AboutAppFragment;
import com.boo.app.ui.fragment.ChangePasswordFragment;
import com.boo.app.ui.fragment.RecentActivityFragment;
import com.boo.app.ui.fragment.BootiqueFragment;
import com.boo.app.ui.fragment.HomeScreenFragment;
import com.boo.app.ui.fragment.TrendingFragment;
import com.boo.app.ui.utils.FragmentUtils;
import com.boo.app.ui.utils.KeyboardUtils;
import com.boo.app.ui.utils.TintUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.ic_menu_home)
    ImageView icMenuHome;
    @Bind(R.id.ic_menu_trending)
    ImageView icMenuTrending;
    @Bind(R.id.ic_menu_activity)
    ImageView icMenuActivity;
    @Bind(R.id.ic_menu_bootique)
    ImageView icMenuBootique;
    @Bind(R.id.home_menu_btn)
    public LinearLayout homeMenuBtn;
    @Bind(R.id.activity_menu_btn)
    LinearLayout recActivityBtn;
    @Bind(R.id.nav_view)
    NavigationView navigationView;

    View lastSelected = null;
    MenuItem navSelectedItem;
    ActionBarDrawerToggle drawerToggle;

    public static Intent getStartIntent(Context context){
        Intent intent=new Intent(context,MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportFragmentManager().addOnBackStackChangedListener(backStackListener);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        FragmentUtils.replaceFragment(this, R.id.home_screen_container, HomeScreenFragment.newInstance(), false);

        TintUtils.setSelectedTint(this, icMenuHome, R.color.colorMenuIcon, R.color.colorMenuIconPressed);
        TintUtils.setSelectedTint(this, icMenuTrending, R.color.colorMenuIcon, R.color.colorMenuIconPressed);
        TintUtils.setSelectedTint(this, icMenuActivity, R.color.colorMenuIcon, R.color.colorMenuIconPressed);
        TintUtils.setSelectedTint(this, icMenuBootique, R.color.colorMenuIcon, R.color.colorMenuIconPressed);

        changeTab(homeMenuBtn);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private FragmentManager.OnBackStackChangedListener backStackListener = new FragmentManager.OnBackStackChangedListener() {
        public void onBackStackChanged() {
            setNavIcon();
        }
    };


    protected void setNavIcon() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        drawerToggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.isDrawerIndicatorEnabled() && drawerToggle.onOptionsItemSelected(item)) {
            KeyboardUtils.hideSoftKeyboard(this);
            return true;
        } else if (item.getItemId() == android.R.id.home &&
                getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawers();
        item.setCheckable(true);
        item.setChecked(true);
        navSelectedItem = item;
        switch (item.getItemId()) {
            case R.id.nav_logout:
                User.deleteCurrent();
                Intent logoutIntent = new Intent(MainActivity.this, WalkThroughActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                break;
            case R.id.nav_change_password:
                FragmentUtils.replaceFragment(this, R.id.home_screen_container, ChangePasswordFragment.newInstance(), false);
                break;
            case R.id.nav_notification:
                FragmentUtils.replaceFragment(this, R.id.home_screen_container, RecentActivityFragment.newInstance(), false);
                changeTab(recActivityBtn);
                break;
            case R.id.nav_about:
                FragmentUtils.replaceFragment(this, R.id.home_screen_container, AboutAppFragment.newInstance(), false);
                break;
        }
        return true;
    }


    @OnClick({R.id.home_menu_btn, R.id.trending_menu_btn, R.id.activity_menu_btn, R.id.bootique_menu_btn})
    public void changeTab(View view) {
        if (lastSelected != null) {
            lastSelected.setSelected(false);
        }
        if (view != null) {
            lastSelected = view;
            view.setSelected(true);

            if (navSelectedItem != null) {
                navSelectedItem.setChecked(false);
                navSelectedItem = null;
            }

            Fragment fragment = null;
            switch (view.getId()) {
                case R.id.home_menu_btn:
                    fragment = HomeScreenFragment.newInstance();
                    break;
                case R.id.trending_menu_btn:
                    fragment = TrendingFragment.newInstance();
                    break;
                case R.id.activity_menu_btn:
                    fragment = RecentActivityFragment.newInstance();
                    break;
                case R.id.bootique_menu_btn:
                    fragment = BootiqueFragment.newInstance(User.getCurrent());
                    break;
            }
            if (fragment != null) {
                FragmentUtils.replaceFragment(this, R.id.home_screen_container, fragment, false);
            }
        }
    }


}

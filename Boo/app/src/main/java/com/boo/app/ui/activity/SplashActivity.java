
package com.boo.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.boo.app.R;
import com.boo.app.model.User;
import com.boo.app.ui.utils.PicassoUtils;
import com.boo.app.ui.utils.TranslucenStatusBarUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends MontserratFontActivity {

    private final static int SPLASH_DISPLAY_DURATION_MS = 2000;

    @Bind(R.id.bg_image)
    ImageView bgImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        TranslucenStatusBarUtils.setTranslucentStatusBar(getWindow());

        PicassoUtils.loadBackgroundImg(this, bgImageView, R.drawable.ic_splash_bg);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user=User.getCurrent();
                Intent intent=null;
                if(user==null) {
                    intent =WalkThroughActivity.getStartIntent(SplashActivity.this);
                }
                else {
                    intent =MainActivity.getStartIntent(SplashActivity.this);
                }
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_DURATION_MS);

    }

}

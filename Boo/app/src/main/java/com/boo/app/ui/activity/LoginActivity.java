package com.boo.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.boo.app.R;
import com.boo.app.api.request.SocialLoginData;
import com.boo.app.ui.utils.PicassoUtils;
import com.boo.app.ui.utils.TranslucenStatusBarUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends SocialActivity{
    @Bind(R.id.bg_image)
    ImageView bgImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        TranslucenStatusBarUtils.setTranslucentStatusBar(getWindow());

        PicassoUtils.loadBackgroundImg(this, bgImageView, R.drawable.ic_login_bg);
    }

    @OnClick(R.id.login_fb_btn)
    protected void loginFacebook(){
        facebookLogIn();
    }

    @OnClick(R.id.login_twitter_btn)
    protected void loginTwitter(){
        twitterLogin();
    }

    @Override
    public void onDataReady(SocialLoginData data) {
        data.setLatitude(1.352083);
        data.setLongitude(103.819836);
        submitSignIn(data);
    }

    @OnClick(R.id.log_in_email_btn)
    void onEmailLoginClick() {
        Intent signUpIntent = new Intent(LoginActivity.this, EmailLoginActivity.class);
        startActivity(signUpIntent);
    }

    @OnClick(R.id.sign_up_btn)
    void onSignUpClick() {
        Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(signUpIntent);
    }
}

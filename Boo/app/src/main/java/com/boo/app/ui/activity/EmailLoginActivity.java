package com.boo.app.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.boo.app.R;
import com.boo.app.api.ApiRequest;
import com.boo.app.api.Response;
import com.boo.app.api.request.LoginData;
import com.boo.app.api.request.SignUpData;
import com.boo.app.api.response.UserResponse;
import com.boo.app.ui.base.BaseActivity;
import com.boo.app.ui.utils.DialogUtils;
import com.boo.app.ui.utils.HashUtils;
import com.boo.app.ui.utils.PicassoUtils;
import com.boo.app.ui.utils.TranslucenStatusBarUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EmailLoginActivity extends BaseActivity {

    @Bind(R.id.bg_image)
    ImageView bgImageView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.login_password)
    EditText edPassword;
    @Bind(R.id.login_username)
    EditText edUsername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        ButterKnife.bind(this);
        TranslucenStatusBarUtils.setTranslucentStatusBar(getWindow());
        PicassoUtils.loadBackgroundImg(this, bgImageView, R.drawable.ic_login_bg);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_up_button_24dp));
    }

    @OnClick(R.id.login_button)
    protected void checkFields() {
        String username = edUsername.getText().toString();
        String password = edPassword.getText().toString();

        boolean error = false;
        if (TextUtils.isEmpty(username)) {
            error = true;
            edUsername.setError(getString(R.string.fill_field));
        }
        if (TextUtils.isEmpty(password)) {
            error = true;
            edPassword.setError(getString(R.string.fill_field));
        }
        if (!error) {
            LoginData data = new LoginData();
            data.setEmail(username);
            data.setPassword(HashUtils.md5(password));
            submit(data);
        }
    }

    protected void submit(final LoginData data) {
        final ProgressDialog pd = DialogUtils.showProgressDialog(this, R.string.please_wait);
        Observable<UserResponse> observable = ApiRequest.getInstance().getApi().signIn(data);
        Observable.Transformer<UserResponse, UserResponse> transformer = bindToLifecycle();
        observable.compose(transformer).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        e.printStackTrace();
                        String msg = Response.getResponse(e);
                        DialogUtils.showAlertDialog(EmailLoginActivity.this, msg);
                    }

                    @Override
                    public void onNext(UserResponse response) {
                        pd.dismiss();
                        if (response.isSuccess()) {
                            response.getUser().save();
                            Intent intent = MainActivity.getStartIntent(EmailLoginActivity.this);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            DialogUtils.showAlertDialog(EmailLoginActivity.this, Response.resolveCode(response));
                        }

                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

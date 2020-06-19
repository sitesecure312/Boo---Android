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
import com.boo.app.api.request.SignUpData;
import com.boo.app.api.request.SocialLoginData;
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

public class SignUpActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.bg_image)
    ImageView bgImageView;
    @Bind(R.id.sign_up_username)
    EditText edUserName;
    @Bind(R.id.sign_up_first_name)
    EditText edFirstName;
    @Bind(R.id.sign_up_last_name)
    EditText edLastName;
    @Bind(R.id.sign_up_email)
    EditText edEmail;
    @Bind(R.id.sign_up_password)
    EditText edPassword;
    @Bind(R.id.sign_up_confirm_password)
    EditText edConfirmPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        TranslucenStatusBarUtils.setTranslucentStatusBar(getWindow());

        PicassoUtils.loadBackgroundImg(this, bgImageView, R.drawable.ic_login_bg);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_up_button_24dp));
    }

    @OnClick(R.id.sign_up_button)
    protected void checkFields() {
        String username = edUserName.getText().toString();
        String firstName = edFirstName.getText().toString();
        String lastName = edLastName.getText().toString();
        String password = edPassword.getText().toString();
        String confirmPassword = edConfirmPassword.getText().toString();
        String email = edEmail.getText().toString();

        boolean error = false;
        if (TextUtils.isEmpty(username)) {
            setError(edUserName, R.string.fill_field);
            error = true;
        }
        if (TextUtils.isEmpty(firstName)) {
            setError(edFirstName, R.string.fill_field);
            error = true;
        }
        if (TextUtils.isEmpty(lastName)) {
            setError(edLastName, R.string.fill_field);
            error = true;
        }
        if (TextUtils.isEmpty(email)) {
            setError(edEmail, R.string.fill_field);
            error = true;
        }
        if (TextUtils.isEmpty(password)) {
            setError(edPassword, R.string.fill_field);
            error = true;
        } else if (!password.equals(confirmPassword)) {
            setError(edConfirmPassword, R.string.passwords_dont_match);
            error = true;
        }

        if (!error) {
            SignUpData signUpData = new SignUpData();
            signUpData.setEmail(email);
            signUpData.setFirstName(firstName);
            signUpData.setLastName(lastName);
            signUpData.setUserName(username);
            signUpData.setPassword(HashUtils.md5(password));
            signUpData.setLatitude(1.352083);
            signUpData.setLongitude(103.819836);
            submitSignUp(signUpData);
        }
    }

    protected void submitSignUp(final SignUpData data) {
        final ProgressDialog pd = DialogUtils.showProgressDialog(this, R.string.please_wait);
        Observable<UserResponse> observable = ApiRequest.getInstance().getApi().signUp(data);
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
                        DialogUtils.showAlertDialog(SignUpActivity.this, msg);
                    }

                    @Override
                    public void onNext(UserResponse response) {
                        pd.dismiss();
                        if (response.isSuccess()) {
                            response.getUser().save();
                            Intent intent = MainActivity.getStartIntent(SignUpActivity.this);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            DialogUtils.showAlertDialog(SignUpActivity.this, Response.resolveCode(response));
                        }

                    }
                });
    }


    private void setError(EditText editText, int error) {
        editText.setError(getString(error));
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

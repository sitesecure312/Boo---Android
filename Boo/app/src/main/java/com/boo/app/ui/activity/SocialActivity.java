package com.boo.app.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.boo.app.R;
import com.boo.app.api.ApiRequest;
import com.boo.app.api.Response;
import com.boo.app.api.request.SocialLoginData;
import com.boo.app.api.response.UserResponse;
import com.boo.app.ui.base.BaseActivity;
import com.boo.app.ui.utils.DialogUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by razir on 4/4/2016.
 */
public abstract class SocialActivity extends BaseActivity {

    CallbackManager callbackManager;
    TwitterAuthClient mTwitterAuthClient = new TwitterAuthClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                onFacebookLoginSuccess(loginResult);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
            }
        });
    }

    protected void facebookLogIn() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }


    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (mTwitterAuthClient.getRequestCode() == requestCode) {
            mTwitterAuthClient.onActivityResult(requestCode, responseCode, intent);
        }
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

    protected void twitterLogin() {
        mTwitterAuthClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {

                mTwitterAuthClient.requestEmail(twitterSessionResult.data, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        fetchTwitterData(result.data);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                        fetchTwitterData(null);
                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });
    }

    private void fetchTwitterData(final String email) {
        Twitter.getApiClient().getAccountService().verifyCredentials(true, false, new Callback<com.twitter.sdk.android.core.models.User>() {
            @Override
            public void success(Result<com.twitter.sdk.android.core.models.User> result) {
                long id = result.data.getId();
                String name = result.data.screenName;
                final SocialLoginData data = new SocialLoginData();
                if(email==null){
                    data.setEmail(result.data.screenName+"@temp.com");
                }
                else {
                    data.setEmail(email);
                }

                data.setMode(SocialLoginData.MODE_TWITTER);
                data.setFirstName(name);
                data.setLastName(name);
                data.setPhotoUrl(result.data.profileImageUrl);
                data.setTwitterId(String.valueOf(id));

                onDataReady(data);

            }

            @Override
            public void failure(TwitterException e) {

            }
        });
    }

    protected void onFacebookLoginSuccess(LoginResult loginResult){
        fetchFacebookUser();
    }

    protected void onFacebookUserCompleted(JSONObject user, GraphResponse graphResponse) {
        try {
            String id = user.getString("id");
            String email = user.getString("email");
            String firstName = user.getString("first_name");
            String lastName = user.getString("last_name");
            String picture = "https://graph.facebook.com/" + id + "/picture?type=large";
            SocialLoginData data = new SocialLoginData();
            data.setEmail(email);
            data.setMode(SocialLoginData.MODE_FACEBOOK);
            data.setFirstName(firstName);
            data.setLastName(lastName);
            data.setPhotoUrl(picture);
            data.setFacebookId(id);
            onDataReady(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public abstract void onDataReady(SocialLoginData data);

    protected void submitSignIn(final SocialLoginData data) {
        final ProgressDialog pd = DialogUtils.showProgressDialog(this, R.string.please_wait);
        Observable<UserResponse> observable = ApiRequest.getInstance().getApi().socialSignIn(data);
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
                        DialogUtils.showAlertDialog(SocialActivity.this, msg);
                    }

                    @Override
                    public void onNext(UserResponse response) {
                        pd.dismiss();
                        if (response.isSuccess()) {
                            response.getUser().save();
                            Intent intent=MainActivity.getStartIntent(SocialActivity.this);
                            startActivity(intent);
                            finish();
                        } else {
                            DialogUtils.showAlertDialog(SocialActivity.this, Response.resolveCode(response));
                        }

                    }
                });
    }


    protected void fetchFacebookUser() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                if (user != null) {
                    onFacebookUserCompleted(user, graphResponse);
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
}

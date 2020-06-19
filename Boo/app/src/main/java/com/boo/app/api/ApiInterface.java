package com.boo.app.api;

import com.boo.app.api.request.ActivityData;
import com.boo.app.api.request.CommentData;
import com.boo.app.api.request.FeedData;
import com.boo.app.api.request.GetFollowersData;
import com.boo.app.api.request.GetFollowingData;
import com.boo.app.api.request.GetPostData;
import com.boo.app.api.request.LoginData;
import com.boo.app.api.request.PostData;
import com.boo.app.api.request.PostManage;
import com.boo.app.api.request.RefData;
import com.boo.app.api.request.SignUpData;
import com.boo.app.api.request.SocialLoginData;
import com.boo.app.api.request.UserPostsData;
import com.boo.app.api.response.ActivityResponse;
import com.boo.app.api.response.FeedResponse;
import com.boo.app.api.response.FollowersResponse;
import com.boo.app.api.response.FollowingResponse;
import com.boo.app.api.response.PostManageResponse;
import com.boo.app.api.response.UserRefResponse;
import com.boo.app.api.response.UserResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by razir on 5/26/2016.
 */
public interface ApiInterface {

    @POST("user_social_signup")
    Observable<UserResponse> socialSignIn(@Body SocialLoginData data);

    @POST("user_manual_signup")
    Observable<UserResponse> signUp(@Body SignUpData data);

    @POST("get_feed_posts")
    Observable<FeedResponse> getFeed(@Body FeedData data);

    @POST("get_user_posts")
    Observable<FeedResponse> getUserPosts(@Body UserPostsData data);

    @POST("get_post")
    Observable<PostManageResponse> getPost(@Body GetPostData data);

    @POST("get_profile")
    Observable<UserRefResponse> getProfile(@Body RefData data);

    @POST("comment_post")
    Observable<PostManageResponse> postComment(@Body CommentData data);

    @POST("user_manual_login")
    Observable<UserResponse> signIn(@Body LoginData data);

    @POST("manage_my_posts")
    Observable<PostManageResponse> postManage(@Body PostManage data);

    @POST("boo_post")
    Observable<PostManageResponse> booPost(@Body PostData data);

    @POST("get_following")
    Observable<FollowingResponse> getFollowing(@Body GetFollowingData data);

    @POST("get_followers")
    Observable<FollowersResponse> getFollowers(@Body GetFollowersData data);

    @POST("activities")
    Observable<ActivityResponse> recentActivities(@Body ActivityData data);
}

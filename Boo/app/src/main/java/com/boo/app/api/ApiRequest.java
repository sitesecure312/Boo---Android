package com.boo.app.api;

import android.content.Intent;

import com.boo.app.ui.activity.LoginActivity;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by razir on 3/29/2016.
 */
public class ApiRequest {

    public static final String API_KEY = "03c48c1b03bde4946f63150e8c0ebc64";
    static ApiRequest apiRequest;
    public static final String MAIN_URL = "http://www.boo-it.com/api/";
    public static final String MEDIA_URL = "http://www.boo-it.com/";
    public static final String MEDIA_USERS = "assets/media/users/";
    public static final String MEDIA_PHOTOS = "assets/media/post_photos/";
    public static final String MEDIA_VIDEOS = "assets/media/post_videos/";
    public static final String MEDIA_VIDEO_THUMB = "assets/media/post_video_thumbs/";
    ApiInterface api;

    public static ApiRequest getInstance() {
        if (apiRequest == null) {
            apiRequest = new ApiRequest();
        }
        return apiRequest;
    }

    public static String getMediaUrl(String url) {
        if(url.startsWith("bo")){
            url=url.replace("bo_","");
            String[] params=url.split("_");
            if(params[0].startsWith("avatar")){
                return MEDIA_URL+MEDIA_USERS+params[1];
            }
            else {
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < params.length; i++) {
                    sb.append(params[i]);
                    if (i != params.length - 1) {
                        sb.append("_");
                    }
                }
                String path=null;
                switch (params[1]){
                    case "photo":
                        path=MEDIA_PHOTOS;
                        break;
                    case "video":
                        path=MEDIA_VIDEOS;
                        break;
                }
                url = ApiRequest.MEDIA_URL + path + sb.toString();
            }
        }
        return url;
    }

    public ApiRequest() {
        Retrofit retrofit = getRetrofit();
        api = retrofit.create(ApiInterface.class);
    }

    public Retrofit getRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder().readTimeout(10,TimeUnit.MINUTES).connectTimeout(10, TimeUnit.MINUTES).writeTimeout(10, TimeUnit.MINUTES).addInterceptor(interceptor);
        client.addInterceptor(new Interceptor() {
                                  @Override
                                  public okhttp3.Response intercept(Chain chain) throws IOException {
                                      Request original = chain.request();
                                      Request.Builder requestBuilder = original.newBuilder();

                                      requestBuilder.header("api-key", API_KEY);

                                      Request request = requestBuilder.method(original.method(), original.body())
                                              .build();
                                      okhttp3.Response response = chain.proceed(request);
                                      if (response.code() == 401) {
//                                          Intent intent = LoginActivity.getStartIntent(App.getContext());
//                                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                          App.getContext().startActivity(intent);
                                      }
                                      return response;
                                  }
                              }
        );

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonConfig.buildDefaultJson()))
                .baseUrl(MAIN_URL)
                .client(client.build())
                .build();
        return retrofit;
    }


    public ApiInterface getApi() {
        return api;
    }
}

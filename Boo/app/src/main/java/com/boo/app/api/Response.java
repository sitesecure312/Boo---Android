package com.boo.app.api;


import com.boo.app.App;
import com.boo.app.R;
import com.boo.app.api.response.ServerResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

public class Response {

    private static final Action1<Throwable> ERROR_HANDLER = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {

        }
    };

    public static Action1<Throwable> handleError() {
        return ERROR_HANDLER;
    }

    public static String getResponse(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            HttpException err = (HttpException) throwable;
            Retrofit retrofit = ApiRequest.getInstance().getRetrofit();
            Converter<ResponseBody, ServerResponse> ec = retrofit.responseBodyConverter(ServerResponse.class, new Annotation[]{});
            try {
                ServerResponse errorResponse = ec.convert(err.response().errorBody());
                if (!errorResponse.getMsg().isEmpty()) {
                    return errorResponse.getMsg();
                } else {
                    return "Empty Message In Response";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return App.getContext().getString(R.string.goes_wrong);
            }
        }
        if (throwable instanceof SocketTimeoutException) {
            return "Timeout";
        }

        return throwable.getMessage();
    }

    public static String resolveCode(ServerResponse response) {
        return response.getMsg();
    }

    public static String getMessage(Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException ex = (HttpException) throwable;
            return App.getContext().getString(R.string.server_error);

        }
        if (throwable instanceof IOException) {
            return App.getContext().getString(R.string.check_internet_connection);
        } else {
            return App.getContext().getString(R.string.undefined_error);
        }

    }
}
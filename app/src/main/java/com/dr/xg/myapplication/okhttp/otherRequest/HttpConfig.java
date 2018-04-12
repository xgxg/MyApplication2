package com.dr.xg.myapplication.okhttp.otherRequest;

import android.util.Log;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 黄冬榕
 * @date 2018/3/27
 * @description
 * @remark
 */

public class HttpConfig {
    public static String ADMIN_SERVER_IP = "http://test.dognesstech.com:8080/";
    private Retrofit retrofit;
    private OkHttpClient mOkHttpClient;

    public void init() {
//        Interceptor interceptor = new Interceptor() {
//
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                Request request = chain.request()
//                        .newBuilder()
//                        .addHeader("token", "")
//                        .addHeader("version", "android31")
//                        .build();
//                return chain.proceed(request);
//            }
//        };

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
//                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS);//设置写入超时时间
        mOkHttpClient = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://test.dognesstech.com:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
    }

    public void request() {
//        CountryService apiService = retrofit.create(CountryService.class);
//        Observable<MyResponse> observable = apiService.login("en");
//        observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        new Subscriber<MyResponse>() {
//                            @Override
//                            public void onCompleted() {
//                                Log.e("onCompleted","cg");
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Log.e("onError",e.getMessage());
//                            }
//
//                            @Override
//                            public void onNext(MyResponse retrofitEntity) {
//                                Log.e("onNext","123");
//                                Log.e("onNext",retrofitEntity.getData().get(0).getName());
//                            }
//                        }
//
//                );
    }
}

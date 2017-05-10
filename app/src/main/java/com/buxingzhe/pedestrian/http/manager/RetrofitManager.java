package com.buxingzhe.pedestrian.http.manager;

import com.buxingzhe.lib.util.FileUtil;
import com.buxingzhe.lib.util.Log;
import com.buxingzhe.lib.util.NetUtil;
import com.buxingzhe.pedestrian.PDConfig;
import com.buxingzhe.pedestrian.http.NetRequestParams;
import com.buxingzhe.pedestrian.http.apiservice.NetRequestService;
import com.buxingzhe.pedestrian.http.factory.strings.StringConverterFactory;
import com.buxingzhe.pedestrian.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by zhaishaoping on 29/03/2017.
 */
public class RetrofitManager {
    /** 设置缓存有效期为2天*/
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    /** http 缓存最大值*/
    private static final long CACHE_MAX_SIZE = 1024 * 1024 * 100;

    public static RetrofitManager getInstance() {
        return SingleHolder.sInstance;
    }

    private static class SingleHolder {
        private static final RetrofitManager sInstance = new RetrofitManager();
    }

    private RetrofitManager() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(NetRequestParams.WALK_SERVER_HOST)
                .client(getOkHttpClient())
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
//                .addConverterFactory(JsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }



    private Retrofit mRetrofit;
    private static volatile OkHttpClient mOkHttpClient;
    private NetRequestService mNetRequestService;

    public NetRequestService getNetRequestService() {
        return mRetrofit.create(NetRequestService.class);
    }

    private OkHttpClient getOkHttpClient() {
        if(mOkHttpClient == null){
            synchronized (RetrofitManager.this){
                Cache cache = new Cache(FileUtil.getCacheDir(PDConfig.getInstance().getContext(),"HttpCache"),CACHE_MAX_SIZE);
                if(mOkHttpClient == null){
                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mCookiesInterceptor)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mLoggingInterceptor)
                            .build();
                }
            }
        }
        return mOkHttpClient;
    }

    /** 云端响应头拦截器，用来配置缓存策略*/
    private static final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isNetWorkConnectted(PDConfig.getInstance().getContext())){
                request = request.newBuilder()
                        // .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                Log.d("no network");
            }

            Response originalResponse = chain.proceed(request);
            if(NetUtil.isNetWorkConnectted(PDConfig.getInstance().getContext())){
                //用网的时候读取接口上的 @Headers里的配置， 你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control",cacheControl)
                        .removeHeader("Pragma")
                        .build();
            }else{
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    private static final Interceptor mLoggingInterceptor = new Interceptor(){

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Log.i(String.format("Sending request %s on %s%n%s",request.url(),chain.connection(),request.headers()));

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            Log.i(String.format(Locale.getDefault(),"Received response for %s in %.1fms%n%s",
                    response.request().url(),(t2-t1)/1e6d,response.headers()));
            return response;
        }
    };


    private static final Interceptor mCookiesInterceptor = new  Interceptor(){

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                final StringBuffer cookieBuffer = new StringBuffer();
                Observable.from(originalResponse.headers("Set-Cookie"))
                        .map(new Func1<String, String>() {
                            @Override
                            public String call(String s) {
                                String[] cookieArray = s.split(";");
                                return cookieArray[0];
                            }
                        })
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String cookie) {
                                cookieBuffer.append(cookie).append(";");
                            }
                        });
                SharedPreferencesUtil.getInstance().getSharedPreferences(PDConfig.getInstance().getContext())
                        .edit()
                        .putString("cookie",cookieBuffer.toString())
                        .commit();
            }
            return originalResponse;
        }
    };

}

package com.bolnizar.code.data.api;

import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.bolnizar.code.BuildConfig;
import com.bolnizar.code.ShaormApp;
import com.bolnizar.code.di.scopes.ApplicationScope;
import com.bolnizar.code.utils.Constants;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

@Module
public class ApiModule {

    @Provides
    @ApplicationScope
    Cache provideCache(ShaormApp app) {
        File httpCacheDirectory = new File(app.getCacheDir().getAbsolutePath(), "HttpCache");
        return new Cache(httpCacheDirectory, Constants.DISK_CACHE_SIZE);
    }

    @Provides
    @ApplicationScope
    HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return interceptor;
    }

    @Provides
    @ApplicationScope
    OkHttpClient provideOkHttpClient(Cache cache,
                                     HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @ApplicationScope
    CallAdapter.Factory provideCallAdapterFactory() {
        return RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
    }

    @Provides
    @ApplicationScope
    @VisibleForTesting
    static Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

    @Provides
    @ApplicationScope
    Converter.Factory provideConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @ApplicationScope
    Retrofit provideRetrofit(final OkHttpClient okHttpClient,
                             final Converter.Factory converter,
                             final CallAdapter.Factory adapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(Constants.ENDPOINT)
                .addCallAdapterFactory(adapterFactory)
                .addConverterFactory(converter)
                .client(okHttpClient)
                .build();
    }
}
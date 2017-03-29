package com.bolnizar.code.di;

import android.content.Context;
import android.net.ConnectivityManager;

import com.bolnizar.code.ShaormApp;
import com.bolnizar.code.di.scopes.ApplicationScope;
import com.bolnizar.code.utils.SystemUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Context mAppContext;

    public ApplicationModule(Context appContext) {
        mAppContext = appContext.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    ShaormApp providesConferenceApplication() {
        return (ShaormApp) mAppContext;
    }

    @Provides
    @ApplicationScope
    ConnectivityManager provideConnectivityManager() {
        return (ConnectivityManager) mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @ApplicationScope
    SystemUtils provideSystemUtils(ConnectivityManager connectivityManager) {
        return new SystemUtils(connectivityManager);
    }

}
package com.bolnizar.code.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.bolnizar.code.ShaormApp;
import com.bolnizar.code.R;
import com.bolnizar.code.di.scopes.ApplicationScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class AppPrefsModule {

    @Provides
    @ApplicationScope
    @AppPrefs
    SharedPreferences provideSharedPreferences(ShaormApp app) {
        return app.getSharedPreferences(app.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    @Provides
    @ApplicationScope
    @Named(AppPrefsConstants.USER_TOKEN)
    StringPreference provideLoginToken(@AppPrefs SharedPreferences sharedPreferences) {
        return new StringPreference(sharedPreferences, AppPrefsConstants.USER_TOKEN);
    }
}
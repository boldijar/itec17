package com.bolnizar.code;

import com.bolnizar.code.di.ApplicationComponent;
import com.bolnizar.code.di.ApplicationModule;
import com.bolnizar.code.di.DaggerApplicationComponent;
import com.facebook.FacebookSdk;
import com.orm.SugarApp;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ShaormApp extends SugarApp {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        buildGraphAndInject();
        initCalligraphy();
        initTimber();
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    private void buildGraphAndInject() {
        final ApplicationModule applicationModule = new ApplicationModule(this);
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(applicationModule)
                .build();
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}

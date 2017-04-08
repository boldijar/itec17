package com.bolnizar.code.di;

import com.bolnizar.code.ShaormApp;
import com.bolnizar.code.data.api.ApiModule;
import com.bolnizar.code.data.prefs.AppPrefsConstants;
import com.bolnizar.code.data.prefs.AppPrefsModule;
import com.bolnizar.code.data.prefs.StringPreference;
import com.bolnizar.code.di.scopes.ApplicationScope;
import com.bolnizar.code.pages.map.BackendService;
import com.bolnizar.code.pages.map.MapActivity;
import com.bolnizar.code.utils.SystemUtils;

import javax.inject.Named;

import dagger.Component;
import retrofit2.Retrofit;

@ApplicationScope
@Component(modules = {ApplicationModule.class, AppPrefsModule.class, ApiModule.class})
public interface ApplicationComponent {

    void inject(ShaormApp shaormApp);

    SystemUtils provideSystemUtils();

    Retrofit provideRetrofit();

    @Named(AppPrefsConstants.USER_TOKEN)
    StringPreference provideUserToken();

    BackendService provideBackendService();

    void inject(MapActivity mapActivity);
}
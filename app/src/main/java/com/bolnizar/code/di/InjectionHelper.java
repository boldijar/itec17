package com.bolnizar.code.di;

import com.bolnizar.code.ShaormApp;
import com.bolnizar.code.pages.intro.login.LoginComponent;

import android.content.Context;

public final class InjectionHelper {

//    private static LoginComponent sLoginComponent;

    private static ApplicationComponent getApplicationComponent(Context context) {
        return ((ShaormApp) context.getApplicationContext()).getApplicationComponent();
    }

//    public static LoginComponent getLoginComponent(Context context) {
//        if (sLoginComponent == null) {
//            sLoginComponent = DaggerLoginComponent.builder()
//                    .applicationComponent(getApplicationComponent(context))
//                    .build();
//        }
//        return sLoginComponent;
//    }
//
//    public static void destroyIntroComponents() {
//        sLoginComponent = null;
//    }
}
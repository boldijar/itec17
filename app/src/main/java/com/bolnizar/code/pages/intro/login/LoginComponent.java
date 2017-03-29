package com.bolnizar.code.pages.intro.login;

import com.bolnizar.code.di.ApplicationComponent;
import com.bolnizar.code.di.scopes.ActivityScope;

import dagger.Component;

//@ActivityScope
//@Component(dependencies = ApplicationComponent.class, modules = LoginModule.class)
public interface LoginComponent {
    void inject(LoginFragment loginFragment);
}

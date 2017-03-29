package com.bolnizar.code.di.scopes;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}
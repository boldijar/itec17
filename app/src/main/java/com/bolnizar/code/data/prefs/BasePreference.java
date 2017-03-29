package com.bolnizar.code.data.prefs;

import android.content.SharedPreferences;

abstract class BasePreference<T> {

    final SharedPreferences mPreferences;
    final String mKey;
    final T mDefaultValue;

    BasePreference(SharedPreferences preferences, String key, T defaultValue) {
        mPreferences = preferences;
        mKey = key;
        mDefaultValue = defaultValue;
    }

    public boolean isSet() {
        return mPreferences.contains(mKey);
    }

    public void delete() {
        mPreferences.edit().remove(mKey).apply();
    }

    public abstract T get();

    public abstract void set(T value);
}
package com.bolnizar.code.data.prefs;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public class StringPreference extends BasePreference<String> {

    StringPreference(@NonNull SharedPreferences preferences, @NonNull String key) {
        super(preferences, key, null);
    }

    @Override
    public String get() {
        return mPreferences.getString(mKey, mDefaultValue);
    }

    @Override
    public void set(String value) {
        if (!TextUtils.isEmpty(value)) {
            mPreferences.edit().putString(mKey, value).apply();
        }
    }
}
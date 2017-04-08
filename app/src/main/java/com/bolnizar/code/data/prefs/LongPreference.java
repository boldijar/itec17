package com.bolnizar.code.data.prefs;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class LongPreference extends BasePreference<Long> {

    LongPreference(@NonNull SharedPreferences preferences, @NonNull String key) {
        super(preferences, key, null);
    }

    @Override
    public Long get() {
        return mPreferences.getLong(mKey, -1);
    }

    @Override
    public void set(Long value) {
        mPreferences.edit().putLong(mKey, value).apply();
    }
}
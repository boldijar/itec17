package com.bolnizar.code.view.activities;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

import com.bolnizar.code.R;

public abstract class BaseFragmentActivity extends BaseActivity {

    protected void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(getContainerId(), fragment, fragment.getTag())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    protected void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(getContainerId(), fragment, fragment.getTag())
                .commit();
    }

    protected abstract
    @IdRes
    int getContainerId();

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
            return;
        }
        super.onBackPressed();
    }
}

package com.bolnizar.code.pages.intro;

import android.content.Context;
import android.support.annotation.StringRes;

import com.bolnizar.code.utils.ActivityUtils;
import com.bolnizar.code.view.fragments.BaseFragment;

public class BaseIntroFragment extends BaseFragment {

    protected IntroNavigation mIntroNavigation;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof IntroNavigation)) {
            throw new RuntimeException("Activity must implement IntroNavigation");
        }
        mIntroNavigation = (IntroNavigation) context;
    }

    public void showError(@StringRes int errorId) {
        ActivityUtils.showError(getContext(), errorId);
    }

    public void showError(String errorText) {
        ActivityUtils.showError(getContext(), errorText);
    }

}

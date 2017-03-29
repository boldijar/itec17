package com.bolnizar.code.view.fragments;

import com.bolnizar.code.view.activities.BaseFragmentActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    protected Unbinder mUnbinder;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getTitle() != 0) {
            getActivity().setTitle(getTitle());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder == null) {
            return;
        }
        mUnbinder.unbind();
    }

    public
    @StringRes
    int getTitle() {
        return 0;
    }

    private BaseFragmentActivity getFragmentActivity() {
        if (!(getActivity() instanceof BaseFragmentActivity)) {
            throw new RuntimeException("You must use BaseFragmentActivity when using BaseFragment!");
        }
        return (BaseFragmentActivity) getActivity();
    }

}

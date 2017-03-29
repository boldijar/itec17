package com.bolnizar.code.pages.intro.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bolnizar.code.R;
import com.bolnizar.code.di.InjectionHelper;
import com.bolnizar.code.pages.intro.BaseIntroFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment   {
/*
    private String mUsername, mPassword;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.login_username)
    EditText mUsernameEditText;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.login_password)
    EditText mPasswordEditText;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.login_progress)
    View mProgressView;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.login_button)
    Button mLoginButton;

    @SuppressWarnings("WeakerAccess")
    @Inject
    LoginPresenter mLoginPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        InjectionHelper.getLoginComponent(getContext()).inject(this);
        mLoginPresenter.init(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        checkExistingCredentials();
    }

    private void checkExistingCredentials() {
        if (mUsername != null) {
            mUsernameEditText.setText(mUsername);
            mUsername = null;
        }
        if (mPassword != null) {
            mPasswordEditText.setText(mPassword);
            mPassword = null;
        }
    }

    @OnClick(R.id.login_button)
    void onLogin() {
        mLoginPresenter.tryLoggingIn(mUsernameEditText.getText().toString(),
                mPasswordEditText.getText().toString());
    }



    public void setCredentials(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    @Override
    public void showProgress(boolean show) {
        mLoginButton.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLoginPresenter.destroy();
    }*/
}

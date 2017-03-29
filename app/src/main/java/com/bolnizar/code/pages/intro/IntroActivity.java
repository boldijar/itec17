package com.bolnizar.code.pages.intro;

import com.bolnizar.code.R;
import com.bolnizar.code.view.activities.BaseFragmentActivity;

import android.os.Bundle;

import butterknife.ButterKnife;

public class IntroActivity extends BaseFragmentActivity implements IntroNavigation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
    }

    @Override
    protected int getContainerId() {
        return R.id.intro_container;
    }

}

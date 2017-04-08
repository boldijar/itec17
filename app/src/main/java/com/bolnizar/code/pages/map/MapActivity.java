package com.bolnizar.code.pages.map;

import com.bolnizar.code.R;
import com.bolnizar.code.di.InjectionHelper;
import com.bolnizar.code.pages.gallery.GalleryFragment;
import com.bolnizar.code.pages.statistics.StatisticsFragment;
import com.bolnizar.code.view.activities.BaseFragmentActivity;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapActivity extends BaseFragmentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.map_login)
    TextView mLoginText;
    @BindView(R.id.profile_image)
    ImageView mImage;
    private ActionBarDrawerToggle mDrawerToggle;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        InjectionHelper.getApplicationComponent(this).inject(this);
        setSupportActionBar(mToolbar);
        startService(new Intent(this, MyLocationService.class));
        initDrawer();
        switchFragment(new PathsMapFragment());
        initFb();
    }

    private void initFb() {
        if (AccessToken.getCurrentAccessToken() != null) {
            mLoginText.setText(R.string.fb_logout);
            Glide.with(this).load("http://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?type=large").into(mImage);
        } else {
            mLoginText.setText(R.string.login_with_facebook);
        }
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loggedIn(loginResult);

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MapActivity.this, R.string.logincancel, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(MapActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loggedIn(LoginResult loginResult) {
        Glide.with(this).load("http://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?type=square").into(mImage);
        mLoginText.setText(R.string.fb_logout);
    }

    private void loggedOut() {
        LoginManager.getInstance().logOut();
        mLoginText.setText(R.string.login_with_facebook);
        mImage.setImageResource(R.drawable.user);
    }

    @OnClick(R.id.map_login)
    void loginOrLogout() {
        mDrawerLayout.closeDrawer(Gravity.START);
        if (AccessToken.getCurrentAccessToken() == null) {
            // want to login
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        } else {
            loggedOut();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, R.string.app_name) {

        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.map_home)
    void homeClicked() {
        mDrawerLayout.closeDrawer(Gravity.START);
        Fragment currentFragment = getTopFragment();
        if (currentFragment instanceof PathsMapFragment) {
            return;
        }
        switchFragment(new PathsMapFragment());
    }

    @OnClick(R.id.map_stats)
    void statsClicked() {
        mDrawerLayout.closeDrawer(Gravity.START);
        Fragment currentFragment = getTopFragment();
        if (currentFragment instanceof StatisticsFragment) {
            return;
        }
        switchFragment(new StatisticsFragment());
    }

    @OnClick(R.id.map_gallery)
    void galleryClicked() {
        mDrawerLayout.closeDrawer(Gravity.START);
        Fragment currentFragment = getTopFragment();
        if (currentFragment instanceof GalleryFragment) {
            return;
        }
        switchFragment(new GalleryFragment());
    }

    protected Fragment getTopFragment() {
        return getSupportFragmentManager().findFragmentById(getContainerId());
    }

    @Override
    protected int getContainerId() {
        return R.id.content_frame;
    }
}

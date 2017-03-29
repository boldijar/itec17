package com.bolnizar.code.utils;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class ActivityUtils {

    public static void clearFullBackStack(AppCompatActivity appCompatActivity) {
        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            // clear the fragment back stack
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStackImmediate(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public static void showError(Context context, int errorId) {
        Toast.makeText(context, errorId, Toast.LENGTH_SHORT).show();
    }

    public static void showError(Context context, String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }

    public static void showSuccess(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

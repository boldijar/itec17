package com.bolnizar.code.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

public class TextUtils {

    private static Pattern sUsernamePattern;

    private static Pattern getUsernamePattern() {
        if (sUsernamePattern == null) {
            sUsernamePattern = Pattern.compile("^[a-zA-Z0-9._]+$");
        }
        return sUsernamePattern;
    }

    public static boolean isEmailInvalid(String email) {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isUsernameInvalidCorrect(String username) {
        if (username == null) {
            return true;
        }
        return !getUsernamePattern().matcher(username).matches();
    }
}
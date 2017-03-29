package com.bolnizar.code.utils;

import android.util.Patterns;

import com.bolnizar.code.data.api.responses.BaseResponse;

import java.util.Set;
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

    public static String extractFirstError(BaseResponse baseResponse) {
        if (baseResponse == null || baseResponse.errors == null || baseResponse.errors.size() == 0) {
            return null;
        }
        Set<String> keys = baseResponse.errors.keySet();
        for (String key : keys) {
            String[] errors = baseResponse.errors.get(key);
            if (errors != null && errors.length > 0) {
                return errors[0];
            }
        }
        return null;
    }
}
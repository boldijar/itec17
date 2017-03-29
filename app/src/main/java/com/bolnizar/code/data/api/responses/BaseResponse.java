package com.bolnizar.code.data.api.responses;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class BaseResponse {

    @SerializedName("success")
    public boolean success;

    @SerializedName("message")
    public String message;

    @SerializedName("errors")
    public HashMap<String, String[]> errors;

}

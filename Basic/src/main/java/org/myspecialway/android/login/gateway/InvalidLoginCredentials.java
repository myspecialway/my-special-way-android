package org.myspecialway.android.login.gateway;

import android.content.Context;

import org.myspecialway.android.R;

public class InvalidLoginCredentials extends Exception {

    public InvalidLoginCredentials(){
        super("Invalid username or password");
    }

    public String getUserShownMessage(Context context){
        return context.getString(R.string.invalid_login);
    }
}

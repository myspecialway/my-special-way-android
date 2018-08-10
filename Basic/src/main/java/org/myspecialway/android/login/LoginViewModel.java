package org.myspecialway.android.login;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.myspecialway.android.MswApplication;
import org.myspecialway.android.session.UserSession;

public class LoginViewModel extends ViewModel {

    public void login(@NonNull String username, @NonNull String password, @NonNull RequestCallback<UserSession> callback){

        MswApplication.getInstance().getUserSessionManager().login(username, password, callback);
    }
}

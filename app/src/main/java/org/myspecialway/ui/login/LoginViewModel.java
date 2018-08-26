package org.myspecialway.ui.login;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.myspecialway.App;
import org.myspecialway.session.UserSession;

public class LoginViewModel extends ViewModel {

    public void login(@NonNull String username, @NonNull String password, @NonNull RequestCallback<UserSession> callback){

        App.Companion.getInstance().getUserSessionManager().login(username, password, callback);
    }
}

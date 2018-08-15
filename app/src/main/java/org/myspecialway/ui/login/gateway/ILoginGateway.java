package org.myspecialway.ui.login.gateway;

import android.support.annotation.NonNull;

import org.myspecialway.ui.login.RequestCallback;

public interface ILoginGateway {

    void login(@NonNull String username, @NonNull String password, @NonNull final RequestCallback<String> callback);
}

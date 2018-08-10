package org.myspecialway.android.login.gateway;

import android.support.annotation.NonNull;

import org.myspecialway.android.login.RequestCallback;

public interface ILoginGateway {

    void login(@NonNull String username, @NonNull String password, @NonNull final RequestCallback<String> callback);
}

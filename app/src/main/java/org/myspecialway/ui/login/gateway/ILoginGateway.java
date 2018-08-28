package org.myspecialway.ui.login.gateway;

import android.support.annotation.NonNull;

public interface ILoginGateway {

    void login(@NonNull String username, @NonNull String password, @NonNull final RequestCallback<String> callback);
}

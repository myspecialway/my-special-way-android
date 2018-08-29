package org.myspecialway.ui.login.gateway;

public interface RequestCallback<T> {

    void onSuccess(T result);

    void onFailure(Throwable t);
}

package org.myspecialway.ui.login;

public interface RequestCallback<T> {

    void onSuccess(T result);

    void onFailure(Throwable t);
}

package org.myspecialway.login;

public interface RequestCallback<T> {

    void onSuccess(T result);

    void onFailure(Throwable t);
}

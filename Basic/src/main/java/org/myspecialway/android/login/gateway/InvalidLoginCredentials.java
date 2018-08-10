package org.myspecialway.android.login.gateway;

public class InvalidLoginCredentials extends Exception {

    public InvalidLoginCredentials(){
        super("Invalid username or password");
    }
}

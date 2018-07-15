package org.myspecialway.android;

public class UserDataRepository {

    UserData userData = new UserData("User1");
//
//    public UserDataRepository(UserData userData) {
//        this.userData = userData;
//    }

    public UserData getUserData() {
        return userData;
    }
}

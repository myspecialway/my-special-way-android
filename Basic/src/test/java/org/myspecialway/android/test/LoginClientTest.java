package org.myspecialway.android.test;

import org.junit.Assert;
import org.junit.Test;
import org.myspecialway.android.login.LoginClient;

public class LoginClientTest {

    @Test
    public void testValidLogin(){

        LoginClient loginClient = new LoginClient();
        loginClient.login("msw-teacher","Aa123456" );
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(loginClient.getLoginAccessToken().getAccessToken());
    }
}

package org.myspecialway.ui.utils;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import static org.myspecialway.ui.utils.Utils.doLogin;
import static org.myspecialway.ui.utils.Utils.logOut;

/**
 * Created by dr9081 on 10/9/2018.
 */

public class BaseClazz {


    @BeforeClass
    public static void startUp() {
        // in case need to perform login
        doLogin();



    }


    @AfterClass
    public static  void tearDown(){

        logOut();
    }



}

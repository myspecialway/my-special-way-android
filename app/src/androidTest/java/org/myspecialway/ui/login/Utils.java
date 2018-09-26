package org.myspecialway.ui.login;

import android.app.Activity;
import android.preference.PreferenceManager;

public class Utils {

    public static void clearPreferences(Activity activity) {
        PreferenceManager.getDefaultSharedPreferences(activity).edit().clear().apply();
    }

}

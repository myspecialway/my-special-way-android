package org.myspecialway.ui.settings

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import org.myspecialway.App
import org.myspecialway.R
import org.myspecialway.common.Navigation
import org.myspecialway.common.logout
import org.myspecialway.ui.login.showLogoutDialog
import org.myspecialway.ui.navigation.NavigationLocationsActivity

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        fun newInstance(locations : String) : SettingsFragment{
            val settingsFragment = SettingsFragment()

            val arg = Bundle()
            arg.putString(NavigationLocationsActivity.LOCATIONS_PAYLOAD_KEY, locations)

            settingsFragment.arguments = arg
            return settingsFragment
        }
    }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val navigationCategory = findPreference("navigation_category")
        val logout = findPreference("logout")
        val navigation = findPreference("navigation")


        logout.setOnPreferenceClickListener {
            showLogoutDialog {
                approveButtonClickListener {
                    App.instance?.applicationContext?.logout()
                }

                cancelButtonClickListener {
                    dialog?.dismiss()
                }
            }.show()
             true;
        }

        val args = arguments
        val navLocations = args?.getString(NavigationLocationsActivity.LOCATIONS_PAYLOAD_KEY, "")

        if(navLocations == null || navLocations.isEmpty()){
            navigationCategory.isVisible = false
            return
        }

        navigation.setOnPreferenceClickListener {
            if(context !=null){
                Navigation.toNavigationLocationsActivity(context!!, navLocations)
            }
            true;
        }
    }
}
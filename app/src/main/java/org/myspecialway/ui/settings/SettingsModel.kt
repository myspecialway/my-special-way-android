package org.myspecialway.ui.settings

import android.content.SharedPreferences
import org.myspecialway.ui.agenda.TEACHER_CODE

data class Settings(var teachercode: Int)
data class SettingsData(var settings: List<Settings>)
data class SettingsResponse(var data: SettingsData)


data class SettingsModel(var teacherCode: Int) {

    fun storeSettings(sp: SharedPreferences, settingsModel: SettingsModel) =
            sp.edit().apply {
                putInt(TEACHER_CODE, settingsModel.teacherCode)
            }.apply()

    fun getTeacherCode(preferences: SharedPreferences, allowUsingCache: Boolean): Int {
        if (allowUsingCache && preferences.contains(TEACHER_CODE)) {
            val teacherCode = preferences.getInt(TEACHER_CODE, 0)
            return teacherCode
        }

        return 1

    }

}
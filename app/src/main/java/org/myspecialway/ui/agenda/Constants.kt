package org.myspecialway.ui.agenda

fun query(token: String) = "{student(id:\"$token\"){reminders{ enabled type schedule{daysindex hours}} schedule{index hours lesson{title icon} location{name location_id position{floor}}} nonActiveTimes{title isAllDayEvent startDateTime endDateTime isAllClassesEvent}}}"
fun locationQuery() = "{locations{name location_id}}"
fun settingsQuery() = "{ settings { teachercode } }"

var USER_MODEL = "user_model"
var TOKEN_MODEL = "token_model"
var TEACHER_CODE = "teacher_code"
const val EMPTY_TEXT = ""
const val FIRST_TIME = "first_time_launch"

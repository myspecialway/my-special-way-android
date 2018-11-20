package org.myspecialway.ui.agenda

fun query(token: String) = "{student(id:\"$token\"){schedule{index lesson{title icon} location{name location_id disabled  position {  latitude  longitude floor } }}}}"
fun locationQuery() = "{ locations { name disabled location_id  }  }"

var USER_MODEL = "user_model"
var TOKEN_MODEL = "token_model"
const val EMPTY_TEXT = ""

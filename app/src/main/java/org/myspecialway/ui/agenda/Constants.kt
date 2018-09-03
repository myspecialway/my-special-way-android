package org.myspecialway.ui.agenda

fun query(token: String) = "query{student(id:\"$token\"){schedule{index lesson{title icon}location{name  disabled  position {  latitude  longitude floor } }}}}"
var USER_MODEL = "user_model"
var TOKEN_MODEL = "token_model"

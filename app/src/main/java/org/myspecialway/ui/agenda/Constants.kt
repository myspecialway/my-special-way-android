package org.myspecialway.ui.agenda

fun query(token: String) = "query{student(id:\"$token\"){schedule{index lesson{title icon}location{name  disabled  position {  latitude  longitude floor } }}}}"

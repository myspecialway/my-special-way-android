package org.myspecialway.ui.agenda

import org.myspecialway.App
import org.myspecialway.session.UserSessionManager


fun query(token: String) = "query{student(id:\"$token\"){schedule{index lesson{title icon}location{name  disabled  position {  latitude  longitude floor } }}}}"

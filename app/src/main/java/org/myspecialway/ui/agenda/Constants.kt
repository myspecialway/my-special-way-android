package org.myspecialway.ui.agenda

import org.myspecialway.App
import org.myspecialway.session.UserSessionManager


fun query() =  "query{student(id:\"${App.instance?.userSessionManager?.userData?.id}\"){schedule{index lesson{title icon}location{name}}}}"

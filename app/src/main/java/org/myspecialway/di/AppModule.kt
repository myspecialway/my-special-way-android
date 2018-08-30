package org.myspecialway.di

import android.content.Context
import android.preference.PreferenceManager
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext
import org.myspecialway.common.ApplicationSchedulerProvider
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.session.Token
import org.myspecialway.ui.notifications.NotificationAlarmManager
import org.myspecialway.ui.agenda.AgendaRepository
import org.myspecialway.ui.agenda.AgendaRepositoryImpl
import org.myspecialway.ui.agenda.AgendaViewModel
import org.myspecialway.ui.login.LoginRepository
import org.myspecialway.ui.login.LoginRepositoryImpl
import org.myspecialway.ui.login.LoginViewModel
import org.myspecialway.utils.TokenParser

val appModule = applicationContext {

    viewModel { LoginViewModel(get(), get()) }
    bean { LoginRepositoryImpl(get(), get()) as LoginRepository }
    bean { TokenParser() }
    bean { Token() }
    bean { getSharedPrefs(get()) }

    viewModel { AgendaViewModel(get(), get()) }
    bean { AgendaRepositoryImpl(get(), get(), get()) as AgendaRepository }
    context("alarms") { bean { NotificationAlarmManager(get()) } }


}

private fun getSharedPrefs(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)

val rxModule = applicationContext {
    bean { ApplicationSchedulerProvider() as SchedulerProvider }
}

val mySpecialWay = listOf(remoteDataSourceModel, localDataSourceModule, rxModule, appModule)
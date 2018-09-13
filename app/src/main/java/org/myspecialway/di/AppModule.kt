package org.myspecialway.di

import android.content.Context
import android.preference.PreferenceManager
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.myspecialway.common.ApplicationSchedulerProvider
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.session.Token
import org.myspecialway.ui.agenda.AgendaRepository
import org.myspecialway.ui.agenda.AgendaRepositoryImpl
import org.myspecialway.ui.agenda.AgendaViewModel
import org.myspecialway.ui.login.LoginRepository
import org.myspecialway.ui.login.LoginRepositoryImpl
import org.myspecialway.ui.login.LoginViewModel
import org.myspecialway.ui.notifications.NotificationAlarmManager
import org.myspecialway.utils.TokenParser

val appModule = module {

    viewModel { LoginViewModel(get(), get(), get()) }
    single { LoginRepositoryImpl(get(), get()) as LoginRepository }
    single { TokenParser() }
    single { Token() }
    viewModel { AgendaViewModel(get(), get()) }
    single { AgendaRepositoryImpl(get(), get(), get()) as AgendaRepository }
    module("alarms") { single { NotificationAlarmManager(get()) } }
}



val rxModule = module {
    single { ApplicationSchedulerProvider() as SchedulerProvider }
}

val mySpecialWay = listOf(remoteDataSourceModel, localDataSourceModule, rxModule, appModule)
package org.myspecialway.di

import android.app.AlarmManager
import android.content.Context
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext
import org.myspecialway.common.ApplicationSchedulerProvider
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.notifications.Alarm
import org.myspecialway.ui.agenda.AgendaRepository
import org.myspecialway.ui.agenda.AgendaRepositoryImpl
import org.myspecialway.ui.agenda.AgendaViewModel

val appModule = applicationContext {
    viewModel { AgendaViewModel(get(), get()) }
    bean { AgendaRepositoryImpl(get(), get()) as AgendaRepository }

    context("alarm") {
        bean { Alarm(get() ) }
        
    }
}

private fun alarmService(context: Context) = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

val rxModule = applicationContext {
    bean { ApplicationSchedulerProvider() as SchedulerProvider }
}

val MySpecialWay = listOf(remoteDataSourceModel, localDataSourceModule, rxModule, appModule)
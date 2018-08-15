package org.myspecialway.di

import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext
import org.myspecialway.common.ApplicationSchedulerProvider
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.ui.agenda.AgendaRepository
import org.myspecialway.ui.agenda.AgendaRepositoryImpl
import org.myspecialway.ui.agenda.AgendaViewModel

val appModule = applicationContext {
    viewModel { AgendaViewModel(get(), get()) }

    bean { AgendaRepositoryImpl(get()) as AgendaRepository }

    // add local source

}

val rxModule = applicationContext {
    bean { ApplicationSchedulerProvider() as SchedulerProvider }
}

val OnMyWayApp = listOf(remoteDataSourceModel, rxModule, appModule)
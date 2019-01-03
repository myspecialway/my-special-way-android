
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.myspecialway.common.ApplicationSchedulerProvider
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.di.localDataSourceModule
import org.myspecialway.di.remoteDataSourceModel
import org.myspecialway.session.Token
import org.myspecialway.ui.login.LoginRepository
import org.myspecialway.ui.login.LoginRepositoryImpl
import org.myspecialway.ui.login.LoginViewModel
import org.myspecialway.ui.settings.SettingsRepository
import org.myspecialway.ui.settings.SettingsRepositoryImpl
//import org.myspecialway.ui.notifications.NotificationAlarmManager
import org.myspecialway.ui.shared.AgendaRepository
import org.myspecialway.ui.shared.AgendaRepositoryImpl
import org.myspecialway.ui.shared.AgendaViewModel
import org.myspecialway.utils.TokenParser

val loginModule = module {
    viewModel { LoginViewModel(get(), get(), get()) }
    single { LoginRepositoryImpl(get(), get()) as LoginRepository }
    single { TokenParser() }
    single { Token() }

}

val agendaModule = module {
    viewModel { AgendaViewModel(get(), get()) }
    single { AgendaRepositoryImpl(get(), get(), get()) as AgendaRepository }
//    module("alarms") { single { NotificationAlarmManager(get()) } }
}

val settingsModule = module {
    single { SettingsRepositoryImpl(get()) as SettingsRepository }
}

val rxModule = module {
    single { ApplicationSchedulerProvider() as SchedulerProvider }
}

val mySpecialWay = listOf(
        remoteDataSourceModel,
        localDataSourceModule,
        rxModule,
        loginModule,
        agendaModule,
        settingsModule
)
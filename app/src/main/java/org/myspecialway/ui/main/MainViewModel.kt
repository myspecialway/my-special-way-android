package org.myspecialway.ui.main

import android.arch.lifecycle.MutableLiveData
import org.myspecialway.common.AbstractViewModel
import org.myspecialway.common.SchedulerProvider
import org.myspecialway.common.with

sealed class MainState
data class DataReady(val data : List<MappedClassesModel>) : MainState()

class MainViewModel(val repository: MainRepository, private val provider: SchedulerProvider) : AbstractViewModel() {
    val mainData = MutableLiveData<MainState>()

    init {
        getMappedClasses()
    }

    private fun getMappedClasses() = launch {
        repository.getSchoolMap()
                .with(provider)
                .subscribe ({
                    mainData.value = DataReady(it)
                }, {
                    failure(it)
                })
    }
}
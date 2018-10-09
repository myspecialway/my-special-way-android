package org.myspecialway.ui.main

import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable



interface MainRepository {
    fun getSchoolMap(): Single<List<MappedClassesModel>>
}

class MainRepositoryImpl(private val mockData: MockClassData) : MainRepository {
    override fun getSchoolMap(): Single<List<MappedClassesModel>> = mockData.data
}

// todo replace with real data from remote source
class MockClassData {
    val data = mutableListOf<MappedClassesModel>().apply {
        add(MappedClassesModel("פטל", "B1", true))
        add(MappedClassesModel("מוזקיה", "B1", false))
        add(MappedClassesModel("שירה", "B1", true))
        add(MappedClassesModel("ארנבת", "B1", true))
    }.toObservable().toList()
}
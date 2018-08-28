package org.myspecialway.ui.login

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single


val lengthGreaterThanFour = ObservableTransformer<String, String> { observable ->
    observable.flatMap {
        Observable.just(it).map { it.trim() }
                .filter { it.length > 4 }
                .singleOrError()
                .onErrorResumeNext {
                    if (it is NoSuchElementException) {
                        Single.error(Exception("Length should be greater than 4"))
                    } else {
                        Single.error(it)
                    }
                }
                .toObservable()
    }
}

inline fun retryWhenError(crossinline onError: (ex: Throwable) -> Unit):
        ObservableTransformer<String, String> = ObservableTransformer { observable ->
    observable.retryWhen { errors ->
        errors.flatMap {
            onError(it)
            Observable.just("")
        }
    }
}
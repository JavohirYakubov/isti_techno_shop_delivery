package uz.ferganagroup.arzonibizdadelivery.repository

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import uz.ferganagroup.arzonibizdadelivery.api.Api
import uz.ferganagroup.arzonibizdadelivery.api.Client

open class BaseRepository {

    val api = Client.retrofit.create(Api::class.java)

    val compositeDisposable = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable): Disposable {
        compositeDisposable.add(disposable)
        return disposable
    }
}
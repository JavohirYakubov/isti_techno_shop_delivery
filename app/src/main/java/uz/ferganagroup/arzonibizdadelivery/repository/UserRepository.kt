package uz.ferganagroup.arzonibizdadelivery.repository

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import uz.ferganagroup.arzonibizdadelivery.api.BaseResponse
import uz.ferganagroup.arzonibizdadelivery.api.CallbackWrapper
import uz.ferganagroup.arzonibizdadelivery.model.*
import uz.ferganagroup.arzonibizdadelivery.model.request.AcceptBronRequest
import uz.ferganagroup.arzonibizdadelivery.model.request.LoginRequest
import uz.ferganagroup.arzonibizdadelivery.model.request.RatingRequest
import uz.ferganagroup.arzonibizdadelivery.utils.Constants
import uz.ferganagroup.arzonibizdadelivery.utils.Prefs

class UserRepository: BaseRepository() {


    fun getOrders(progress: MutableLiveData<Boolean>, error: MutableLiveData<String>, data: MutableLiveData<List<OrderModel>>){
        compositeDisposable.add(api.getOrders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribeWith(object: CallbackWrapper<BaseResponse<List<OrderModel>?>>(error){
                override fun onSuccess(t: BaseResponse<List<OrderModel>?>) {
                    if (!t.error){
                        data.value = t.data
                    }else{
                        error.value = t.message ?: ""
                    }
                }
            })
        )
    }


    fun getMyOrders(progress: MutableLiveData<Boolean>, error: MutableLiveData<String>, data: MutableLiveData<List<OrderModel>>){
        compositeDisposable.add(api.getMyOrders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribeWith(object: CallbackWrapper<BaseResponse<List<OrderModel>?>>(error){
                override fun onSuccess(t: BaseResponse<List<OrderModel>?>) {
                    if (!t.error){
                        data.value = t.data
                    }else{
                        error.value = t.message ?: ""
                    }
                }
            })
        )
    }

    fun acceptOrder(id: String, progress: MutableLiveData<Boolean>, error: MutableLiveData<String>, data: MutableLiveData<OrderModel>){
        val request = AcceptBronRequest(id, Prefs.getToken())

        compositeDisposable.add(api.acceptBron(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribeWith(object: CallbackWrapper<BaseResponse<OrderModel?>>(error){
                override fun onSuccess(t: BaseResponse<OrderModel?>) {
                    if (!t.error){
                        data.value = t.data
                    }else{
                        error.value = t.message ?: ""
                    }
                }
            })
        )
    }

    fun onwayOrder(id: String, progress: MutableLiveData<Boolean>, error: MutableLiveData<String>, data: MutableLiveData<OrderModel>){
        val request = AcceptBronRequest(id, Prefs.getToken())

        compositeDisposable.add(api.onWayBron(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribeWith(object: CallbackWrapper<BaseResponse<OrderModel?>>(error){
                override fun onSuccess(t: BaseResponse<OrderModel?>) {
                    if (!t.error){
                        data.value = t.data
                    }else{
                        error.value = t.message ?: ""
                    }
                }
            })
        )
    }

    fun finishOrder(id: String, progress: MutableLiveData<Boolean>, error: MutableLiveData<String>, data: MutableLiveData<OrderModel>){
        val request = AcceptBronRequest(id, Prefs.getToken())

        compositeDisposable.add(api.finishBron(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribeWith(object: CallbackWrapper<BaseResponse<OrderModel?>>(error){
                override fun onSuccess(t: BaseResponse<OrderModel?>) {
                    if (!t.error){
                        data.value = t.data
                    }else{
                        error.value = t.message ?: ""
                    }
                }
            })
        )
    }

    fun updateLocation(lat: Double, long: Double){
        compositeDisposable.add(api.updateLocation(lat, long)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { }
            .doOnSubscribe {  }
            .subscribeWith(object: CallbackWrapper<BaseResponse<Any?>>(MutableLiveData()){
                override fun onSuccess(t: BaseResponse<Any?>) {
                    if (!t.error){

                    }else{

                    }
                }
            })
        )
    }

    fun getDeliveryInfo(){
        compositeDisposable.add(api.getDeliveryInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { }
            .doOnSubscribe {  }
            .subscribeWith(object: CallbackWrapper<BaseResponse<DeliveryInfoModel?>>(MutableLiveData()){
                override fun onSuccess(t: BaseResponse<DeliveryInfoModel?>) {
                    if (!t.error && t.data != null){
                        Prefs.setDelivery(t.data)
                    }else{

                    }
                }
            })
        )
    }
}
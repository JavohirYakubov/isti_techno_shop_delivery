package uz.ferganagroup.arzonibizdadelivery.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.ferganagroup.arzonibizdadelivery.api.BaseResponse
import uz.ferganagroup.arzonibizdadelivery.api.CallbackWrapper
import uz.ferganagroup.arzonibizdadelivery.model.LoginConfirmResponse
import uz.ferganagroup.arzonibizdadelivery.model.PhoneCheckResponse
import uz.ferganagroup.arzonibizdadelivery.model.UserSettings
import uz.ferganagroup.arzonibizdadelivery.model.request.CodeConfirmRequest
import uz.ferganagroup.arzonibizdadelivery.model.request.LoginRequest
import uz.ferganagroup.arzonibizdadelivery.utils.Prefs

class AuthRepository : BaseRepository(){
    fun login(phone: String, progress: MutableLiveData<Boolean>, error: MutableLiveData<String>, success: MutableLiveData<PhoneCheckResponse>){
        compositeDisposable.clear()
        compositeDisposable.add(api.login(phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribeWith(object: CallbackWrapper<BaseResponse<PhoneCheckResponse?>>(error){
                override fun onSuccess(t: BaseResponse<PhoneCheckResponse?>) {
                    if (!t.error){
                        success.value = t.data
                    }else{
                        error.value = t.message
                    }
                }
            })
        )
    }

    fun loginUser(login: String, password: String, progress: MutableLiveData<Boolean>, error: MutableLiveData<String>, success: MutableLiveData<UserSettings>){
        compositeDisposable.clear()
        compositeDisposable.add(api.loginUser(login, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribeWith(object: CallbackWrapper<BaseResponse<UserSettings?>>(error){
                override fun onSuccess(t: BaseResponse<UserSettings?>) {
                    if (!t.error){
                        success.value = t.data
                    }else{
                        error.value = t.message
                    }
                }
            })
        )
    }

    fun loginConfirm(phone: String, code: String, fullname: String?, districtId: String?, progress: MutableLiveData<Boolean>, error: MutableLiveData<String>, data: MutableLiveData<LoginConfirmResponse?>){
        compositeDisposable.clear()
        val request = CodeConfirmRequest(phone, code, fullname, districtId)
        compositeDisposable.add(api.loginConfirm(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribeWith(object: CallbackWrapper<BaseResponse<LoginConfirmResponse?>>(error){
                override fun onSuccess(t: BaseResponse<LoginConfirmResponse?>) {
                    if (!t.error){
                        data.value =  t.data
                    }else{
                        error.value = t.message
                    }
                }
            })
        )
    }
}
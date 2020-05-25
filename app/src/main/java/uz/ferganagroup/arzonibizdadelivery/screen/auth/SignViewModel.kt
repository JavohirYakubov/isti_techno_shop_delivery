package uz.ferganagroup.arzonibizdadelivery.screen.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.ferganagroup.arzonibizdadelivery.model.LoginConfirmResponse
import uz.ferganagroup.arzonibizdadelivery.model.PhoneCheckResponse
import uz.ferganagroup.arzonibizdadelivery.model.UserSettings
import uz.ferganagroup.arzonibizdadelivery.repository.AuthRepository

class SignViewModel: ViewModel(){

    val authRepository = AuthRepository()

    val progress = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val loginResponse = MutableLiveData<UserSettings>()


    fun login(login: String, password: String){
        authRepository.loginUser(login, password, progress, error, loginResponse)
    }
}
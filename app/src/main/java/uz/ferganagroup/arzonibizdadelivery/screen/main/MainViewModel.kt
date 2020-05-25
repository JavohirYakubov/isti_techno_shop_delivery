package uz.ferganagroup.arzonibizdadelivery.screen.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.ferganagroup.arzonibizdadelivery.api.BaseResponse
import uz.ferganagroup.arzonibizdadelivery.model.*
import uz.ferganagroup.arzonibizdadelivery.model.request.RatingRequest
import uz.ferganagroup.arzonibizdadelivery.repository.UserRepository

class MainViewModel : ViewModel() {

    val userRepository = UserRepository()

    val error = MutableLiveData<String>()
    val progress = MutableLiveData<Boolean>()
    val updateProgress = MutableLiveData<Boolean>()
    val ordersData = MutableLiveData<List<OrderModel>>()
    val myOrdersData = MutableLiveData<List<OrderModel>>()
    val updateStatusData = MutableLiveData<OrderModel>()
    val deliveryInfoData = MutableLiveData<DeliveryInfoModel>()


    fun getOrders(){
        userRepository.getOrders(progress, error, ordersData)
    }

    fun getMyOrders(){
        userRepository.getMyOrders(updateProgress, error, myOrdersData)
    }

    fun acceptOrder(id: String){
        userRepository.acceptOrder(id, progress, error, updateStatusData)
    }

    fun onwayOrder(id: String){
        userRepository.onwayOrder(id, updateProgress, error, updateStatusData)
    }

    fun finishOrder(id: String){
        userRepository.finishOrder(id, updateProgress, error, updateStatusData)
    }

    fun updateLocation(lat: Double, long: Double){
        userRepository.updateLocation(lat, long)
    }

    fun getDeliveryInfo(){
        userRepository.getDeliveryInfo()
    }

}
package uz.ferganagroup.arzonibizdadelivery.api

import io.reactivex.Observable
import retrofit2.http.GET
import uz.ferganagroup.arzonibizdadelivery.model.ISTIBaseResponse
import java.util.*

interface ISTIService {
    @GET("load_config/Metro_Delux")
    fun getConfig(): Observable<ISTIBaseResponse<ISTICheckModel>>
}
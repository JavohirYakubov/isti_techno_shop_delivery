package uz.ferganagroup.arzonibizdadelivery.api

import io.reactivex.Observable
import retrofit2.http.*
import uz.ferganagroup.arzonibizdadelivery.model.*
import uz.ferganagroup.arzonibizdadelivery.model.request.AcceptBronRequest
import uz.ferganagroup.arzonibizdadelivery.model.request.CodeConfirmRequest
import uz.ferganagroup.arzonibizdadelivery.model.request.RatingRequest
import uz.ferganagroup.arzonibizdadelivery.utils.Prefs

interface Api {

    @GET("GetViloyatWithDist")
    fun getRegions(): Observable<BaseResponse<List<RegionModel>?>>

    @GET("SmsCheck")
    fun login(@Query("telephone") phone: String): Observable<BaseResponse<PhoneCheckResponse?>>

    @POST("RegistryClient")
    fun loginConfirm(@Body request: CodeConfirmRequest): Observable<BaseResponse<LoginConfirmResponse?>>

    @GET("DeliveryLog")
    fun loginUser(@Query("login") login: String, @Query("parol") parol: String): Observable<BaseResponse<UserSettings?>>

    @POST("ClientInfoEdit")
    fun getInfo(@Body request: ClientInfoRequest): Observable<BaseResponse<ClientInfoModel?>>

    @GET("GetCategory")
    fun getCategories(@Query("skladid") storeId: String = Prefs.getStore()?.id ?: ""): Observable<BaseResponse<List<CategoryModel>?>>

    @GET("GetBolim")
    fun getSections(@Query("catid") catId: String, @Query("skladid") storeId: String = Prefs.getStore()?.id ?: ""): Observable<BaseResponse<List<SectionModel>?>>

    @GET("GetBrend")
    fun getBrand(@Query("bolimid") bolimid: String,@Query("catid") catId: String, @Query("skladid") storeId: String = Prefs.getStore()?.id ?: ""): Observable<BaseResponse<List<BrandModel>?>>

    @GET("GetTovarByBrend")
    fun getProducts(@Query("brendid") brendId: String, @Query("skladid") storeId: String = Prefs.getStore()?.id ?: ""): Observable<BaseResponse<List<ProductModel>?>>

    @GET("GetTovarInfo")
    fun getProductById(@Query("id") id: String, @Query("skladid") storeId: String = Prefs.getStore()?.id ?: ""): Observable<BaseResponse<List<ProductModel>?>>

    @GET("GetTovar")
    fun getAllProducts(@Query("skladid") storeId: String = Prefs.getStore()?.id ?: ""): Observable<BaseResponse<List<ProductModel>?>>

    @GET("GetSkladInfo")
    fun getStoreInfo(@Query("id") id: String = Prefs.getStore()?.id ?: ""): Observable<BaseResponse<List<StoreModel>?>>

    @POST("PostBron")
    fun createOrder(@Body request: MakeOrderModel, @Query("token") token: String = Prefs.getToken()): Observable<BaseResponse<PostBronModel?>>

    @POST("PostTakeBronDel")
    fun acceptBron(@Body request: AcceptBronRequest): Observable<BaseResponse<OrderModel?>>

    @POST("PostOnWayDel")
    fun onWayBron(@Body request: AcceptBronRequest): Observable<BaseResponse<OrderModel?>>

    @POST("PostFinishDel")
    fun finishBron(@Body request: AcceptBronRequest): Observable<BaseResponse<OrderModel?>>

    @GET("PublicDeliveryList")
    fun getOrders(@Query("token") token: String = Prefs.getToken()): Observable<BaseResponse<List<OrderModel>?>>

    @GET("DeliveryList")
    fun getMyOrders(@Query("token") token: String = Prefs.getToken()): Observable<BaseResponse<List<OrderModel>?>>

    @GET("GetLocationDel")
    fun updateLocation(@Query("lat") lat: Double, @Query("long") long: Double, @Query("token") token: String = Prefs.getToken()): Observable<BaseResponse<Any?>>

    @GET("DeliverInfo")
    fun getDeliveryInfo(@Query("token") token: String = Prefs.getToken()): Observable<BaseResponse<DeliveryInfoModel?>>
}


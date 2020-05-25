package uz.ferganagroup.arzonibizdadelivery.utils

import com.orhanobut.hawk.Hawk
import uz.ferganagroup.arzonibizdadelivery.App
import uz.ferganagroup.arzonibizdadelivery.model.*

const val PREF_TOKEN = "pref_token"
const val PREF_USER_SETTINGS = "pref_user_settings"
const val PREF_FAVOURITE = "pref_favourites"
const val PREF_CART = "pref_cart"
const val PREF_CART_DATA = "pref_cart_data"
const val PREF_DISTRICT = "pref_district"
const val PREF_STORE = "pref_store"
const val PREF_DELIVERY = "pref_delivery"
const val PREF_LANG = "pref_lang"
const val PREF_FCM = "pref_fcm"

class Prefs {

    companion object{
        fun init(app: App){
            Hawk.init(app).build()
        }

        //d1cb7fe42c63d70cd6ad17475cbc78deab72f066
        fun getToken(): String{
            return Hawk.get(PREF_TOKEN, "")
        }

        fun setToken(token: String){
            Hawk.put(PREF_TOKEN, token)
        }

        fun getLang(): String{
            return Hawk.get(PREF_LANG, "en")
        }

        fun setLang(lang: String){
            Hawk.put(PREF_LANG, lang)
        }

        fun getDistrictId(): String{
            return Hawk.get(PREF_DISTRICT, "")
        }

        fun setDistrictId(value: String){
            Hawk.put(PREF_DISTRICT, value)
        }

        fun setStore(value: DistrictModel){
            Hawk.put(PREF_STORE, value)
        }

        fun getStore(): DistrictModel?{
            return Hawk.get(PREF_DELIVERY)
        }

        fun setDelivery(value: DeliveryInfoModel){
            Hawk.put(PREF_DELIVERY, value)
        }

        fun getDelivery(): DeliveryInfoModel?{
            return Hawk.get(PREF_DELIVERY)
        }

        fun setFCM(value: String){
            Hawk.put(PREF_FCM, value)
        }

        fun getFCM(): String?{
            return Hawk.get(PREF_FCM)
        }

        fun setClientInfo(user: UserSettings){
            Hawk.put(PREF_USER_SETTINGS, user)
        }

        fun getClientInfo(): UserSettings?{
            return Hawk.get(PREF_USER_SETTINGS)
        }

        fun getCartModel(): CartEventModel{
            return Hawk.get<CartEventModel>(PREF_CART_DATA, CartEventModel(Constants.EVENT_UPDATE_CART, 0, 0.0))
        }

        fun setCartModel(cart: CartEventModel){
            Hawk.put(PREF_CART_DATA, cart)
        }

        fun getCartList(): List<BasketModel> {
            return Hawk.get<List<BasketModel>>(PREF_CART, emptyList())
        }

        fun add2Cart(item: BasketModel){
            val items = getCartList().toMutableList()
            if (item.count == 0){
                var index = 0
                var removeIndex = -1
                items.forEach {
                    if (it.id == item.id){
                        removeIndex = index
                    }
                    index++
                }
                if (removeIndex > -1){
                    items.removeAt(removeIndex)
                }
            }else{
                var isHave = false
                items.forEach {
                    if (it.id == item.id){
                        it.count = item.count
                        isHave = true
                    }
                }

                if (!isHave){
                    items.add(0, item)
                }
            }
            Hawk.put(PREF_CART, items)
        }

        fun clearCart(){
            Hawk.put(PREF_CART, null)
        }


        fun getFavourites(): List<String>{
            return Hawk.get<List<String>>(PREF_FAVOURITE, emptyList())
        }

        fun setFavouriteItem(item: String, favourite: Boolean){
            val items = getFavourites().toMutableList()
            if (favourite){
                items.forEach {
                    if (item == it){
                        return
                    }
                }

                items.add(0, item)
            }else{
                items.remove(item)
            }

            Hawk.put(PREF_FAVOURITE, items.toList())
        }

        fun isFavourite(item: String): Boolean{
            getFavourites().forEach {
                if (item == it){
                    return true
                }
            }
            return false
        }

        fun clearAll(){
            Hawk.deleteAll()
        }

    }
}
package uz.ferganagroup.arzonibizdadelivery.model.enum

import com.google.gson.annotations.SerializedName

enum class OfferTypeEnum(val value: String){
    @SerializedName("link")
    LINK("link"),

    @SerializedName("product")
    PRODUCT("product")
}
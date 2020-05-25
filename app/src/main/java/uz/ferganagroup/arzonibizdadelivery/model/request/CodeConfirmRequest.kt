package uz.ferganagroup.arzonibizdadelivery.model.request

import com.google.gson.annotations.SerializedName

data class CodeConfirmRequest(
    @SerializedName("telephone")
    val phone: String,
    @SerializedName("sms")
    val code: String,
    @SerializedName("fio")
    val fullName: String?,
    @SerializedName("tuman")
    val districtId: String?,
    val gender: Int = 0,
    val chatId: String = "",
    val date: String = ""
)
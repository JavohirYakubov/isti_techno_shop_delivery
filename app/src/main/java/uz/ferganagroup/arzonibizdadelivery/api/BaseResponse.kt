package uz.ferganagroup.arzonibizdadelivery.api

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    val error: Boolean,
    val message: String?,
    val data: T
)
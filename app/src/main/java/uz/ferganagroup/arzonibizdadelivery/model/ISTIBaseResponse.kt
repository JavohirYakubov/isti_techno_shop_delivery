package uz.ferganagroup.arzonibizdadelivery.model

data class ISTIBaseResponse<T>(
    val error: Boolean,
    val message: String,
    val items: T
)
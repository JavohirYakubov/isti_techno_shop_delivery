package uz.ferganagroup.arzonibizdadelivery.model.request

data class LoginRequest(
    val phone: String,
    val code: String
)
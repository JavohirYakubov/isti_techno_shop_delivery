package uz.ferganagroup.arzonibizdadelivery.model.request

data class AcceptBronRequest(
    val refKey: String,
    val token: String
)
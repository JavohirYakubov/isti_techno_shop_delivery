package uz.ferganagroup.arzonibizdadelivery.model

data class CartEventModel(
    val event: Int,
    val count: Int,
    val totalAmount: Double
)
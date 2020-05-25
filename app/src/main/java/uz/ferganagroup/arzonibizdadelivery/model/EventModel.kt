package uz.ferganagroup.arzonibizdadelivery.model

data class EventModel<T>(
    val event: Int,
    val data: T
)
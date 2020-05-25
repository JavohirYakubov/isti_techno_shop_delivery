package uz.ferganagroup.arzonibizdadelivery.model

import java.io.Serializable

data class AddressModel(
    var address: String?,
    val lat: Double,
    val lon: Double,
    var additional: String = ""
): Serializable
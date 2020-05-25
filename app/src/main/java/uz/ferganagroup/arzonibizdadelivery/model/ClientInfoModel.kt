package uz.ferganagroup.arzonibizdadelivery.model

import java.io.Serializable

data class ClientInfoModel(
    val name: String,
    val phone: String,
    val tuman: String,
    val date: String,
    val jins: String,
    val token: String
): Serializable
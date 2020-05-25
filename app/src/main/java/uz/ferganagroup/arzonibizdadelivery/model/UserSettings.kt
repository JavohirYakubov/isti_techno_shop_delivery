package uz.ferganagroup.arzonibizdadelivery.model

import java.io.Serializable

data class UserSettings(
    val id: Int,
    val phone: String,
    val username: String?,
    val token: String,
    val coins: String,
    val province_id: Int?,
    val region_id: Int?,
    val district_id: Int?,
    val name: String?,
    val tel: String?
): Serializable
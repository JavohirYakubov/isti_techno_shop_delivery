package uz.ferganagroup.arzonibizdadelivery.model

data class ClientInfoRequest(
    val token: String = "",
    val telephone: String = "",
    val fio: String = "",
    val date: String = "",
    val gender: Int = 0,
    val chatId: String = "",
    val tuman: String = "",
    val fcm_token: String = ""
)
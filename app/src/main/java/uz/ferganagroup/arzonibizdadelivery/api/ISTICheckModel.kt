package uz.ferganagroup.arzonibizdadelivery.api

data class ISTICheckModel(
    val ipaddress: String,
    val ipport: String,
    val href_address: String,
    val bdate: String,
    val edate: String,
    val status: String,
    val secret_name: String,
    val delivery_version_code: String?
)
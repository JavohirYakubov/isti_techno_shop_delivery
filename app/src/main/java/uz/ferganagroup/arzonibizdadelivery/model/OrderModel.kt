package uz.ferganagroup.arzonibizdadelivery.model

import java.io.Serializable

data class OrderModel(
    val refKey: String,
    val date: String,
    val number: Int,
    val skladid: Int,
    val skladname: String,
    val agentid: String,
    val agentname: String,
    val id: String,
    val long: String,
    val lat: String,
    val where: Int,
    val cashbox: Int,
    val delivery: Int,
    val deliverySumma: Int,
    val summa: Int,
    val discount: Int,
    val Payme: Boolean,
    val isAccepted: Boolean,
    val paidSumma: Int,
    val array: List<MakeOrderProductModel>,
    val skladAddress: String? = "Farg'ona shaxar. 24 uy",
    val deliveryAddress: String? = "Farg'ona shaxar. 32 uy",
    val status: Int,
    val clientTelephone: String?,
    val clientName: String?

): Serializable
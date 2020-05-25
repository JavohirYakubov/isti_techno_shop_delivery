package uz.ferganagroup.arzonibizdadelivery.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StoreModel(
    val id: String,
    val name: String,
    val longitude: String,
    val latitude: String,
    val information: String,
    val radius: Int,
    val minimalSavdoSummasi: Int,
    val maximalPullikDostavka: Int,
    val dostavkaKmSumma: Int
): Serializable
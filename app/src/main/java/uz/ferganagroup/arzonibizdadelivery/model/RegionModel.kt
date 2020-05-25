package uz.ferganagroup.arzonibizdadelivery.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RegionModel(
    val id: String,
    val name: String,
    @SerializedName("tuman")
    val districts: List<DistrictModel>,
    @SerializedName("sklad")
    val stores: List<DistrictModel>,
    var checked: Boolean = false
): Serializable

data class DistrictModel(
    val id: String,
    val name: String,
    var checked: Boolean = false
): Serializable
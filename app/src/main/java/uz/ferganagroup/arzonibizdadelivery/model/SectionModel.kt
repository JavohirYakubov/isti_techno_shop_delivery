package uz.ferganagroup.arzonibizdadelivery.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SectionModel(
    val id: String,
    val name: String,
    val priority: Int,
    val image: String?
): Serializable
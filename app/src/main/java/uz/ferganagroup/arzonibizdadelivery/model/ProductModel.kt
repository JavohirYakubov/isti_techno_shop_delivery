package uz.ferganagroup.arzonibizdadelivery.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductModel(
    val id: String,
    val name: String,
    val priority: Int,
    val image: String?,
    val price: Double,
    @SerializedName("ostatka")
    val productCount: Int,
    @SerializedName("donaBlok")
    val unity: String,
    val blok: Int?,
    val blokostatok: Double,
    val blokprice: Double,
    val kg: Int,
    val information: String,
    val donalibonus: Boolean,
    val limitbonus: Int,
    val tovarbonus: Int,
    val foizbonus: Boolean,
    val foiz: Int,
    val yangi_rasm: Int,
    val file_id: String,
    val calculation: List<Double>,
    var favourite: Boolean = false,
    var cartCount: Int = 0
): Serializable
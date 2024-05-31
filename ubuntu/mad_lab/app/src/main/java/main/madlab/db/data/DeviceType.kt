package main.madlab.db.data

import androidx.annotation.DrawableRes
import main.madlab.R

data class DeviceType(
    var id: Int,
    var name: String,
    @DrawableRes var imgId: Int
)

private val definedDeviceTypes = listOf(
    DeviceType(1, "Кондиционер", R.drawable.conditioner),
    DeviceType(2, "Лампа", R.drawable.lamp),
    DeviceType(3, "Светодиодная лента", R.drawable.mini_light),
    DeviceType(4, "Умная колонка", R.drawable.umnaya_kolonka),
    DeviceType(5, "Робот пылесос", R.drawable.vacuum_cleaner)
)

fun getDefinedDeviceTypes(): List<DeviceType> {
    return definedDeviceTypes
}

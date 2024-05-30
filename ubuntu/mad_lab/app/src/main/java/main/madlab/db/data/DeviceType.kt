package main.madlab.db.data

import androidx.annotation.DrawableRes
import main.madlab.R

data class DeviceType(
    var id: Int,
    var name: String,
    @DrawableRes var imgId: Int
)

private val definedDeviceTypes = listOf(
    DeviceType(0, "Кондиционер", R.drawable.conditioner),
    DeviceType(1, "Лампа", R.drawable.lamp),
    DeviceType(2, "Светодиодная лента", R.drawable.mini_light),
    DeviceType(3, "Умная колонка", R.drawable.umnaya_kolonka),
    DeviceType(4, "Робот пылесос", R.drawable.vacuum_cleaner)
)

fun getDefinedDeviceTypes(): List<DeviceType> {
    return definedDeviceTypes
}

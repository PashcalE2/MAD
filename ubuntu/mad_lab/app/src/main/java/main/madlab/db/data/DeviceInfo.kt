package main.madlab.db.data

import androidx.annotation.DrawableRes

data class DeviceInfo(
    val deviceId: Int,
    @DrawableRes val imgId: Int,
    val deviceName: String,
    val roomName: String
)

fun generateRandom(): List<DeviceInfo> {
    val devices = mutableListOf<DeviceInfo>()
    val deviceTypes = getDeviceTypes()

    for (i in 0..10) {
        val deviceType = deviceTypes.random()
        devices.add(DeviceInfo(i, deviceType.imgId, deviceType.name, "Кухня"))
    }

    return devices
}
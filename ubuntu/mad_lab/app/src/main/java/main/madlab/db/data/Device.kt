package main.madlab.db.data

import main.madlab.R

data class Device(
    val id: Int,
    val roomId: Int,
    val name: String,
    val typeId: Int
)

fun generateRandom(): List<Device> {
    val devices = mutableListOf<Device>()
    val deviceTypes = getDeviceTypes()

    for (i in 0..10) {
        val deviceType = deviceTypes.random()
        devices.add(Device(i, 0, deviceType.name, deviceType.id))
    }

    return devices
}